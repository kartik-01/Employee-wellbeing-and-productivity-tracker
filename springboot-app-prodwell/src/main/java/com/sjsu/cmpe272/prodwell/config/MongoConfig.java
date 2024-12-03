package com.sjsu.cmpe272.prodwell.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import com.sjsu.cmpe272.prodwell.security.EncryptionService;

import java.util.ArrayList;
import java.util.List;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class MongoConfig {
    @Autowired
    private EncryptionService encryptionService;

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new EncryptedAnswerWritingConverter(encryptionService));
        converters.add(new EncryptedAnswerReadingConverter(encryptionService));
        converters.add(new EncryptedAIInsightWritingConverter(encryptionService));
        converters.add(new EncryptedAIInsightReadingConverter(encryptionService));
        converters.add(new EncryptedDoubleWritingConverter(encryptionService));
        converters.add(new EncryptedDoubleReadingConverter(encryptionService));
        return new MongoCustomConversions(converters);
    }
}