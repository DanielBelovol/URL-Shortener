CREATE TABLE url_profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_url VARCHAR(2048) NOT NULL,
    short_url VARCHAR(255) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    views INT NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);