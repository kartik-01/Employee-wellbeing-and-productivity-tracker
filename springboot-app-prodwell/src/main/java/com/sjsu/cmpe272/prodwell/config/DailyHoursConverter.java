package com.sjsu.cmpe272.prodwell.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class DailyHoursConverter {
    @WritingConverter
    public static class DailyHoursWritingConverter implements Converter<List<Map<String, Object>>, String> {
        private final ObjectMapper objectMapper;

        public DailyHoursWritingConverter() {
            this.objectMapper = new ObjectMapper();
            this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }

        @Override
        public String convert(List<Map<String, Object>> source) {
            try {
                return objectMapper.writeValueAsString(source);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Error converting daily hours", e);
            }
        }
    }

    @ReadingConverter
    public static class DailyHoursReadingConverter implements Converter<String, List<Map<String, Object>>> {
        private final ObjectMapper objectMapper;

        public DailyHoursReadingConverter() {
            this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        }

        @Override
        public List<Map<String, Object>> convert(String source) {
            try {
                return objectMapper.readValue(source, new TypeReference<List<Map<String, Object>>>() {});
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
    }
}