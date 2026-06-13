package com.saasgateway.gateway_service.analytics.service;

import java.util.Map;
import java.util.UUID;
import com.saasgateway.gateway_service.analytics.dto.GatewayMetricsDto;

public interface GatewayMetricsService {
    void incrementRateLimitedRequests();
    long getRateLimitedRequests();
    void incrementRequestsPerMinute();
    long getCurrentRequestsPerMinute();
    void incrementCacheHits();
    void incrementCacheMisses();
    long getCacheHits();
    long getCacheMisses();
    GatewayMetricsDto getMetrics();
    long getTotalCachedRoutes();
    void incrementTenantRateLimited(UUID tenantId);
    Map<UUID, Long> getTenantRateLimitedRequests();
}