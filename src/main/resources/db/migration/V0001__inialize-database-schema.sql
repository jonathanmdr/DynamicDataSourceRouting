CREATE TABLE billionaires (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    career VARCHAR(255) NOT NULL,
    idempotency_id VARCHAR(36) NOT NULL
);
