package com.sjsu.cmpe272.prodwell.config;

import com.sjsu.cmpe272.prodwell.security.EncryptionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class EncryptedDoubleReadingConverter implements Converter<String, Double> {
    private final EncryptionService encryptionService;

    public EncryptedDoubleReadingConverter(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public Double convert(String encrypted) {
        try {
            String decrypted = encryptionService.decrypt(encrypted);
            return Double.parseDouble(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting double value", e);
        }
    }
}