package com.sjsu.cmpe272.prodwell.config;

import com.sjsu.cmpe272.prodwell.security.EncryptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MongoConfigTest {

    @Autowired
    private MongoConfig mongoConfig;

    @Autowired
    private EncryptionService encryptionService;

    @Test
    void shouldCreateCustomConversions() {
        MongoCustomConversions conversions = mongoConfig.customConversions();
        assertNotNull(conversions);
        assertTrue(conversions.hasCustomWriteTarget(Double.class));
    }
}