package com.saasgateway.gateway_service.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import com.saasgateway.gateway_service.api_key.service.ApiKeyValidationService;
import com.saasgateway.gateway_service.common.exception.ApiKeyNotFoundException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ApiKeyValidationFilter implements GlobalFilter, Ordered {

    private final ApiKeyValidationService apiKeyValidationService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String apiKey = exchange.getRequest().getHeaders().getFirst("X-API-KEY");

        if (!StringUtils.hasText(apiKey)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            var tenant = apiKeyValidationService.validate(apiKey);

            exchange.getAttributes().put("tenant", tenant);

            return chain.filter(exchange);
        } catch (ApiKeyNotFoundException ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        } catch (Exception ex) {
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete();
        }

    }

    @Override
    public int getOrder() {
        return -100;
    }
    
}