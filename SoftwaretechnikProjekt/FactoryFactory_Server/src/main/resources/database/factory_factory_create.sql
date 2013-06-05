CREATE DOMAIN user_name AS VARCHAR(80) CHECK (VALUE ~'^[a-zA-Z]\w*\.?\w+$');

CREATE TABLE member(
	login user_name,
	password VARCHAR(80) NOT NULL,
	PRIMARY KEY (login)
);

CREATE TABLE factory(
	factory_id INTEGER,
	name VARCHAR(80) NOT NULL,
	data bytea,
	PRIMARY KEY (factory_id)
);

CREATE TABLE member_access(
	login user_name,
	factory_id INTEGER,
	PRIMARY KEY (login, factory_id),
	FOREIGN KEY (login) REFERENCES member (login),
	FOREIGN KEY (factory_id) REFERENCES factory (factory_id)
);
