-- ======================================================================
-- Return the latest Darwin schedule entries for a train
-- ======================================================================

-- Note: this returns additional but for now this will always be false.
--
-- This will later be set to true if the schedule entry has been added.
--
-- For example: 201509090600675 was STP to FAV via Ebbsfleet & Chatham but
-- was cancelled Ebbsfleet via chatham due to a person hit at Higham so was
-- diverted via AFK to FAV. On the detail rtt page we show ashford between
-- ROC & Chatham which is wrong, so later (if we can do this) we'll try to
-- mark new subsequent entries in the schedule so in the route diagram
-- AFK would have been to the right in the bypass route, not as a dent in
-- the original cancelled route.

SET search_path = darwin;

-- Return the schedule for a train

-- DROP FUNCTION darwin.getScheduleEntries( prid TEXT );

CREATE OR REPLACE FUNCTION darwin.getScheduleEntries( prid TEXT )
RETURNS TABLE(
    id          BIGINT,
    schedule    BIGINT,
    type        NAME,
    tpl         VARCHAR(16),
    -- Public timetable
    pta         TIME WITHOUT TIME ZONE,
    ptd         TIME WITHOUT TIME ZONE,
    -- Working timetable
    wta         TIME WITHOUT TIME ZONE,
    wtd         TIME WITHOUT TIME ZONE,
    wtp         TIME WITHOUT TIME ZONE,
    --
    act         NAME,
    can         BOOLEAN,
    -- additional entry
    add         BOOLEAN,
    -- the tpl's id
    tplid       INTEGER,
    -- working time, first non null of wtd, wta or wtp in that order
    wtm         TIME WITHOUT TIME ZONE
) AS $$
DECLARE
    sid         BIGINT;
BEGIN
    sid = darwin.getScheduleID(prid);
    IF sid IS NOT NULL THEN
        RETURN QUERY
            WITH entries AS (
                SELECT
                    s.id,
                    s.schedule,
                    s.type,
                    t.tpl,
                    s.pta, s.ptd,
                    s.wta, s.wtd, s.wtp,
                    s.act,
                    COALESCE( s.can, false ),
                    false,
                    s.tpl,
                    COALESCE( s.wtd, s.wta, s.wtp ) as wtm
                FROM darwin.schedule_entry s
                    INNER JOIN darwin.tiploc t ON s.tpl = t.id
                WHERE s.schedule = sid
            UNION
                SELECT
                    s.id,
                    s.schedule,
                    s.type,
                    t.tpl,
                    s.pta, s.ptd,
                    s.wta, s.wtd, s.wtp,
                    s.act,
                    COALESCE( s.can, false ),
                    false,
                    s.tpl,
                    COALESCE( s.wtd, s.wta, s.wtp ) as wtm
                FROM darwin.schedule_entryarc s
                    INNER JOIN darwin.tiploc t ON s.tpl = t.id
                WHERE s.schedule = sid
            )
            SELECT * FROM entries ORDER BY wtm;
    END IF;
    RETURN;
END;
$$ LANGUAGE plpgsql;
