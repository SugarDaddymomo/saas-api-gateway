CREATE TABLE api_request_log
(
    id UUID PRIMARY KEY,

    tenant_id UUID NOT NULL,

    route_id UUID,

    method VARCHAR(10) NOT NULL,

    path VARCHAR(500) NOT NULL,

    status_code INTEGER NOT NULL,

    response_time_ms BIGINT NOT NULL,

    timestamp TIMESTAMP NOT NULL
);

CREATE INDEX idx_api_request_log_tenant_id
ON api_request_log(tenant_id);

CREATE INDEX idx_api_request_log_route_id
ON api_request_log(route_id);

CREATE INDEX idx_api_request_log_timestamp
ON api_request_log(timestamp);