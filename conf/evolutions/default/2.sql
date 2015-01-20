# --- !Ups

CREATE TABLE dd_user (
    id serial,
    email character varying(150) NOT NULL,
    password character varying(250) NOT NULL,
    name character varying(250) NOT NULL
    );

INSERT INTO dd_user (email, password, name) VALUES ('jeffusan@atware.jp', 'secret', 'Jeff');

# --- !Downs

DROP TABLE dd_user;
