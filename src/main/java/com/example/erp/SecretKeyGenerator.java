package com.example.erp;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {

    public static void main(String[] args) {
        // Define the length of the secret key in bytes (e.g., 32 bytes for a 256-bit key)
        int keyLengthBytes = 32;

        // Generate a secure random key
        byte[] secretKeyBytes = generateSecureRandomKey(keyLengthBytes);

        // Encode the byte array as a Base64 string
        String secretKey = Base64.getEncoder().encodeToString(secretKeyBytes);

        // Print the generated secret key
        System.out.println("Generated Secret Key: " + secretKey);
    }

    public static byte[] generateSecureRandomKey(int keyLengthBytes) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[keyLengthBytes];
        secureRandom.nextBytes(key);
        return key;
    }
}

