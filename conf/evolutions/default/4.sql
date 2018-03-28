# --- !Ups
CREATE TABLE code_gen (
  name VARCHAR(100) NOT NULL PRIMARY KEY,
  alias VARCHAR(50) NOT NULL,
  current_code INT NOT NULL
);


# --- !Downs
DROP TABLE code_gen;
