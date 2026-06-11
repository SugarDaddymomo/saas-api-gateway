package com.saasgateway.gateway_service.route.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import com.saasgateway.gateway_service.route.entity.Route;
import com.saasgateway.gateway_service.route.entity.RouteStatus;
import com.saasgateway.gateway_service.route.repository.RouteRepository;
import com.saasgateway.gateway_service.route.service.RouteLookupService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteLookupServiceImpl implements RouteLookupService {

    private final AntPathMatcher matcher = new AntPathMatcher();

    private final RouteRepository routeRepository;
    
    @Override
    public Optional<Route> findRoute(UUID tenantId, String path, String method) {
        List<Route> routes = routeRepository.findByTenantIdAndStatus(tenantId, RouteStatus.ACTIVE);

        return routes.stream()
            .filter(route -> matches(route.getPathPattern(), path))
            .filter(route -> route.getMethod().name().equalsIgnoreCase(method))
            .findFirst();
    }

    private boolean matches(String pattern, String path) {
        return matcher.match(pattern, path);
    }

}