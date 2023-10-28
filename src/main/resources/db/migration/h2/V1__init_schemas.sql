CREATE TABLE IF NOT EXISTS departments
(
    id                     BIGINT PRIMARY KEY AUTO_INCREMENT,
    department_code        VARCHAR(255) NOT NULL UNIQUE,
    department_name        VARCHAR(255) NOT NULL UNIQUE,
    department_description VARCHAR(255) NOT NULL,
    created_at             TIMESTAMP,
    updated_at             TIMESTAMP
);

CREATE TABLE IF NOT EXISTS employees
(
    id            CHAR(36) PRIMARY KEY,
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    email         VARCHAR(255) NOT NULL UNIQUE,
    salary        DECIMAL(10, 2),
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    department_id BIGINT NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments (id)
);
