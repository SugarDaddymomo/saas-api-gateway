package com.saasgateway.admin.common.exception;

import java.util.UUID;

public class ApiKeyNotFoundException extends RuntimeException {

    public ApiKeyNotFoundException(UUID apiKeyId) {
        super("Api key not found: " + apiKeyId);
    }
}