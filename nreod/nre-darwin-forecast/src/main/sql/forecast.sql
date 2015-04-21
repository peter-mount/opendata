-- ----------------------------------------------------------------------
-- Darwin Forecasts
--
-- Version  1 2015/04/14 Initial version
-- ----------------------------------------------------------------------

--CREATE SCHEMA darwin;
SET search_path = darwin;

DROP TABLE forecast_entry;
DROP TABLE forecast;
DROP TABLE tiploc;

-- ----------------------------------------------------------------------
-- Normalisation table of tiplocs
-- ----------------------------------------------------------------------
CREATE TABLE tiploc (
    id          SERIAL,
    tpl         varCHAR(16),
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX tiploc_c ON tiploc(tpl);
ALTER TABLE tiploc OWNER TO rail;
ALTER TABLE tiploc_id_seq OWNER TO rail;

-- ----------------------------------------------------------------------
-- Train forecast
-- ----------------------------------------------------------------------
CREATE TABLE forecast (
    id          SERIAL,
    rid         VARCHAR(16) NOT NULL,
    uid         VARCHAR(16) NOT NULL,
    ssd         VARCHAR(16) NOT NULL,
    active      BOOLEAN,
    deactive    BOOLEAN,
    xml         TEXT,
    dt          TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY(id)
);
CREATE UNIQUE INDEX forecast_r ON forecast(rid);
CREATE INDEX forecast_u ON forecast(uid);
CREATE INDEX forecast_s ON forecast(ssd);
CREATE INDEX forecast_d ON forecast(dt);
ALTER TABLE forecast OWNER TO rail;
ALTER TABLE forecast_id_seq OWNER TO rail;

-- ----------------------------------------------------------------------
-- Maps a forecast to the tiploc
-- ----------------------------------------------------------------------
CREATE TABLE forecast_entry (
    fid         BIGINT NOT NULL REFERENCES forecast(id),
    tpl         INTEGER NOT NULL REFERENCES tiploc(id)
);
CREATE UNIQUE INDEX forecast_entry_ft ON forecast_entry(fid,tpl);
CREATE INDEX forecast_entry_f ON forecast_entry(fid);
CREATE INDEX forecast_entry_t ON forecast_entry(tpl);
ALTER TABLE forecast_entry OWNER TO rail;

-- ----------------------------------------------------------------------
-- Handles the insertion & updates of a forecast
-- ----------------------------------------------------------------------
CREATE OR REPLACE FUNCTION darwin.forecast
  (
    pid         VARCHAR,
    puid        VARCHAR,
    pssd        VARCHAR,
    pactive     BOOLEAN,
    pdeactive   BOOLEAN,
    pxml        TEXT,
    ptpl        TEXT
  )
RETURNS VARCHAR AS $$
DECLARE
    rec     RECORD;
    atpl    TEXT;
    feid    BIGINT;
    aid     INTEGER;
BEGIN
    SELECT * INTO rec FROM darwin.forecast WHERE rid=pid;
    IF FOUND THEN
        UPDATE darwin.forecast SET uid=puid, ssd=pssd, active=pactive, deactive=pdeactive, xml=pxml, dt=now() WHERE rid=pid;
        feid=rec.id;
    ELSE
        INSERT INTO darwin.forecast (rid,uid,ssd,active,deactive,xml,dt) VALUES (pid,puid,pssd,pactive,pdeactive,pxml,now());
        feid=currval('darwin.forecast_id_seq');
    END IF;

    FOREACH atpl IN ARRAY string_to_array(ptpl,',')
    LOOP
        SELECT * INTO rec FROM darwin.tiploc WHERE tpl=atpl;
        IF FOUND THEN
            aid = rec.id;
        ELSE
            INSERT INTO darwin.tiploc (tpl) VALUES (atpl);
            aid = currval('darwin.tiploc_id_seq');
        END IF;

        SELECT * INTO rec FROM darwin.forecast_entry WHERE fid=feid AND tpl=aid;
        IF NOT FOUND THEN
            INSERT INTO darwin.forecast_entry (fid,tpl) VALUES (feid,aid);
        END IF;

    END LOOP;

    RETURN pid;
END;
$$ LANGUAGE plpgsql;
