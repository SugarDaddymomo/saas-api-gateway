package com.saasgateway.gateway_service.analytics.dto;

import lombok.Builder;

@Builder
public record AnalyticsSummaryDto(
        long totalRequests,
        long successfulRequests,
        long failedRequests,
        double avgResponseTimeMs
) {
}