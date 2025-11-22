-- Inserting employees for Human Resources
INSERT INTO employees (id, first_name, last_name, email, salary, department_id, created_at, updated_at)
VALUES
    ('1a2b3c4d-5e6f-7g8h-9i0j-abcdefgh1234', 'Alice', 'Johnson', 'alice.johnson@email.com', 50000.00, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('2a3b4c5d-6e7f-8g9h-0i1j-abcdefgh5678', 'Bob', 'Smith', 'bob.smith@email.com', 55000.00, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserting employees for Information Technology
INSERT INTO employees (id, first_name, last_name, email, salary, department_id, created_at, updated_at)
VALUES
    ('3a4b5c6d-7e8f-9g0h-1i2j-abcdefgh9101', 'Charlie', 'Brown', 'charlie.brown@email.com', 60000.00, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('4a5b6c7d-8e9f-0g1h-2i3j-abcdefgh1121', 'David', 'Lee', 'david.lee@email.com', 65000.00, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserting employees for Marketing
INSERT INTO employees (id, first_name, last_name, email, salary, department_id, created_at, updated_at)
VALUES
    ('5a6b7c8d-9e0f-1g2h-3i4j-abcdefgh3141', 'Eve', 'Adams', 'eve.adams@email.com', 52000.00, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Inserting employees for Sales
INSERT INTO employees (id, first_name, last_name, email, salary, department_id, created_at, updated_at)
VALUES
    ('6a7b8c9d-0e1f-2g3h-4i5j-abcdefgh5161', 'Frank', 'White', 'frank.white@email.com', 57000.00, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
