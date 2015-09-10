
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
                COALESCE( e.wtd, e.wta, e.wtp ) as tm,
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
                COALESCE( e.wtd, e.wta, e.wtp ) as tm,
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
    idx     INTEGER = 1;
BEGIN
    RETURN QUERY SELECT * FROM darwin.getRouteDetails( prid, idx );

    -- Note filter out NP associations here
    FOR rec IN
        SELECT sa.rid
            FROM darwin.schedule_assoc a
                INNER JOIN darwin.schedule s ON a.mainid = s.id
                INNER JOIN darwin.schedule sa ON a.associd = sa.id
            WHERE s.rid = prid AND a.cat != 'NP'
    UNION
        SELECT sa.rid
            FROM darwin.schedule_assocarc a
                INNER JOIN darwin.schedulearc s ON a.mainid = s.id
                INNER JOIN darwin.schedulearc sa ON a.associd = sa.id
            WHERE s.rid = prid AND a.cat != 'NP'
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
    mode    BOOLEAN[] = ARRAY[0];
    ltpl    VARCHAR(16)[] = ARRAY[0];
    tpl     VARCHAR(16)[] = ARRAY[0];
    y       FLOAT = starty;
    fst     BOOLEAN[] = ARRAY[0];
    ty      FLOAT[] = ARRAY[0];
    ly      FLOAT[] = ARRAY[0];
    xo      FLOAT = 0.5;
    xs      FLOAT[] = ARRAY[0];
BEGIN

    FOR rec IN SELECT * FROM darwin.getFullRouteDetails( prid ) ORDER BY tm
    LOOP
        x = rec.index;

        IF NOT COALESCE(fst[x],FALSE) THEN
            fst[x]=TRUE;
            mode[x]=rec.canc;

            ltpl[x] = rec.tiploc;
            ly[x] = y;
            xs[x] = x-1;

            -- Splits need to join to first point
             IF x > 1 THEN
                 RETURN QUERY SELECT rec.tiploc, rec.tiploc, xs[x]-1, ly[x]-1, xs[x], ly[x], mode[x], rec.pass, rec.stop, rec.past;
            ELSE
                -- A point to ensure we can display the start point
                RETURN QUERY SELECT rec.tiploc, rec.tiploc, xs[x], ly[x], xs[x], ly[x], mode[x], rec.pass, rec.stop, rec.past;
            END IF;
        ELSE
            IF mode[x] = rec.canc THEN
                RETURN QUERY SELECT ltpl[x], rec.tiploc, xs[x], ly[x], xs[x], y, rec.canc, rec.pass, rec.stop, rec.past;
            ELSE
                RAISE NOTICE '% % % %', ltpl[x], rec.tiploc, mode[x], rec.canc;
                RETURN QUERY SELECT ltpl[x], rec.tiploc, xs[x], ly[x], xs[x], y, true, rec.pass, rec.stop, rec.past;

                IF rec.canc THEN
                    -- switching to cancelled
                    tpl[x] = ltpl[x];
                    ty[x] = y-1;
                ELSE
                    -- Route around the cancelled stations
                    RETURN QUERY SELECT tpl[x], rec.tiploc, xs[x], ty[x], xs[x]+xo, ty[x]+1, FALSE, FALSE, FALSE, rec.past;
                    IF ty[x] < (y-1) THEN
                        RETURN QUERY SELECT tpl[x], rec.tiploc, xs[x]+xo, ty[x]+1, xs[x]+xo, y-1, FALSE, FALSE, FALSE, rec.past;
                    END IF;
                    RETURN QUERY SELECT tpl[x], rec.tiploc, xs[x]+xo, y-1, xs[x], y, FALSE, rec.pass, rec.stop, rec.past;
                END IF;

                mode[x] = rec.canc;
            END IF;

            ly[x] = y;
        END IF;

        ltpl[x] = rec.tiploc;
        y = y + 1;

    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- select darwin.getRoute( '201509090600675', 0.0, 0.0 );

