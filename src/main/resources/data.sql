INSERT IGNORE INTO users (id, user_name, email, password, first_name, last_name)
VALUES (1, 'admin', 'admin@outlook.com', 'admin', 'admin', 'admin');
INSERT IGNORE INTO users (id, user_name, email, password, first_name, last_name)
VALUES (2, 'user', 'user@outlook.com', 'user', 'user', 'user');

INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_USER');
INSERT IGNORE INTO roles (id, name) VALUES (3, 'ROLE_GUEST');

INSERT IGNORE INTO user_role (user_id, role_id) VALUES (1, 1); -- admin has role ADMIN
INSERT IGNORE INTO user_role (user_id, role_id) VALUES (2, 2); -- user has role USER
