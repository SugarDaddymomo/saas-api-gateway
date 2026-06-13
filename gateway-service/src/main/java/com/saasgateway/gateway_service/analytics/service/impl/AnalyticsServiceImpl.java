package com.saasgateway.gateway_service.analytics.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

import com.saasgateway.gateway_service.analytics.dto.AnalyticsSummaryDto;
import com.saasgateway.gateway_service.analytics.dto.ApiUsageTrendDto;
import com.saasgateway.gateway_service.analytics.dto.TenantUsageDto;
import com.saasgateway.gateway_service.analytics.dto.TopRateLimitedTenantDto;
import com.saasgateway.gateway_service.analytics.dto.TopRouteDto;
import com.saasgateway.gateway_service.analytics.entity.ApiRequestLog;
import com.saasgateway.gateway_service.analytics.repository.ApiRequestLogRepository;
import com.saasgateway.gateway_service.analytics.service.AnalyticsService;
import com.saasgateway.gateway_service.analytics.service.GatewayMetricsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ApiRequestLogRepository apiRequestLogRepository;
    private final GatewayMetricsService gatewayMetricsService;
    
    @Override
    public AnalyticsSummaryDto getSummary() {
        return AnalyticsSummaryDto.builder()
            .totalRequests(apiRequestLogRepository.count())
            .successfulRequests(apiRequestLogRepository.countSuccessfulRequests())
            .failedRequests(apiRequestLogRepository.countFailedRequests())
            .avgResponseTimeMs(apiRequestLogRepository.averageResponseTime())
            .build();
    }

    @Override
    public TenantUsageDto getTenantUsage(UUID tenantId) {
        return TenantUsageDto.builder()
            .tenantId(tenantId)
            .totalRequests(apiRequestLogRepository.countByTenantId(tenantId))
            .successfulRequests(apiRequestLogRepository.countSuccessfulRequestsByTenantId(tenantId))
            .failedRequests(apiRequestLogRepository.countFailedRequestsByTenantId(tenantId))
            .avgResponseTimeMs(apiRequestLogRepository.averageResponseTimeByTenantId(tenantId))
            .build();
    }

    @Override
    public List<TopRouteDto> getTopRoutes() {
        return apiRequestLogRepository.findTopRoutes()
            .stream()
            .map(
                route -> TopRouteDto.builder()
                    .path(route.getPath())
                    .requestCount(route.getRequestCount())
                    .build()
            )
            .toList();
    }

    @Override
    public List<ApiUsageTrendDto> getUsageTrend() {
        return apiRequestLogRepository.findDailyUsageTrend()
            .stream()
            .map(
                trend -> ApiUsageTrendDto.builder()
                    .date(trend.getDate())
                    .requestCount(trend.getRequestCount())
                    .build()
            )
            .toList();
    }

    @Override
    public void logRequest(UUID tenantId, UUID routeId, String method, String path, int statusCode, long responseTimeMs) {

        try {
            ApiRequestLog logEntry = ApiRequestLog.builder()
                    .id(UUID.randomUUID())
                    .tenantId(tenantId)
                    .routeId(routeId)
                    .method(method)
                    .path(path)
                    .statusCode(statusCode)
                    .responseTimeMs(responseTimeMs)
                    .timestamp(Instant.now())
                    .build();

            apiRequestLogRepository.save(logEntry);
        } catch (Exception ex) {
            log.error(
                    "Failed to persist analytics event. tenantId={}, routeId={}",
                    tenantId,
                    routeId,
                    ex
            );
        }
    }

    @Override
    public List<TopRateLimitedTenantDto> getTopRateLimitedTenants() {
        return gatewayMetricsService.getTenantRateLimitedRequests().entrySet()
                .stream()
                .sorted(Map.Entry.<UUID, Long>comparingByValue().reversed())
                .limit(10)
                .map(entry ->
                        TopRateLimitedTenantDto
                                .builder()
                                .tenantId(entry.getKey())
                                .blockedRequests(entry.getValue())
                                .build()
                )
                .toList();
    }

}