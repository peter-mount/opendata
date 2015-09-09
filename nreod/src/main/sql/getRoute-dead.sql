
-- Return a route based on a train in Darwin
CREATE OR REPLACE FUNCTION darwin.getRoute( prid TEXT )
RETURNS TABLE( tpl TEXT, lid INTEGER) AS $$
DECLARE
    rec     RECORD;
    mrt     TEXT[];
    -- For splitting
    ary     TEXT[];
    fst     BOOLEAN = true;
    mode    BOOLEAN;
    ltpl    TEXT = NULL;
BEGIN
    -- First get all of the tiplocs for the entire route
    mrt = ARRAY( 
        WITH tiplocs AS (
            SELECT t.tpl as tiploc,
                COALESCE( e.ptd, e.pta, e.wtd, e.wta, e.wtp ) as tm
                FROM darwin.forecast_entry e
                INNER JOIN darwin.forecast f ON e.fid = f.id
                INNER JOIN darwin.tiploc t ON e.tpl=t.id
               WHERE f.rid = prid and e.wtp is null
            UNION
            SELECT t.tpl as tiploc,
                COALESCE( e.ptd, e.pta, e.wtd, e.wta, e.wtp ) as tm
                    FROM darwin.forecast_entryarc e
                    INNER JOIN darwin.forecastarc f ON e.fid = f.id
                    INNER JOIN darwin.tiploc t ON e.tpl=t.id
               WHERE f.rid = prid and e.wtp is null
            ORDER BY tm
        )
        SELECT tiploc FROM tiplocs
    )::TEXT[];
    --RAISE NOTICE 'tiplocs %', mrt;

    -- Pass 1 Generate our full route
--     PERFORM routemap.route( mrt );

    -- Pass 1 Generate a route based on the schedule
   PERFORM routemap.route( ARRAY( 
       WITH tiplocs AS (
           SELECT t.tpl as tiploc,
               COALESCE( e.ptd, e.pta, e.wtd, e.wta ) as tm
               FROM darwin.forecast f
               LEFT OUTER JOIN darwin.schedule s ON s.rid = f.rid
               LEFT OUTER JOIN darwin.schedule_entry e ON e.schedule=s.id
               LEFT OUTER JOIN darwin.tiploc t ON e.tpl=t.id
               WHERE f.rid = prid --and e.wtp is null
           UNION
           SELECT t.tpl as tiploc,
               COALESCE( e.ptd, e.pta, e.wtd, e.wta ) as tm
                   FROM darwin.forecastarc f
                   LEFT OUTER JOIN darwin.schedulearc s ON s.rid = f.rid
                    LEFT OUTER JOIN darwin.schedule_entryarc e ON e.schedule=s.id
                    LEFT OUTER JOIN darwin.tiploc t ON e.tpl=t.id
               WHERE f.rid = prid --and e.wtp is null
            ORDER BY tm
        )
        SELECT tiploc FROM tiplocs
    )::TEXT[] );

    -- Pass 2 Run through the route breaking it into parts where the 
    fst=true;
    FOR rec IN 
        WITH tiplocs AS (
            SELECT t.tpl as tiploc,
                COALESCE( e.ptd, e.pta, e.wtd, e.wta, e.wtp ) as tm,
                COALESCE( se.can, false ) AS canc
                FROM darwin.forecast_entry e
                INNER JOIN darwin.forecast f ON e.fid = f.id
                INNER JOIN darwin.tiploc t ON e.tpl=t.id
                LEFT OUTER JOIN darwin.schedule s ON s.rid = f.rid
                LEFT OUTER JOIN darwin.schedule_entry se ON se.schedule=s.id AND se.tpl=e.tpl
                WHERE f.rid = prid --AND e.wtp IS NULL
        UNION
            SELECT t.tpl as tiploc,
                COALESCE( e.ptd, e.pta, e.wtd, e.wta, e.wtp ) as tm,
                COALESCE( se.can, false ) AS canc
                FROM darwin.forecast_entryarc e
                INNER JOIN darwin.forecastarc f ON e.fid = f.id
                INNER JOIN darwin.tiploc t ON e.tpl=t.id
                LEFT OUTER JOIN darwin.schedulearc s ON s.rid = f.rid
                LEFT OUTER JOIN darwin.schedule_entryarc se ON se.schedule=s.id AND se.tpl=e.tpl
                WHERE f.rid = prid --AND e.wtp IS NULL
        ) SELECT tiploc, tm, canc FROM tiplocs WHERE canc = false ORDER BY tm
    LOOP
        ltpl = rec.tiploc;
        IF fst THEN
            fst=false;
            mode=rec.canc;
            ary = ARRAY[ltpl]::TEXT[];
        ELSE
            ary = array_append(ary,ltpl);
            
            -- A break?
            RAISE NOTICE '% % %', ltpl, mode, rec.canc;
            IF mode != rec.canc THEN
                -- save what we have
                RAISE NOTICE 'Switching % %', mode, ary;
                --PERFORM routemap.route( ary );
                RETURN QUERY SELECT * FROM routemap.route( ary );
                -- Now reset the array with this tiploc as the start
                ary = ARRAY[ltpl]::TEXT[];
            END IF;

            mode=rec.canc;
        END IF;
    END LOOP;
    -- If we still have a route to finish off
    IF array_length(ary,1)>1 THEN
        RAISE NOTICE 'Finalising %', ary;
        --PERFORM routemap.route( ary );
        RETURN QUERY SELECT * FROM routemap.route( ary );
    END IF;

    -- Now run through the route again to get the final result
--     RETURN QUERY SELECT * FROM routemap.route( mrt );
END;
$$ LANGUAGE plpgsql;
select routemap.resetmap();
select  routemap.linktiploc('EBS','AFK'), routemap.linktiploc('AFK','FAV');

 select darwin.getRoute( '201509090600675' );

