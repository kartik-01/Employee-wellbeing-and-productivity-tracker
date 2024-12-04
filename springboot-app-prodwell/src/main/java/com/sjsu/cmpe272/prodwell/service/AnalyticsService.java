package com.sjsu.cmpe272.prodwell.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.sjsu.cmpe272.prodwell.dto.ManagerAnalyticsDTO;
import com.sjsu.cmpe272.prodwell.dto.TaskCompletionStats;
import com.sjsu.cmpe272.prodwell.dto.StressLevelTrend;
import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.entity.AIInsights;
import com.sjsu.cmpe272.prodwell.repository.AIInsightsRepository;
import com.sjsu.cmpe272.prodwell.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    private static final String PPLX_API_URL = "https://api.perplexity.ai/chat/completions";
    private static final String PPLX_TOKEN = "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";

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AIInsightsRepository aiInsightsRepository;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;

    public ManagerAnalyticsDTO getAnalytics(String projectCode, boolean isHR) {
        List<Task> tasks;
        if (isHR) {
            tasks = taskRepository.findAll();
        } else {
            tasks = taskRepository.findByProjectCode(projectCode);
        }
        
        Set<String> userIds = tasks.stream()
            .map(Task::getUserId)
            .collect(Collectors.toSet());
        
        TaskCompletionStats taskStats = calculateTaskStats(tasks);
        List<StressLevelTrend> stressLevels = getTeamStressLevels(userIds);
        int peopleCount = userIds.size();
        
        return new ManagerAnalyticsDTO(
            taskStats, 
            stressLevels, 
            isHR ? "ALL" : projectCode,
            peopleCount
        );
    }

    private List<StressLevelTrend> getTeamStressLevels(Set<String> userIds) {
        List<AIInsights> allTeamInsights = aiInsightsRepository.findByOidIn(userIds);
        Map<String, List<Double>> dateStressLevels = new HashMap<>();
        
        ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        for (AIInsights insight : allTeamInsights) {
            try {
                String dailyStressLevelsJson = mapper.writeValueAsString(insight.getDailyStressLevels());
                List<Map<String, Object>> dailyLevels = mapper.readValue(
                    dailyStressLevelsJson,
                    new TypeReference<List<Map<String, Object>>>() {}
                );
                
                for (Map<String, Object> daily : dailyLevels) {
                    String date = (String) daily.get("date");
                    Double stressLevel = ((Number) daily.get("stressLevel")).doubleValue();
                    dateStressLevels.computeIfAbsent(date, k -> new ArrayList<>())
                        .add(stressLevel);
                }
            } catch (Exception e) {
                // Log the error but continue processing other insights
                System.err.println("Error processing insight for user: " + insight.getOid());
                e.printStackTrace();
                continue;
            }
        }
    
        return dateStressLevels.entrySet().stream()
            .map(entry -> {
                double averageStress = entry.getValue().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
                return new StressLevelTrend(entry.getKey(), averageStress);
            })
            .sorted(Comparator.comparing(StressLevelTrend::getDate))
            .collect(Collectors.toList());
    }

    private TaskCompletionStats calculateTaskStats(List<Task> tasks) {
        long beforeTime = 0, onTime = 0, late = 0;
    
        for (Task task : tasks) {
            if (task.getTaskEndDate() != null && task.getDeadlineDate() != null) {
                if (task.getTaskEndDate().isBefore(task.getDeadlineDate())) {
                    beforeTime++;
                } else if (task.getTaskEndDate().isEqual(task.getDeadlineDate())) {
                    onTime++;
                } else {
                    late++;
                }
            }
        }
    
        return new TaskCompletionStats(beforeTime, onTime, late);
    }

    public String teamInsights(String projectCode, boolean isHR) {
        try {
            ManagerAnalyticsDTO teamData = getAnalytics(projectCode, isHR);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(PPLX_TOKEN);
    
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama-3.1-sonar-small-128k-online");
            requestBody.put("temperature", 0.3);
    
            List<Map<String, String>> messages = new ArrayList<>();
            
            String systemPrompt = isHR 
                ? "You are an AI assistant that analyzes employee stress levels and health across the organization. " +
                  "Provide a brief overview of the employees' stress levels and overall health based on the given data. " +
                  "Exclude task completion rates from your analysis. " +
                  "Respond with a JSON object containing only two fields: 'stressLevels' and 'recommendations'. " +
                  "Each field should contain a brief textual explanation without any numerical values. " +
                  "Keep the entire response concise, within 2-3 sentences for each field. " +
                  "Address the insights for the entire organization's employees."
                : "You are an AI assistant that analyzes team stress levels and health. " +
                  "Provide a brief overview of the team's stress levels and overall health based on the given data. " +
                  "Exclude task completion rates from your analysis. " +
                  "Respond with a JSON object containing only two fields: 'stressLevels' and 'recommendations'. " +
                  "Each field should contain a brief textual explanation without any numerical values. " +
                  "Keep the entire response concise, within 2-3 sentences for each field. " +
                  "Address the insights specifically for the team.";
    
            messages.add(Map.of("role", "system", "content", systemPrompt));
            messages.add(Map.of(
                "role", "user",
                "content", objectMapper.writeValueAsString(teamData)
            ));
    
            requestBody.put("messages", messages);
    
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
    
            String response = restTemplate.postForObject(PPLX_API_URL, entity, String.class);
            JsonNode rootNode = objectMapper.readTree(response);
            String content = rootNode.path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asText()
                .trim();
    
            // Remove Markdown code block delimiters
            content = content.replaceAll("^```json\\s*", "").replaceAll("\\s*```$", "");
    
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating insights: " + e.getMessage();
        }
    }
}