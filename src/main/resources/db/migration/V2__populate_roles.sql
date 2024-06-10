INSERT INTO roles (name) VALUES
('ROLE_USER'),
('ROLE_MODERATOR'),
('ROLE_ADMIN');


insert into users (username, password, email, role_id)
values ('admin', '$2a$12$eAB9KFFlQI./J/tB90AT4OzVlXdulX8DPQq592lKln0AnzsbQgKvu', 'admin@gmail.com', 3);