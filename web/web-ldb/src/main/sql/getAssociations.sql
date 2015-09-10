-- ======================================================================
-- Return the latest Darwin associations for a train
-- ======================================================================

SET search_path = darwin;

-- Return the schedule for a train

-- DROP FUNCTION darwin.getAssociations( prid TEXT );

CREATE OR REPLACE FUNCTION darwin.getAssociations( prid TEXT )
RETURNS TABLE(
    mainid      BIGINT,
    associd     BIGINT,
    main        VARCHAR(16),
    assoc       VARCHAR(16),
    tpl         VARCHAR(16),
    cat         CHAR(2),
    cancelled   BOOLEAN,
    deleted     BOOLEAN,
    -- Public timetable
    pta         TIME WITHOUT TIME ZONE,
    ptd         TIME WITHOUT TIME ZONE,
    -- Working timetable
    wta         TIME WITHOUT TIME ZONE,
    wtd         TIME WITHOUT TIME ZONE,
    -- coalesced working time, wtd then wta
    wtm         TIME WITHOUT TIME ZONE,
    tplid       INTEGER
) AS $$
DECLARE
    schedid     BIGINT;
BEGIN
    schedid = darwin.getScheduleID(prid);
    IF schedid IS NOT NULL THEN
        RETURN QUERY
            WITH associations AS (
                SELECT
                    a.mainid,
                    a.associd,
                    ma.rid,
                    aa.rid,
                    t.tpl,
                    a.cat,
                    a.cancelled,
                    a.deleted,
                    a.pta, a.ptd,
                    a.wta, a.wtd,
                    COALESCE( a.wtd, a.wta) AS wtm,
                    a.tpl
                FROM darwin.schedule_assoc a
                    INNER JOIN darwin.schedule ma ON a.mainid=ma.id
                    INNER JOIN darwin.schedule aa ON a.associd=aa.id
                    INNER JOIN darwin.tiploc t ON a.tpl = t.id
                WHERE a.mainid = schedid
            UNION
                SELECT
                    a.mainid,
                    a.associd,
                    ma.rid,
                    aa.rid,
                    t.tpl,
                    a.cat,
                    a.cancelled,
                    a.deleted,
                    a.pta, a.ptd,
                    a.wta, a.wtd,
                    COALESCE( a.wtd, a.wta) AS wtm,
                    a.tpl
                FROM darwin.schedule_assocarc a
                    INNER JOIN darwin.schedulearc ma ON a.mainid=ma.id
                    INNER JOIN darwin.schedulearc aa ON a.associd=aa.id
                    INNER JOIN darwin.tiploc t ON a.tpl = t.id
                WHERE a.mainid = schedid
            )
            SELECT * FROM associations order by wtm;
    END IF;
    RETURN;
END;
$$ LANGUAGE plpgsql;
