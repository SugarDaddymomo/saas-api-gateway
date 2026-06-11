CREATE TABLE tenant
(
    id UUID PRIMARY KEY,

    name VARCHAR(255) NOT NULL,

    status VARCHAR(50) NOT NULL,

    plan VARCHAR(50) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);
