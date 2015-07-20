-- ----------------------------------------------------------------------
-- TfL /Line/ API
--
-- ----------------------------------------------------------------------

--CREATE SCHEMA tfl;
SET search_path = tfl;

DROP TABLE boards;

DROP TABLE station_platform;
DROP TABLE platform;

DROP TABLE station_line;
DROP TABLE station;

DROP TABLE line;

DROP TABLE direction;

-- ----------------------------------------------------------------------
-- Direction, usually '', inbound or outbound
-- ----------------------------------------------------------------------
CREATE TABLE direction (
    id      SERIAL NOT NULL,
    dir     NAME,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX direction_d ON direction(dir);
ALTER TABLE direction OWNER TO rail;

CREATE OR REPLACE FUNCTION tfl.direction (pdir TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
    adir    NAME;
BEGIN
    IF pdir IS NULL THEN
        adir = '';
    ELSE
        adir = trim(pdir);
    END IF;
    LOOP
        SELECT * INTO rec FROM tfl.direction WHERE dir=adir;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO tfl.direction (dir) VALUES (adir);
            RETURN currval('tfl.direction_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------
-- Lines
-- ----------------------------------------------------------------------

CREATE TABLE line (
    id      SERIAL NOT NULL,
    code    NAME NOT NULL,
    name    NAME NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX line_c ON line(code);
CREATE INDEX line_n ON line(name);
ALTER TABLE line OWNER TO rail;

CREATE OR REPLACE FUNCTION tfl.line (plineid TEXT, pline TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
BEGIN
    LOOP
        SELECT * INTO rec FROM tfl.line WHERE code=plineid;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO tfl.line (code,name) VALUES (plineid,pline);
            RETURN currval('tfl.line_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------
-- stations
-- ----------------------------------------------------------------------
CREATE TABLE station (
    id      SERIAL NOT NULL,
    naptan  NAME NOT NULL,
    name    NAME NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX station_n1 ON station(naptan);
CREATE INDEX station_n2 ON station(name);

ALTER TABLE station OWNER TO rail;

-- General function to insert a station/destination
CREATE OR REPLACE FUNCTION tfl.station (pnaptan TEXT, pname TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
    aname   TEXT;
BEGIN
    -- Feed has unwanted text on the end so remove it
    aname = regexp_replace( pname, ' (DLR|Underground) Station$', '');

    LOOP
        SELECT * INTO rec FROM tfl.station WHERE naptan=pnaptan;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO tfl.station (naptan,name) VALUES (pnaptan,aname);
            RETURN currval('tfl.station_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE station_line (
    lineid      INTEGER NOT NULL REFERENCES tfl.line(id),
    stationid   INTEGER NOT NULL REFERENCES tfl.station(id),
    PRIMARY KEY (lineid,stationid)
);

ALTER TABLE station_line OWNER TO rail;

-- Utility function to insert a primary station
CREATE OR REPLACE FUNCTION tfl.station (plineid TEXT,pline TEXT,pnaptan TEXT, pname TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec         RECORD;
    alineid     INTEGER;
    astationid  INTEGER;
BEGIN
    alineid = tfl.line( plineid, pline );
    astationid = tfl.station( pnaptan, pname );

    LOOP
        SELECT * INTO REC FROM tfl.station_line WHERE lineid=alineid AND stationid=astationid LIMIT 1;
        IF FOUND THEN
            RETURN astationid;
        END IF;
        BEGIN
            INSERT INTO tfl.station_line (lineid,stationid) VALUES (alineid,astationid);
            RETURN astationid;
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------
-- Prepopulate station from NAPTAN
CREATE OR REPLACE FUNCTION tfl.populate()
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
    cnt     INTEGER;
BEGIN
    cnt = 0;

    FOR rec IN SELECT * FROM reference.naptan_stopareas
        WHERE stopareatype = 'GTMU'
            AND (name LIKE '%Underground Station' OR name LIKE '%DLR Station')
        ORDER BY name
    LOOP
        PERFORM tfl.station(rec.code,rec.name);
        cnt=cnt+1;
    END LOOP;

    RETURN cnt;
END;
$$ LANGUAGE plpgsql;
SELECT tfl.populate();
DROP FUNCTION tfl.populate();

-- ----------------------------------------------------------------------

-- ----------------------------------------------------------------------
-- Platforms
-- ----------------------------------------------------------------------
CREATE TABLE platform (
    id          SERIAL NOT NULL,
    plat        INTEGER NOT NULL DEFAULT 0,
    name        NAME NOT NULL,
    fullname    NAME NOT NULL,
    PRIMARY KEY(id)
);
CREATE INDEX platform_p ON platform(plat);
CREATE INDEX platform_n ON platform(name);
CREATE UNIQUE INDEX platform_f ON platform(fullname);

ALTER TABLE platform OWNER TO rail;

CREATE TABLE station_platform (
    id          SERIAL NOT NULL,
    stationid   INTEGER NOT NULL REFERENCES tfl.station(id),
    platid      INTEGER NOT NULL REFERENCES tfl.platform(id),
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX station_platform_sp ON station_platform(stationid,platid);

ALTER TABLE station_platform OWNER TO rail;

CREATE OR REPLACE FUNCTION tfl.platform (pplat TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
    tplat   NAME;
    aplat   INTEGER;
    aname   NAME;
BEGIN
    tplat=trim(pplat);
    LOOP
        SELECT * INTO rec FROM tfl.platform WHERE fullname=tplat;
        IF FOUND THEN
            RETURN rec.id;
        END IF;

        -- Try to extract the platform number at the station.
        aname = substring(tplat FROM '[0-9a-zA-Z]+$');
        aplat = 0;
        BEGIN
            -- Some platforms are just integer
            aplat = substring(aname FROM '^[0-9]+')::INTEGER;
        EXCEPTION WHEN invalid_text_representation THEN
            -- TfL seems to put it at the end of the platform name
            BEGIN
                aplat = substring(tplat FROM '.{2}$')::INTEGER;
            EXCEPTION WHEN invalid_text_representation THEN
                -- Invalid so set to 0
                aplat = 0;
            END;
        END;
        IF aplat IS NULL THEN aplat = 0; END IF;

        BEGIN
            INSERT INTO tfl.platform (plat,name,fullname) VALUES (aplat,aname,tplat);
            RETURN currval('tfl.platform_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION tfl.platform (pstationid INTEGER, pplat TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
    aplatid INTEGER;
BEGIN
    aplatid = tfl.platform(pplat);

    LOOP
        SELECT * INTO rec FROM tfl.station_platform WHERE stationid=pstationid AND platid=aplatid;
        IF FOUND THEN
            RETURN rec.id;
        END IF;

        BEGIN
            INSERT INTO tfl.station_platform (stationid,platid) VALUES (pstationid,aplatid);
            RETURN currval('tfl.station_platform_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------
-- Boards
-- ----------------------------------------------------------------------
CREATE TABLE boards (
    id          SERIAL NOT NULL,
    platid      INTEGER NOT NULL REFERENCES tfl.station_platform(id),
    lineid      INTEGER NOT NULL REFERENCES tfl.line(id),
    dest        INTEGER REFERENCES tfl.station(id),
    due         INTEGER NOT NULL DEFAULT -99,
    ts          TIMESTAMP WITH TIME ZONE,
    expt        TIMESTAMP WITH TIME ZONE,
    dir         INTEGER NOT NULL REFERENCES tfl.direction(id),
    curloc      TEXT,
    towards     TEXT,
    optype      NAME,
    vehicleid   NAME,
    mode        NAME,
    PRIMARY KEY (id)
);
CREATE INDEX boards_p ON boards(platid);
CREATE INDEX boards_pd ON boards(platid,due);
CREATE INDEX boards_pe ON boards(platid,expt);

ALTER TABLE boards OWNER TO rail;
