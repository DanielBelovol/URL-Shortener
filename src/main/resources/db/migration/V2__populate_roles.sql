INSERT INTO roles (name) VALUES
('ROLE_USER'),
('ROLE_MODERATOR'),
('ROLE_ADMIN');
insert into users (username, password, email, role_id)
values ('Krasher3310', '$2a$12$eAB9KFFlQI./J/tB90AT4OzVlXdulX8DPQq592lKln0AnzsbQgKvu', 'user@gmail.com', 1);