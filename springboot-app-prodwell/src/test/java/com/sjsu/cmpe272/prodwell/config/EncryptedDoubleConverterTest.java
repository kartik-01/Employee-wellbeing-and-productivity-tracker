package com.sjsu.cmpe272.prodwell.config;

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
class EncryptedDoubleConverterTest {

    @Mock
    private EncryptionService encryptionService;

    private EncryptedDoubleReadingConverter readingConverter;
    private EncryptedDoubleWritingConverter writingConverter;

    @BeforeEach
    void setUp() {
        readingConverter = new EncryptedDoubleReadingConverter(encryptionService);
        writingConverter = new EncryptedDoubleWritingConverter(encryptionService);
    }

    @Test
    void shouldConvertEncryptedToDouble() {
        when(encryptionService.decrypt(anyString())).thenReturn("123.45");
        Double result = readingConverter.convert("encrypted");
        assertEquals(123.45, result);
    }

    @Test
    void shouldConvertDoubleToEncrypted() {
        when(encryptionService.encrypt(anyString())).thenReturn("encrypted");
        String result = writingConverter.convert(123.45);
        assertEquals("encrypted", result);
    }
}