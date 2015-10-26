-- ----------------------------------------------------------------------
-- Darwin TimeTable - imports the daily reference xml
--
-- ----------------------------------------------------------------------

--CREATE SCHEMA darwin;
SET search_path = darwin;

CREATE OR REPLACE FUNCTION darwin.darwintimetable(pxml XML)
RETURNS VOID AS $$
DECLARE
    -- Pushport namespaces
    ns          TEXT[][] := ARRAY[
                ['pport',   'http://www.thalesgroup.com/rtti/PushPort/v12'],
                ['alarm',   'http://www.thalesgroup.com/rtti/PushPort/Alarms/v1'],
                ['ct',      'http://www.thalesgroup.com/rtti/PushPort/CommonTypes/v1'],
                ['fcst',    'http://www.thalesgroup.com/rtti/PushPort/Forecasts/v2'],
                ['sched',   'http://www.thalesgroup.com/rtti/PushPort/Schedules/v1'],
                ['msg',     'http://www.thalesgroup.com/rtti/PushPort/StationMessages/v1'],
                ['status',  'http://thalesgroup.com/RTTI/PushPortStatus/root_1'],
                ['tddata',  'http://www.thalesgroup.com/rtti/PushPort/TDData/v1'],
                ['alert',   'http://www.thalesgroup.com/rtti/PushPort/TrainAlerts/v1'],
                ['tord',    'http://www.thalesgroup.com/rtti/PushPort/TrainOrder/v1'],
                ['ref',     'http://www.thalesgroup.com/rtti/XmlRefData/v3'],
                ['tt',      'http://www.thalesgroup.com/rtti/XmlTimetable/v8']
            ];
    --
    ats         TIMESTAMP WITH TIME ZONE;
    --
    axml        XML;
    ajourney    RECORD;
    asid        BIGINT;
    afid        BIGINT;

    avia        INTEGER;
    id1         BIGINT;
    id2         BIGINT;
    --
    arec        RECORD;
    arec2       RECORD;
    arec3       RECORD;
    axml2       XML;
    acrs        BIGINT;
    atpl        BIGINT;
    tpls        INTEGER[];
    aidx1       INTEGER;
    aidx2       INTEGER;
BEGIN
    ats = now();

    FOREACH axml IN ARRAY xpath('//tt:Journey',pxml,ns)
    LOOP
        SELECT
                (xpath('//tt:Journey/@rid',axml,ns))[1]::TEXT AS rid,
                (xpath('//tt:Journey/@uid',axml,ns))[1]::TEXT AS uid,
                (xpath('//tt:Journey/@trainId',axml,ns))[1]::TEXT AS trainid,
                (xpath('//tt:Journey/@ssd',axml,ns))[1]::TEXT::DATE AS ssd,
                (xpath('//tt:Journey/@toc',axml,ns))[1]::TEXT AS toc,
                (xpath('//tt:Journey/@trainCat',axml,ns))[1]::TEXT AS trainCat,
                (xpath('//tt:OR/@tpl',axml,ns))[1]::TEXT AS por,
                (xpath('//tt:OPOR/@tpl',axml,ns))[1]::TEXT AS wor,
                (xpath('//tt:DT/@tpl',axml,ns))[1]::TEXT AS pdt,
                (xpath('//tt:OPDT/@tpl',axml,ns))[1]::TEXT AS wdt
            INTO ajourney LIMIT 1;

        IF ajourney.por IS NOT NULL THEN
            id1 = darwin.tiploc(ajourney.por);
        ELSE
            id1 = darwin.tiploc(ajourney.wor);
        END IF;

        IF ajourney.por IS NOT NULL THEN
            id2 = darwin.tiploc(ajourney.pdt);
        ELSE
            id2 = darwin.tiploc(ajourney.wdt);
        END IF;

        -- We must have both origin & destination
        IF id1 IS NULL OR id2 IS NULL
        THEN
        ELSE

            -- resolve possible VIA entry
            avia = NULL;
            SELECT c.id INTO acrs FROM darwin.crs c INNER JOIN darwin.location l ON c.id=l.crs WHERE l.tpl = id1;
            IF FOUND THEN
                -- tiplocs in schedule
                tpls = ARRAY[]::INTEGER[];
                -- condition restricts this to tpl where planned arrival or departure exists
                FOREACH axml2 IN ARRAY xpath('//tt:*/@tpl[../@pta or ../@ptd]',axml,ns)
                LOOP
                    tpls=array_append(tpls,darwin.tiploc(axml2::TEXT));
                END LOOP;

                FOR arec2 IN SELECT * FROM darwin.via WHERE at=acrs AND dest=id2 AND loc1=ANY(tpls) AND loc2=ANY(tpls)
                LOOP
                    aidx1 = darwin.array_search(arec2.loc1,tpls);
                    aidx2 = darwin.array_search(arec2.loc2,tpls,aidx1+1);
                    IF aidx2 > 0 THEN
                        avia = arec2.id;
                        EXIT;
                    END IF;
                END LOOP;

                IF avia IS NULL THEN
                    SELECT id INTO arec2 FROM darwin.via WHERE at=acrs AND dest=id2 AND loc1=ANY(tpls) AND loc2 IS NULL LIMIT 1;
                    IF FOUND THEN
                        avia = arec2.id;
                    END IF;
                END IF;
            END IF;

            -- Create schedule entry
            LOOP
                SELECT id INTO arec FROM darwin.schedule WHERE rid=ajourney.rid;
                IF FOUND THEN
                    asid = arec.id;
                    EXIT;
                ELSE
                    BEGIN
                        INSERT INTO darwin.schedule (rid,uid,ssd,trainid,toc,ts,origin,dest,via)
                            VALUES (ajourney.rid,ajourney.uid,ajourney.ssd,ajourney.trainid,ajourney.toc,ats,id1,id2,avia);
                        asid=currval('darwin.schedule_id_seq');
                    EXCEPTION WHEN unique_violation THEN
                        -- Ignore & try again, the update will then be performed
                    END;
                END IF;
            END LOOP;

            -- Also create forecast table entry marked with activated and linked to the schedule
            LOOP
                SELECT id INTO arec FROM darwin.forecast WHERE rid=ajourney.rid;
                IF FOUND THEN
                    afid=arec.id;
                    EXIT;
                ELSE
                    BEGIN
                        INSERT INTO darwin.forecast (rid,uid,ssd,ts,activated,schedule)
                            VALUES (ajourney.rid,ajourney.uid,ajourney.ssd,ats,true,asid);
                        afid=currval('darwin.forecast_id_seq');
                    EXCEPTION WHEN unique_violation THEN
                        -- Ignore & try again, the update will then be performed
                    END;
                END IF;
            END LOOP;

            -- Now schedule entries
            --DELETE FROM darwin.schedule_entry WHERE schedule=id1;
            FOREACH axml2 IN ARRAY xpath('//tt:Journey/tt:*',axml,ns)
            LOOP
                SELECT  (xpath('local-name(/*)',axml2,ns))[1]::TEXT AS type,
                        (xpath('//@tpl',axml2,ns))[1]::TEXT AS tpl,
                        (xpath('//@can',axml2,ns))[1]::TEXT::BOOLEAN AS can,
                        (xpath('//@act',axml2,ns))[1]::TEXT AS act,
                        (xpath('//@pta',axml2,ns))[1]::TEXT::TIME AS pta,
                        (xpath('//@ptd',axml2,ns))[1]::TEXT::TIME AS ptd,
                        (xpath('//@wta',axml2,ns))[1]::TEXT::TIME AS wta,
                        (xpath('//@wtd',axml2,ns))[1]::TEXT::TIME AS wtd,
                        (xpath('//@wtp',axml2,ns))[1]::TEXT::TIME AS wtp,
                        (xpath('//@plat',axml2,ns))[1]::TEXT AS plat
                    INTO arec2 LIMIT 1;
                IF arec2.type != 'cancelReason' THEN
                    atpl = darwin.tiploc(arec2.tpl);

                    -- Schedule Entry
                    LOOP
                        SELECT * INTO arec3
                            FROM darwin.schedule_entry
                            WHERE schedule=asid AND tpl=atpl;
                        IF FOUND THEN
                            EXIT;
                        ELSE
                            INSERT INTO darwin.schedule_entry (schedule,type,tpl,pta,ptd,wta,wtd,wtp,act,can)
                                VALUES (asid,arec2.type,atpl,arec2.pta,arec2.ptd,arec2.wta,arec2.wtd,arec2.wtp,arec2.act,arec2.can);
                        END IF;
                    END LOOP;

                    -- Forecast Entry
                    LOOP
                        SELECT * INTO arec3
                            FROM darwin.forecast_entry
                            WHERE fid=afid AND tpl=atpl;
                        IF FOUND THEN
                            EXIT;
                        ELSE
                            INSERT INTO darwin.forecast_entry(fid,tpl,pta,ptd,wta,wtd,wtp,plat)
                                VALUES (afid,atpl,arec2.pta,arec2.ptd,arec2.wta,arec2.wtd,arec2.wtp,arec2.plat);
                        END IF;
                    END LOOP;
                END IF;
            END LOOP;

        -- End if not valid entry
        END IF;

    END LOOP;
END;
$$ LANGUAGE plpgsql;
