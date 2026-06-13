package com.saasgateway.gateway_service.analytics.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.saasgateway.gateway_service.analytics.dto.AnalyticsSummaryDto;
import com.saasgateway.gateway_service.analytics.dto.ApiUsageTrendDto;
import com.saasgateway.gateway_service.analytics.dto.GatewayMetricsDto;
import com.saasgateway.gateway_service.analytics.dto.TenantUsageDto;
import com.saasgateway.gateway_service.analytics.dto.TopRateLimitedTenantDto;
import com.saasgateway.gateway_service.analytics.dto.TopRouteDto;
import com.saasgateway.gateway_service.analytics.service.AnalyticsService;
import com.saasgateway.gateway_service.analytics.service.GatewayMetricsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final GatewayMetricsService gatewayMetricsService;

    @GetMapping("/summary")
    @ResponseStatus(HttpStatus.OK)
    public AnalyticsSummaryDto getSummary() {
        log.info("Fetching analytics summary");
        return analyticsService.getSummary();
    }

    @GetMapping("/top-routes")
    @ResponseStatus(HttpStatus.OK)
    public List<TopRouteDto> getTopRoutes() {
        log.info("Fetching analytics top routes");
        return analyticsService.getTopRoutes();
    }

    @GetMapping("/trend")
    @ResponseStatus(HttpStatus.OK)
    public List<ApiUsageTrendDto> getUsageTrend() {
        log.info("Fetching analytics trend");
        return analyticsService.getUsageTrend();
    }

    @GetMapping("/tenant/{tenantId}")
    @ResponseStatus(HttpStatus.OK)
    public TenantUsageDto getTenantUsage(@PathVariable UUID tenantId) {
        log.info("Fetching analytics for tenant: {}", tenantId);
        return analyticsService.getTenantUsage(tenantId);
    }

    @GetMapping("/gateway-metrics")
    @ResponseStatus(HttpStatus.OK)
    public GatewayMetricsDto getGatewayMetrics() {
        return gatewayMetricsService.getMetrics();
    }

    @GetMapping("/top-rate-limited")
    @ResponseStatus(HttpStatus.OK)
    public List<TopRateLimitedTenantDto>
    getTopRateLimitedTenants() {
        return analyticsService.getTopRateLimitedTenants();
    }
    
}