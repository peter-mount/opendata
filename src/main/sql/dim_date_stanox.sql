-- ======================================================================
-- intermediary dimension linking a stanox with a date.
--
-- This is required in reports when selecting multiple facts and you want
-- to ensure you get every entry when some facts do not exist
-- ======================================================================

CREATE SCHEMA report;
SET search_path = report;

DROP TABLE dim_date_stanox CASCADE;

CREATE TABLE dim_date_stanox (
       dt_stanox    BIGINT,
       dt_id        BIGINT REFERENCES datetime.dim_date(dt_id),
       stanox       BIGINT REFERENCES report.dim_stanox(stanox),
       PRIMARY KEY (dt_stanox)
);

ALTER TABLE dim_date_stanox OWNER TO rail;

CREATE INDEX dim_date_stanox_1 ON dim_date_stanox(dt_id);
CREATE INDEX dim_date_stanox_2 ON dim_date_stanox(stanox);
CREATE UNIQUE INDEX dim_date_stanox_3 ON dim_date_stanox(dt_id,stanox);

-- ======================================================================
-- Function that creates an entry in the dim_date_stanox table
--
-- Syntax: accepts the dt and stanox and returns the stanox.
-- It will create all associated entries as required
-- ======================================================================

CREATE OR REPLACE FUNCTION report.dim_dt_stanox( DATE, BIGINT )
RETURNS BIGINT AS $$
DECLARE
    i       BIGINT;
    d       BIGINT;
    s       BIGINT;
    rec     RECORD;
BEGIN
    d = datetime.dim_dt_id( $1 );
    s = report.dim_stanox( $2 );
    SELECT * INTO rec FROM report.dim_date_stanox WHERE dt_id=d AND stanox=s;
    IF FOUND THEN
        RETURN rec.dt_stanox;
    ELSE
        i = (d*100000::BIGINT)+s;
       INSERT INTO report.dim_date_stanox (dt_stanox,dt_id,stanox) VALUES( i, d, s);
       RETURN i;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION report.dim_dt_stanox( TIMESTAMP, BIGINT )
RETURNS BIGINT AS $$
BEGIN
    RETURN report.dim_dt_stanox( $1::DATE, $2 );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION report.dim_dt_stanox( TIMESTAMP WITH TIME ZONE, BIGINT )
RETURNS BIGINT AS $$
BEGIN
    RETURN report.dim_dt_stanox( $1::DATE, $2 );
END;
$$ LANGUAGE plpgsql;

-- This takes a Java epoch timestamp
CREATE OR REPLACE FUNCTION report.dim_dt_stanox( BIGINT, BIGINT )
RETURNS BIGINT AS $$
DECLARE
    d    BIGINT;
    s    BIGINT;
    rec    RECORD;
BEGIN
    d = datetime.dim_dt_id( $1 );
    s = report.dim_stanox( $2 );
    SELECT * INTO rec FROM report.dim_date_stanox WHERE dt_id=d AND stanox=s;
    IF NOT FOUND THEN
       INSERT INTO report.dim_date_stanox (dt_id,stanox) VALUES(d,s);
    END IF;
    RETURN s;
END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA report TO rail;
