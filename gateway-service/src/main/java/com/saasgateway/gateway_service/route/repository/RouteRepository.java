package com.saasgateway.gateway_service.route.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.saasgateway.gateway_service.route.entity.Route;
import com.saasgateway.gateway_service.route.entity.RouteStatus;

@Repository
public interface RouteRepository extends JpaRepository<Route, UUID>, JpaSpecificationExecutor<Route> {

    Optional<Route> findByTenantIdAndPathPatternAndMethodAndStatus(UUID tenantId, String pathPattern, String method, RouteStatus status);

    List<Route> findByTenantIdAndStatus(UUID tenantId, RouteStatus status);
}