package com.sjsu.cmpe272.prodwell.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionService();
        ReflectionTestUtils.setField(encryptionService, "encryptionKey", "12345678901234567890123456789012");
    }

    @Test
    void shouldEncryptAndDecryptValue() {
        String originalValue = "test-data";
        String encrypted = encryptionService.encrypt(originalValue);
        String decrypted = encryptionService.decrypt(encrypted);

        assertNotEquals(originalValue, encrypted);
        assertEquals(originalValue, decrypted);
    }

    @Test
    void shouldHandleSpecialCharacters() {
        String originalValue = "test@data#123$";
        String encrypted = encryptionService.encrypt(originalValue);
        String decrypted = encryptionService.decrypt(encrypted);

        assertEquals(originalValue, decrypted);
    }

    @Test
    void shouldThrowExceptionForInvalidDecryption() {
        assertThrows(RuntimeException.class, () ->
                encryptionService.decrypt("invalid-encrypted-data")
        );
    }
}