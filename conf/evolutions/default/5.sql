# --- !Ups
INSERT INTO code_gen(name, alias, current_code) VALUES ('user' , 'U', 1);
INSERT INTO code_gen(name, alias, current_code) VALUES ('transaction', 'TN', 1);
INSERT INTO code_gen(name, alias, current_code) VALUES ('term_job', 'TJ', 1);
INSERT INTO code_gen(name, alias, current_code) VALUES ('term_industry' , 'TI', 1);
INSERT INTO code_gen(name, alias, current_code) VALUES ('term_unit' , 'TU', 1);
INSERT INTO code_gen(name, alias, current_code) VALUES ('term_customer' , 'TC', 1);
INSERT INTO code_gen(name, alias, current_code) VALUES ('term_prefecture' , 'TP', 1);
INSERT INTO code_gen(name, alias, current_code) VALUES ('term_weather' , 'TW', 1);
INSERT INTO code_gen(name, alias, current_code) VALUES ('term_trouble' , 'TT', 1);
INSERT INTO code_gen(name, alias, current_code) VALUES ('term_trader' , 'TR', 1);

# --- !Downs
DELETE TABLE code_gen;
