package com.sjsu.cmpe272.prodwell.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjsu.cmpe272.prodwell.entity.AIInsights;
import com.sjsu.cmpe272.prodwell.security.EncryptionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class EncryptedAIInsightWritingConverter implements Converter<AIInsights.Analysis, String> {
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;

    public EncryptedAIInsightWritingConverter(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String convert(AIInsights.Analysis analysis) {
        try {
            String json = objectMapper.writeValueAsString(analysis);
            return encryptionService.encrypt(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error encrypting analysis", e);
        }
    }
}