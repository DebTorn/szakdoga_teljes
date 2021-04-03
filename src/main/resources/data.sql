INSERT INTO users (aktiv, alaporaber, bruttoszorzo, email, password, teljesnev) VALUES (1, 2000, 1, "szelforgo10@gmail.com", "1234", "Admin")
INSERT INTO roles (name) VALUES ("ADMIN")
INSERT INTO roles (name) VALUES ("USER")
INSERT INTO users_roles (users_id, roles_id) VALUES (1, 1)