package com.saasgateway.gateway_service.test;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.saasgateway.gateway_service.api_key.service.ApiKeyValidationService;
import com.saasgateway.gateway_service.route.entity.Route;
import com.saasgateway.gateway_service.route.service.RouteLookupService;
import com.saasgateway.gateway_service.tenant.entity.Tenant;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {


    private final ApiKeyValidationService apiKeyValidationService;
    private final RouteLookupService routeLookupService;

    @GetMapping("/validate")
    public String validate(@RequestHeader("X-API-Key") String apiKey) {
        Tenant tenant = apiKeyValidationService.validate(apiKey);
        return tenant.getName();
    }

    @GetMapping("/route")
    public String route(
        @RequestParam UUID tenantId,
        @RequestParam String path,
        @RequestParam String method
    ) {
        return routeLookupService.findRoute(tenantId, path, method).map(Route::getTargetUrl).orElse("NOT_FOUND");
    }

    @GetMapping("/resolved-route")
    public String route(ServerWebExchange exchange) {
        Route route = exchange.getAttribute("route");
        if (route == null) {
            return "NO_ROUTE";
        }
        return route.getTargetUrl();
    }

}