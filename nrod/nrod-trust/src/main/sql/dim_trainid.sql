-- ======================================================================
-- dim_trainid
--
-- normalises a train TRUST id
-- ======================================================================

CREATE SCHEMA report;
SET search_path = report;

DROP TABLE dim_trainid;

CREATE TABLE dim_trainid (
    id  SERIAL,
    uid CHAR(10) NOT NULL
);
ALTER TABLE dim_trainid OWNER TO rail;
ALTER SEQUENCE tim_trainid_id_seq OWNER TO rail;

CREATE UNIQUE INDEX dim_trainid_i ON dim_trainid(id);
CREATE UNIQUE INDEX dim_trainid_t ON dim_trainid(uid);

-- Function to translate tiploc to it's bigint equivalent
CREATE OR REPLACE FUNCTION report.dim_trainid(tid CHAR(10))
RETURNS BIGINT AS $$
DECLARE
    tmp RECORD;
BEGIN
    SELECT * INTO tmp FROM report.dim_trainid WHERE uid=tid;
    IF FOUND THEN
        RETURN tmp.id;
    END IF;

    INSERT INTO report.dim_trainid (uid) VALUES (tid);
    RETURN currval('report.dim_trainid_id_seq');
END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA report TO rail;

