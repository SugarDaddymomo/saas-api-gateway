package com.saasgateway.admin.api_key.util;

import java.security.SecureRandom;
import java.util.Base64;

public final class ApiKeyGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private ApiKeyGenerator() {
    
    }

    public static String generate() {

        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return "sgw_live_" +
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(bytes);
    }
}