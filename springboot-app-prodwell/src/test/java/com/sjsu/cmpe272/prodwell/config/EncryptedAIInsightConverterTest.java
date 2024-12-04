package com.sjsu.cmpe272.prodwell.config;

import com.sjsu.cmpe272.prodwell.entity.AIInsights;
import com.sjsu.cmpe272.prodwell.security.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EncryptedAIInsightConverterTest {

    @Mock
    private EncryptionService encryptionService;

    private EncryptedAIInsightReadingConverter readingConverter;
    private EncryptedAIInsightWritingConverter writingConverter;

    @BeforeEach
    void setUp() {
        readingConverter = new EncryptedAIInsightReadingConverter(encryptionService);
        writingConverter = new EncryptedAIInsightWritingConverter(encryptionService);
    }

    @Test
    void shouldConvertEncryptedToAnalysis() {
        String json = "{\"overview\":\"test\",\"workloadAnalysis\":\"test\"}";
        when(encryptionService.decrypt(anyString())).thenReturn(json);

        AIInsights.Analysis result = readingConverter.convert("encrypted");
        assertNotNull(result);
        assertEquals("test", result.getOverview());
    }

    @Test
    void shouldConvertAnalysisToEncrypted() {
        AIInsights.Analysis analysis = new AIInsights.Analysis();
        analysis.setOverview("test");
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted");

        String result = writingConverter.convert(analysis);
        assertEquals("encrypted", result);
    }
}