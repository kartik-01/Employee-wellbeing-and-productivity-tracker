package com.sjsu.cmpe272.prodwell.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjsu.cmpe272.prodwell.entity.AIInsights;
import com.sjsu.cmpe272.prodwell.security.EncryptionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class EncryptedAIInsightReadingConverter implements Converter<String, AIInsights.Analysis> {
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;

    public EncryptedAIInsightReadingConverter(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public AIInsights.Analysis convert(String encrypted) {
        try {
            String json = encryptionService.decrypt(encrypted);
            return objectMapper.readValue(json, AIInsights.Analysis.class);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting analysis", e);
        }
    }
}