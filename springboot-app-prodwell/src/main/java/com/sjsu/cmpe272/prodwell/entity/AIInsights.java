package com.sjsu.cmpe272.prodwell.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Encrypted;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Document(collection = "AI-insights")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIInsights {
    @Id
    private String oid;
    
    @Field
    private List<DailyStressLevel> dailyStressLevels;
    
    @Field
    @Encrypted
    private double averageStressLevel;
    
    @Field
    private Analysis analysis;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyStressLevel {
        private String date;
        private double stressLevel;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Analysis {
        private String overview;
        private String workloadAnalysis;
        private Suggestions suggestions;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Suggestions {
        private List<String> taskManagement;
        private List<String> personalWellbeing;
        private List<String> counselling;
    }
}