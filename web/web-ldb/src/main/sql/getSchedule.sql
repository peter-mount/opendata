-- ======================================================================
-- Return the latest Darwin schedule for a train
-- ======================================================================

SET search_path = darwin;

-- Return the schedule for a train

-- DROP FUNCTION darwin.getSchedule( prid TEXT );

CREATE OR REPLACE FUNCTION darwin.getSchedule( prid TEXT )
RETURNS TABLE(
    id          BIGINT,
    rid         VARCHAR(16),
    uid         VARCHAR(16),
    ssd         DATE,
    ts          TIMESTAMP WITH TIME ZONE,
    trainid     CHAR(4),
    toc         CHAR(2),
    cancreason  INTEGER,
    via         INTEGER,
    origin      VARCHAR(16),
    dest        VARCHAR(16),
    archived    BOOLEAN
) AS $$
BEGIN
    RETURN QUERY
        WITH schedules AS (
            SELECT
                s.id,
                s.rid, s.uid, s.ssd, s.ts,
                s.trainid, s.toc, s.cancreason,
                s.via,
                o.tpl,
                d.tpl,
                FALSE
            FROM darwin.schedule s
                INNER JOIN darwin.tiploc o ON s.origin = o.id
                INNER JOIN darwin.tiploc d ON s.dest = d.id
            WHERE s.rid = prid
        UNION
            SELECT
                s.id,
                s.rid, s.uid, s.ssd, s.ts,
                s.trainid, s.toc, s.cancreason,
                s.via,
                o.tpl,
                d.tpl,
                TRUE
            FROM darwin.schedulearc s
                INNER JOIN darwin.tiploc o ON s.origin = o.id
                INNER JOIN darwin.tiploc d ON s.dest = d.id
            WHERE s.rid = prid
        )
        SELECT * FROM schedules ORDER BY id DESC LIMIT 1;
END;
$$ LANGUAGE plpgsql;

-- Utility to return the schedule id for a RID
CREATE OR REPLACE FUNCTION darwin.getScheduleId( prid TEXT )
RETURNS BIGINT AS $$
DECLARE
    sid BIGINT;
BEGIN
    SELECT INTO sid s.id FROM darwin.schedule s WHERE s.rid = prid ORDER BY s.id DESC LIMIT 1;
    IF FOUND THEN
        RETURN sid;
    END IF;

    SELECT INTO sid s.id FROM darwin.schedulearc s WHERE s.rid = prid ORDER BY s.id DESC LIMIT 1;
    IF FOUND THEN
        RETURN sid;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;
