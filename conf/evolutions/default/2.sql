# --- !Ups
INSERT INTO users (user_id, user_name, department, is_deleted, created_at, updated_at) VALUES ('U0001', 'admin', 'admin', '0', '2017-12-12 00:00:00', '2017-12-12 00:00:00');
INSERT INTO password_info (hasher, password, salt, login_info_id) VALUES ('bcrypt', '$2a$10$MYso8aVCaPNhv6yoZwZSF.0XNiOoz/38MMUANBBBOvzGMvub4Y4YC', NULL, '1');
INSERT INTO user_login_info (user_id, login_info_id) VALUES ('U0001', '1');
INSERT INTO login_info (id, provider_id, provider_key) VALUES ('1', 'credentials', 'admin');

# --- !Downs
DELETE FROM users;
DELETE FROM password_info;
DELETE FROM user_login_info;
DELETE FROM login_info;