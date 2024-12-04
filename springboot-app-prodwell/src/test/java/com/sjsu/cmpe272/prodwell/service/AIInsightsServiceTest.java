package com.sjsu.cmpe272.prodwell.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjsu.cmpe272.prodwell.entity.AIInsights;
import com.sjsu.cmpe272.prodwell.repository.AIInsightsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AIInsightsServiceTest {

    @Mock
    private AIInsightsRepository repository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AIInsightsService service;

    @Test
    void shouldSaveInsightWithFullData() throws Exception {
        String llmResponse = "{\"dailyStressLevels\":[{\"date\":\"2024-01-01\",\"level\":5.0}],\"averageStressLevel\":5.0,\"analysis\":{\"overview\":\"test\",\"workloadAnalysis\":\"test\"}}";
        JsonNode mockNode = new ObjectMapper().readTree(llmResponse);
        AIInsights insight = new AIInsights();
        insight.setOid("test-oid");

        when(objectMapper.readTree(llmResponse)).thenReturn(mockNode);
        when(objectMapper.convertValue(any(), any(TypeReference.class))).thenReturn(Arrays.asList(new AIInsights.DailyStressLevel()));
        when(objectMapper.convertValue(any(), eq(AIInsights.Analysis.class))).thenReturn(new AIInsights.Analysis());
        when(repository.save(any(AIInsights.class))).thenReturn(insight);

        AIInsights result = service.saveInsight("test-oid", llmResponse);
        assertNotNull(result);
        assertEquals("test-oid", result.getOid());
        verify(objectMapper).convertValue(any(), any(TypeReference.class));
    }

    @Test
    void shouldHandleNullFieldsInResponse() throws Exception {
        String llmResponse = "{\"dailyStressLevels\":null,\"averageStressLevel\":null,\"analysis\":null}";
        JsonNode mockNode = new ObjectMapper().readTree(llmResponse);

        when(objectMapper.readTree(llmResponse)).thenReturn(mockNode);
        when(repository.save(any(AIInsights.class))).thenReturn(new AIInsights());

        AIInsights result = service.saveInsight("test-oid", llmResponse);
        assertNotNull(result);
    }

    @Test
    void shouldHandleEmptyResponse() {
        String emptyResponse = "{}";
        assertThrows(RuntimeException.class, () -> service.saveInsight("test-oid", emptyResponse));
    }

    @Test
    void shouldHandleObjectMapperException() throws Exception {
        when(objectMapper.readTree(anyString())).thenThrow(new JsonProcessingException("Test exception") {});

        assertThrows(RuntimeException.class, () -> service.saveInsight("test-oid", "{}"));
    }

    @Test
    void shouldHandleRepositorySaveException() throws Exception {
        String llmResponse = "{\"dailyStressLevels\":[],\"averageStressLevel\":5.0,\"analysis\":{}}";
        JsonNode mockNode = new ObjectMapper().readTree(llmResponse);

        when(objectMapper.readTree(anyString())).thenReturn(mockNode);
        when(repository.save(any())).thenThrow(new RuntimeException("Save failed"));

        assertThrows(RuntimeException.class, () -> service.saveInsight("test-oid", llmResponse));
    }

    @Test
    void shouldGetAllInsightsWhenEmpty() {
        when(repository.findAll()).thenReturn(Arrays.asList());
        List<AIInsights> results = service.getAllInsights();
        assertTrue(results.isEmpty());
    }

    @Test
    void shouldHandleDeleteNonExistentInsight() {
        doThrow(new RuntimeException("Not found")).when(repository).deleteById("non-existent");
        assertThrows(RuntimeException.class, () -> service.deleteInsight("non-existent"));
    }

    @Test
    void shouldReturnEmptyOptionalForNonExistentInsight() {
        when(repository.findById("non-existent")).thenReturn(Optional.empty());
        Optional<AIInsights> result = service.getInsightByOid("non-existent");
        assertFalse(result.isPresent());
    }

    @Test
    void shouldHandleNullInsightId() {
        assertThrows(IllegalArgumentException.class, () -> service.getInsightByOid(null));
    }
}