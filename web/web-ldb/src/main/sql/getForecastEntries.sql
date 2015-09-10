-- ======================================================================
-- Return the latest Darwin forecast entries for a train
-- ======================================================================

SET search_path = darwin;

-- Return the schedule for a train

-- DROP FUNCTION darwin.getForecastEntries( prid TEXT );

CREATE OR REPLACE FUNCTION darwin.getForecastEntries( prid TEXT )
RETURNS TABLE(
    fid         BIGINT,
    tpl         VARCHAR(16),
    supp        BOOLEAN,
    -- Public timetable
    pta         TIME WITHOUT TIME ZONE,
    ptd         TIME WITHOUT TIME ZONE,
    -- Working timetable
    wta         TIME WITHOUT TIME ZONE,
    wtd         TIME WITHOUT TIME ZONE,
    wtp         TIME WITHOUT TIME ZONE,
    -- Observations
    delay       INTERVAL,
    arr         TIME WITHOUT TIME ZONE,
    dep         TIME WITHOUT TIME ZONE,
    pass        TIME WITHOUT TIME ZONE,
    etarr       TIME WITHOUT TIME ZONE,
    etdep       TIME WITHOUT TIME ZONE,
    etpass      TIME WITHOUT TIME ZONE,
    -- Platforms
    plat        VARCHAR(4),
    platsup     BOOLEAN,
    cisplatsup  BOOLEAN,
    platsrc     NAME,
    -- Train details
    length      INTEGER,
    detachfront BOOLEAN,
    -- LDB details
    ldb         BOOLEAN,
    tm          TIME WITHOUT TIME ZONE,
    term        BOOLEAN,
    etarrdel    BOOLEAN,
    etdepdel    BOOLEAN,
    etpassdel   BOOLEAN,
    ldbdel      BOOLEAN,
    -- tpl id
    tplid       INTEGER,
    -- Cancelled (via schedule)
    canc        BOOLEAN,
    -- Working time
    wtm         TIME WITHOUT TIME ZONE
) AS $$
DECLARE
    fcstid      BIGINT;
    schedid     BIGINT;
BEGIN
    fcstid = darwin.getForecastID(prid);
    IF fcstid IS NOT NULL THEN
        schedid = darwin.getScheduleID(prid);
        RETURN QUERY
            WITH entries AS (
                SELECT
                    f.fid,
                    t.tpl,
                    COALESCE( f.supp, false),
                    f.pta, f.ptd,
                    f.wta, f.wtd, f.wtp,
                    f.delay,
                    f.arr, f.dep, f.pass,
                    f.etarr, f.etdep, f.etpass,
                    f.plat,
                    COALESCE( f.platsup, false),
                    COALESCE( f.cisplatsup, false),
                    f.platsrc,
                    COALESCE( f.length, 0 ),
                    f.detachfront,
                    COALESCE( f.ldb, false),
                    f.tm,
                    COALESCE( f.term, false),
                    COALESCE( f.etarrdel, false),
                    COALESCE( f.etdepdel, false),
                    COALESCE( f.etpassdel, false),
                    COALESCE( f.ldbdel, false),
                    f.tpl,
                    COALESCE( se.can, false ),
                    COALESCE( f.wtd, f.wta, f.wtp ) AS wtm
                FROM darwin.forecast_entry f
                    INNER JOIN darwin.tiploc t ON f.tpl = t.id
                    LEFT OUTER JOIN darwin.schedule_entry se ON se.schedule=schedid AND se.tpl=f.tpl
                WHERE f.fid = fcstid
            UNION
                SELECT
                    f.fid,
                    t.tpl,
                    COALESCE( f.supp, false),
                    f.pta, f.ptd,
                    f.wta, f.wtd, f.wtp,
                    f.delay,
                    f.arr, f.dep, f.pass,
                    f.etarr, f.etdep, f.etpass,
                    f.plat,
                    COALESCE( f.platsup, false),
                    COALESCE( f.cisplatsup, false),
                    f.platsrc,
                    COALESCE( f.length, 0 ),
                    f.detachfront,
                    COALESCE( f.ldb, false),
                    f.tm,
                    COALESCE( f.term, false),
                    COALESCE( f.etarrdel, false),
                    COALESCE( f.etdepdel, false),
                    COALESCE( f.etpassdel, false),
                    COALESCE( f.ldbdel, false),
                    f.tpl,
                    COALESCE( se.can, false ),
                    COALESCE( f.wtd, f.wta, f.wtp ) AS wtm
                FROM darwin.forecast_entryarc f
                    INNER JOIN darwin.tiploc t ON f.tpl = t.id
                    LEFT OUTER JOIN darwin.schedule_entryarc se ON se.schedule=schedid AND se.tpl=f.tpl
                WHERE f.fid = fcstid
            )
            SELECT * FROM entries ORDER BY wtm;
    END IF;
    RETURN;
END;
$$ LANGUAGE plpgsql;
