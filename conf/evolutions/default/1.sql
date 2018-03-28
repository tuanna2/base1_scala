# --- !Ups

CREATE TABLE users (
  user_id VARCHAR(10) NOT NULL PRIMARY KEY,
  user_name VARCHAR(500),
  department VARCHAR(500),
  note VARCHAR(500),
  is_deleted INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(10),
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_by VARCHAR(10)
);

CREATE TABLE auth_tokens (
  id VARCHAR(50) PRIMARY KEY,
  user_id VARCHAR(10) NOT NULL,
  expiry VARCHAR(100)
);

CREATE TABLE login_info (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  provider_id VARCHAR(500) NOT NULL,
  provider_key VARCHAR(500) NOT NULL
);

CREATE TABLE user_login_info (
  user_id VARCHAR(10) NOT NULL,
  login_info_id INT(6) NOT NULL
);

CREATE TABLE password_info (
  hasher VARCHAR(500) NOT NULL,
  password VARCHAR(500) NOT NULL,
  salt VARCHAR(500),
  login_info_id INT(6) NOT NULL
);

CREATE TABLE oauth1_info (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  token VARCHAR(500),
  secret VARCHAR(500) NOT NULL,
  login_info_id INT(6) NOT NULL
);

CREATE TABLE oauth2_info (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  access_token VARCHAR(500) NOT NULL,
  token_type VARCHAR(500),
  expires_in INT,
  refresh_token VARCHAR(500),
  login_info_id INT(6) NOT NULL
);


# --- !Downs

DROP TABLE oauth2_info;
DROP TABLE oauth1_info;
DROP TABLE password_info;
DROP TABLE user_login_info;
DROP TABLE login_info;
DROP TABLE users;