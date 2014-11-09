-- ======================================================================
-- A single function which is called by the analyser to record a
-- trust movement.
--
-- What this does is to call other functions on the passed data, so we
-- do as much as possible in a single call to the db.
--
-- Parameters:
--  pdate       The date of this movement
--  ptuid       The train schedule uid
--  ptid        The trust train id
--  pmsgtype    The trust message type
--  pjson       The json of this message
--
-- ======================================================================

CREATE SCHEMA report;
SET search_path = report;


CREATE OR REPLACE FUNCTION report.trust(
    pdate       DATE,
    ptuid       CHAR(6),
    ptid        CHAR(10),
    ptclass     INTEGER,
    pmsgtype    INTEGER,
    pstanox     BIGINT,
    popid       INTEGER,
    pdelay      INTEGER,
    pjson       TEXT
)
RETURNS VOID AS $$
DECLARE
    vdt     BIGINT;
    vti     BIGINT;
    vtid     BIGINT;
    vdts    BIGINT;
    vtclass INTEGER;
    shed    BIGINT;
BEGIN
    vdt = datetime.dim_dt_id(pdate);
    vtid = report.dim_trainid(ptid);

    -- Record the JSON
    EXECUTE report.train_movement(vdt, vtid, pjson);

    -- Train activation then pull in the schedule
    IF pmsgtype = 1 THEN
        vti = report.dim_trainuid(ptuid);
        shed=report.resolve_schedule(vdt,vti);

        -- Add the schedule if we've resovled it
        IF shed IS NOT NULL THEN
            UPDATE report.train_movement
                SET schedule = shed
                WHERE dt_id=vdt AND trainid=vtid;
        END IF;
    END IF;

    -- Performance reporting
    IF pstanox > 0 THEN
        vdts = report.dim_dt_stanox(pdate,pstanox);
        EXECUTE report.perf_stanox( vdts, report.dim_operator(popid), ptclass, pdelay );
    END IF;
END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA report TO rail;
