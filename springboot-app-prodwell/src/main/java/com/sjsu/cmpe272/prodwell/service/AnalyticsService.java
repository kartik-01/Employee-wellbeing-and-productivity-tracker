package com.sjsu.cmpe272.prodwell.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.sjsu.cmpe272.prodwell.dto.ManagerAnalyticsDTO;
import com.sjsu.cmpe272.prodwell.dto.TaskCompletionStats;
import com.sjsu.cmpe272.prodwell.dto.StressLevelTrend;
import com.sjsu.cmpe272.prodwell.entity.Task;
import com.sjsu.cmpe272.prodwell.entity.AIInsights;
import com.sjsu.cmpe272.prodwell.repository.TaskRepository;
import com.sjsu.cmpe272.prodwell.repository.AIInsightsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AIInsightsRepository aiInsightsRepository;

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
}