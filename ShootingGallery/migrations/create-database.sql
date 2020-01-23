CREATE ROLE "adminDB" WITH
	NOLOGIN
	NOSUPERUSER
	NOCREATEDB
	NOCREATEROLE
	INHERIT
	NOREPLICATION
	CONNECTION LIMIT -1
	PASSWORD 'password';

CREATE DATABASE "SHOOTING_GALLERY"
    WITH 
    OWNER = "adminDB"
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;