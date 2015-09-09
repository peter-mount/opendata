
-- Return a route based on a train in Darwin

-- DROP FUNCTION darwin.getRoute( prid TEXT, x FLOAT, starty FLOAT );

CREATE OR REPLACE FUNCTION darwin.getRoute( prid TEXT, x FLOAT, starty FLOAT )
RETURNS TABLE( stpl VARCHAR(16), etpl VARCHAR(16), sx FLOAT, sy FLOAT, ex FLOAT, ey FLOAT, can BOOLEAN, pass BOOLEAN, stop BOOLEAN ) AS $$
DECLARE
    rec     RECORD;
    fst     BOOLEAN = TRUE;
    mode    BOOLEAN;
    ltpl    VARCHAR(16) = NULL;
    tpl     VARCHAR(16) = NULL;
    y       FLOAT = starty;
    ty      FLOAT;
    xo      FLOAT = 0.5;
BEGIN

    FOR rec IN 
        WITH tiplocs AS (
            SELECT t.tpl as tiploc,
                COALESCE( e.ptd, e.pta, e.wtd, e.wta, e.wtp ) as tm,
                COALESCE( se.can, false ) AS canc,
                e.wtp IS NOT NULL AS pass,
                COALESCE( e.ptd, e.pta ) IS NOT NULL AS stop
                FROM darwin.forecast_entry e
                INNER JOIN darwin.forecast f ON e.fid = f.id
                INNER JOIN darwin.tiploc t ON e.tpl=t.id
                LEFT OUTER JOIN darwin.schedule s ON s.rid = f.rid
                LEFT OUTER JOIN darwin.schedule_entry se ON se.schedule=s.id AND se.tpl=e.tpl
                WHERE f.rid = prid --AND e.wtp IS NULL
        UNION
            SELECT t.tpl as tiploc,
                COALESCE( e.ptd, e.pta, e.wtd, e.wta, e.wtp ) as tm,
                COALESCE( se.can, false ) AS canc,
                e.wtp IS NOT NULL AS pass,
                COALESCE( e.ptd, e.pta ) IS NOT NULL AS stop
                FROM darwin.forecast_entryarc e
                INNER JOIN darwin.forecastarc f ON e.fid = f.id
                INNER JOIN darwin.tiploc t ON e.tpl=t.id
                LEFT OUTER JOIN darwin.schedulearc s ON s.rid = f.rid
                LEFT OUTER JOIN darwin.schedule_entryarc se ON se.schedule=s.id AND se.tpl=e.tpl
                WHERE f.rid = prid --AND e.wtp IS NULL
        ) SELECT * FROM tiplocs ORDER BY tm
    LOOP
        IF fst THEN
            fst=FALSE;
            mode=rec.canc;
            ltpl = rec.tiploc;
            -- A point to ensure we can display the start point
            RETURN QUERY SELECT ltpl, rec.tiploc, x, y, x, y, mode, rec.pass, rec.stop;
            y = y + 1;
        ELSE
            IF mode != rec.canc THEN
                mode = rec.canc;
                IF mode THEN
                    -- switching to cancelled
                    tpl = ltpl;
                    ty = y-1;
                ELSE
                    IF tpl IS NOT NULL THEN
                        -- Route around the cancelled stations
                        RETURN QUERY SELECT tpl, rec.tiploc, x, ty, x+xo, ty+1, FALSE, FALSE, FALSE;
                        IF ty < (y-1) THEN
                            RETURN QUERY SELECT tpl, rec.tiploc, x+xo, ty+1, x+xo, y-1, FALSE, FALSE, FALSE;
                        END IF;
                        RETURN QUERY SELECT tpl, rec.tiploc, x+xo, y-1, x, y, FALSE, FALSE, FALSE;
                    END IF;
                    tpl=NULL;
                END IF;
            END IF;

            RETURN QUERY SELECT ltpl, rec.tiploc, x, y-1, x, y, mode, rec.pass, rec.stop;
            ltpl = rec.tiploc;
            y = y + 1;
        END IF;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- select darwin.getRoute( '201509090600675', 0.0, 0.0 );

