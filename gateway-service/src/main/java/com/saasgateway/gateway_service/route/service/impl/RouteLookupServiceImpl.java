package com.saasgateway.gateway_service.route.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import com.saasgateway.gateway_service.analytics.service.GatewayMetricsService;
import com.saasgateway.gateway_service.route.cache.CachedRoute;
import com.saasgateway.gateway_service.route.cache.RouteCacheService;
import com.saasgateway.gateway_service.route.entity.Route;
import com.saasgateway.gateway_service.route.entity.RouteStatus;
import com.saasgateway.gateway_service.route.repository.RouteRepository;
import com.saasgateway.gateway_service.route.service.RouteLookupService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RouteLookupServiceImpl implements RouteLookupService {

    private final RouteCacheService routeCacheService;
    private final AntPathMatcher matcher = new AntPathMatcher();
    private final RouteRepository routeRepository;
    private final GatewayMetricsService gatewayMetricsService;
    
    @Override
    public Optional<Route> findRoute(UUID tenantId, String path, String method) {
        Optional<CachedRoute> cachedRoute = routeCacheService.get(tenantId, path, method);
        if (cachedRoute.isPresent()) {
            gatewayMetricsService.incrementCacheHits();
            log.info("Route cache HIT");
            CachedRoute cached = cachedRoute.get();
            return Optional.of(
                Route.builder()
                        .id(cached.id())
                        .name(cached.name())
                        .pathPattern(cached.pathPattern())
                        .targetUrl(cached.targetUrl())
                        .method(cached.method())
                        .build()
            );
        }
        gatewayMetricsService.incrementCacheMisses();
        log.info("Route cache MISS");
        List<Route> routes = routeRepository.findByTenantIdAndStatus(tenantId, RouteStatus.ACTIVE);
        Optional<Route> route = routes.stream()
                    .filter(r -> matches(r.getPathPattern(), path))
                    .filter(r -> r.getMethod()
                            .name()
                            .equalsIgnoreCase(method))
                    .findFirst();
        route.ifPresent(r ->
            routeCacheService.put(
                    tenantId,
                    path,
                    method,
                    new CachedRoute(
                            r.getId(),
                            r.getName(),
                            r.getPathPattern(),
                            r.getTargetUrl(),
                            r.getMethod()
                    )
        ));
        return route;
    }

    private boolean matches(String pattern, String path) {
        return matcher.match(pattern, path);
    }

}