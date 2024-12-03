package com.sjsu.cmpe272.prodwell.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
public class EncryptionService {
    @Value("${encryption.key}")
    private String encryptionKey; // 16, 24, or 32 bytes key

    private SecretKeySpec createKey() {
        return new SecretKeySpec(encryptionKey.getBytes(), "AES");
    }

    public String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, createKey());
            return Base64.getEncoder()
                .encodeToString(cipher.doFinal(value.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    public String decrypt(String encrypted) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, createKey());
            return new String(cipher.doFinal(Base64.getDecoder()
                .decode(encrypted)));
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}