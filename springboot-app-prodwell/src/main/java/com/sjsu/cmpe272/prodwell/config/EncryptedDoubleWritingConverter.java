package com.sjsu.cmpe272.prodwell.config;

import com.sjsu.cmpe272.prodwell.security.EncryptionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.convert.ReadingConverter;

@WritingConverter
public class EncryptedDoubleWritingConverter implements Converter<Double, String> {
    private final EncryptionService encryptionService;

    public EncryptedDoubleWritingConverter(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public String convert(Double value) {
        try {
            return encryptionService.encrypt(String.valueOf(value));
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting double value", e);
        }
    }
}