-- ======================================================================
-- Dimension which defines the day of the week a schedule runs on.

-- Handles schedules by resolving them from the timetable and storing them
-- so we can record movements from a schedule.
--
-- This also normalises the schedule so we can save space but also
-- report how well a specific train service performs.
-- ======================================================================

CREATE SCHEMA datetime;
SET search_path = datetime;

DROP TABLE dim_dayofweek;
CREATE TABLE dim_dayofweek (
    id      INTEGER,
    name    NAME,
    PRIMARY KEY (id)
);
INSERT INTO dim_dayofweek VALUES ( 0, 'Mon' );
INSERT INTO dim_dayofweek VALUES ( 1, 'Tue' );
INSERT INTO dim_dayofweek VALUES ( 2, 'Wed' );
INSERT INTO dim_dayofweek VALUES ( 3, 'Thu' );
INSERT INTO dim_dayofweek VALUES ( 4, 'Fri' );
INSERT INTO dim_dayofweek VALUES ( 5, 'Sat' );
INSERT INTO dim_dayofweek VALUES ( 6, 'Sun' );

DROP TABLE dim_daysrun CASCADE;

CREATE TABLE dim_daysrun (
    id      INTEGER NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE dim_daysrun OWNER TO rail;

DROP TABLE dim_daysrun_dow;
CREATE TABLE dim_daysrun_dow (
    dayrun  INTEGER NOT NULL REFERENCES dim_daysrun(id),
    dow     INTEGER NOT NULL REFERENCES dim_dayofweek(id),
    PRIMARY KEY (dayrun,dow)
);

ALTER TABLE dim_daysrun_dow OWNER TO rail;

CREATE INDEX dim_daysrun_dow_r ON dim_daysrun_dow(dayrun);
CREATE INDEX dim_daysrun_dow_d ON dim_daysrun_dow(dow);

-- Preload dim_daysrun
CREATE OR REPLACE FUNCTION dim_daysrun_load (  )
RETURNS VOID AS $$
DECLARE
    dr      INTEGER;
    dow     INTEGER;
    v       INTEGER;
BEGIN
    DELETE FROM datetime.dim_daysrun;
    FOR dr IN 0..127
    LOOP
        INSERT INTO datetime.dim_daysrun VALUES (dr);
        FOR dow IN 0..6
        LOOP
            v = 1<<dow;
            IF (dr & v) = v THEN
                INSERT INTO datetime.dim_daysrun_dow VALUES ( dr, dow );
            END IF;
        END LOOP;
    END LOOP;
END;
$$ LANGUAGE plpgsql;
SELECT dim_daysrun_load();
DROP FUNCTION dim_daysrun_load();

-- =================================================================
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA report TO rail;

