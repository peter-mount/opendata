-- ======================================================================
-- DDL for the dim_time dimension
--
-- This table represents the time of day accurate to the minute.
-- ======================================================================

CREATE SCHEMA datetime;
SET search_path = datetime;

DROP TABLE dim_time;

CREATE TABLE dim_time (
       -- primary key, this is also the minute of the day
       tm_id 	      INTEGER,
       -- the TIME of day
       tm	      TIME UNIQUE NOT NULL,
       -- the hour & minute
       hour	      INTEGER NOT NULL CHECK( hour >=0 AND hour <24 ),
       minute	      INTEGER NOT NULL CHECK( minute >=0 AND minute < 60 ),
       -- the hour & minute but as 2 chars each, so for midnight both are '00'
       hs	      CHAR(2) NOT NULL,
       ms	      CHAR(2) NOT NULL,
       -- true for morning peak
       morning_peak   BOOLEAN NOT NULL,
       -- true for evening peak
       evening_peak   BOOLEAN NOT NULL,
       -- true for both peak periods
       peak	      BOOLEAN NOT NULL,
       -- off peak
       off_peak	      BOOLEAN NOT NULL,
       -- place against the previous calendar day, i.e. rail day starts at 2am not midnight
       prev_day	      BOOLEAN NOT NULL,
       PRIMARY KEY(tm_id)
);

CREATE INDEX dim_time_tm ON dim_time(tm);
CREATE INDEX dim_time_h  ON dim_time(hour);
CREATE INDEX dim_time_hm ON dim_time(hour,minute);
CREATE INDEX dim_time_m  ON dim_time(minute);

-- Temporary function to populate the table as there's a limited amount
-- of data that goes in here

\echo Populating dim_time

CREATE OR REPLACE FUNCTION populate_dim_time()
RETURNS INTEGER AS $$
DECLARE
	c  INTEGER;
  	h  INTEGER;
  	m  INTEGER;
  	hs TEXT;
  	ms TEXT;
  	ts TEXT;
BEGIN
  c=0;
  FOR h IN 0..23 LOOP
    FOR m IN 0..59 LOOP

      -- Create the TIME
      hs = '';
      IF h < 10 THEN
      	hs = '0';
      END IF;
      hs = hs || h;

      ms = '';
      IF m < 10 THEN
      	ms = '0';
      END IF;
      ms = ms || m;

      ts = hs || ':' || ms || ':00';

      INSERT INTO dim_time VALUES (
        -- primary key, can calculate this as minute of the day
        (h*60)+m,
	-- the actual time type
	ts::TIME,
	-- the split time
	h, m,
	hs, ms,
	-- morning peak
	h >= 6 AND h < 10,
	-- evening peak
	h >= 16 AND h < 19,
	-- peak time
	(h >= 6 AND h < 10) OR (h >= 16 AND h < 19),
	-- off peak
	(h >= 10 and h < 16 ) OR h >= 19,
	-- previous day
	h < 2
      );

      c=c+1;

    END LOOP;
  END LOOP;
  RETURN c;
END;
$$ LANGUAGE plpgsql;
SELECT populate_dim_time();
DROP FUNCTION populate_dim_time();

-- ======================================================================
-- Function that returns the tm_id for a TIME, TIMESTAMP or time millis
--
-- Using these may be better than using a query as these will handle
-- things like ignoring any seconds from a TIME for example
-- ======================================================================

CREATE OR REPLACE FUNCTION datetime.dim_tm_id( TIME )
RETURNS INTEGER AS $$
DECLARE
  rec RECORD;
BEGIN

  SELECT * INTO STRICT rec FROM datetime.dim_time WHERE tm = date_trunc('minute',$1);
  RETURN rec.tm_id;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    RAISE EXCEPTION 'Time % not found', $1;
  WHEN TOO_MANY_ROWS THEN
    RAISE EXCEPTION 'Time % not unique', $1;

END;
$$ LANGUAGE plpgsql;

-- convenience to take a TIMESTAMP
CREATE OR REPLACE FUNCTION datetime.dim_tm_id( TIMESTAMP )
RETURNS INTEGER AS $$
BEGIN
  RETURN datetime.dim_tm_id( $1::TIME );
END;
$$ LANGUAGE plpgsql;

-- convenience to take a TIMESTAMP WITH TIME ZONE
CREATE OR REPLACE FUNCTION datetime.dim_tm_id( TIMESTAMP WITH TIME ZONE )
RETURNS INTEGER AS $$
BEGIN
  RETURN datetime.dim_tm_id( $1::TIME );
END;
$$ LANGUAGE plpgsql;

-- Accepts milliseconds since 1970-01-01 i.e. the Java Epoch
CREATE OR REPLACE FUNCTION datetime.dim_tm_id( BIGINT )
RETURNS INTEGER AS $$
BEGIN
   RETURN datetime.dim_tm_id( to_timestamp( $1 / 1000 )::TIME );
END;
$$ LANGUAGE plpgsql;

-- Handles integer hour and minute
CREATE OR REPLACE FUNCTION datetime.dim_tm_id( INTEGER, INTEGER )
RETURNS INTEGER AS $$
DECLARE
  rec RECORD;
BEGIN

  SELECT * INTO STRICT rec FROM dim_time WHERE hour=$1 AND minute=$2;
  RETURN rec.tm_id;

EXCEPTION
  WHEN NO_DATA_FOUND THEN
    RAISE EXCEPTION 'Time %, % not found', $1, $2;
  WHEN TOO_MANY_ROWS THEN
    RAISE EXCEPTION 'Time %, % not unique', $1, $2;

END;
$$ LANGUAGE plpgsql;

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA datetime TO rail;
