-- ======================================================================
-- Handles schedules by resolving them from the timetable and storing them
-- so we can record movements from a schedule.
--
-- This also normalises the schedule so we can save space but also
-- report how well a specific train service performs.
-- ======================================================================

CREATE SCHEMA report;
SET search_path = report;

DROP TABLE dim_schedule CASCADE;

CREATE TABLE dim_schedule (
    id      SERIAL,
    trainuid    BIGINT NOT NULL REFERENCES report.dim_trainuid(id),
    runsfrom    BIGINT NOT NULL REFERENCES datetime.dim_date(dt_id),
    runsto      BIGINT NOT NULL REFERENCES datetime.dim_date(dt_id),
    daysrun     INTEGER NOT NULL REFERENCES datetime.dim_daysrun(id),
    schedule    TEXT,
    PRIMARY KEY (id)
);
ALTER TABLE dim_schedule OWNER TO rail;

-- ======================================================================
-- Resolve a schedule
-- ======================================================================

CREATE OR REPLACE FUNCTION report.resolve_schedule (pdt BIGINT,pti BIGINT)
RETURNS BIGINT AS $$
DECLARE
    tdt     RECORD;
    tmp     RECORD;
    tdow    INTEGER;
    sid     BIGINT;
BEGIN
    SELECT * INTO tdt FROM datetime.dim_date WHERE dt_id=pdt;
    tdow = tdt.isodow-1;

    SELECT s.* INTO tmp
        FROM report.dim_schedule s
            INNER JOIN datetime.dim_daysrun dr ON s.daysrun = dr.id
            INNER JOIN datetime.dim_daysrun_dow drw ON dr.id = drw.dayrun
        WHERE s.trainuid = pti
            AND pdt BETWEEN runsfrom AND runsto
            AND drw.dow = tdow;
    IF FOUND THEN
        RETURN tmp.id;
    END IF;

    -- Resolve the schedule
    SELECT s.* INTO tmp
        FROM timetable.schedule s
            -- Pair trainuids with the timetable
            INNER JOIN timetable.trainuid t ON s.trainuid = t.id
            INNER JOIN report.dim_trainuid d ON t.uid=d.uid
            -- Include only those which run on this date
            INNER JOIN datetime.dim_daysrun dr ON s.dayrun = dr.id
            INNER JOIN datetime.dim_daysrun_dow drw ON dr.id = drw.dayrun

        WHERE d.id = pti
            AND tdt.dt BETWEEN s.runsfrom AND s.runsto
            AND drw.dow = tdow

        ORDER BY runsfrom DESC, runsto
        LIMIT 1;

    IF FOUND THEN
        sid = nextval('report.dim_schedule_id_seq');
        INSERT INTO report.dim_schedule (id,trainuid,runsfrom,runsto,daysrun,schedule)
            VALUES (
                sid,
                pti,
                datetime.dim_dt_id( tmp.runsfrom ),
                datetime.dim_dt_id( tmp.runsto ),
                tmp.dayrun,
                tmp.schedule
            );
        RETURN sid;
    ELSE
        -- No match in the timetable
        RETURN null;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- =================================================================
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA report TO rail;
