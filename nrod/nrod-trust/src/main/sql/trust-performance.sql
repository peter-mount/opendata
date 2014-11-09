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
    trainCount  INTEGER,
    totalDelay  INTEGER,
    minDelay    INTEGER,
    maxDelay    INTEGER,
    aveDelay    INTEGER,
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
    trainCount  INTEGER,
    totalDelay  INTEGER,
    minDelay    INTEGER,
    maxDelay    INTEGER,
    aveDelay    INTEGER,
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
    trainClass  INTEGER,
    trainCount  INTEGER,
    totalDelay  INTEGER,
    minDelay    INTEGER,
    maxDelay    INTEGER,
    aveDelay    INTEGER,
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

CREATE OR REPLACE FUNCTION perf_stanox
    ( pdts BIGINT, popid INTEGER, ptclass INTEGER, pdelay INTEGER )
RETURNS VOID AS $$
DECLARE
    rec     RECORD;
BEGIN

    -- All operators
    SELECT * INTO rec FROM report.perf_stanox_all WHERE dt_stanox = pdts;
    IF FOUND THEN
        UPDATE report.perf_stanox_all
            SET trainCount = rec.trainCount + 1,
                totaldelay = rec.totaldelay + pdelay,
                minDelay = LEAST( rec.minDelay, pdelay),
                maxDelay = GREATEST( rec.maxDelay, pdelay),
                aveDelay = totaldelay / traincount
            WHERE dt_stanox = pdts;
    ELSE
        INSERT INTO report.perf_stanox_all
            (dt_stanox, trainCount, totalDelay, minDelay, maxDelay, aveDelay)
            VALUES ( pdts, 1, pdelay,  pdelay,  pdelay,  pdelay);
    END IF;

    -- By operator
    SELECT * INTO rec FROM report.perf_stanox_toc
        WHERE dt_stanox = pdts AND operatorid = popid;
    IF FOUND THEN
        UPDATE report.perf_stanox_toc
            SET trainCount = rec.trainCount + 1,
                totaldelay = rec.totaldelay + pdelay,
                minDelay = LEAST( rec.minDelay, pdelay),
                maxDelay = GREATEST( rec.maxDelay, pdelay),
                aveDelay = totaldelay / traincount
            WHERE dt_stanox = pdts AND operatorid = popid;
    ELSE
        INSERT INTO report.perf_stanox_toc
            (dt_stanox, operatorid, trainCount, totalDelay, minDelay, maxDelay, aveDelay)
            VALUES ( pdts, popid, 1, pdelay,  pdelay,  pdelay,  pdelay);
    END IF;

    -- By operator & class
    SELECT * INTO rec FROM report.perf_stanox_toc_class
        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
    IF FOUND THEN
        UPDATE report.perf_stanox_toc_class
            SET trainCount = rec.trainCount + 1,
                totaldelay = rec.totaldelay + pdelay,
                minDelay = LEAST( rec.minDelay, pdelay),
                maxDelay = GREATEST( rec.maxDelay, pdelay),
                aveDelay = totaldelay / traincount
            WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
    ELSE
        INSERT INTO report.perf_stanox_toc_class
            (dt_stanox, operatorid, trainClass, trainCount, totalDelay, minDelay, maxDelay, aveDelay)
            VALUES ( pdts, popid, ptclass, 1, pdelay,  pdelay,  pdelay,  pdelay);
    END IF;

END;
$$ LANGUAGE plpgsql;

-- ======================================================================
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA report TO rail;
