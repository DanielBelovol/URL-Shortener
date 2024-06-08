INSERT INTO roles (name) VALUES
('ROLE_USER'),
('ROLE_MODERATOR'),
('ROLE_ADMIN');

INSERT INTO user_roles (user_id, role_id) VALUES (1, (SELECT id FROM roles WHERE name = 'ADMIN'));
INSERT INTO user_roles (user_id, role_id) VALUES (2, (SELECT id FROM roles WHERE name = 'MODERATOR'));