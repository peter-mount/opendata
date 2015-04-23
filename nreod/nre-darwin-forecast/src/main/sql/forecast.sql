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

CREATE OR REPLACE FUNCTION darwin.tiploc (ptpl TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
BEGIN
    LOOP
        SELECT * INTO rec FROM darwin.tiploc WHERE tpl=ptpl;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO darwin.tiploc (tpl) VALUES (ptpl);
            RETURN currval('darwin.tiploc_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

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
    LOOP
        SELECT * INTO rec FROM darwin.forecast WHERE rid=pid;
        IF FOUND THEN
            UPDATE darwin.forecast SET uid=puid, ssd=pssd, active=pactive, deactive=pdeactive, xml=pxml, dt=now() WHERE rid=pid;
            feid=rec.id;
            EXIT;
        ELSE
            BEGIN
                INSERT INTO darwin.forecast (rid,uid,ssd,active,deactive,xml,dt) VALUES (pid,puid,pssd,pactive,pdeactive,pxml,now());
                feid=currval('darwin.forecast_id_seq');
            EXCEPTION WHEN unique_violation THEN
                -- Do nothing, loop & try again.
                -- This should not happen on live as its sequential but load testing does cause this
                -- however if this does happen then we may have inconsistent xml
            END;
        END IF;
    END LOOP;

    -- Create a link to each tiploc
    FOREACH atpl IN ARRAY string_to_array(ptpl,',')
    LOOP
        aid = darwin.tiploc(atpl);

        INSERT INTO darwin.forecast_entry (fid,tpl)
            SELECT feid,aid WHERE NOT EXISTS ( SELECT 1 FROM darwin.forecast_entry  WHERE fid=feid AND tpl=aid );

    END LOOP;

    RETURN pid;
END;
$$ LANGUAGE plpgsql;
