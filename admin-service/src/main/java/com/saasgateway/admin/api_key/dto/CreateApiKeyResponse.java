package com.saasgateway.admin.api_key.dto;

import java.util.UUID;

public record CreateApiKeyResponse(
    
    UUID id,
    String name,
    String apiKey
) {
}