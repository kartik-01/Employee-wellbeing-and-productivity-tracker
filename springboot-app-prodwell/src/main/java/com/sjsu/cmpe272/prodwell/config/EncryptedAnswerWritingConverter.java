package com.sjsu.cmpe272.prodwell.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjsu.cmpe272.prodwell.entity.PersonalityAnswer.QuestionAnswer;
import com.sjsu.cmpe272.prodwell.security.EncryptionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import java.util.List;

@WritingConverter
public class EncryptedAnswerWritingConverter implements Converter<List<QuestionAnswer>, String> {
    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;

    public EncryptedAnswerWritingConverter(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public String convert(List<QuestionAnswer> answers) {
        try {
            String json = objectMapper.writeValueAsString(answers);
            return encryptionService.encrypt(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting answers", e);
        }
    }
}