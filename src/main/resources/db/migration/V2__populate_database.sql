insert into roles (name)
values ('ROLE_USER'),
('ROLE_MODERATOR'),
('ROLE_ADMIN');

insert into users (name, password, email, role_id)
values ('user', '$2a$12$eAB9KFFlQI./J/tB90AT4OzVlXdulX8DPQq592lKln0AnzsbQgKvu', 'user@gmail.com', 1);