package com.sjsu.cmpe272.prodwell.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjsu.cmpe272.prodwell.entity.User;
import com.sjsu.cmpe272.prodwell.entity.UserDataDTO;
import com.sjsu.cmpe272.prodwell.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private static final String PPLX_API_URL = "https://api.perplexity.ai/chat/completions";
    private static final String PPLX_TOKEN = "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";

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserDataService userDataService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AIInsightsService aiInsightsService;

    public User checkAndCreateUser(User userData) {
        return userRepository.findByOid(userData.getOid())
            .orElseGet(() -> createNewUser(userData));
    }

    private User createNewUser(User userData) {
        User newUser = new User();
        newUser.setOid(userData.getOid());
        newUser.setGiven_name(userData.getGiven_name());
        newUser.setFamily_name(userData.getFamily_name());
        newUser.setJobTitle(userData.getJobTitle());
        return userRepository.save(newUser);
    }

    public UserDataDTO getUserCompleteData(String oid) {
        UserDataDTO userData = userDataService.getUserData(oid);
        if (userData.getUser() != null) {
            return userData;
        }
        return null;
    }

    public String analyzeUserStress(UserDataDTO userData) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(PPLX_TOKEN);
    
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama-3.1-sonar-large-128k-online");
            requestBody.put("temperature", 0.3);  // Lower temperature for more consistent outputs
            
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of(
                "role", "system",
                "content", "You are an AI assistant that evaluates employee stress levels. " +
                          "Calculate stress levels using this formula: " +
                          "- Base stress (5) + Hours factor ((daily hours - 8) × 0.5) + Task overlap factor (concurrent tasks × 0.5) " +
                          "- If hours > 8: add +1 for each hour over 8 " +
                          "- If multiple tasks on same day: add +0.5 for each overlapping task " +
                          "- If task end date is after deadline: add +2 " +
                          "- Cap final stress level at 10 " +
                          "Analyze the data and respond with a clean JSON object containing: " +
                          "'name', 'dailyStressLevels' (array with date and stressLevel 0-10, calculated using above formula), " +
                          "'averageStressLevel' (average of all daily levels, rounded to 2 decimals), " +
                          "'analysis' object with: " +
                          "'overview' (direct address to user about their personality and work pattern, starting with their first name), " +
                          "'workloadAnalysis' (direct analysis of their tasks and hours, using 'you' and 'your'), " +
                          "'suggestions' object containing: {" +
                          "  'taskManagement' (2-3 personalized points on handling workload), " +
                          "  'personalWellbeing' (2-3 points addressing user directly), " +
                          "  'counselling' ( Suggest this if average stress level is above 8, otherwise this field is not needed. 2-3 points using 'you' and 'your')" +
                          "}. " +
                          "Use direct address (you/your) throughout the analysis. NO EXPLANATION IS NEEDED, ONLY JSON OBJECT"
            ));
            
            String userDataJson = objectMapper.writeValueAsString(userData);
            messages.add(Map.of("role", "user", "content", userDataJson));
            
            requestBody.put("messages", messages);
    
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                PPLX_API_URL,
                HttpMethod.POST,
                entity,
                String.class
            );
            
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            String content = rootNode.path("choices")
                .path(0)
                .path("message")
                .path("content")
                .asText()
                .replace("```json\n", "")
                .replace("\n```", "")
                .trim();
            
                 // Save the AI insights
                aiInsightsService.saveInsight(userData.getUser().getOid(), content);
                
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}