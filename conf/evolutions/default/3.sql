# --- !Ups
CREATE TABLE term (
  term_code VARCHAR(10) NOT NULL PRIMARY KEY,
  term_type VARCHAR(100),
  term_name VARCHAR(100),
  note VARCHAR(500),
  is_deleted INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(10),
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  updated_by VARCHAR(10)
);

# --- !Downs
DROP TABLE term;