
-- DROP DATABASE myfinances;

CREATE DATABASE myfinances;

CREATE SCHEMA finances;

CREATE TABLE finances.user
(
  id bigserial NOT NULL PRIMARY KEY,
  name character varying(150),
  email character varying(100),
  password character varying(20),
  date_register date default now()
);

CREATE TABLE finances.entries
(
  id bigserial NOT NULL PRIMARY KEY ,
  description character varying(100) NOT NULL,
  month integer NOT NULL,
  year integer NOT NULL,
  value numeric(16,2),
  type character varying(20),
  status character varying(20),
  id_user bigint REFERENCES finances.user (id),
  date_register date default now()
);