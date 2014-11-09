-- ----------------------------------------------------------------------
-- RTPPM - Real Time Public Performance Measure
-- ----------------------------------------------------------------------

CREATE SCHEMA rtppm;
SET search_path = rtppm;

DROP TABLE daily;
DROP TABLE realtime;

DROP TABLE operator;

-- ----------------------------------------------------------------------
-- Normalisation table of the reported tocs
-- ----------------------------------------------------------------------
CREATE TABLE operator (
    id          SERIAL,
    operator    NAME,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX operator_n ON operator(operator);


-- Function to translate operator name to id
CREATE OR REPLACE FUNCTION operator( n name)
RETURNS BIGINT AS $$
DECLARE
    tmp RECORD;
BEGIN
    SELECT * INTO tmp FROM rtppm.operator WHERE operator=n;
    IF NOT FOUND THEN
        INSERT INTO rtppm.operator (operator) VALUES (n);
        RETURN currval('rtppm.operator_id_seq');
    ELSE
        RETURN tmp.id;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------
-- The final daily performance per Operator
-- ----------------------------------------------------------------------
CREATE TABLE daily (
    id SERIAL,
    -- The normalized date & time
    dt		BIGINT NOT NULL REFERENCES datetime.dim_date(dt_id),
    -- Performance data
    operator    BIGINT REFERENCES operator(id),
    run         INTEGER,                        -- No of trains run today
    ontime      INTEGER,                        -- No on time <5m or <10m
    late        INTEGER,                        -- No late 5m < d < 30m
    canc        INTEGER,                        -- Cancelled or Very Late >30m
    ppm         INTEGER,                        -- PPM %age
    rolling     INTEGER,                        -- Rolling PPM
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX daily_i ON daily(id);
CREATE INDEX daily_o ON daily(operator);
CREATE INDEX daily_d ON daily(dt);
CREATE UNIQUE INDEX daily_to ON daily(dt,operator);

-- ----------------------------------------------------------------------
-- The current performance, updated once per minute
-- ----------------------------------------------------------------------
CREATE TABLE realtime (
    id SERIAL,
    -- The normalized date & time
    dt		BIGINT NOT NULL REFERENCES datetime.dim_date(dt_id),
    tm          INTEGER NOT NULL REFERENCES datetime.dim_time(tm_id),
    -- Performance data
    operator    BIGINT REFERENCES operator(id),
    run         INTEGER,                        -- No of trains run today
    ontime      INTEGER,                        -- No on time <5m or <10m
    late        INTEGER,                        -- No late 5m < d < 30m
    canc        INTEGER,                        -- Cancelled or Very Late >30m
    ppm         INTEGER,                        -- PPM %age
    rolling     INTEGER,                        -- Rolling PPM
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX realtime_i ON realtime(id);
CREATE INDEX realtime_o ON realtime(operator);
CREATE INDEX realtime_dt ON realtime(dt,tm);
CREATE INDEX realtime_d ON realtime(dt);
CREATE UNIQUE INDEX realtime_dto ON realtime(dt,tm,operator);

-- ----------------------------------------------------------------------
-- Function used to record the operatorpageppm data
-- ----------------------------------------------------------------------

CREATE OR REPLACE FUNCTION rtppm.operatorppm
  (
    p_operator    NAME,
    p_ts          TIMESTAMP,
    p_run         INTEGER,
    p_ontime      INTEGER,
    p_late        INTEGER,
    p_canc        INTEGER,
    p_ppm         INTEGER,
    p_rolling     INTEGER
  )
RETURNS BIGINT AS $$
DECLARE
    rec           RECORD;
    d_dt          BIGINT;
    d_tm          INTEGER;
    d_operator    BIGINT;
    d_id          BIGINT;
    d_ts          TIMESTAMP;
BEGIN

    d_dt = datetime.dim_dt_id(p_ts);
    d_tm = datetime.dim_tm_id(p_ts);
    d_operator = rtppm.operator(p_operator);

    SELECT * INTO rec FROM rtppm.realtime WHERE dt=d_dt AND tm=d_tm AND operator=d_operator;
    IF FOUND THEN
        -- We already have data - usually when importing archives
        d_id = rec.id;
    ELSE
        SET search_path = rtppm;
        INSERT INTO rtppm.realtime
            (dt,tm,operator,run,ontime,late,canc,ppm,rolling)
            VALUES ( d_dt, d_tm, d_operator, p_run, p_ontime, p_late, p_canc, p_ppm, p_rolling);
        d_id = CURRVAL('rtppm.realtime_id_seq');
    END IF;

    -- Also update daily table

    -- Adjust timestamp by 2 hours as a rail day starts at 2am.
    d_ts = p_ts - '2 hours'::INTERVAL;
    d_dt = datetime.dim_dt_id(d_ts);
    d_tm = datetime.dim_tm_id(d_ts);

    -- Ignore early hours, so this will ignore 0200-0400. We'll pick it up after then
    -- This also allows for data arriving with post 0200 timestamps which I have spotted
    IF d_tm > 120 THEN

        SELECT * INTO rec FROM rtppm.daily WHERE dt=d_dt AND operator=d_operator;
        IF NOT FOUND THEN
            SET search_path = rtppm;
            INSERT INTO rtppm.daily
                (dt,operator,run,ontime,late,canc,ppm,rolling)
                VALUES ( d_dt, d_operator, p_run, p_ontime, p_late, p_canc, p_ppm, p_rolling);
            d_id = CURRVAL('rtppm.realtime_id_seq');
        ELSE IF p_run > rec.run OR p_ontime > rec.ontime OR p_late > rec.late OR p_canc > rec.canc THEN
            UPDATE rtppm.daily
                SET run = p_run, ontime = p_ontime, late=p_late, canc=p_canc, ppm=p_ppm, rolling=p_rolling
                WHERE id = rec.id;
        END IF;

    END IF;

    RETURN d_id;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA rtppm TO rail;
