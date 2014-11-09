-- ======================================================================
-- dim_trainuid
--
-- normalises a train uid
-- ======================================================================

CREATE SCHEMA report;
SET search_path = report;

DROP TABLE dim_trainuid;

CREATE TABLE dim_trainuid (
    id  SERIAL,
    uid CHAR(6) NOT NULL
);
ALTER TABLE dim_trainuid OWNER TO rail;
ALTER SEQUENCE tim_trainuid_id_seq OWNER TO rail;

CREATE UNIQUE INDEX dim_trainuid_i ON dim_trainuid(id);
CREATE UNIQUE INDEX dim_trainuid_t ON dim_trainuid(uid);

-- Function to translate tiploc to it's bigint equivalent
CREATE OR REPLACE FUNCTION report.dim_trainuid(tid CHAR(6))
RETURNS BIGINT AS $$
DECLARE
    tmp RECORD;
BEGIN
    SELECT * INTO tmp FROM report.dim_trainuid WHERE uid=tid;
    IF FOUND THEN
        RETURN tmp.id;
    END IF;

    INSERT INTO report.dim_trainuid (uid) VALUES (tid);
    RETURN currval('report.dim_trainuid_id_seq');
END;
$$ LANGUAGE plpgsql;
