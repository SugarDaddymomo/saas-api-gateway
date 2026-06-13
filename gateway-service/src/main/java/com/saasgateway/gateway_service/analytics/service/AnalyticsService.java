package com.saasgateway.gateway_service.analytics.service;

import java.util.List;
import java.util.UUID;

import com.saasgateway.gateway_service.analytics.dto.AnalyticsSummaryDto;
import com.saasgateway.gateway_service.analytics.dto.ApiUsageTrendDto;
import com.saasgateway.gateway_service.analytics.dto.TenantUsageDto;
import com.saasgateway.gateway_service.analytics.dto.TopRateLimitedTenantDto;
import com.saasgateway.gateway_service.analytics.dto.TopRouteDto;

public interface AnalyticsService {
    
    void logRequest(UUID tenantId, UUID routeId, String method, String path, int statusCode, long responseTimeMs);
    AnalyticsSummaryDto getSummary();
    TenantUsageDto getTenantUsage(UUID tenantId);
    List<TopRouteDto> getTopRoutes();
    List<ApiUsageTrendDto> getUsageTrend();
    List<TopRateLimitedTenantDto> getTopRateLimitedTenants();
}