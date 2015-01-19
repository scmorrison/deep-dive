# --- !Ups

CREATE TABLE monitoring_team (
    id serial,
    name character varying(200) NOT NULL
    );

CREATE TABLE reef_type (
    id serial,
    name character varying(200) NOT NULL,
    depth character varying (20) NOT NULL
    );

CREATE TABLE region (
    id serial,
    name character varying(200) NOT NULL
    );

CREATE TABLE site (
    id serial,
    subregion_id integer NOT NULL,
    reef_type_id integer NOT NULL,
    name character varying(200),
    latitude numeric(21,10) NOT NULL,
    longitude numeric(21,10) NOT NULL,
    map_datum character varying(200) NOT NULL
    );

CREATE TABLE subregion (
    id serial,
    name character varying(200) NOT NULL,
    region_id integer NOT NULL,
    code character varying(10) NOT NULL
    );

CREATE TABLE survey_event (
    id serial,
    site_id integer NOT NULL,
    event_date date NOT NULL,
    transect_length smallint NOT NULL,
    photographer character varying(25) NOT NULL,
    analyzer character varying(25) NOT NULL,
    transect_depth smallint NOT NULL
    );

CREATE TABLE surveyevent_monitoring_team (
    id serial,
    surveyevent_id integer NOT NULL,
    monitoringteam_id integer NOT NULL
    );


# --- !Downs

DROP TABLE monitoring_team;

DROP TABLE reef_type;

DROP TABLE region;

DROP TABLE site;

DROP TABLE subregion;

DROP TABLE survey_event;

DROP TABLE surveyevent_monitoring_team;
