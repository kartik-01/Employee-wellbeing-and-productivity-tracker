package com.sjsu.cmpe272.prodwell.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RestConfigTest {

    @Autowired
    private RestConfig restConfig;

    @Test
    void shouldCreateRestTemplate() {
        RestTemplate restTemplate = restConfig.restTemplate();
        assertNotNull(restTemplate);
    }

    @Test
    void shouldCreateObjectMapper() {
        ObjectMapper objectMapper = restConfig.objectMapper();
        assertNotNull(objectMapper);
        assertTrue(objectMapper.findModules().stream()
                .anyMatch(module -> module.getClass().getSimpleName().contains("JavaTimeModule")));
    }
}