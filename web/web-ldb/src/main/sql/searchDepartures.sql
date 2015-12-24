-- ======================================================================
-- Search for departures from a crs within one hour of a specified
-- date and time.
--
-- This is the backend for the RTT search page for the current day and
-- for past dates
-- ======================================================================

SET search_path = darwin;

-- Fix some missing indices

CREATE INDEX forecast_s ON darwin.forecast(ssd);
CREATE INDEX forecast_u ON darwin.forecast(uid);

CREATE INDEX forecastarc_s ON darwin.forecastarc(ssd);
CREATE INDEX forecastarc_u ON darwin.forecastarc(uid);

CREATE INDEX schedule_entry_st ON darwin.schedule_entry(schedule,tpl);
CREATE INDEX schedule_entry_t ON darwin.schedule_entry(tpl);
CREATE INDEX schedule_entryarc_st ON darwin.schedule_entryarc(schedule,tpl);
CREATE INDEX schedule_entryarc_t ON darwin.schedule_entryarc(tpl);

-- Search for departures within one hour of a specified date/time
-- Used for the rtt search page

-- DROP FUNCTION darwin.searchDepartures( pcrs TEXT, pssd DATE, ps TIME WITHOUT TIME ZONE );

CREATE OR REPLACE FUNCTION darwin.searchDepartures( pcrs TEXT, pssd DATE, ps TIME WITHOUT TIME ZONE )
RETURNS TABLE(
    rid         VARCHAR(16),
    dest        VARCHAR(16),
    via         INTEGER,
    -- Public timetable
    pta         TIME WITHOUT TIME ZONE,
    ptd         TIME WITHOUT TIME ZONE,
    -- Working timetable
    wta         TIME WITHOUT TIME ZONE,
    wtd         TIME WITHOUT TIME ZONE,
    wtp         TIME WITHOUT TIME ZONE,
    -- Platforms
    plat        VARCHAR(4),
    platsup     BOOLEAN,
    cisplatsup  BOOLEAN,
    can         BOOLEAN,
    -- Working time, first of wtd, wta and wtp
    wtm     TIME WITHOUT TIME ZONE,
    term        BOOLEAN,
    trainid     CHAR(4),
    assoc       VARCHAR(16),
    assoctpl    VARCHAR(16)
) AS $$
DECLARE
    crsid   INTEGER;
    rec     RECORD;
    pe      TIME WITHOUT TIME ZONE = ps + '59 minutes 59 seconds'::INTERVAL;
BEGIN
    crsid = darwin.crs(pcrs);
    IF crsid IS NOT NULL THEN
        RETURN QUERY
            WITH departures AS (
                SELECT
                    f.rid,
                    t.tpl,
                    s.via,
                    e.pta,
                    e.ptd,
                    e.wta,
                    e.wtd,
                    e.wtp,
                    e.plat,
                    e.platsup,
                    e.cisplatsup,
                    COALESCE( se.can, false ),
                    COALESCE( e.wtd,e.wta,e.wtp) AS wtm,
                    sl.crs=crsid,
                    s.trainid,
                    sas.rid,
                    sat.tpl
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
            UNION
                SELECT
                    f.rid,
                    t.tpl,
                    s.via,
                    e.pta,
                    e.ptd,
                    e.wta,
                    e.wtd,
                    e.wtp,
                    e.plat,
                    e.platsup,
                    e.cisplatsup,
                    COALESCE( se.can, false ),
                    COALESCE( e.wtd,e.wta,e.wtp) AS wtm,
                    sl.crs=crsid,
                    s.trainid,
                    sas.rid,
                    sat.tpl
                FROM darwin.forecastarc f
                    INNER JOIN darwin.forecast_entryarc e ON f.id=e.fid
                    INNER JOIN darwin.location l ON e.tpl=l.tpl
                    INNER JOIN darwin.crs c ON l.crs = c.id
                    LEFT OUTER JOIN darwin.schedulearc s ON f.schedule=s.id
                    LEFT OUTER JOIN darwin.tiploc t ON s.dest=t.id
                    LEFT OUTER JOIN darwin.schedule_entryarc se ON se.schedule=f.schedule AND se.tpl=e.tpl
                    LEFT OUTER JOIN darwin.location sl ON s.dest=sl.tpl
                    LEFT OUTER JOIN darwin.schedule_assoc sa ON f.schedule=sa.mainid AND sa.cat='VV'
                    LEFT OUTER JOIN darwin.schedule sas ON sa.associd = sas.id
                    LEFT OUTER JOIN darwin.tiploc sat ON sa.tpl=sat.id
                WHERE l.crs=crsid AND f.ssd=pssd
            )
            SELECT * from departures d WHERE d.wtm BETWEEN ps AND pe ORDER BY wtm;
    END IF;
    RETURN;
END;
$$ LANGUAGE plpgsql;
