ALTER TABLE tenant
ADD COLUMN rate_limit INTEGER NOT NULL DEFAULT 100;

ALTER TABLE tenant
ADD COLUMN rate_limit_window_seconds INTEGER NOT NULL DEFAULT 60;

UPDATE tenant
SET rate_limit = CASE plan
    WHEN 'FREE' THEN 100
    WHEN 'PRO' THEN 1000
    WHEN 'ENTERPRISE' THEN 10000
    ELSE 100
END;

UPDATE tenant
SET rate_limit_window_seconds = 60;