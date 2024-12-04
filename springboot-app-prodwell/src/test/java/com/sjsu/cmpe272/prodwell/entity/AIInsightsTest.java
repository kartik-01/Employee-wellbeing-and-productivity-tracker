package com.sjsu.cmpe272.prodwell.entity;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class AIInsightsTest {

    @Test
    void shouldCreateAIInsightsWithAllFields() {
        AIInsights.DailyStressLevel dailyLevel = new AIInsights.DailyStressLevel("2024-01-01", 5.0);
        List<String> taskManagement = Arrays.asList("Task 1", "Task 2");
        List<String> wellbeing = Arrays.asList("Exercise", "Meditate");
        List<String> counselling = Arrays.asList("Seek support");

        AIInsights.Suggestions suggestions = new AIInsights.Suggestions(taskManagement, wellbeing, counselling);
        AIInsights.Analysis analysis = new AIInsights.Analysis("Overview text", "Workload analysis", suggestions);

        AIInsights insights = new AIInsights();
        insights.setOid("test-oid");
        insights.setDailyStressLevels(Arrays.asList(dailyLevel));
        insights.setAverageStressLevel(4.5);
        insights.setAnalysis(analysis);

        assertEquals("test-oid", insights.getOid());
        assertEquals(4.5, insights.getAverageStressLevel());
        assertEquals(1, insights.getDailyStressLevels().size());
        assertEquals("Overview text", insights.getAnalysis().getOverview());
    }

    @Test
    void shouldHandleNullFields() {
        AIInsights insights = new AIInsights();
        assertNull(insights.getOid());
        assertNull(insights.getDailyStressLevels());
        assertEquals(0.0, insights.getAverageStressLevel());
        assertNull(insights.getAnalysis());
    }
}