-- ======================================================================
-- Stanox Dimension
--
-- This exists as a stanox can map to multiple locations
-- ======================================================================

CREATE SCHEMA report;
SET search_path = report;

DROP TABLE dim_stanox CASCADE;

CREATE TABLE dim_stanox (
       stanox		BIGINT,
       PRIMARY KEY (stanox)
);

ALTER TABLE dim_stanox OWNER TO rail;

-- Return the stanox by stanox.
--
-- Now this may sound weird but this also ensures that the record exists.

CREATE OR REPLACE FUNCTION report.dim_stanox( BIGINT )
RETURNS BIGINT AS $$
DECLARE
    rec RECORD;
BEGIN
    SELECT * INTO rec FROM report.dim_stanox WHERE stanox = $1;
    IF NOT FOUND THEN
        INSERT INTO report.dim_stanox (stanox) VALUES ($1);
    END IF;
    RETURN $1;
END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA report TO rail;
