package com.sjsu.cmpe272.prodwell.service;

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
    void shouldSaveInsight() throws Exception {
        String llmResponse = "{\"dailyStressLevels\":[],\"averageStressLevel\":5.0,\"analysis\":{}}";
        JsonNode mockNode = new ObjectMapper().readTree(llmResponse);
        AIInsights insight = new AIInsights();
        insight.setOid("test-oid");

        when(objectMapper.readTree(llmResponse)).thenReturn(mockNode);
        when(repository.save(any(AIInsights.class))).thenReturn(insight);

        AIInsights result = service.saveInsight("test-oid", llmResponse);
        assertNotNull(result);
        assertEquals("test-oid", result.getOid());
    }

    @Test
    void shouldGetAllInsights() {
        AIInsights insight = new AIInsights();
        when(repository.findAll()).thenReturn(Arrays.asList(insight));

        List<AIInsights> results = service.getAllInsights();
        assertFalse(results.isEmpty());
    }

    @Test
    void shouldDeleteInsight() {
        doNothing().when(repository).deleteById("test-oid");
        service.deleteInsight("test-oid");
        verify(repository).deleteById("test-oid");
    }

    @Test
    void shouldThrowExceptionOnInvalidJson() {
        String invalidJson = "{invalid}";
        assertThrows(RuntimeException.class, () -> service.saveInsight("test-oid", invalidJson));
    }

    @Test
    void shouldGetInsightByOid() {
        AIInsights insight = new AIInsights();
        when(repository.findById("test-oid")).thenReturn(Optional.of(insight));

        Optional<AIInsights> result = service.getInsightByOid("test-oid");
        assertTrue(result.isPresent());
    }
}