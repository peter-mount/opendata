-- ======================================================================
-- Operator Dimension
--
-- Links records to an operating company
-- ======================================================================

CREATE SCHEMA report;
SET search_path = report;

DROP TABLE dim_operator CASCADE;

CREATE TABLE dim_operator (
       operatorid   INTEGER,
       name         NAME DEFAULT '',
       PRIMARY KEY (operatorid)
);

ALTER TABLE dim_operator OWNER TO rail;

-- Return the operatorid by operatorid
--
-- Now this may sound weird but this also ensures that the record exists.
-- Once created we can then update the table with static content.
--
-- One purpose of this is for when a new operator begins, we will just
-- carry on recording the new operator.

CREATE OR REPLACE FUNCTION dim_operator( INTEGER )
RETURNS INTEGER AS $$
DECLARE
    rec RECORD;
BEGIN
    SELECT * INTO rec FROM report.dim_operator WHERE operatorid = $1;
    IF NOT FOUND THEN
        INSERT INTO report.dim_operator (operatorid) VALUES ($1);
    END IF;
    RETURN $1;
END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA report TO rail;
