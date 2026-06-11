package com.saasgateway.admin.api_key.dto;

import java.time.Instant;
import java.util.UUID;
import com.saasgateway.admin.api_key.entity.ApiKeyStatus;

public record ApiKeyResponse(
    
    UUID id,
    String name,
    ApiKeyStatus status,
    Instant createdAt,
    Instant lastUsedAt
) {
}