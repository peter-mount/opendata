-- ======================================================================
-- This will monitor and record arrivals, departures separately as
-- performance statistics.
--
-- Note: A train terminating is counted as an arrival.
--       A train passing is not included in these stats
-- ======================================================================

CREATE SCHEMA report;
SET search_path = report;

-- ======================================================================
-- Arrivals at a stanox by date, operator and class.
-- Note a termination is counted as an arrival
-- ======================================================================

DROP TABLE perf_stanox_arrival;

CREATE TABLE perf_stanox_arrival (
    dt_stanox   BIGINT NOT NULL REFERENCES report.dim_date_stanox(dt_stanox),
    operatorid  INTEGER NOT NULL REFERENCES report.dim_operator(operatorid),
    trainCount  INTEGER DEFAULT 0,
    totalDelay  INTEGER DEFAULT 0,
    delayCount  INTEGER DEFAULT 0,
    minDelay    INTEGER DEFAULT 999,
    maxDelay    INTEGER DEFAULT 0,
    earlyCount  INTEGER DEFAULT 0,
    maxEarly    INTEGER DEFAULT 0,
    ontime      INTEGER DEFAULT 0,
    ppmearly    INTEGER DEFAULT 0,
    ppm5        INTEGER DEFAULT 0,
    ppm10       INTEGER DEFAULT 0,
    ppm30       INTEGER DEFAULT 0,
    PRIMARY KEY(dt_stanox,operatorid,trainClass)
);

ALTER TABLE perf_stanox_arrival OWNER TO rail;

CREATE INDEX perf_stanox_arrival_do ON perf_stanox_arrival(dt_stanox,operatorid);
CREATE INDEX perf_stanox_arrival_d ON perf_stanox_arrival(dt_stanox);
CREATE INDEX perf_stanox_arrival_o ON perf_stanox_arrival(operatorid);
CREATE INDEX perf_stanox_arrival_dc ON perf_stanox_arrival(dt_stanox,trainclass);
CREATE INDEX perf_stanox_arrival_oc ON perf_stanox_arrival(operatorid,trainclass);

-- ======================================================================
-- Departures at a stanox by date, operator and class
-- ======================================================================

DROP TABLE perf_stanox_departure;

CREATE TABLE perf_stanox_departure (
    dt_stanox   BIGINT NOT NULL REFERENCES report.dim_date_stanox(dt_stanox),
    operatorid  INTEGER NOT NULL REFERENCES report.dim_operator(operatorid),
    trainCount  INTEGER DEFAULT 0,
    totalDelay  INTEGER DEFAULT 0,
    delayCount  INTEGER DEFAULT 0,
    minDelay    INTEGER DEFAULT 999,
    maxDelay    INTEGER DEFAULT 0,
    earlyCount  INTEGER DEFAULT 0,
    maxEarly    INTEGER DEFAULT 0,
    ontime      INTEGER DEFAULT 0,
    ppmearly    INTEGER DEFAULT 0,
    ppm5        INTEGER DEFAULT 0,
    ppm10       INTEGER DEFAULT 0,
    ppm30       INTEGER DEFAULT 0,
    PRIMARY KEY(dt_stanox,operatorid,trainClass)
);

ALTER TABLE perf_stanox_departure OWNER TO rail;

CREATE INDEX perf_stanox_departure_do ON perf_stanox_departure(dt_stanox,operatorid);
CREATE INDEX perf_stanox_departure_d ON perf_stanox_departure(dt_stanox);
CREATE INDEX perf_stanox_departure_o ON perf_stanox_departure(operatorid);
CREATE INDEX perf_stanox_departure_dc ON perf_stanox_departure(dt_stanox,trainclass);
CREATE INDEX perf_stanox_departure_oc ON perf_stanox_departure(operatorid,trainclass);

-- ======================================================================
-- Function to record arrival/departure stats
--
-- ptype is 0 for departure, 1 arrival
-- ======================================================================

CREATE OR REPLACE FUNCTION report.perf_stanox_ad
    (pdts BIGINT, popid INTEGER, ptclass INTEGER, pdelay INTEGER, ptype INTEGER)
RETURNS VOID AS $$
DECLARE
    rec     RECORD;
BEGIN
    CASE ptype
        WHEN 0 THEN
            SELECT * INTO rec FROM report.perf_stanox_arrival
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
            IF FOUND THEN
                IF pdelay BETWEEN -60 AND 60 THEN
                    UPDATE report.perf_stanox_arrival
                        SET trainCount = rec.trainCount + 1,
                            ontime = ontime + 1
                        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
                ELSIF pdelay > 0 THEN
                    UPDATE report.perf_stanox_arrival
                        SET trainCount = rec.trainCount + 1,
                            delayCount = rec.delayCount + 1,
                            totaldelay = rec.totaldelay + pdelay,
                            minDelay = LEAST( rec.minDelay, pdelay),
                            maxDelay = GREATEST( rec.maxDelay, pdelay)
                        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
                ELSE
                    UPDATE report.perf_stanox_arrival
                        SET trainCount = rec.trainCount + 1,
                            earlycount = rec.earlycount + 1,
                            maxEarly = GREATEST( rec.maxEarly, -pdelay)
                        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
                END IF;
            ELSE
                IF pdelay BETWEEN -60 AND 60 THEN
                    INSERT INTO report.perf_stanox_arrival
                        (dt_stanox, operatorid, trainClass, trainCount, ontime)
                        VALUES ( pdts, popid, ptclass, 1, 1);
                ELSIF pdelay > 0 THEN
                    INSERT INTO report.perf_stanox_arrival
                        (dt_stanox, operatorid, trainClass, delayCount, trainCount, totalDelay, minDelay, maxDelay, aveDelay)
                        VALUES ( pdts, popid, ptclass, 1, 1, pdelay,  pdelay,  pdelay,  pdelay);
                ELSE
                    INSERT INTO report.perf_stanox_arrival
                        (dt_stanox, operatorid, trainClass, trainCount, earlyCount, maxEarly)
                        VALUES ( pdts, popid, ptclass, 1, 1, -pdelay);
                END IF;
            END IF;
            IF pdelay >30 THEN
                IF pdelay <=300 THEN
                    UPDATE report.perf_stanox_arrival
                        SET ppm5 = ppm5 + 1
                        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
                ELSIF pdelay <=600 THEN
                    UPDATE report.perf_stanox_arrival
                        SET ppm10 = ppm10 + 1
                        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
                ELSIF pdelay >= 1800 THEN
                    UPDATE report.perf_stanox_arrival
                        SET ppm30 = ppm30 + 1
                        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
                END IF;
            END IF;
        WHEN 1 THEN
            SELECT * INTO rec FROM report.perf_stanox_departure
                WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
            IF FOUND THEN
                IF pdelay BETWEEN -60 AND 60 THEN
                    UPDATE report.perf_stanox_departure
                        SET trainCount = rec.trainCount + 1,
                            ontime = ontime + 1
                        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
                ELSIF pdelay > 0 THEN
                    UPDATE report.perf_stanox_departure
                        SET trainCount = rec.trainCount + 1,
                            delayCount = rec.delayCount + 1,
                            totaldelay = rec.totaldelay + pdelay,
                            minDelay = LEAST( rec.minDelay, pdelay),
                            maxDelay = GREATEST( rec.maxDelay, pdelay)
                        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
                ELSE
                    UPDATE report.perf_stanox_departure
                        SET trainCount = rec.trainCount + 1,
                            earlycount = rec.earlycount + 1,
                            maxEarly = GREATEST( rec.maxEarly, -pdelay)
                        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
                END IF;
            ELSE
                IF pdelay BETWEEN -60 AND 60 THEN
                    INSERT INTO report.perf_stanox_departure
                        (dt_stanox, operatorid, trainClass, trainCount, ontime)
                        VALUES ( pdts, popid, ptclass, 1, 1);
                ELSIF pdelay > 0 THEN
                    INSERT INTO report.perf_stanox_departure
                        (dt_stanox, operatorid, trainClass, delayCount, trainCount, totalDelay, minDelay, maxDelay, aveDelay)
                        VALUES ( pdts, popid, ptclass, 1, 1, pdelay,  pdelay,  pdelay,  pdelay);
                ELSE
                    INSERT INTO report.perf_stanox_departure
                        (dt_stanox, operatorid, trainClass, trainCount, earlyCount, maxEarly)
                        VALUES ( pdts, popid, ptclass, 1, 1, -pdelay);
                END IF;
            END IF;
            IF pdelay >30 THEN
                IF pdelay <=300 THEN
                    UPDATE report.perf_stanox_departure
                        SET ppm5 = ppm5 + 1
                        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
                ELSIF pdelay <=600 THEN
                    UPDATE report.perf_stanox_departure
                        SET ppm10 = ppm10 + 1
                        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
                ELSIF pdelay >= 1800 THEN
                    UPDATE report.perf_stanox_departure
                        SET ppm30 = ppm30 + 1
                        WHERE dt_stanox = pdts AND operatorid = popid AND trainclass = ptclass;
                END IF;
            END IF;
    END CASE;
END;
$$ LANGUAGE plpgsql;

-- ======================================================================
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA report TO rail;
