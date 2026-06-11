package com.saasgateway.admin.route.controller;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.saasgateway.admin.route.dto.CreateRouteRequest;
import com.saasgateway.admin.route.dto.CreateRouteResponse;
import com.saasgateway.admin.route.dto.RouteResponse;
import com.saasgateway.admin.route.dto.RouteSearchRequest;
import com.saasgateway.admin.route.entity.RouteStatus;
import com.saasgateway.admin.route.service.RouteService;
import com.saasgateway.admin.tenant.dto.MessageResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/tenants/{tenantId}/routes")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateRouteResponse createRoute(@PathVariable UUID tenantId, @RequestBody CreateRouteRequest request) {
        return routeService.createRoute(tenantId, request);
    }

    @GetMapping("/routes/{routeId}")
    @ResponseStatus(HttpStatus.OK)
    public RouteResponse getSingleRoute(@PathVariable UUID routeId) {
        return routeService.getRoute(routeId);
    }

    @GetMapping("/tenants/{tenantId}/routes")
    @ResponseStatus(HttpStatus.OK)
    public Page<RouteResponse> getRoutes(
        @PathVariable UUID tenantId,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) RouteStatus status,
        @RequestParam(defaultValue = "createdAt") String sortBy,
        @RequestParam(defaultValue = "DESC") String sortDirection,
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "20") Integer size
    ) {
        RouteSearchRequest routeSearchRequest = new RouteSearchRequest(search, status, sortBy, sortDirection, page, size);
        return routeService.getRoutes(tenantId, routeSearchRequest);
    }

    @PatchMapping("/routes/{routeId}/enable")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse enableRoute(@PathVariable UUID routeId) {
        return routeService.enableRoute(routeId);
    }

    @PatchMapping("/routes/{routeId}/disable")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponse disableRoute(@PathVariable UUID routeId) {
        return routeService.disableRoute(routeId);
    }

    @PutMapping("/routes/{routeId}")
    public RouteResponse updateRoute(@PathVariable UUID routeId, @RequestBody CreateRouteRequest request) {
        return routeService.updateRoute(routeId, request);
    }

}