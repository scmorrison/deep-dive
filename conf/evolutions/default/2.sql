# --- !Ups

ALTER TABLE ONLY monitoring_team
    ADD CONSTRAINT monitoring_team_pkey PRIMARY KEY (id);

ALTER TABLE ONLY reef_type
    ADD CONSTRAINT reef_type_pkey PRIMARY KEY (id);

ALTER TABLE ONLY region
    ADD CONSTRAINT region_pkey PRIMARY KEY (id);

ALTER TABLE ONLY site
    ADD CONSTRAINT site_pkey PRIMARY KEY (id);

ALTER TABLE ONLY subregion
    ADD CONSTRAINT subregion_pkey PRIMARY KEY (id);

ALTER TABLE ONLY surveyevent_monitoring_team
    ADD CONSTRAINT surveyevent_monitoring_surveyevent_id_monitoringteam_key UNIQUE (surveyevent_id, monitoringteam_id);

ALTER TABLE ONLY surveyevent_monitoring_team
    ADD CONSTRAINT surveyevent_monitoring_team_pkey PRIMARY KEY (id);

ALTER TABLE ONLY survey_event
    ADD CONSTRAINT survey_event_pkey PRIMARY KEY (id);


CREATE INDEX site_reef_type_id ON site USING btree (reef_type_id);

CREATE INDEX site_subregion_id ON site USING btree (subregion_id);

CREATE INDEX subregion_region_id ON subregion USING btree (region_id);

CREATE INDEX surveyevent_monitoring_team_monitoringteam_id ON surveyevent_monitoring_team USING btree (monitoringteam_id);

CREATE INDEX surveyevent_monitoring_team_surveyevent_id ON surveyevent_monitoring_team USING btree (surveyevent_id);

CREATE INDEX survey_event_site_id ON survey_event USING btree (site_id);

ALTER TABLE ONLY site
    ADD CONSTRAINT site_reef_type_id_fkey FOREIGN KEY (reef_type_id) REFERENCES reef_type(id) DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE ONLY site
    ADD CONSTRAINT site_subregion_id_fkey FOREIGN KEY (subregion_id) REFERENCES subregion(id) DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE ONLY subregion
    ADD CONSTRAINT subregion_region_id_fkey FOREIGN KEY (region_id) REFERENCES region(id) DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE ONLY surveyevent_monitoring_team
    ADD CONSTRAINT surveyevent_monitoring_team_monitoringteam_id_fkey FOREIGN KEY (monitoringteam_id) REFERENCES monitoring_team(id) DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE ONLY survey_event
    ADD CONSTRAINT survey_event_site_id_fkey FOREIGN KEY (site_id) REFERENCES site(id) DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE ONLY surveyevent_monitoring_team
    ADD CONSTRAINT surveyevent_id_refs_id_c36946ba FOREIGN KEY (surveyevent_id) REFERENCES survey_event(id) DEFERRABLE INITIALLY DEFERRED;
