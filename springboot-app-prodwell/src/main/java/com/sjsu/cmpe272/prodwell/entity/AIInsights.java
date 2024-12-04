package com.sjsu.cmpe272.prodwell.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Encrypted;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Document(collection = "AI-insights")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AIInsights {
    @Id
    private String oid;
    
    @Field("dailyStressLevels")
    private List<DailyStressLevel> dailyStressLevels;
    
    @Field
    @Encrypted
    private double averageStressLevel;
    
    @Field
    @Encrypted
    private Analysis analysis;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DailyStressLevel {
        private String date;
        private double stressLevel;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Analysis {
        private String overview;
        private String workloadAnalysis;
        private Suggestions suggestions;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Suggestions {
        private List<String> taskManagement;
        private List<String> personalWellbeing;
        private List<String> counselling;
    }
}