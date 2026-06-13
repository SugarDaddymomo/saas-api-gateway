package com.saasgateway.gateway_service.analytics.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.saasgateway.gateway_service.analytics.entity.ApiRequestLog;
import com.saasgateway.gateway_service.analytics.projection.DailyUsageProjection;
import com.saasgateway.gateway_service.analytics.projection.RouteUsageProjection;

@Repository
public interface ApiRequestLogRepository extends JpaRepository<ApiRequestLog, UUID> {

    @Query("select count(a) from ApiRequestLog a where a.statusCode between 200 and 299")
    long countSuccessfulRequests();

    @Query("select count(a) from ApiRequestLog a where a.statusCode >= 400")
    long countFailedRequests();

    @Query("select coalesce(avg(a.responseTimeMs), 0) from ApiRequestLog a")
    Double averageResponseTime();

    long countByTenantId(UUID tenantId);

    @Query("select count(a) from ApiRequestLog a where a.tenantId = :tenantId and a.statusCode between 200 and 299")
    long countSuccessfulRequestsByTenantId(UUID tenantId);

    @Query("select count(a) from ApiRequestLog a where a.tenantId = :tenantId and a.statusCode >= 400")
    long countFailedRequestsByTenantId(UUID tenantId);

    @Query("select coalesce(avg(a.responseTimeMs), 0) from ApiRequestLog a where a.tenantId = :tenantId")
    Double averageResponseTimeByTenantId(UUID tenantId);

    @Query("select a.path as path, count(a) as requestCount from ApiRequestLog a group by a.path order by count(a) desc")
    List<RouteUsageProjection> findTopRoutes();

    @Query(value = "select cast(timestamp as date) as date, count(*) as requestCount from api_request_log group by cast(timestamp as date) order by date", nativeQuery = true)
    List<DailyUsageProjection> findDailyUsageTrend();
}