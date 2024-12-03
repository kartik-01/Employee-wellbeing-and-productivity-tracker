package com.sjsu.cmpe272.prodwell.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer.QuestionAnswer;
import com.sjsu.cmpe272.prodwell.security.EncryptionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import java.util.List;

@ReadingConverter
public class EncryptedAnswerReadingConverter implements Converter<String, List<QuestionAnswer>> {
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;

    public EncryptedAnswerReadingConverter(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public List<QuestionAnswer> convert(String encrypted) {
        try {
            String json = encryptionService.decrypt(encrypted);
            return objectMapper.readValue(json, new TypeReference<List<QuestionAnswer>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting answers", e);
        }
    }
}