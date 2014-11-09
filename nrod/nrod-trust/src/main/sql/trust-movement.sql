-- ======================================================================
-- DDL for trust movement recording
--
-- ======================================================================

CREATE SCHEMA report;
SET search_path = report;

-- ======================================================================
-- dim_trainuid
--
-- normalises a train uid
-- ----------------------------------------------------------------------

DROP TABLE train_movement;

CREATE TABLE train_movement (
    dt_id       BIGINT NOT NULL REFERENCES datetime.dim_date(dt_id),
    trainid     BIGINT NOT NULL REFERENCES report.dim_trainid(id),
    schedule    BIGINT REFERENCES report.dim_schedule(id),
    movement    TEXT NOT NULL
);
ALTER TABLE train_movement OWNER TO rail;

CREATE UNIQUE INDEX train_movement_dt ON train_movement(dt_id,trainid);
CREATE INDEX train_movement_d ON train_movement(dt_id);
CREATE INDEX train_movement_t ON train_movement(trainid);

-- ======================================================================
-- Function to insert a train movement
--
-- Parameters:
--  pdt     dim_date id
--  pti     Train UID
--  pmv     JSON to append
--
-- ======================================================================

CREATE OR REPLACE FUNCTION report.train_movement( pdt BIGINT, pti BIGINT, pmv TEXT)
RETURNS VOID AS $$
DECLARE
    shed    BIGINT;
    json    TEXT;
    tmp     RECORD;
BEGIN
    SELECT * INTO tmp FROM report.train_movement WHERE dt_id=pdt AND trainid=pti;
    IF FOUND THEN
        UPDATE report.train_movement
            SET movement = CONCAT(tmp.movement,CONCAT(',',pmv))
            WHERE dt_id=pdt AND trainid=pti;
    ELSE
        INSERT INTO report.train_movement
            (dt_id,trainid,movement)
            VALUES (pdt,pti,pmv);
    END IF;
END;
$$ LANGUAGE plpgsql;

-- ======================================================================
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA report TO rail;
