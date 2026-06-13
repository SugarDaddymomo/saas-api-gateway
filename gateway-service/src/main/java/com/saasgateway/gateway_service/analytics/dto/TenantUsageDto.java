package com.saasgateway.gateway_service.analytics.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record TenantUsageDto(
        UUID tenantId,
        long totalRequests,
        long successfulRequests,
        long failedRequests,
        double avgResponseTimeMs
) {}