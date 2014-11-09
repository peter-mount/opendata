-- ======================================================================
-- DDL for the dim_date dimension
--
-- This table represents a date. The id is actually calculated based on
-- the year, month & day but is not necessarily in sequence.
--
-- ======================================================================

CREATE SCHEMA datetime;
SET search_path = datetime;

DROP TABLE dim_date CASCADE;

-- ======================================================================
-- Utility that calculates the date for a java time in millis
-- ======================================================================
CREATE OR REPLACE FUNCTION java_date(BIGINT)
RETURNS DATE AS $$
BEGIN
  RETURN to_timestamp($1/1000)::DATE;
END;
$$ LANGUAGE plpgsql;

-- ======================================================================
-- Utility that calculates the date for a java time in millis
--
-- Unlike java_date this handles rail days which start at 2am and not
-- midnight
-- ======================================================================
CREATE OR REPLACE FUNCTION java_raildate(BIGINT)
RETURNS DATE AS $$
BEGIN
  RETURN date_trunc('day',to_timestamp($1/1000) - INTERVAL '2 hours')::DATE;
END;
$$ LANGUAGE plpgsql;

-- ======================================================================
-- The Date dimension, simply provides a foreign key for a specific date
--
-- Note: dt_id is calculated with the formula (y*366)+doy rather than
-- using a sequence.
--
-- We do this because in some fact tables we have to partition them due
-- to their size, but we can only add a check() constraint on dt_id
-- so to keep them in chronological order we use the epoch value of dt.
--
-- NB: 366 is used to account for leap years
--
CREATE TABLE dim_date (
       dt_id		BIGINT NOT NULL,
       dt		DATE UNIQUE NOT NULL,
       year		INTEGER NOT NULL,
       month		INTEGER NOT NULL,
       day		INTEGER NOT NULL,
       doy		INTEGER NOT NULL,
       isoyear		INTEGER NOT NULL,
       week		INTEGER NOT NULL,
       dow		INTEGER NOT NULL,
       isodow		INTEGER NOT NULL,
       quarter		INTEGER NOT NULL,
       month_index	CHAR(7) NOT NULL,
       PRIMARY KEY(dt_id)
);
ALTER TABLE dim_date OWNER TO rail;

CREATE INDEX dim_date_1 on dim_date(dt);
CREATE INDEX dim_date_2 on dim_date(year);
CREATE INDEX dim_date_3 on dim_date(month);
CREATE INDEX dim_date_4 on dim_date(day);
CREATE INDEX dim_date_5 on dim_date(dow);
CREATE INDEX dim_date_6 on dim_date(doy);
CREATE INDEX dim_date_7 on dim_date(week);
CREATE INDEX dim_date_8 on dim_date(quarter);
CREATE INDEX dim_date_9 on dim_date(isodow);
CREATE INDEX dim_date_A on dim_date(isoyear);

-- ======================================================================
-- Function that returns the dateId for a DATE, TIMESTAMP or time millis
-- creating an entry as required
-- ======================================================================

CREATE OR REPLACE FUNCTION datetime.dim_dt_id( DATE )
RETURNS INTEGER AS $$
DECLARE
  ret BIGINT;
  rec RECORD;
BEGIN 
  SELECT * INTO rec FROM datetime.dim_date WHERE dt = $1;
  IF FOUND THEN
    RETURN rec.dt_id;
  ELSE
    ret = (extract( year from $1)*400::BIGINT)+extract(doy from $1);
    INSERT INTO datetime.dim_date VALUES (
        ret,
	$1,
	extract( year from $1 ),
    	extract( month from $1 ),
    	extract( day from $1 ),
    	extract( doy from $1 ),
    	extract( isoyear from $1 ),
    	extract( week from $1 ),
    	extract( dow from $1 ),
    	extract( isodow from $1 ),
    	extract( quarter from $1 ),
	extract( year from $1 ) || '_' || lpad(extract( month from $1 )::TEXT,2,'0')
    );
    RETURN ret;
  END IF;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION datetime.dim_dt_id( TIMESTAMP )
RETURNS INTEGER AS $$
BEGIN
   RETURN datetime.dim_dt_id( date_trunc('day', $1 )::DATE );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION datetime.dim_dt_id( TIMESTAMP WITH TIME ZONE )
RETURNS INTEGER AS $$
BEGIN
   RETURN datetime.dim_dt_id( date_trunc('day', $1 )::DATE );
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION datetime.dim_dt_id( TIMESTAMP WITHOUT TIME ZONE )
RETURNS INTEGER AS $$
BEGIN
   RETURN datetime.dim_dt_id( date_trunc('day', $1 )::DATE );
END;
$$ LANGUAGE plpgsql;

-- Accepts milliseconds since 1970-01-01 i.e. the Java Epoch
CREATE OR REPLACE FUNCTION datetime.dim_dt_id( BIGINT )
RETURNS INTEGER AS $$
BEGIN
   RETURN datetime.dim_dt_id( java_date( $1 ) );
END;
$$ LANGUAGE plpgsql;

CREATE TABLE dim_date_dow (
       dow   		  INTEGER NOT NULL,
       dayofweek	  TEXT,
       PRIMARY KEY (DOW)
);
INSERT INTO dim_date_dow VALUES ( 0, 'Sun' );
INSERT INTO dim_date_dow VALUES ( 1, 'Mon' );
INSERT INTO dim_date_dow VALUES ( 2, 'Tue' );
INSERT INTO dim_date_dow VALUES ( 3, 'Wed' );
INSERT INTO dim_date_dow VALUES ( 4, 'Thu' );
INSERT INTO dim_date_dow VALUES ( 5, 'Fri' );
INSERT INTO dim_date_dow VALUES ( 6, 'Sat' );

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA datetime TO rail;
