package com.sjsu.cmpe272.prodwell.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjsu.cmpe272.prodwell.entity.AIInsights;
import com.sjsu.cmpe272.prodwell.repository.AIInsightsRepository;
import java.util.List;
import java.util.Optional;

@Service
public class AIInsightsService {
    @Autowired
    private AIInsightsRepository repository;
    
    @Autowired
    private ObjectMapper objectMapper;

    public AIInsights saveInsight(String oid, String llmResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(llmResponse);
            AIInsights insight = new AIInsights();
            insight.setOid(oid);
            insight.setDailyStressLevels(objectMapper.convertValue(rootNode.get("dailyStressLevels"), 
                new TypeReference<List<AIInsights.DailyStressLevel>>() {}));
            insight.setAverageStressLevel(rootNode.get("averageStressLevel").asDouble());
            insight.setAnalysis(objectMapper.convertValue(rootNode.get("analysis"), 
                AIInsights.Analysis.class));
            
            return repository.save(insight);
        } catch (Exception e) {
            throw new RuntimeException("Error saving AI insight", e);
        }
    }

    public Optional<AIInsights> getInsightByOid(String oid) {
        return repository.findById(oid);
    }

    public List<AIInsights> getAllInsights() {
        return repository.findAll();
    }

    public void deleteInsight(String oid) {
        repository.deleteById(oid);
    }
}