package com.saasgateway.gateway_service.analytics.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record TopRateLimitedTenantDto(
        UUID tenantId,
        long blockedRequests
) {
}