
-- Returns the forecast entries for getRoute & others for a train
-- DROP FUNCTION darwin.getRouteDetails( prid TEXT, idx INTEGER );
CREATE OR REPLACE FUNCTION darwin.getRouteDetails( prid TEXT, idx INTEGER )
RETURNS TABLE( index INTEGER, fid BIGINT, tiploc VARCHAR(16), tm TIME WITHOUT TIME ZONE, canc BOOLEAN, pass BOOLEAN, stop BOOLEAN, past BOOLEAN ) AS $$
DECLARE
    rec     RECORD;
    past    BOOLEAN = FALSE;
BEGIN
    FOR rec IN 
        WITH tiplocs AS (
            SELECT
                idx as index,
                e.fid as fid,
                t.tpl as tiploc,
                COALESCE( e.ptd, e.pta, e.wtd, e.wta, e.wtp ) as tm,
                COALESCE( se.can, false ) AS canc,
                e.wtp IS NOT NULL AS pass,
                COALESCE( e.ptd, e.pta ) IS NOT NULL AS stop,
                COALESCE( e.dep, e.arr, e.pass ) AS arrived
            FROM darwin.forecast_entry e
                INNER JOIN darwin.forecast f ON e.fid = f.id
                INNER JOIN darwin.tiploc t ON e.tpl=t.id
                LEFT OUTER JOIN darwin.schedule s ON s.rid = f.rid
                LEFT OUTER JOIN darwin.schedule_entry se ON se.schedule=s.id AND se.tpl=e.tpl
            WHERE f.rid = prid --AND e.wtp IS NULL
        UNION
            SELECT
                idx as index,
                e.fid as fid,
                t.tpl as tiploc,
                COALESCE( e.ptd, e.pta, e.wtd, e.wta, e.wtp ) as tm,
                COALESCE( se.can, false ) AS canc,
                e.wtp IS NOT NULL AS pass,
                COALESCE( e.ptd, e.pta ) IS NOT NULL AS stop,
                COALESCE( e.dep, e.arr, e.pass ) AS arrived
            FROM darwin.forecast_entryarc e
                INNER JOIN darwin.forecastarc f ON e.fid = f.id
                INNER JOIN darwin.tiploc t ON e.tpl=t.id
                LEFT OUTER JOIN darwin.schedulearc s ON s.rid = f.rid
                LEFT OUTER JOIN darwin.schedule_entryarc se ON se.schedule=s.id AND se.tpl=e.tpl
            WHERE f.rid = prid --AND e.wtp IS NULL
        )
        SELECT * FROM tiplocs ORDER BY tm DESC
    LOOP
        IF NOT past AND rec.arrived IS NOT NULL THEN
            past = TRUE;
        END IF;
        RETURN QUERY SELECT rec.index, rec.fid, rec.tiploc, rec.tm, rec.canc, rec.pass, rec.stop, past;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Returns all entries including associations
-- DROP FUNCTION darwin.getFullRouteDetails( prid TEXT );
CREATE OR REPLACE FUNCTION darwin.getFullRouteDetails( prid TEXT )
RETURNS TABLE( index INTEGER, fid BIGINT, tiploc VARCHAR(16), tm TIME WITHOUT TIME ZONE, canc BOOLEAN, pass BOOLEAN, stop BOOLEAN, past BOOLEAN ) AS $$
DECLARE
    rec     RECORD;
    idx     INTEGER = 0;
BEGIN
    RETURN QUERY SELECT * FROM darwin.getRouteDetails( prid, idx );

    FOR rec IN
        SELECT sa.rid
            FROM darwin.schedule_assoc a
                INNER JOIN darwin.schedule s ON a.mainid = s.id
                INNER JOIN darwin.schedule sa ON a.associd = sa.id
            WHERE s.rid = prid
    UNION
        SELECT sa.rid
            FROM darwin.schedule_assocarc a
                INNER JOIN darwin.schedulearc s ON a.mainid = s.id
                INNER JOIN darwin.schedulearc sa ON a.associd = sa.id
            WHERE s.rid = prid
    LOOP
        idx = idx + 1;
        RETURN QUERY SELECT * FROM darwin.getRouteDetails( rec.rid, idx );
    END LOOP;

END;
$$ LANGUAGE plpgsql;

-- Return a route based on a train in Darwin

-- DROP FUNCTION darwin.getRoute( prid TEXT, x FLOAT, starty FLOAT );

CREATE OR REPLACE FUNCTION darwin.getRoute( prid TEXT, x FLOAT, starty FLOAT )
RETURNS TABLE( stpl VARCHAR(16), etpl VARCHAR(16), sx FLOAT, sy FLOAT, ex FLOAT, ey FLOAT, can BOOLEAN, pass BOOLEAN, stop BOOLEAN, past BOOLEAN ) AS $$
DECLARE
    rec     RECORD;
    fst     BOOLEAN = TRUE;
    mode    BOOLEAN;
    ltpl    VARCHAR(16) = NULL;
    tpl     VARCHAR(16) = NULL;
    y       FLOAT = starty;
    ty      FLOAT[] = ARRAY[0];
    ly      FLOAT[] = ARRAY[0];
    xo      FLOAT = 0.5;
    xs      FLOAT;
BEGIN

    FOR rec IN SELECT * FROM darwin.getFullRouteDetails( prid ) ORDER BY tm
    LOOP
        x = rec.index;

        IF fst THEN
            fst=FALSE;
            mode=rec.canc;
            ltpl = rec.tiploc;
            -- A point to ensure we can display the start point
            RETURN QUERY SELECT ltpl, rec.tiploc, x, y, x, y, mode, rec.pass, rec.stop, rec.past;
            y = y + 1;
        ELSE
            -- Start of a new branch?
            IF ly[x] = 0 THEN
                ly[x] = y-1;
                xs = x-1;
            ELSE
                xs = x;
            END IF;

            IF mode != rec.canc THEN
                mode = rec.canc;
                IF mode THEN
                    -- switching to cancelled
                    tpl = ltpl;
                    ty[x] = y-1;
                ELSE
                    IF tpl IS NOT NULL THEN
                        -- Route around the cancelled stations
                        RETURN QUERY SELECT tpl, rec.tiploc, x, ty[x], x+xo, ty[x]+1, FALSE, FALSE, FALSE, rec.past;
                        IF ty[x] < (y-1) THEN
                            RETURN QUERY SELECT tpl, rec.tiploc, x+xo, ty[x]+1, x+xo, y-1, FALSE, FALSE, FALSE, rec.past;
                        END IF;
                        RETURN QUERY SELECT tpl, rec.tiploc, x+xo, y-1, x, y, FALSE, FALSE, FALSE, rec.past;
                    END IF;
                    tpl=NULL;
                END IF;
            END IF;

            RETURN QUERY SELECT ltpl, rec.tiploc, xs, ly[x], x, y, mode, rec.pass, rec.stop, rec.past;
            ltpl = rec.tiploc;
            ly[x] = y;
            y = y + 1;
        END IF;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- select darwin.getRoute( '201509090600675', 0.0, 0.0 );

