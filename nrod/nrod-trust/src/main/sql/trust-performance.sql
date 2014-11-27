-- ======================================================================
-- DDL for trust performance
--
-- ======================================================================

CREATE SCHEMA report;
SET search_path = report;

-- ======================================================================
-- perf_stanox_all - The performance at a specific stanox for all operators
-- ======================================================================

DROP TABLE perf_stanox_all CASCADE;

CREATE TABLE perf_stanox_all (
    dt_stanox   BIGINT NOT NULL REFERENCES report.dim_date_stanox(dt_stanox),
    trainCount  INTEGER DEFAULT 0,
    totalDelay  INTEGER DEFAULT 0,
    delayCount  INTEGER DEFAULT 0,
    minDelay    INTEGER DEFAULT 9999999,
    maxDelay    INTEGER DEFAULT 0,
    aveDelay    INTEGER,
    earlyCount  INTEGER DEFAULT 0,
    maxEarly    INTEGER DEFAULT 0,
    ontime      INTEGER DEFAULT 0,
    ppmearly    INTEGER DEFAULT 0,
    ppm5        INTEGER DEFAULT 0,
    ppm10       INTEGER DEFAULT 0,
    ppm30       INTEGER DEFAULT 0,
    PRIMARY KEY (dt_stanox)
);

ALTER TABLE perf_stanox_all OWNER TO rail;

-- ======================================================================
-- perf_stanox_toc - The performance at a specific stanox for a
--                   specific operator
-- ======================================================================

DROP TABLE perf_stanox_toc CASCADE;

CREATE TABLE perf_stanox_toc (
    dt_stanox   BIGINT NOT NULL REFERENCES report.dim_date_stanox(dt_stanox),
    operatorid  INTEGER NOT NULL REFERENCES report.dim_operator(operatorid),
    trainCount  INTEGER DEFAULT 0,
    totalDelay  INTEGER DEFAULT 0,
    delayCount  INTEGER DEFAULT 0,
    minDelay    INTEGER DEFAULT 9999999,
    maxDelay    INTEGER DEFAULT 0,
    aveDelay    INTEGER,
    earlyCount  INTEGER DEFAULT 0,
    maxEarly    INTEGER DEFAULT 0,
    ontime      INTEGER DEFAULT 0,
    ppmearly    INTEGER DEFAULT 0,
    ppm5        INTEGER DEFAULT 0,
    ppm10       INTEGER DEFAULT 0,
    ppm30       INTEGER DEFAULT 0,
    PRIMARY KEY (dt_stanox,operatorid)
);

ALTER TABLE perf_stanox_toc OWNER TO rail;

CREATE INDEX perf_stanox_toc_d ON perf_stanox_toc(dt_stanox);
CREATE INDEX perf_stanox_toc_o ON perf_stanox_toc(operatorid);

-- ======================================================================
-- perf_stanox_toc - The performance at a specific stanox for a
--                   specific operator and train class
-- ======================================================================

DROP TABLE perf_stanox_toc_class CASCADE;

CREATE TABLE perf_stanox_toc_class (
    dt_stanox   BIGINT NOT NULL REFERENCES report.dim_date_stanox(dt_stanox),
    operatorid  INTEGER NOT NULL REFERENCES report.dim_operator(operatorid),
    trainCount  INTEGER DEFAULT 0,
    totalDelay  INTEGER DEFAULT 0,
    delayCount  INTEGER DEFAULT 0,
    minDelay    INTEGER DEFAULT 9999999,
    maxDelay    INTEGER DEFAULT 0,
    aveDelay    INTEGER,
    earlyCount  INTEGER DEFAULT 0,
    maxEarly    INTEGER DEFAULT 0,
    ontime      INTEGER DEFAULT 0,
    ppmearly    INTEGER DEFAULT 0,
    ppm5        INTEGER DEFAULT 0,
    ppm10       INTEGER DEFAULT 0,
    ppm30       INTEGER DEFAULT 0,
    PRIMARY KEY (dt_stanox,operatorid, trainClass)
);

ALTER TABLE perf_stanox_toc_class OWNER TO rail;

CREATE INDEX perf_stanox_toc_class_do ON perf_stanox_toc_class(dt_stanox,operatorid);
CREATE INDEX perf_stanox_toc_class_d ON perf_stanox_toc_class(dt_stanox);
CREATE INDEX perf_stanox_toc_class_o ON perf_stanox_toc_class(operatorid);
CREATE INDEX perf_stanox_toc_class_dc ON perf_stanox_toc_class(dt_stanox,trainclass);
CREATE INDEX perf_stanox_toc_class_oc ON perf_stanox_toc_class(operatorid,trainclass);

-- ======================================================================
-- Function to record the performance at a specific stanox.
--
-- This one function manages all of the above tables, so with one call
-- everything is updated.
--
-- Parameters:
--  pdts    The dim_dt_stanox value
--  popid   The operatorId
--  ptclass The train class
--  pdelay  The delay in seconds
--
-- ======================================================================

CREATE OR REPLACE FUNCTION report.perf_stanox
    ( pdts BIGINT, popid INTEGER, ptclass INTEGER, pdelay INTEGER )
RETURNS VOID AS $$
DECLARE
    rec     RECORD;
BEGIN

    -- All operators
    SELECT * INTO rec FROM report.perf_stanox_all WHERE dt_stanox = pdts;
    IF FOUND THEN
        IF pdelay = 0 THEN
            UPDATE report.perf_stanox_all
                SET trainCount = rec.trainCount + 1,
                    ontime = rec.ontime + 1
                WHERE dt_stanox = pdts;
        ELSIF pdelay > 0 THEN
            UPDATE report.perf_stanox_all
                SET trainCount = rec.trainCount + 1,
                    delayCount = rec.delayCount + 1,
                    totaldelay = rec.totaldelay + pdelay,
                    minDelay = LEAST( rec.minDelay, pdelay),
                    maxDelay = GREATEST( rec.maxDelay, pdelay)
                WHERE dt_stanox = pdts;
        ELSE
            UPDATE report.perf_stanox_all
                SET trainCount = rec.trainCount + 1,
                    earlyCount = rec.earlyCount + 1,
                    maxEarly = GREATEST( rec.maxEarly, -pdelay )
                WHERE dt_stanox = pdts;
        END IF;
    ELSE
        IF pdelay = 0 THEN
            INSERT INTO report.perf_stanox_all
                (dt_stanox, trainCount, ontime)
                VALUES ( pdts, 1, 1);
        ELSIF pdelay > 0 THEN
            INSERT INTO report.perf_stanox_all
                (dt_stanox, trainCount, delayCount, totalDelay, minDelay, maxDelay, aveDelay)
                VALUES ( pdts, 1, 1, pdelay,  pdelay,  pdelay,  pdelay);
        ELSE
            INSERT INTO report.perf_stanox_all
                (dt_stanox, trainCount, earlyCount, maxEarly)
                VALUES ( pdts, 1, 1, -pdelay);
        END IF;
    END IF;

    -- By operator
    SELECT * INTO rec FROM report.perf_stanox_toc
        WHERE dt_stanox = pdts AND operatorid = popid;
    IF FOUND THEN
        IF pdelay = 0 THEN
            UPDATE report.perf_stanox_toc
                SET trainCount = rec.trainCount + 1,
                    ontime = rec.ontime + 1
                WHERE dt_stanox = pdts AND operatorid = popid;
        ELSIF pdelay > 0 THEN
            UPDATE report.perf_stanox_toc
                SET trainCount = rec.trainCount + 1,
                    delayCount = rec.delayCount + 1,
                    totaldelay = rec.totaldelay + pdelay,
                    minDelay = LEAST( rec.minDelay, pdelay),
                    maxDelay = GREATEST( rec.maxDelay, pdelay)
                WHERE dt_stanox = pdts AND operatorid = popid;
        ELSE
            UPDATE report.perf_stanox_toc
                SET trainCount = rec.trainCount + 1,
                    earlycount = rec.earlycount + 1,
                    maxEarly = GREATEST( rec.maxEarly, -pdelay)
                WHERE dt_stanox = pdts AND operatorid = popid;
        END IF;
    ELSE
        IF pdelay = 0 THEN
            INSERT INTO report.perf_stanox_toc
                (dt_stanox, operatorid, trainCount, ontime)
                VALUES ( pdts, popid, 1, 1);
        ELSIF pdelay > 0 THEN
            INSERT INTO report.perf_stanox_toc
                (dt_stanox, operatorid, trainCount, delayCount, totalDelay, minDelay, maxDelay, aveDelay)
                VALUES ( pdts, popid, 1, 1, pdelay,  pdelay,  pdelay,  pdelay);
        ELSE
            INSERT INTO report.perf_stanox_toc
                (dt_stanox, operatorid, trainCount, earlyCount, maxEarly)
                VALUES ( pdts, popid, 1, 1, -pdelay);
        END IF;
    END IF;

    -- By operator & class
    SELECT * INTO rec FROM report.perf_stanox_toc_class
        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
    IF FOUND THEN
        IF pdelay = 0 THEN
            UPDATE report.perf_stanox_toc_class
                SET trainCount = rec.trainCount + 1,
                    ontime = ontime + 1
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
        ELSIF pdelay > 0 THEN
            UPDATE report.perf_stanox_toc_class
                SET trainCount = rec.trainCount + 1,
                    delayCount = rec.delayCount + 1,
                    totaldelay = rec.totaldelay + pdelay,
                    minDelay = LEAST( rec.minDelay, pdelay),
                    maxDelay = GREATEST( rec.maxDelay, pdelay)
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
        ELSE
            UPDATE report.perf_stanox_toc_class
                SET trainCount = rec.trainCount + 1,
                    earlycount = rec.earlycount + 1,
                    maxEarly = GREATEST( rec.maxEarly, -pdelay)
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
        END IF;
    ELSE
        IF pdelay = 0 THEN
            INSERT INTO report.perf_stanox_toc_class
                (dt_stanox, operatorid, trainClass, trainCount, ontime)
                VALUES ( pdts, popid, ptclass, 1, 1);
        ELSIF pdelay > 0 THEN
            INSERT INTO report.perf_stanox_toc_class
                (dt_stanox, operatorid, trainClass, delayCount, trainCount, totalDelay, minDelay, maxDelay, aveDelay)
                VALUES ( pdts, popid, ptclass, 1, 1, pdelay,  pdelay,  pdelay,  pdelay);
        ELSE
            INSERT INTO report.perf_stanox_toc_class
                (dt_stanox, operatorid, trainClass, trainCount, earlyCount, maxEarly)
                VALUES ( pdts, popid, ptclass, 1, 1, -pdelay);
        END IF;
    END IF;

    -- PPM
    IF pdelay >0 THEN
        IF pdelay <=300 THEN
            UPDATE report.perf_stanox_all
                SET ppm5 = ppm5 + 1
                WHERE dt_stanox = pdts;
            UPDATE report.perf_stanox_toc
                SET ppm5 = ppm5 + 1
                WHERE dt_stanox = pdts AND operatorid = popid;
            UPDATE report.perf_stanox_toc_class
                SET ppm5 = ppm5 + 1
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
        ELSIF pdelay <=600 THEN
            UPDATE report.perf_stanox_all
                SET ppm10 = ppm10 + 1
                WHERE dt_stanox = pdts;
            UPDATE report.perf_stanox_toc
                SET ppm10 = ppm10 + 1
                WHERE dt_stanox = pdts AND operatorid = popid;
            UPDATE report.perf_stanox_toc_class
                SET ppm10 = ppm10 + 1
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
        ELSIF pdelay >= 1800 THEN
            UPDATE report.perf_stanox_all
                SET ppm30 = ppm30 + 1
                WHERE dt_stanox = pdts;
            UPDATE report.perf_stanox_toc
                SET ppm30 = ppm30 + 1
                WHERE dt_stanox = pdts AND operatorid = popid;
            UPDATE report.perf_stanox_toc_class
                SET ppm30 = ppm30 + 1
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
        END IF;
    END IF;

END;
$$ LANGUAGE plpgsql;

-- ======================================================================
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA report TO rail;
