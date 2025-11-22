CREATE TABLE IF NOT EXISTS departments
(
    id                     BIGSERIAL PRIMARY KEY,
    department_code        VARCHAR(255) NOT NULL UNIQUE,
    department_name        VARCHAR(255) NOT NULL UNIQUE,
    department_description VARCHAR(255) NOT NULL,
    created_at             TIMESTAMP WITHOUT TIME ZONE,
    updated_at             TIMESTAMP WITHOUT TIME ZONE
);

CREATE TABLE IF NOT EXISTS employees
(
    id            VARCHAR(255) PRIMARY KEY,
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    email         VARCHAR(255) NOT NULL UNIQUE,
    salary        DECIMAL(10, 2),
    created_at    TIMESTAMP WITHOUT TIME ZONE,
    updated_at    TIMESTAMP WITHOUT TIME ZONE,
    department_id BIGINT NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments (id)
);
