-- ======================================================================
-- Search for departures from a crs at the current time.
--
-- This is the backend for the departureboards.mobi departure board.
-- ======================================================================

SET search_path = darwin;

-- Search for departures within one hour of a specified date/time
-- Used for the rtt search page

-- DROP FUNCTION darwin.callingPoints(prid TEXT);

CREATE OR REPLACE FUNCTION darwin.callingPoints(prid TEXT)
RETURNS TEXT AS $$
BEGIN
    RETURN darwin.callingPoints(prid,'00:00:00'::TIME WITHOUT TIME ZONE);
END;
$$ LANGUAGE plpgsql;

-- DROP FUNCTION darwin.callingPoints(prid TEXT, ps TIME WITHOUT TIME ZONE);

CREATE OR REPLACE FUNCTION darwin.callingPoints(prid TEXT, ps TIME WITHOUT TIME ZONE)
RETURNS TEXT AS $$
DECLARE
    rec     RECORD;
    ret     JSON[] = ARRAY[]::JSON[];
BEGIN
    IF prid IS NOT NULL THEN
        FOR rec IN SELECT cp.tpl,cp.tm FROM darwin.getForecastEntries(prid) cp WHERE NOT cp.canc AND cp.tm > ps ORDER BY cp.wtm
        LOOP
            ret = array_append(ret, array_to_json(ARRAY[ rec.tpl::TEXT, rec.tm::TEXT ]));
        END LOOP;
    END IF;
    RETURN array_to_json(ret)::TEXT;
END;
$$ LANGUAGE plpgsql;

-- Return the last report for a train
CREATE OR REPLACE FUNCTION darwin.lastReport(prid TEXT)
RETURNS VARCHAR(16) AS $$
DECLARE
    rec     RECORD;
BEGIN
    IF prid IS NOT NULL THEN
        SELECT INTO rec * FROM darwin.getForecastEntries(prid) e WHERE COALESCE( e.dep, e.arr, e.pass ) IS NOT NULL ORDER BY e.wtm DESC LIMIT 1;
        IF FOUND THEN
            RETURN rec.tpl;
        END IF;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql;

-- DROP FUNCTION darwin.departureBoard( pcrs TEXT );

CREATE OR REPLACE FUNCTION darwin.departureBoard( pcrs TEXT )
RETURNS TABLE(
    type        CHAR(1),
    dest        VARCHAR(16),
    -- Platforms
    plat        VARCHAR(4),
    platsup     BOOLEAN,
    cisplatsup  BOOLEAN,
    -- Public timetable
    pta         TIME WITHOUT TIME ZONE,
    ptd         TIME WITHOUT TIME ZONE,
    etarr       TIME WITHOUT TIME ZONE,
    etdep       TIME WITHOUT TIME ZONE,
    delayed     BOOLEAN,
    canc        BOOLEAN,
    term        BOOLEAN,
    -- meta
    rid         VARCHAR(16),
    via         INTEGER,
    -- Time, first of ptd, pta
    tm          TIME WITHOUT TIME ZONE,
    -- meta
    callpoint   TEXT,
    lastreport  VARCHAR(16),
    -- association
    assoc       VARCHAR(16),
    assoctpl    VARCHAR(16),
    assoccp     TEXT
) AS $$
DECLARE
    crsid   INTEGER;
    rec     RECORD;
    pssd    DATE = now();
    ps      TIME WITHOUT TIME ZONE = now() AT TIME ZONE 'Europe/London';
    pe      TIME WITHOUT TIME ZONE = ps + '1 hour'::INTERVAL;
BEGIN
    IF length(pcrs) = 3 THEN
        -- Darwin search
        crsid = darwin.crs(pcrs);
        IF crsid IS NOT NULL THEN
            RETURN QUERY
                WITH departures AS (
                    SELECT
                        'D'::CHAR,
                        t.tpl,
                        e.plat,
                        e.platsup,
                        e.cisplatsup,
                        e.pta,
                        e.ptd,
                        e.etarr,
                        e.etdep,
                        COALESCE( e.etdepdel, etarrdel, FALSE),
                        COALESCE( se.can, false ),
                        sl.crs=crsid,
                        f.rid,
                        s.via,
                        COALESCE( e.ptd, e.pta ) AS ptm,
                        -- main calling points
                        darwin.callingPoints(f.rid, COALESCE( e.ptd, e.pta )),
                        darwin.lastreport(f.rid),
                        -- association
                        sas.rid,
                        sat.tpl,
                        darwin.callingPoints(sas.rid)
                    FROM darwin.forecast f
                        INNER JOIN darwin.forecast_entry e ON f.id=e.fid
                        INNER JOIN darwin.location l ON e.tpl=l.tpl
                        INNER JOIN darwin.crs c ON l.crs = c.id
                        LEFT OUTER JOIN darwin.schedule s ON f.schedule=s.id
                        LEFT OUTER JOIN darwin.tiploc t ON s.dest=t.id
                        LEFT OUTER JOIN darwin.schedule_entry se ON se.schedule=f.schedule AND se.tpl=e.tpl
                        LEFT OUTER JOIN darwin.location sl ON s.dest=sl.tpl
                        LEFT OUTER JOIN darwin.schedule_assoc sa ON f.schedule=sa.mainid AND sa.cat='VV'
                        LEFT OUTER JOIN darwin.schedule sas ON sa.associd = sas.id
                        LEFT OUTER JOIN darwin.tiploc sat ON sa.tpl=sat.id
                    WHERE l.crs=crsid AND f.ssd=pssd
                        AND e.wtp IS NULL
                )
                SELECT * FROM departures where ptm BETWEEN ps AND pe ORDER BY ptm;
        END IF;
    ELSE
        -- TODO add TFL, LUxxx for Tube, DLxxx for DVR
    END IF;
    RETURN;
END;
$$ LANGUAGE plpgsql;
