package com.saasgateway.gateway_service.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.saasgateway.gateway_service.analytics.service.GatewayMetricsService;
import com.saasgateway.gateway_service.ratelimit.service.RateLimitService;
import com.saasgateway.gateway_service.tenant.entity.Tenant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitFilter implements GlobalFilter, Ordered {

    private final RateLimitService rateLimitService;
    private final GatewayMetricsService gatewayMetricsService;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info(">>> RateLimitFilter path=" + exchange.getRequest().getURI().getPath());
        Tenant tenant = exchange.getAttribute("tenant");
        if (tenant == null) {
            return chain.filter(exchange);
        }

        boolean allowed = rateLimitService.isAllowed(tenant.getId().toString(), tenant.getRateLimit(), tenant.getRateLimitWindowSeconds());
        if (!allowed) {
            gatewayMetricsService.incrementRateLimitedRequests();
            gatewayMetricsService.incrementTenantRateLimited(tenant.getId());
            log.info("Rate Limited Requests={}", gatewayMetricsService.getRateLimitedRequests());
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -90;
    }

}