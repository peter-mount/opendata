-- ----------------------------------------------------------------------
-- Darwin Forecasts
--
-- Version  1 2015/04/14 Initial version
-- ----------------------------------------------------------------------

--CREATE SCHEMA darwin;
SET search_path = darwin;


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
-- Train forecast
-- ----------------------------------------------------------------------
CREATE TABLE forecast (
    id          SERIAL,
    rid         VARCHAR(16) NOT NULL,
    dt          TIMESTAMP WITHOUT TIME ZONE,
    xml         TEXT,
    PRIMARY KEY(id)
);
CREATE UNIQUE INDEX forecast_r ON forecast(rid);
CREATE INDEX forecast_d ON forecast(dt);
ALTER TABLE forecast OWNER TO rail;
ALTER TABLE forecast_id_seq OWNER TO rail;

-- ----------------------------------------------------------------------
-- Maps a forecast to the tiploc & contains the forecast details
-- needed by ldb
-- ----------------------------------------------------------------------
CREATE TABLE forecast_entry (
    fid         BIGINT NOT NULL REFERENCES forecast(id),
    tpl         INTEGER NOT NULL REFERENCES tiploc(id),
    -- Suppress this entry?
    supp        BOOLEAN DEFAULT false,
    -- From one of the figures
    arr         TIME,
    dep         TIME,
    pass        TIME,
    -- Platform
    plat        VARCHAR(4),
    platsup     BOOLEAN DEFAULT false,
    --
    PRIMARY KEY (fid,tpl)
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
    pxml        XML
  )
RETURNS VARCHAR AS $$
DECLARE
    -- Pushport namespaces
    ns          TEXT[][] := ARRAY[
                ['fcst','http://www.thalesgroup.com/rtti/PushPort/Forecasts/v2']
                ];
    rec         RECORD;
    tpls        TEXT[];
    atpl        TEXT;
    feid        BIGINT;
    axml        XML;
    aid         INTEGER;
    arec        RECORD;
BEGIN

    -- Update the forecast table
    LOOP
        SELECT * INTO rec FROM darwin.forecast WHERE rid=pid;
        IF FOUND THEN
            UPDATE darwin.forecast SET xml=pxml, dt=now() WHERE rid=pid;
            feid=rec.id;
            EXIT;
        ELSE
            BEGIN
                INSERT INTO darwin.forecast (rid,xml,dt) VALUES (pid,pxml,now());
                feid=currval('darwin.forecast_id_seq');
            EXCEPTION WHEN unique_violation THEN
                -- Do nothing, loop & try again.
                -- This should not happen on live as its sequential but load testing does cause this
                -- however if this does happen then we may have inconsistent xml
            END;
        END IF;
    END LOOP;

    -- Update the forecast_entry table
    FOREACH axml IN ARRAY xpath('//fcst:Location',pxml,ns)
    LOOP
        -- extract data from each location element
        SELECT  (xpath('//@tpl',axml,ns))[1]::text AS tpl,
                (xpath('//fcst:plat/text()',axml,ns))[1]::text AS plat,
                (xpath('//fcst:plat/@platsup',axml,ns))[1]::text::boolean AS platsup
            INTO arec LIMIT 1;

        aid = darwin.tiploc(arec.tpl);

        -- resolve/create the tiploc
        LOOP
            SELECT * INTO rec FROM darwin.forecast_entry WHERE fid=feid AND tpl=aid;
            IF FOUND THEN
                UPDATE darwin.forecast_entry
                    SET plat=arec.plat,
                        platsup=arec.platsup
                    WHERE fid=feid AND tpl=aid;
                EXIT;
            ELSE
                BEGIN
                    INSERT INTO darwin.forecast_entry
                        (fid,tpl,plat,platsup)
                        VALUES (feid,aid,
                            arec.plat,
                            arec.platsup
                        );
                EXCEPTION WHEN unique_violation THEN
                    -- Do nothing, loop & try again.
                    -- This should not happen on live as its sequential but load testing does cause this
                    -- however if this does happen then we may have inconsistent xml
                END;
            END IF;
        END LOOP;

--        INSERT INTO darwin.forecast_entry (fid,tpl)
--            SELECT feid,aid WHERE NOT EXISTS ( SELECT 1 FROM darwin.forecast_entry  WHERE fid=feid AND tpl=aid );

    END LOOP;

    RETURN pid;
END;
$$ LANGUAGE plpgsql;
