package com.saasgateway.gateway_service.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.reactive.function.BodyInserters;

import com.saasgateway.gateway_service.analytics.service.AnalyticsService;
import com.saasgateway.gateway_service.analytics.service.GatewayMetricsService;
import com.saasgateway.gateway_service.route.entity.Route;
import com.saasgateway.gateway_service.tenant.entity.Tenant;

import org.springframework.web.util.UriComponentsBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class DynamicRoutingFilter implements GlobalFilter, Ordered {

    private final WebClient webClient;
    private final AnalyticsService analyticsService;
    private final GatewayMetricsService gatewayMetricsService;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        log.info(">>> DynamicRoutingFilter path=" + exchange.getRequest().getURI().getPath());
        Route route = exchange.getAttribute("resolvedRoute");
        if (route == null) {
            return chain.filter(exchange);
        }
        gatewayMetricsService.incrementRequestsPerMinute();
        log.info("Current RPM={}", gatewayMetricsService.getCurrentRequestsPerMinute());
        log.info("Route Found: " + route.getName());

        String path = exchange.getRequest().getURI().getRawPath();
        String query = exchange.getRequest().getURI().getRawQuery();
        String targetUrl = UriComponentsBuilder
                .fromUriString(route.getTargetUrl())
                .path(path)
                .query(query)
                .build(true)
            .toUriString();
        
        log.info("Incoming URI: " + exchange.getRequest().getURI());
        log.info("Forwarding to: " + targetUrl);

        return webClient
                .method(exchange.getRequest().getMethod())
                .uri(targetUrl)
                .headers(header -> {
                    header.addAll(exchange.getRequest().getHeaders());
                })
                .body(BodyInserters.fromDataBuffers(exchange.getRequest().getBody()))
                .exchangeToMono(response -> {
                    exchange.getResponse().setStatusCode(response.statusCode());
                    exchange.getResponse().getHeaders().addAll(response.headers().asHttpHeaders());
                    try {
                        long responseTimeMs = System.currentTimeMillis() - startTime;
                        Tenant tenant = exchange.getAttribute("tenant");
                        if (tenant != null) {
                            analyticsService.logRequest(
                                    tenant.getId(),
                                    route.getId(),
                                    exchange.getRequest()
                                            .getMethod()
                                            .name(),
                                    exchange.getRequest()
                                            .getURI()
                                            .getPath(),
                                    response.statusCode().value(),
                                    responseTimeMs
                            );
                        }
                    } catch (Exception ex) {
                        log.error("Failed to record analytics", ex);
                    }
                    return response.bodyToMono(String.class)
                        .flatMap(body -> {
                            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes());
                            return exchange.getResponse().writeWith(Mono.just(buffer));
                        });
                });

    }

    @Override
    public int getOrder() {
        return -70;
    }
    
}