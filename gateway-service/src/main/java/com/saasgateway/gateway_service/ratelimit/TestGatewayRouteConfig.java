package com.saasgateway.gateway_service.ratelimit;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestGatewayRouteConfig {

    @Bean
    RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(
                "catch-all",
                r -> r.path("/**")
                .uri("https://httpbin.org")
            )
            .build();
    }
}