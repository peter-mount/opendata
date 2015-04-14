-- ----------------------------------------------------------------------
-- Darwin Station Messages
--
-- Version  1 2015/04/14 Initial version
-- ----------------------------------------------------------------------

--CREATE SCHEMA darwin;
SET search_path = darwin;

DROP TABLE message_station;
DROP TABLE message;
DROP TABLE station;

-- ----------------------------------------------------------------------
-- Normalisation table of stations
-- ----------------------------------------------------------------------
CREATE TABLE station (
    id          SERIAL,
    crs         CHAR(3),
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX station_c ON station(crs);

-- ----------------------------------------------------------------------
-- Station message
-- ----------------------------------------------------------------------

CREATE TABLE message (
    id          BIGINT NOT NULL,
    cat         VARCHAR(16) NOT NULL,
    sev         VARCHAR(16) NOT NULL,
    suppress    BOOLEAN,
    xml         TEXT,
    dt          TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id)
);
CREATE INDEX message_d ON message(dt);

-- ----------------------------------------------------------------------
-- Links message to station
-- ----------------------------------------------------------------------

CREATE TABLE message_station (
    msgid       BIGINT NOT NULL REFERENCES message(id),
    stationid   BIGINT NOT NULL REFERENCES station(id)
);
CREATE UNIQUE INDEX message_station_ms ON message_station(msgid,stationid);
CREATE INDEX message_station_m ON message_station(msgid);
CREATE INDEX message_station_s ON message_station(stationid);

-- ----------------------------------------------------------------------
-- Handles the insertion & updates of station messages from Darwin
-- ----------------------------------------------------------------------

CREATE OR REPLACE FUNCTION darwin.message
  (
    pid INTEGER,
    pcat TEXT,
    psev TEXT,
    psup BOOLEAN,
    pxml TEXT,
    pcrs TEXT
  )
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
    acrs    TEXT;
    sid     INTEGER;
BEGIN
    SELECT * INTO rec FROM darwin.message WHERE id=pid;
    IF FOUND THEN
        UPDATE darwin.message SET cat=pcat, sev=psev, suppress=psup, xml=pxml, dt=now() WHERE id=pid;
    ELSE
        INSERT INTO darwin.message (id,cat,sev,suppress,xml,dt) VALUES (pid,pcat,psev,psup,pxml,now()); 
    END IF;

    DELETE FROM darwin.message_station WHERE msgid=pid;

    FOREACH acrs IN ARRAY string_to_array(pcrs,',')
    LOOP
        SELECT * INTO rec FROM darwin.station WHERE crs=acrs;
        IF FOUND THEN
            sid = rec.id;
        ELSE
            INSERT INTO darwin.station (crs) VALUES (acrs);
            sid = currval('darwin.station_id_seq');
        END IF;

        INSERT INTO darwin.message_station (msgid,stationid) VALUES (pid,sid);
    END LOOP;

    RETURN pid;
END;
$$ LANGUAGE plpgsql;

