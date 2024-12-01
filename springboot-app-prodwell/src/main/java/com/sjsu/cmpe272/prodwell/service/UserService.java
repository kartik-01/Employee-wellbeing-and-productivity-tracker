package com.sjsu.cmpe272.prodwell.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public String testLLMIntegration() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(PPLX_TOKEN);
    
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama-3.1-sonar-large-128k-online");
            
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of(
                "role", "system",
                "content", "You are an AI assistant that evaluates employee stress levels based on metrics like HoursSpent where 8 is ideal. Always respond in a JSON format with two fields: 'stresslevel' (a number between 0 and 10, where 0 is the lowest stress and 5 is neutral) and 'suggestion' (a concise sentence providing feedback or advice based on the users Personality and likes - any one which seems appropriate, don't mention personality type in final output). Do not include any additional text or explanation outside of the JSON object."
            ));
            
            messages.add(Map.of(
                "role", "user",
                "content", "{\\\"Personality\\\": \\\"Indoor\\\", \\\"TaskAssigned\\\": 3, \\\"HoursSpent\\\": 9, \\\"SubmissionTime\\\": \\\"BeforeTime\\\", \\\"Hobbies\\\": \\\"Chess, Painting, Listening to Music, Dancing\\\"}"
            ));
            
            requestBody.put("messages", messages);
    
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<String> response = restTemplate.exchange(
                PPLX_API_URL,
                HttpMethod.POST,
                entity,
                String.class
            );
            
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
    
}