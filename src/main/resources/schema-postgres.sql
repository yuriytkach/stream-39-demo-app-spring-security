DROP TYPE IF EXISTS status_type CASCADE;
CREATE TYPE status_type AS ENUM ('REGULAR', 'PAID', 'VIP');

DROP TABLE IF EXISTS api_keys CASCADE;
CREATE TABLE api_keys
(
    api_key VARCHAR(10) PRIMARY KEY,
    status  status_type  NOT NULL DEFAULT 'REGULAR',
    client  VARCHAR(255) NOT NULL
);
