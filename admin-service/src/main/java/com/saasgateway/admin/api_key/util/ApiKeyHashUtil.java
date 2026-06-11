package com.saasgateway.admin.api_key.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class ApiKeyHashUtil {

    private ApiKeyHashUtil() {

    }

    public static String hash(String apiKey) {

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(apiKey.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();

            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(
                    "Unable to hash api key",
                    ex);
        }
    }
}