package com.saasgateway.admin.route.service.impl;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.saasgateway.admin.common.exception.RouteNotFoundException;
import com.saasgateway.admin.common.exception.TenantNotFoundException;
import com.saasgateway.admin.route.dto.CreateRouteRequest;
import com.saasgateway.admin.route.dto.CreateRouteResponse;
import com.saasgateway.admin.route.dto.RouteResponse;
import com.saasgateway.admin.route.dto.RouteSearchRequest;
import com.saasgateway.admin.route.entity.Route;
import com.saasgateway.admin.route.entity.RouteStatus;
import com.saasgateway.admin.route.repository.RouteRepository;
import com.saasgateway.admin.route.service.RouteService;
import com.saasgateway.admin.route.specification.RouteSpecification;
import com.saasgateway.admin.tenant.dto.MessageResponse;
import com.saasgateway.admin.tenant.entity.Tenant;
import com.saasgateway.admin.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

    private final TenantRepository tenantRepository;
    private final RouteRepository routeRepository;

    private static final Set<String>
        ALLOWED_SORT_FIELDS =
        Set.of(
                "name",
                "createdAt",
                "updatedAt",
                "status"
        );

    @Override
    public CreateRouteResponse createRoute(UUID tenantId, CreateRouteRequest request) {
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow(() -> new TenantNotFoundException(tenantId));

        Route route = Route.builder()
                    .id(UUID.randomUUID())
                    .tenant(tenant)
                    .name(request.name())
                    .pathPattern(request.pathPattern())
                    .targetUrl(request.targetUrl())
                    .method(request.method())
                    .status(RouteStatus.ACTIVE)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

        route = routeRepository.save(route);
        return new CreateRouteResponse(
            route.getId(),
            route.getName(),
            route.getPathPattern(),
            route.getTargetUrl(),
            route.getMethod(),
            route.getStatus()
        );

    }

    @Override
    public MessageResponse disableRoute(UUID routeId) {
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new RouteNotFoundException(routeId));
        route.setStatus(RouteStatus.DISABLED);
        route.setUpdatedAt(Instant.now());
        routeRepository.save(route);
        return new MessageResponse("Route disabled successfully");
    }

    @Override
    public MessageResponse enableRoute(UUID routeId) {
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new RouteNotFoundException(routeId));
        route.setStatus(RouteStatus.ACTIVE);
        route.setUpdatedAt(Instant.now());
        routeRepository.save(route);
        return new MessageResponse("Route enabled successfully");
    }

    @Override
    public RouteResponse getRoute(UUID routeId) {
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new RouteNotFoundException(routeId));
        return new RouteResponse(
            route.getId(),
            route.getName(),
            route.getPathPattern(),
            route.getTargetUrl(),
            route.getMethod(),
            route.getStatus()
        );

    }

    @Override
    public Page<RouteResponse> getRoutes(UUID tenantId, RouteSearchRequest request) {

        tenantRepository.findById(tenantId).orElseThrow(() -> new TenantNotFoundException(tenantId));

        Specification<Route> spec = RouteSpecification.belongsToTenant(tenantId);
        if (StringUtils.hasText(request.search())) {
            spec = spec.and(RouteSpecification.hasName(request.search()));
        }
        if (request.status() != null) {
            spec = spec.and(RouteSpecification.hasStatus(request.status()));
        }

        String sortBy = request.sortBy();
        if (!ALLOWED_SORT_FIELDS.contains(sortBy)) {
            sortBy = "createdAt";
        }
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(request.sortDirection());
        } catch (Exception ex) {
            direction = Sort.Direction.DESC;
        }

        Pageable pageable = PageRequest.of(request.page(), request.size(), Sort.by(direction, sortBy));

        return routeRepository.findAll(spec, pageable).map(
            route -> new RouteResponse(
                route.getId(),
                route.getName(),
                route.getPathPattern(),
                route.getTargetUrl(),
                route.getMethod(),
                route.getStatus()
            )
        );
    }

    @Override
    public RouteResponse updateRoute(UUID routeId, CreateRouteRequest request) {
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new RouteNotFoundException(routeId));

        if (StringUtils.hasText(request.name())) {
            route.setName(request.name());
        }
        if (StringUtils.hasText(request.pathPattern())) {
            route.setPathPattern(request.pathPattern());
        }
        if (StringUtils.hasText(request.targetUrl())) {
            route.setTargetUrl(request.targetUrl());
        }
        if (Objects.nonNull(request.method())) {
            route.setMethod(request.method());
        }
        route.setUpdatedAt(Instant.now());
        route = routeRepository.save(route);

        return new RouteResponse(
            route.getId(),
            route.getName(),
            route.getPathPattern(),
            route.getTargetUrl(),
            route.getMethod(),
            route.getStatus()
        );
    }

}