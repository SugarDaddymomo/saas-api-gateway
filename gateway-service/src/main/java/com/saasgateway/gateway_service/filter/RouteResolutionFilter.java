package com.saasgateway.gateway_service.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.saasgateway.gateway_service.route.entity.Route;
import com.saasgateway.gateway_service.route.service.RouteLookupService;
import com.saasgateway.gateway_service.tenant.entity.Tenant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class RouteResolutionFilter implements GlobalFilter, Ordered {

    private final RouteLookupService routeLookupService;
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info(">>> RouteResolutionFilter path=" + exchange.getRequest().getURI().getPath());
        String path = exchange.getRequest().getURI().getPath();

        //skip test endpoints for now
        if (path.startsWith("/test")) {
            return chain.filter(exchange);
        }

        Tenant tenant = exchange.getAttribute("tenant");
        if (tenant == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String method = exchange.getRequest().getMethod().name();
        Route route = routeLookupService.findRoute(tenant.getId(), path, method).orElse(null);

        if (route == null) {
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            return exchange.getResponse().setComplete();
        }

        exchange.getAttributes().put("resolvedRoute", route);

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -80;
    }

}