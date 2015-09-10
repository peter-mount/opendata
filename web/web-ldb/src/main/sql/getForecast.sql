-- ======================================================================
-- Return the latest Darwin forecast for a train
-- ======================================================================

SET search_path = darwin;

-- Return the schedule for a train

-- DROP FUNCTION darwin.getForecast( prid TEXT );

CREATE OR REPLACE FUNCTION darwin.getForecast( prid TEXT )
RETURNS TABLE(
    id          BIGINT,
    rid         VARCHAR(16),
    uid         VARCHAR(16),
    ssd         DATE,
    ts          TIMESTAMP WITH TIME ZONE,
    latereason  INTEGER,
    activated   BOOLEAN,
    deactivated BOOLEAN,
    schedule    BIGINT,
    archived    BOOLEAN
) AS $$
BEGIN
    RETURN QUERY
        WITH forecasts AS (
            SELECT
                f.id,
                f.rid, f.uid, f.ssd, f.ts,
                f.latereason,
                f.activated,
                f.deactivated,
                f.schedule,
                false
            FROM darwin.forecast f
            WHERE f.rid = prid
        UNION
            SELECT
                f.id,
                f.rid, f.uid, f.ssd, f.ts,
                f.latereason,
                f.activated,
                f.deactivated,
                f.schedule,
                true
            FROM darwin.forecastarc f
            WHERE f.rid = prid
        )
        SELECT * FROM forecasts ORDER BY id DESC LIMIT 1;
END;
$$ LANGUAGE plpgsql;

-- Utility to return the forecast id for a RID
CREATE OR REPLACE FUNCTION darwin.getForecastId( prid TEXT )
RETURNS BIGINT AS $$
DECLARE
    fid BIGINT;
BEGIN
    SELECT INTO fid f.id FROM darwin.forecast f WHERE f.rid = prid ORDER BY f.id DESC LIMIT 1;
    IF FOUND THEN
        RETURN fid;
    END IF;

    SELECT INTO fid f.id FROM darwin.forecastarc f WHERE f.rid = prid ORDER BY f.id DESC LIMIT 1;
    IF FOUND THEN
        RETURN fid;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;
