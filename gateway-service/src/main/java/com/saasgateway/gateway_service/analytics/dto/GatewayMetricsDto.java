package com.saasgateway.gateway_service.analytics.dto;

import lombok.Builder;

@Builder
public record GatewayMetricsDto(
        long rateLimitedRequests,
        long requestsPerMinute,
        long cacheHits,
        long cacheMisses,
        long totalCachedRoutes
) {
}