CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS roles (
    role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users_roles (
    users_roles_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    KEY user_fk_idx (user_id),
    KEY role_fk_idx (role_id),
    CONSTRAINT role_fk FOREIGN KEY (role_id) REFERENCES roles (role_id),
    CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES users (user_id)
);