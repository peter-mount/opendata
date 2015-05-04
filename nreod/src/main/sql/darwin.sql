-- ----------------------------------------------------------------------
-- Darwin Analyser
--
-- This set of SQL parses the raw Pport xml directly into the database
--
-- ----------------------------------------------------------------------

--CREATE SCHEMA darwin;
SET search_path = darwin;

DROP TABLE trainorder;

DROP TABLE alarms;

DROP TABLE message_station;
DROP TABLE message;

DROP TABLE forecast_entry;
DROP TABLE forecast;

DROP TABLE forecast_entryarc;
DROP TABLE forecastarc;

DROP TABLE schedule_assoc;
DROP TABLE schedule_entry;
DROP TABLE schedule;

DROP TABLE schedule_assocarc;
DROP TABLE schedule_entryarc;
DROP TABLE schedulearc;

DROP TABLE crs;
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
-- Normalisation table of crs
-- ----------------------------------------------------------------------
CREATE TABLE crs (
    id          SERIAL,
    crs         CHAR(3),
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX crs_c ON crs(crs);
ALTER TABLE crs OWNER TO rail;
ALTER TABLE crs_id_seq OWNER TO rail;

CREATE OR REPLACE FUNCTION darwin.crs (pcrs TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
BEGIN
    LOOP
        SELECT * INTO rec FROM darwin.crs WHERE crs=pcrs;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO darwin.crs (crs) VALUES (pcrs);
            RETURN currval('darwin.crs_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------
-- Schedule
--
-- This comprises two sets of tables, the active one forecast and
-- an archive table forecastarc. The second set of tables are used when
-- a train is deactivated.
--
-- This is so that the live updates operate on a smaller table, keeping
-- performance up.
-- ----------------------------------------------------------------------

CREATE TABLE schedule (
    id          SERIAL,
    rid         VARCHAR(16) NOT NULL,
    uid         VARCHAR(16) NOT NULL,
    ssd         DATE,
    ts          TIMESTAMP WITH TIME ZONE,
    trainId     CHAR(4),
    toc         CHAR(2),
    origin      INTEGER NOT NULL REFERENCES tiploc(id),
    dest        INTEGER NOT NULL REFERENCES tiploc(id),
    PRIMARY KEY(id)
);
CREATE INDEX schedule_r ON schedule(rid);
CREATE INDEX schedule_t ON schedule(ts);
CREATE INDEX schedule_h ON schedule(trainId);
CREATE INDEX schedule_sh ON schedule(ssd,trainId);
CREATE INDEX schedule_so ON schedule(ssd,toc);
CREATE INDEX schedule_o ON schedule(toc);
ALTER TABLE schedule OWNER TO rail;
ALTER TABLE schedule_id_seq OWNER TO rail;

CREATE TABLE schedulearc (
    id          BIGINT NOT NULL,
    rid         VARCHAR(16) NOT NULL,
    uid         VARCHAR(16) NOT NULL,
    ssd         DATE,
    ts          TIMESTAMP WITH TIME ZONE,
    trainId     CHAR(4),
    toc         CHAR(2),
    origin      INTEGER NOT NULL REFERENCES tiploc(id),
    dest        INTEGER NOT NULL REFERENCES tiploc(id),
    PRIMARY KEY(id)
);
CREATE INDEX schedulearc_r ON schedulearc(rid);
CREATE INDEX schedulearc_t ON schedulearc(ts);
CREATE INDEX schedulearc_h ON schedulearc(trainId);
CREATE INDEX schedulearc_sh ON schedulearc(ssd,trainId);
CREATE INDEX schedulearc_so ON schedulearc(ssd,toc);
CREATE INDEX schedulearc_o ON schedulearc(toc);
ALTER TABLE schedulearc OWNER TO rail;

CREATE TABLE schedule_entry (
    id          SERIAL,
    schedule    BIGINT NOT NULL REFERENCES schedule(id),
    type        NAME,
    tpl         INTEGER NOT NULL REFERENCES tiploc(id),
    pta         TIME,
    ptd         TIME,
    wta         TIME,
    wtd         TIME,
    wtp         TIME,
    act         NAME,
    PRIMARY KEY(id)
);
CREATE INDEX schedule_entry_s ON schedule_entry(schedule);
ALTER TABLE schedule_entry OWNER TO rail;
ALTER TABLE schedule_entry_id_seq OWNER TO rail;

CREATE TABLE schedule_entryarc (
    id          BIGINT NOT NULL,
    schedule    BIGINT NOT NULL REFERENCES schedulearc(id),
    type        NAME,
    tpl         INTEGER NOT NULL REFERENCES tiploc(id),
    pta         TIME,
    ptd         TIME,
    wta         TIME,
    wtd         TIME,
    wtp         TIME,
    act         NAME,
    PRIMARY KEY(id)
);
CREATE INDEX schedule_entryarc_s ON schedule_entryarc(schedule);
ALTER TABLE schedule_entryarc OWNER TO rail;

CREATE TABLE schedule_assoc (
    mainid      BIGINT NOT NULL REFERENCES schedule(id),
    associd     BIGINT NOT NULL REFERENCES schedule(id),
    tpl         INTEGER NOT NULL REFERENCES tiploc(id),
    cat         CHAR(2),
    cancelled   BOOLEAN DEFAULT false,
    deleted     BOOLEAN DEFAULT false,
    -- main arrival
    pta         TIME,
    wta         TIME,
    -- assoc depart
    ptd         TIME,
    wtd         TIME,
    PRIMARY KEY (mainid,associd)
);
CREATE INDEX schedule_assoc_t ON schedule_assoc(tpl);
CREATE INDEX schedule_assoc_m ON schedule_assoc(mainid);
CREATE INDEX schedule_assoc_a ON schedule_assoc(associd);
ALTER TABLE schedule_assoc OWNER TO rail;

-- No references as one of these may not be archived when this entry is created
CREATE TABLE schedule_assocarc (
    mainid      BIGINT, -- NOT NULL REFERENCES schedulearc(id),
    associd     BIGINT, -- NOT NULL REFERENCES schedulearc(id),
    tpl         INTEGER NOT NULL REFERENCES tiploc(id),
    cat         CHAR(2),
    cancelled   BOOLEAN DEFAULT false,
    deleted     BOOLEAN DEFAULT false,
    -- main arrival
    pta         TIME,
    wta         TIME,
    -- assoc depart
    ptd         TIME,
    wtd         TIME,
    PRIMARY KEY (mainid,associd)
);
CREATE INDEX schedule_assocarc_t ON schedule_assocarc(tpl);
CREATE INDEX schedule_assocarc_m ON schedule_assocarc(mainid);
CREATE INDEX schedule_assocarc_a ON schedule_assocarc(associd);
ALTER TABLE schedule_assocarc OWNER TO rail;

-- ----------------------------------------------------------------------
-- Train forecast
--
-- This comprises two sets of tables, the active one forecast and
-- an archive table forecastarc. The second set of tables are used when
-- a train is deactivated.
--
-- This is so that the live updates operate on a smaller table, keeping
-- performance up.
-- ----------------------------------------------------------------------

CREATE TABLE forecast (
    id          SERIAL,
    rid         VARCHAR(16) NOT NULL,
    uid         VARCHAR(16) NOT NULL,
    ssd         DATE,
    ts          TIMESTAMP WITH TIME ZONE,
    latereason  INTEGER,
    activated   BOOLEAN DEFAULT false,
    deactivated BOOLEAN DEFAULT false,
    schedule    BIGINT REFERENCES schedule(id),
    PRIMARY KEY(id)
);
CREATE INDEX forecast_r ON forecast(rid);
CREATE INDEX forecast_t ON forecast(ts);
ALTER TABLE forecast OWNER TO rail;
ALTER TABLE forecast_id_seq OWNER TO rail;

CREATE TABLE forecastarc (
    id          BIGINT NOT NULL,
    rid         VARCHAR(16) NOT NULL,
    uid         VARCHAR(16) NOT NULL,
    ssd         DATE,
    ts          TIMESTAMP WITH TIME ZONE,
    latereason  INTEGER,
    activated   BOOLEAN DEFAULT false,
    deactivated BOOLEAN DEFAULT false,
    schedulearc    BIGINT REFERENCES schedulearc(id),
    PRIMARY KEY(id)
);
CREATE INDEX forecastarc_r ON forecastarc(rid);
CREATE INDEX forecastarc_t ON forecastarc(ts);
ALTER TABLE forecastarc OWNER TO rail;

-- ----------------------------------------------------------------------
-- Maps a forecast to the tiploc & contains the forecast details
-- needed by ldb
-- ----------------------------------------------------------------------
CREATE TABLE forecast_entry (
    fid         BIGINT NOT NULL REFERENCES forecast(id),
    tpl         INTEGER NOT NULL REFERENCES tiploc(id),
    -- Suppress this entry?
    supp        BOOLEAN DEFAULT false,
    -- timetable entry
    pta         TIME,
    ptd         TIME,
    wta         TIME,
    wtd         TIME,
    wtp         TIME,
    -- calculated delat
    delay       INTERVAL,
    -- Actual times
    arr         TIME,
    dep         TIME,
    pass        TIME,
    -- Estimated times
    etarr       TIME,
    etdep       TIME,
    etpass      TIME,
    -- Platform
    plat        VARCHAR(4),
    platsup     BOOLEAN DEFAULT false,
    cisplatsup  BOOLEAN DEFAULT false,
    platsrc     NAME,
    --
    PRIMARY KEY (fid,tpl)
);
CREATE UNIQUE INDEX forecast_entry_ft ON forecast_entry(fid,tpl);
CREATE INDEX forecast_entry_f ON forecast_entry(fid);
CREATE INDEX forecast_entry_t ON forecast_entry(tpl);
ALTER TABLE forecast_entry OWNER TO rail;

CREATE TABLE forecast_entryarc (
    fid         BIGINT NOT NULL REFERENCES forecastarc(id),
    tpl         INTEGER NOT NULL REFERENCES tiploc(id),
    -- Suppress this entry?
    supp        BOOLEAN DEFAULT false,
    -- timetable entry
    pta         TIME,
    ptd         TIME,
    wta         TIME,
    wtd         TIME,
    wtp         TIME,
    -- calculated delat
    delay       INTERVAL,
    -- Actual times
    arr         TIME,
    dep         TIME,
    pass        TIME,
    -- Estimated times
    etarr       TIME,
    etdep       TIME,
    etpass      TIME,
    -- Platform
    plat        VARCHAR(4),
    platsup     BOOLEAN DEFAULT false,
    cisplatsup  BOOLEAN DEFAULT false,
    platsrc     NAME,
    --
    PRIMARY KEY (fid,tpl)
);
CREATE UNIQUE INDEX forecast_entryarc_ft ON forecast_entryarc(fid,tpl);
CREATE INDEX forecast_entryarc_f ON forecast_entryarc(fid);
CREATE INDEX forecast_entryarc_t ON forecast_entryarc(tpl);
ALTER TABLE forecast_entryarc OWNER TO rail;

DROP TABLE log;
CREATE TABLE log (t TEXT);
ALTER TABLE log OWNER TO rail;

-- ----------------------------------------------------------------------
-- Station messages
-- ----------------------------------------------------------------------

CREATE TABLE message (
    id          BIGINT NOT NULL,
    cat         VARCHAR(16) NOT NULL,
    sev         VARCHAR(16) NOT NULL,
    suppress    BOOLEAN,
    xml         TEXT,
    ts          TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);
CREATE INDEX message_t ON message(ts);
ALTER TABLE message OWNER TO rail;

-- Link message to stations

CREATE TABLE message_station (
    msgid   BIGINT NOT NULL REFERENCES message(id),
    crsid   BIGINT NOT NULL REFERENCES crs(id)
);
CREATE UNIQUE INDEX message_station_mc ON message_station(msgid,crsid);
CREATE INDEX message_station_m ON message_station(msgid);
CREATE INDEX message_station_c ON message_station(crsid);
ALTER TABLE message_station OWNER TO rail;

-- ----------------------------------------------------------------------
-- Alarms
-- ----------------------------------------------------------------------

CREATE TABLE alarms (
    id          SERIAL NOT NULL,
    aid         NAME,
    -- When the alarm was set
    setts       TIMESTAMP WITH TIME ZONE,
    -- has it been cleared and when
    cleared     BOOLEAN DEFAULT false,
    clearedts   TIMESTAMP WITH TIME ZONE,
    -- type of alarm
    type        NAME,
    -- XML of the alarm
    xml         TEXT,
    PRIMARY KEY (id)
);
ALTER TABLE alarms OWNER TO rail;

-- ----------------------------------------------------------------------
-- Train Order
-- ----------------------------------------------------------------------
CREATE TABLE trainorder (
    id          SERIAL NOT NULL,
    tpl         INTEGER REFERENCES tiploc(id),
    crs         INTEGER REFERENCES crs(id),
    plat        NAME,
    ts          TIMESTAMP WITH TIME ZONE,
    -- first
    rid1        VARCHAR(16),
    tid1        CHAR(4),
    pta1        TIME,
    wta1        TIME,
    ptd1        TIME,
    wtd1        TIME,
    -- second
    rid2        VARCHAR(16),
    tid2        CHAR(4),
    pta2        TIME,
    wta2        TIME,
    ptd2        TIME,
    wtd2        TIME,
    -- third
    rid3        VARCHAR(16),
    tid3        CHAR(4),
    pta3        TIME,
    wta3        TIME,
    ptd3        TIME,
    wtd3        TIME,
    PRIMARY KEY(id)
);
CREATE INDEX trainorder_t ON trainorder(tpl);
CREATE INDEX trainorder_c ON trainorder(crs);
CREATE INDEX trainorder_r1 ON trainorder(rid1);
CREATE INDEX trainorder_t1 ON trainorder(tid1);
CREATE INDEX trainorder_r2 ON trainorder(rid2);
CREATE INDEX trainorder_t2 ON trainorder(tid2);
CREATE INDEX trainorder_r3 ON trainorder(rid3);
CREATE INDEX trainorder_t3 ON trainorder(tid3);
ALTER TABLE trainorder OWNER TO rail;
