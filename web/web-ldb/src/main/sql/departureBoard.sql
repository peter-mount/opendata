-- ======================================================================
-- Search for departures from a crs at the current time.
--
-- This is the backend for the departureboards.mobi departure board.
-- ======================================================================

SET search_path = darwin;

-- Missing indices
CREATE INDEX forecast_entry_pd ON darwin.forecast_entry(ptd);
CREATE INDEX forecast_entry_pa ON darwin.forecast_entry(pta);
CREATE INDEX forecast_entry_pad ON darwin.forecast_entry(pta,ptd);

CREATE INDEX schedule_ssd ON darwin.schedule(ssd);
CREATE INDEX schedulearc_ssd ON darwin.schedulearc(ssd);

CREATE INDEX schedulearc_ssddest ON darwin.schedulearc(ssd,dest);
CREATE INDEX schedule_ssddest ON darwin.schedule(ssd,dest);

CREATE INDEX schedule_assoc_mac ON darwin.schedule_assoc(mainid,associd,cat);
CREATE INDEX schedule_assoc_ac ON darwin.schedule_assoc(associd,cat);
CREATE INDEX schedule_assoc_c ON darwin.schedule_assoc(cat);
CREATE INDEX schedule_assoc_mc ON darwin.schedule_assoc(mainid,cat);



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
        FOR rec IN
            SELECT cp.tpl,cp.tm
                FROM darwin.getForecastEntries(prid) cp
                WHERE NOT cp.canc AND cp.wtp IS NULL AND cp.tm > ps ORDER BY cp.wtm
        LOOP
            ret = array_append(ret, array_to_json(ARRAY[ rec.tpl::TEXT, rec.tm::TEXT ]));
        END LOOP;
    END IF;
    RETURN array_to_json(ret)::TEXT;
END;
$$ LANGUAGE plpgsql;

-- Return the last report for a train
CREATE OR REPLACE FUNCTION darwin.lastReport(prid TEXT)
RETURNS TEXT AS $$
DECLARE
    rec     RECORD;
BEGIN
    IF prid IS NOT NULL THEN
        SELECT INTO rec * FROM darwin.getForecastEntries(prid) e WHERE COALESCE( e.dep, e.arr, e.pass ) IS NOT NULL ORDER BY e.wtm DESC LIMIT 1;
        IF FOUND THEN
            RETURN array_to_json(ARRAY[rec.tpl::TEXT, rec.tm::TEXT]);
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
    arrived     BOOLEAN,
    departed    BOOLEAN,
    delayed     BOOLEAN,
    latereason  INTEGER,
    canc        BOOLEAN,
    cancreason  INTEGER,
    term        BOOLEAN,
    -- meta
    rid         VARCHAR(16),
    via         INTEGER,
    -- Time, first of ptd, pta
    tm          TIME WITHOUT TIME ZONE,
    -- meta
    callpoint   TEXT,
    lastreport  TEXT,
    length      INTEGER,
    toc         CHAR(2),
    -- association
    assoc       VARCHAR(16),
    assoctpl    VARCHAR(16),
    assoccp     TEXT,
    -- Ordering
    tmord       TIME WITHOUT TIME ZONE
) AS $$
DECLARE
    crsid   INTEGER;
    rec     RECORD;
    pnow    TIMESTAMP WITHOUT TIME ZONE;
    pssd    DATE;
    ps      TIME WITHOUT TIME ZONE;
    pe      TIME WITHOUT TIME ZONE;
BEGIN
    --pnow    = '2015-12-24 23:05:00'::TIMESTAMP WITHOUT TIME ZONE;--now();
    pnow    = now();
    pssd    = pnow::DATE;
    ps      = pnow::TIME;
    pe      = ps + '1 hour'::INTERVAL;
    IF length(pcrs) = 3 THEN
        -- Darwin search
        crsid = darwin.crs(pcrs);
        IF crsid IS NOT NULL THEN
            IF ps < pe THEN
                -- Normal query, not crossing midnight
                RETURN QUERY
                    WITH departures AS (
                        SELECT DISTINCT ON (f.rid)
                            'D'::CHAR,
                            t.tpl,
                            e.plat,
                            e.platsup,
                            e.cisplatsup,
                            e.pta,
                            e.ptd,
                            e.etarr,
                            e.etdep,
                            e.arr IS NOT NULL,
                            e.dep IS NOT NULL,
                            COALESCE( e.etdepdel, etarrdel, FALSE),
                            f.latereason,
                            COALESCE( se.can, false ),
                            COALESCE( s.cancreason, 0 ),
                            COALESCE( e.term, false ),
                            f.rid,
                            s.via,
                            COALESCE( e.dep, e.etdep, e.arr, e.etarr, e.ptd, e.pta ) AS ptm,
                            -- main calling points
                            darwin.callingPoints(f.rid, COALESCE( e.ptd, e.pta )),
                            darwin.lastreport(f.rid),
                            COALESCE( e.length, 0 ),
                            s.toc,
                            -- association
                            sas.rid,
                            sat.tpl,
                            darwin.callingPoints(sas.rid),
                            COALESCE( e.dep, e.etdep, e.arr, e.etarr, e.ptd, e.pta ) AS tord
                        FROM darwin.forecast f
                            INNER JOIN darwin.forecast_entry e ON f.id=e.fid
                            INNER JOIN darwin.location l ON e.tpl=l.tpl
                            LEFT OUTER JOIN darwin.schedule s ON f.schedule=s.id
                            LEFT OUTER JOIN darwin.tiploc t ON s.dest=t.id
                            LEFT OUTER JOIN darwin.schedule_entry se ON se.schedule=f.schedule AND se.tpl=e.tpl
                            LEFT OUTER JOIN darwin.location sl ON s.dest=sl.tpl
                            LEFT OUTER JOIN darwin.schedule_assoc sa ON f.schedule=sa.mainid AND sa.cat='VV'
                            LEFT OUTER JOIN darwin.schedule sas ON sa.associd = sas.id
                            LEFT OUTER JOIN darwin.tiploc sat ON sa.tpl=sat.id
                        WHERE l.crs=crsid AND f.ssd=pssd
                            AND e.wtp IS NULL
                            AND COALESCE( e.dep, e.etdep, e.ptd, e.arr, e.etarr, e.pta) BETWEEN ps AND pe
                    )
                    SELECT * FROM departures ORDER BY ptm;
            ELSE
                -- Crossing Midnight
                RETURN QUERY
                    WITH departures1 AS (
                        SELECT DISTINCT ON (f.rid)
                            'D'::CHAR,
                            t.tpl,
                            e.plat,
                            e.platsup,
                            e.cisplatsup,
                            e.pta,
                            e.ptd,
                            e.etarr,
                            e.etdep,
                            e.arr IS NOT NULL,
                            e.dep IS NOT NULL,
                            COALESCE( e.etdepdel, etarrdel, FALSE),
                            f.latereason,
                            COALESCE( se.can, false ),
                            COALESCE( s.cancreason, 0 ),
                            COALESCE( e.term, false ),
                            f.rid,
                            s.via,
                            COALESCE( e.dep, e.etdep, e.arr, e.etarr, e.ptd, e.pta ) AS ptm,
                            -- main calling points
                            darwin.callingPoints(f.rid, COALESCE( e.ptd, e.pta )),
                            darwin.lastreport(f.rid),
                            COALESCE( e.length, 0 ),
                            s.toc,
                            -- association
                            sas.rid,
                            sat.tpl,
                            darwin.callingPoints(sas.rid),
                            COALESCE( e.dep, e.etdep, e.arr, e.etarr, e.ptd, e.pta )+'12 hours'::INTERVAL AS tord
                        FROM darwin.forecast f
                            INNER JOIN darwin.forecast_entry e ON f.id=e.fid
                            INNER JOIN darwin.location l ON e.tpl=l.tpl
                            LEFT OUTER JOIN darwin.schedule s ON f.schedule=s.id
                            LEFT OUTER JOIN darwin.tiploc t ON s.dest=t.id
                            LEFT OUTER JOIN darwin.schedule_entry se ON se.schedule=f.schedule AND se.tpl=e.tpl
                            LEFT OUTER JOIN darwin.location sl ON s.dest=sl.tpl
                            LEFT OUTER JOIN darwin.schedule_assoc sa ON f.schedule=sa.mainid AND sa.cat='VV'
                            LEFT OUTER JOIN darwin.schedule sas ON sa.associd = sas.id
                            LEFT OUTER JOIN darwin.tiploc sat ON sa.tpl=sat.id
                        WHERE l.crs=crsid AND f.ssd=pssd
                            AND e.wtp IS NULL
                            AND COALESCE( e.dep, e.etdep, e.ptd, e.arr, e.etarr, e.pta) BETWEEN ps AND '23:59'::TIME
                    ),departures2 AS (
                        SELECT DISTINCT ON (f.rid)
                            'D'::CHAR,
                            t.tpl,
                            e.plat,
                            e.platsup,
                            e.cisplatsup,
                            e.pta,
                            e.ptd,
                            e.etarr,
                            e.etdep,
                            e.arr IS NOT NULL,
                            e.dep IS NOT NULL,
                            COALESCE( e.etdepdel, etarrdel, FALSE),
                            f.latereason,
                            COALESCE( se.can, false ),
                            COALESCE( s.cancreason, 0 ),
                            COALESCE( e.term, false ),
                            f.rid,
                            s.via,
                            COALESCE( e.dep, e.etdep, e.arr, e.etarr, e.ptd, e.pta ) AS ptm,
                            -- main calling points
                            darwin.callingPoints(f.rid, COALESCE( e.ptd, e.pta )),
                            darwin.lastreport(f.rid),
                            COALESCE( e.length, 0 ),
                            s.toc,
                            -- association
                            sas.rid,
                            sat.tpl,
                            darwin.callingPoints(sas.rid),
                            COALESCE( e.dep, e.etdep, e.arr, e.etarr, e.ptd, e.pta )+'12 hours'::INTERVAL AS tord
                        FROM darwin.forecast f
                            INNER JOIN darwin.forecast_entry e ON f.id=e.fid
                            INNER JOIN darwin.location l ON e.tpl=l.tpl
                            LEFT OUTER JOIN darwin.schedule s ON f.schedule=s.id
                            LEFT OUTER JOIN darwin.tiploc t ON s.dest=t.id
                            LEFT OUTER JOIN darwin.schedule_entry se ON se.schedule=f.schedule AND se.tpl=e.tpl
                            LEFT OUTER JOIN darwin.location sl ON s.dest=sl.tpl
                            LEFT OUTER JOIN darwin.schedule_assoc sa ON f.schedule=sa.mainid AND sa.cat='VV'
                            LEFT OUTER JOIN darwin.schedule sas ON sa.associd = sas.id
                            LEFT OUTER JOIN darwin.tiploc sat ON sa.tpl=sat.id
                        WHERE l.crs=crsid AND f.ssd=pssd
                            AND e.wtp IS NULL
                            AND COALESCE( e.dep, e.etdep, e.ptd, e.arr, e.etarr, e.pta) BETWEEN '00:00'::TIME AND pe
                    )
                    SELECT * FROM departures1 UNION SELECT * FROM departures2 ORDER BY tord;
            END IF;
        END IF;
    ELSE
        -- TODO add TFL, LUxxx for Tube, DLxxx for DVR
    END IF;
    RETURN;
END;
$$ LANGUAGE plpgsql;

select * from darwin.departureboard('MDE');
