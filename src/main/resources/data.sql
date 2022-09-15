INSERT IGNORE INTO users (user_id, user_name, email, password, first_name, last_name)
VALUES (1, 'admin', 'admin@outlook.com', 'admin', 'admin', 'admin');
INSERT IGNORE INTO users (user_id, user_name, email, password, first_name, last_name)
VALUES (2, 'user', 'user@outlook.com', 'user', 'user', 'user');

INSERT IGNORE INTO roles (role_id, role_name) VALUES (1, 'ROLE_ADMIN');
INSERT IGNORE INTO roles (role_id, role_name) VALUES (2, 'ROLE_USER');
INSERT IGNORE INTO roles (role_id, role_name) VALUES (3, 'ROLE_GUEST');

INSERT IGNORE INTO users_roles (users_roles_id, user_id, role_id) VALUES (1, 1, 1); -- admin has role ADMIN
INSERT IGNORE INTO users_roles (users_roles_id, user_id, role_id) VALUES (2, 2, 2); -- user has role USER
