SET search_path = timetable;

-- Drop tables in sequence
DROP TABLE schedule;
DROP TABLE association;

DROP TABLE trainuid;
DROP TABLE tiploc;

DROP TABLE lastupdate;

-- days of week
DROP TABLE daysrun;

-- enum mappings
DROP TABLE ATOCCode;
DROP TABLE ATSCode;
DROP TABLE Activity;
DROP TABLE AssociationCategory;
DROP TABLE AssociationDateIndicator;
DROP TABLE AssociationType;
DROP TABLE BankHolidayRunning;
DROP TABLE BusSec;
DROP TABLE Catering;
DROP TABLE OperatingCharacteristics;
DROP TABLE PowerType;
DROP TABLE Reservations;
DROP TABLE STPIndicator;
DROP TABLE ServiceBranding;
DROP TABLE Sleepers;
DROP TABLE TimingLoad;
DROP TABLE TrainCategory;
DROP TABLE TrainClass;
DROP TABLE TrainStatus;

-- ----------------------------------------------------------------------
--       Table: lastupdate
--
-- DescriptiON: Holds the date we last updated the schedule
--
--     History: 2014/10/20 Initial Version
-- ----------------------------------------------------------------------
CREATE TABLE lastupdate (
    id          SERIAL,
    extracted   TIMESTAMP NOT NULL,
    imported    TIMESTAMP NOT NULL,
    filename    TEXT,
    PRIMARY KEY (extracted)
);

ALTER TABLE lastupdate OWNER TO rail;
ALTER SEQUENCE lastupdate_id_seq OWNER TO rail;

-- ----------------------------------------------------------------------
--       Table: tiploc
--
-- DescriptiON: Holds the tiploc locations in the schedule
--
--     History: 2014/10/20 Initial Version
-- ----------------------------------------------------------------------

CREATE TABLE tiploc (
    id          SERIAL,
    tiploc      CHAR(7) not null,
    caps        INTEGER,
    nalco       INTEGER,
    nlccheck    CHAR(1),
    tpsdesc     VARCHAR(32),
    stanox      INTEGER,
    crs         CHAR(3),
    description VARCHAR(32)
);
ALTER TABLE tiploc OWNER TO rail;
ALTER SEQUENCE tiploc_id_seq OWNER TO rail;

CREATE UNIQUE INDEX tiploc_i ON tiploc(id);
CREATE UNIQUE INDEX tiploc_t ON tiploc(tiploc);

-- Some tiploc's share the same stanox so not unique.
CREATE INDEX tiploc_s ON tiploc(stanox) WHERE stanox IS NOT NULL;

-- Unique index of valid CRS records
CREATE UNIQUE INDEX tiploc_c ON tiploc(crs) WHERE crs IS NOT NULL;
CREATE UNIQUE INDEX tiploc_n ON tiploc(nalco);

-- Function to translate tiploc to it's bigint equivalent
CREATE OR REPLACE FUNCTION tiploc(loc CHAR(7))
RETURNS BIGINT AS $$
DECLARE
    tmp RECORD;
BEGIN
    SELECT * INTO tmp FROM timetable.tiploc WHERE tiploc=loc;
    IF FOUND THEN
        RETURN tmp.id;
    END IF;

    RAISE EXCEPTION 'Nonexistent tiploc %', loc
        USING HINT = 'Check tiplocs are up to date';
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------
--       Table: trainuid
--
-- Description: Normalises trainuid's
--
--     History: 2014/10/20 Initial Version
-- ----------------------------------------------------------------------

CREATE TABLE trainuid (
    id  SERIAL,
    uid CHAR(6) NOT NULL
);
ALTER TABLE trainuid OWNER TO rail;
ALTER SEQUENCE trainuid_id_seq OWNER TO rail;

CREATE UNIQUE INDEX trainuid_i ON trainuid(id);
CREATE UNIQUE INDEX trainuid_t ON trainuid(uid);

-- Function to translate tiploc to it's bigint equivalent
CREATE OR REPLACE FUNCTION trainuid(tid CHAR(6))
RETURNS BIGINT AS $$
DECLARE
    tmp RECORD;
BEGIN
    SELECT * INTO tmp FROM timetable.trainuid WHERE uid=tid;
    IF FOUND THEN
        RETURN tmp.id;
    END IF;

    INSERT INTO timetable.trainuid (uid) VALUES (tid);
    RETURN currval('timetable.trainuid_id_seq');
END;
$$ LANGUAGE plpgsql;

-- Function to translate tiploc to it's bigint equivalent
CREATE OR REPLACE FUNCTION trainuid(tid CHARACTER VARYING)
RETURNS BIGINT AS $$
DECLARE
    tmp RECORD;
BEGIN
    SELECT * INTO tmp FROM timetable.trainuid WHERE uid=tid;
    IF FOUND THEN
        RETURN tmp.id;
    END IF;

    INSERT INTO timetable.trainuid (uid) VALUES (tid);
    RETURN currval('timetable.trainuid_id_seq');
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------
--       Table: various
--
-- Description: Normalisation tables linked to several enums.
--
--        Note: most of these are unused by this db, but we still
--             initialise them anyhow for completeness.
--
--     History: 2014/10/20 Initial Version
-- ----------------------------------------------------------------------

CREATE TABLE IF NOT EXISTS ATOCCode ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE ATOCCode OWNER TO rail;

CREATE TABLE IF NOT EXISTS ATSCode ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE ATSCode OWNER TO rail;

CREATE TABLE IF NOT EXISTS Activity ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE Activity OWNER TO rail;

CREATE TABLE IF NOT EXISTS AssociationCategory ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE AssociationCategory OWNER TO rail;

CREATE TABLE IF NOT EXISTS AssociationDateIndicator ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE AssociationDateIndicator OWNER TO rail;

CREATE TABLE IF NOT EXISTS AssociationType ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE AssociationType OWNER TO rail;

CREATE TABLE IF NOT EXISTS BankHolidayRunning ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE BankHolidayRunning OWNER TO rail;

CREATE TABLE IF NOT EXISTS BusSec ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE BusSec OWNER TO rail;

CREATE TABLE IF NOT EXISTS Catering ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE Catering OWNER TO rail;

CREATE TABLE IF NOT EXISTS OperatingCharacteristics ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE OperatingCharacteristics OWNER TO rail;

CREATE TABLE IF NOT EXISTS PowerType ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE PowerType OWNER TO rail;

CREATE TABLE IF NOT EXISTS Reservations ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE Reservations OWNER TO rail;

CREATE TABLE IF NOT EXISTS STPIndicator ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE STPIndicator OWNER TO rail;

CREATE TABLE IF NOT EXISTS ServiceBranding ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE ServiceBranding OWNER TO rail;

CREATE TABLE IF NOT EXISTS Sleepers ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE Sleepers OWNER TO rail;

CREATE TABLE IF NOT EXISTS TimingLoad ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE TimingLoad OWNER TO rail;

CREATE TABLE IF NOT EXISTS TrainCategory ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE TrainCategory OWNER TO rail;

CREATE TABLE IF NOT EXISTS TrainClass ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE TrainClass OWNER TO rail;

CREATE TABLE IF NOT EXISTS TrainStatus ( id INTEGER, code VARCHAR, PRIMARY KEY (id) );
ALTER TABLE TrainStatus OWNER TO rail;

-- ----------------------------------------------------------------------
--       Table: dayrun
--
-- Description: Holds the associations for the days a train runs
--
--     History: 2014/10/20 Initial Version
-- ----------------------------------------------------------------------

CREATE TABLE daysrun (
    id          INTEGER,
    monday      BOOLEAN,
    tuesday     BOOLEAN,
    wednesday   BOOLEAN,
    thursday    BOOLEAN,
    friday      BOOLEAN,
    saturday    BOOLEAN,
    sunday      BOOLEAN,
    PRIMARY KEY (id)
);
ALTER TABLE daysrun OWNER TO rail;

-- ----------------------------------------------------------------------
--       Table: association
--
-- Description: Holds the associations between multiple train services
--
--     History: 2014/10/20 Initial Version
-- ----------------------------------------------------------------------

CREATE TABLE association (
    id              SERIAL,
    mainUid         BIGINT REFERENCES trainuid(id),
    assocUid        BIGINT REFERENCES trainuid(id),
    startdt         DATE,
    enddt           DATE,
    assocDays       INTEGER,
    assocCat        INTEGER REFERENCES AssociationCategory(id),
    assocDateInd    INTEGER REFERENCES AssociationDateIndicator(id),
    tiploc          BIGINT REFERENCES tiploc(id),
    baseLocSuff     CHAR(1),
    assocLocSuff    CHAR(1),
    assocType       INTEGER REFERENCES AssociationType(id),
    stpInd          INTEGER REFERENCES STPIndicator(id),
    PRIMARY KEY(
        mainUid, assocUid, startdt, enddt, assocDays, assocCat,
        assocDateInd, tiploc,
        baseLocSuff, assocLocSuff,
        assocType,
        stpInd
    )
);
ALTER TABLE association OWNER TO rail;
ALTER SEQUENCE association_id_seq OWNER TO rail;

CREATE INDEX association_m  ON association (mainuid);
CREATE INDEX association_a  ON association (assocuid);
CREATE INDEX association_ma ON association (mainuid,assocuid);
CREATE INDEX association_se ON association (startdt,enddt);
CREATE INDEX association_s  ON association (startdt);
CREATE INDEX association_e  ON association (enddt);

-- ----------------------------------------------------------------------
--       Table: schedule
--
-- Description: Holds an encoded schedule
--
--     History: 2014/10/20 Initial Version
-- ----------------------------------------------------------------------

CREATE TABLE schedule (
    id              SERIAL,
    -- The primary key - taken from BS Delete records in CIF
    trainuid        BIGINT REFERENCES trainuid(id),
    runsfrom        DATE NOT NULL,
    stpIndicator    INTEGER REFERENCES STPIndicator(id),
    -- Some details from BasicSchedule which might be useful
    runsto          DATE NOT NULL,
    dayrun          INTEGER REFERENCES daysrun(id),
    bankholrun      INTEGER REFERENCES BankHolidayRunning(id),
    trainidentity   CHAR(4),
    headcode        CHAR(4),
    -- Some details from BasicScheduleExtras
    atoccode        INTEGER REFERENCES ATOCCode(id),
    -- This is the full json content for this schedule
    schedule        TEXT
);
ALTER TABLE schedule OWNER TO rail;
ALTER SEQUENCE schedule_id_seq OWNER TO rail;

CREATE UNIQUE INDEX schedule_i ON schedule(id);
CREATE INDEX schedule_t ON schedule(trainuid);
CREATE INDEX schedule_f ON schedule(runsfrom);
CREATE INDEX schedule_e ON schedule(runsto);
CREATE INDEX schedule_fe ON schedule(runsfrom,runsto);
CREATE INDEX schedule_a ON schedule(atoccode);

-- ----------------------------------------------------------------------
--       Table: schedule_loc
--
-- Description: maps every Tiploc in a schedule
--
--     History: 2014/10/20 Initial Version
-- ----------------------------------------------------------------------

CREATE TABLE schedule_loc (
    scheduleid  BIGINT REFERENCES schedule(id),
    tiploc      BIGINT REFERENCES tiploc(id),
    PRIMARY KEY (scheduleid,tiploc)
);
ALTER TABLE schedule_loc OWNER TO rail;

CREATE UNIQUE INDEX schedule_loc_s ON schedule_loc(scheduleid);
CREATE INDEX schedule_loc_t ON schedule_loc(tiploc);
