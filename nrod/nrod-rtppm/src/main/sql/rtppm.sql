-- ----------------------------------------------------------------------
-- RTPPM - Real Time Public Performance Measure
-- ----------------------------------------------------------------------

CREATE SCHEMA rtppm;
SET search_path = rtppm;

DROP TABLE daily;
DROP TABLE realtime;

DROP TABLE performance;

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
-- Parent table defining a performance statistic
-- ----------------------------------------------------------------------
CREATE TABLE performance (
    id SERIAL,
    operator    BIGINT REFERENCES operator(id),
    ts          TIMESTAMP,
    run         INTEGER,                        -- No of trains run today
    ontime      INTEGER,                        -- No on time <5m or <10m
    late        INTEGER,                        -- No late 5m < d < 30m
    canc        INTEGER,                        -- Cancelled or Very Late >30m
    ppm         INTEGER,                        -- PPM %age
    rolling     INTEGER                         -- Rolling PPM
);

-- ----------------------------------------------------------------------
-- The final daily performance per Operator
-- ----------------------------------------------------------------------
CREATE TABLE daily () INHERITS (performance);

-- ----------------------------------------------------------------------
-- The current performance, updated once per minute
-- ----------------------------------------------------------------------
CREATE TABLE realtime () INHERITS (performance);
CREATE UNIQUE INDEX realtime_i ON realtime(id);
CREATE INDEX realtime_o ON realtime(operator);
