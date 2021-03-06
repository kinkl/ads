CREATE USER ads WITH PASSWORD '1qazxsw2';

CREATE DATABASE ads
  WITH OWNER = ads
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Russian_Russia.1251'
       LC_CTYPE = 'Russian_Russia.1251'
       CONNECTION LIMIT = -1;
GRANT CONNECT, TEMPORARY ON DATABASE ads TO ads;
GRANT ALL ON DATABASE ads TO ads;

--Connect to "ads" database before execute following commands
CREATE TABLE users (
  username CHARACTER VARYING(50) PRIMARY KEY,
  password CHARACTER VARYING(50) NOT NULL,
  enabled BOOLEAN NOT NULL
);

ALTER TABLE users OWNER TO ads;

INSERT INTO users (username, password, enabled) VALUES ('ADMIN', '1qazxsw2', TRUE);

CREATE TABLE authorities (
  id INTEGER PRIMARY KEY,
  username CHARACTER VARYING(50) NOT NULL REFERENCES users(username),
  authority CHARACTER VARYING(50) NOT NULL
);

ALTER TABLE authorities OWNER TO ads;

CREATE SEQUENCE authorities_seq
  INCREMENT BY 1
  MINVALUE 1
  MAXVALUE 99999999
  START WITH 1
  CACHE 1;

ALTER SEQUENCE authorities_seq OWNER TO ads;

INSERT INTO authorities (id, username, authority) VALUES (NEXTVAL('authorities_seq'), 'ADMIN', 'ROLE_ADMIN');
INSERT INTO authorities (id, username, authority) VALUES (NEXTVAL('authorities_seq'), 'ADMIN', 'ROLE_USER');

CREATE TABLE advertisements (
  id INTEGER PRIMARY KEY,
  text CHARACTER VARYING(1000) NOT NULL,
  username CHARACTER VARYING(50) NOT NULL REFERENCES users(username)
);

ALTER TABLE advertisements OWNER TO ads;

CREATE SEQUENCE advertisements_seq
  INCREMENT BY 1
  MINVALUE 1
  MAXVALUE 99999999
  START WITH 1
  CACHE 1;

ALTER SEQUENCE advertisements_seq OWNER TO ads;

ALTER TABLE advertisements ADD COLUMN datetime TIMESTAMP WITHOUT TIME ZONE NOT NULL;

CREATE TABLE votes (
  id INTEGER PRIMARY KEY,
  advertisement_id INTEGER NOT NULL REFERENCES advertisements(id) ON DELETE CASCADE,
  username CHARACTER VARYING(50) NOT NULL REFERENCES users(username) ON DELETE CASCADE,
  vote_value INTEGER NOT NULL CHECK (vote_value = -1 OR vote_value = 1),
  UNIQUE (advertisement_id, username)
);

ALTER TABLE votes OWNER TO ads;

CREATE SEQUENCE votes_seq
  INCREMENT BY 1
  MINVALUE 1
  MAXVALUE 99999999
  START WITH 1
  CACHE 1;

ALTER SEQUENCE votes_seq OWNER TO ads;
