-- ----------------------------------------------------------------------
-- Darwin Analyser
--
-- PLPGSQL functions to handle processing pushport xml
--
-- ----------------------------------------------------------------------

--CREATE SCHEMA darwin;
SET search_path = darwin;

-- ----------------------------------------------------------------------
-- Search index i array
-- http://stackoverflow.com/questions/8798055/finding-the-position-of-a-value-in-postgresql-arrays
-- ----------------------------------------------------------------------

CREATE OR REPLACE FUNCTION array_search(needle ANYELEMENT, haystack ANYARRAY)
RETURNS INT AS $$
    SELECT i
      FROM generate_subscripts($2, 1) AS i
     WHERE $2[i] = $1
  ORDER BY i
$$ LANGUAGE sql STABLE;

CREATE OR REPLACE FUNCTION array_search(needle ANYELEMENT, haystack ANYARRAY, ofs INTEGER)
RETURNS INT AS $$
    SELECT i
      FROM generate_subscripts($2, 1) AS i
     WHERE i >= $3 AND $2[i] = $1
  ORDER BY i
$$ LANGUAGE sql STABLE;

-- ----------------------------------------------------------------------
-- Archives a train by rid
-- Primarily used by darwinimport but is separate to allow maintenance
-- scripts to archive entries
-- ----------------------------------------------------------------------
CREATE OR REPLACE FUNCTION darwin.darwinarchive(tid TEXT)
RETURNS VOID AS $$
DECLARE
    id2     BIGINT;
    id3     BIGINT;
BEGIN

    SELECT id INTO id2 FROM darwin.schedule WHERE rid=tid;
    SELECT id INTO id3 FROM darwin.forecast WHERE rid=tid;

    -- Now archive the schedule
    INSERT INTO darwin.schedulearc
        SELECT * FROM darwin.schedule WHERE id=id2;
    INSERT INTO darwin.schedule_entryarc
        SELECT * FROM darwin.schedule_entry WHERE schedule=id2;
    INSERT INTO darwin.schedule_assocarc
        SELECT * FROM darwin.schedule_assoc WHERE mainid=id2 OR associd=id2;

    -- archive the forecasts
    INSERT INTO darwin.forecastarc
        SELECT * FROM darwin.forecast WHERE id=id3;
    INSERT INTO darwin.forecast_entryarc
        SELECT * FROM darwin.forecast_entry WHERE fid=id3;

    -- Now remove data from from the live tables
    DELETE FROM darwin.forecast_entry WHERE fid=id3;
    DELETE FROM darwin.forecast WHERE id=id3;
    DELETE FROM darwin.schedule_entry WHERE schedule=id2;
    DELETE FROM darwin.schedule_assoc WHERE mainid=id2 OR associd=id2;
    DELETE FROM darwin.schedule WHERE id=id2;

END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------

-- ----------------------------------------------------------------------
-- Parses a push port message importing its contents into the database
-- ----------------------------------------------------------------------
CREATE OR REPLACE FUNCTION darwin.darwinimport(pxml XML)
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
                ['tt',      'http://www.thalesgroup.com/rtti/XmlRefData/v3']
            ];
    -- Timestamp in the Pport element
    ats         TIMESTAMP WITH TIME ZONE;
    -- record used in xml parsing
    arec        RECORD;
    arec2       RECORD;
    aat         TIME;
    apt         TIME;
    awt         TIME;
    adelay      INTERVAL;
    -- record used in db updates
    rec         RECORD;
    -- XML from xpath & used in loops
    arxml       XML[];
    axml        XML;
    axml2       XML;
    --
    tid         TEXT;
    aldb        BOOLEAN;
    aldbdel     BOOLEAN;
    aterm       BOOLEAN;
    -- ID's
    id1         BIGINT;
    id2         BIGINT;
    id3         BIGINT;
    -- Resolve via
    avia         BIGINT;
    acrs        BIGINT;
    tpls        INTEGER[];
    aidx1       INTEGER;
    aidx2       INTEGER;
BEGIN

    ats = (xpath('//pport:Pport/@ts',pxml,ns))[1]::TEXT::TIMESTAMP WITH TIME ZONE;

    -- ---------------------------------------------------------------------------
    -- Schedules
    FOREACH axml IN ARRAY xpath('//pport:schedule',pxml,ns)
    LOOP
        -- The outer schedule element
        SELECT  (xpath('//pport:schedule/@rid',axml,ns))[1]::TEXT AS rid,
                (xpath('//pport:schedule/@uid',axml,ns))[1]::TEXT AS uid,
                (xpath('//pport:schedule/@ssd',axml,ns))[1]::TEXT::DATE AS ssd,
                (xpath('//pport:schedule/@trainId',axml,ns))[1]::TEXT AS trainid,
                (xpath('//pport:schedule/@toc',axml,ns))[1]::TEXT AS toc,
                (xpath('//sched:OR/@tpl',axml,ns))[1]::TEXT AS por,
                (xpath('//sched:OPOR/@tpl',axml,ns))[1]::TEXT AS wor,
                (xpath('//sched:DT/@tpl',axml,ns))[1]::TEXT AS pdt,
                (xpath('//sched:OPDT/@tpl',axml,ns))[1]::TEXT AS wdt,
                (xpath('//sched:cancelReason/text()',axml,ns))[1]::TEXT::INTEGER AS canc
            INTO arec LIMIT 1;

        -- Resolve origin & destination
        IF arec.por IS NOT NULL THEN
            id2 = darwin.tiploc(arec.por);
        ELSE
            id2 = darwin.tiploc(arec.wor);
        END IF;

        IF arec.pdt IS NOT NULL THEN
            id3 = darwin.tiploc(arec.pdt);
        ELSE
            id3 = darwin.tiploc(arec.wdt);
        END IF;
        
        -- resolve possible VIA entry
        avia = NULL;
        SELECT c.id INTO acrs FROM darwin.crs c INNER JOIN darwin.location l ON c.id=l.crs WHERE l.tpl = id2;
        IF FOUND THEN
            -- tiplocs in schedule
            tpls = ARRAY[]::INTEGER[];
            -- condition restricts this to tpl where planned arrival or departure exists
            FOREACH axml2 IN ARRAY xpath('//sched:*/@tpl[../@pta or ../@ptd]',axml,ns)
            LOOP
                tpls=array_append(tpls,darwin.tiploc(axml2::TEXT));
            END LOOP;

            FOR arec2 IN SELECT * FROM darwin.via WHERE at=acrs AND dest=id3 AND loc1=ANY(tpls) AND loc2=ANY(tpls)
            LOOP
                aidx1 = darwin.array_search(arec2.loc1,tpls);
                aidx2 = darwin.array_search(arec2.loc2,tpls,aidx1+1);
                IF aidx2 > 0 THEN
                    avia = arec2.id;
                    EXIT;
                END IF;
            END LOOP;

            IF avia IS NULL THEN
                SELECT id INTO arec2 FROM darwin.via WHERE at=acrs AND dest=id3 AND loc1=ANY(tpls) AND loc2 IS NULL LIMIT 1;
                IF FOUND THEN
                    avia = arec2.id;
                END IF;
            END IF;
        END IF;

        -- Create/update the schedule table
        LOOP
            SELECT id INTO rec FROM darwin.schedule WHERE rid=arec.rid;
            IF FOUND THEN
                UPDATE darwin.schedule
                    SET ts = ats, cancreason=arec.canc, via=avia
                    WHERE rid=arec.rid;
                id1 = rec.id;
                EXIT;
            ELSE
                BEGIN
                    INSERT INTO darwin.schedule (rid,uid,ssd,trainid,toc,ts,origin,dest,cancreason,via)
                        VALUES (arec.rid,arec.uid,arec.ssd,arec.trainid,arec.toc,ats,id2,id3,arec.canc,avia);
                    id1=currval('darwin.schedule_id_seq');
                EXCEPTION WHEN unique_violation THEN
                    -- Ignore & try again, the update will then be performed
                END;
            END IF;
        END LOOP;

        -- Also create forecast table entry marked with activated and linked to the schedule
        LOOP
            SELECT id INTO rec FROM darwin.forecast WHERE rid=arec.rid;
            IF FOUND THEN
                UPDATE darwin.forecast SET ts = ats, activated=true, schedule=id1 WHERE rid=arec.rid;
                EXIT;
            ELSE
                BEGIN
                    INSERT INTO darwin.forecast (rid,uid,ssd,ts,activated,schedule) VALUES (arec.rid,arec.uid,arec.ssd,ats,true,id1);
                EXCEPTION WHEN unique_violation THEN
                    -- Ignore & try again, the update will then be performed
                END;
            END IF;
        END LOOP;

        -- Now schedule entries
        DELETE FROM darwin.schedule_entry WHERE schedule=id1;
        FOREACH axml2 IN ARRAY xpath('//sched:*',axml,ns)
        LOOP
            SELECT  (xpath('local-name(/*)',axml2,ns))[1]::TEXT AS type,
                    (xpath('//@tpl',axml2,ns))[1]::TEXT AS tpl,
                    (xpath('//@can',axml2,ns))[1]::TEXT::BOOLEAN AS can,
                    (xpath('//@act',axml2,ns))[1]::TEXT AS act,
                    (xpath('//@pta',axml2,ns))[1]::TEXT::TIME AS pta,
                    (xpath('//@ptd',axml2,ns))[1]::TEXT::TIME AS ptd,
                    (xpath('//@wta',axml2,ns))[1]::TEXT::TIME AS wta,
                    (xpath('//@wtd',axml2,ns))[1]::TEXT::TIME AS wtd,
                    (xpath('//@wtp',axml2,ns))[1]::TEXT::TIME AS wtp
                INTO arec2 LIMIT 1;
            IF arec2.type != 'cancelReason' THEN
                INSERT INTO darwin.schedule_entry (schedule,type,tpl,pta,ptd,wta,wtd,wtp,act,can)
                    VALUES (id1,arec2.type,darwin.tiploc(arec2.tpl),arec2.pta,arec2.ptd,arec2.wta,arec2.wtd,arec2.wtp,arec2.act,arec2.can);
            END IF;
        END LOOP;

    END LOOP;
    -- End of schedules

    -- ---------------------------------------------------------------------------
    -- Forecasts
    FOREACH axml IN ARRAY xpath('//pport:TS',pxml,ns)
    LOOP
        -- The outer TS element
        SELECT  (xpath('//pport:TS/@rid',axml,ns))[1]::TEXT AS rid,
                (xpath('//pport:TS/@uid',axml,ns))[1]::TEXT AS uid,
                (xpath('//pport:TS/@ssd',axml,ns))[1]::TEXT::DATE AS ssd,
                (xpath('//fcst:LateReason/text()',axml,ns))[1]::TEXT::INTEGER AS latereason
            INTO arec LIMIT 1;

        -- Create/update the forecast table
        LOOP
            SELECT id INTO rec FROM darwin.forecast WHERE rid=arec.rid;
            IF FOUND THEN
                UPDATE darwin.forecast SET ts = ats WHERE rid=arec.rid;
                id1 = rec.id;
                EXIT;
            ELSE
                BEGIN
                    INSERT INTO darwin.forecast (rid,uid,ssd,ts) VALUES (arec.rid,arec.uid,arec.ssd,ats);
                    id1=currval('darwin.forecast_id_seq');
                EXCEPTION WHEN unique_violation THEN
                    -- Ignore & try again, the update will then be performed
                END;
            END IF;
        END LOOP;

        IF arec.latereason IS NOT NULL THEN
            UPDATE darwin.forecast SET latereason = arec.latereason WHERE rid=arec.rid;
        END IF;

        -- Now the forecast table entries
        -- FIXME this won't handle instances of a tiploc being visited twice right now

        -- Update the forecast_entry table
        FOREACH axml2 IN ARRAY xpath('//fcst:Location',axml,ns)
        LOOP
            -- extract data from each location element
            SELECT  (xpath('//fcst:Location/@tpl',axml2,ns))[1]::TEXT AS tpl,
                    (xpath('//fcst:Location/@pta',axml2,ns))[1]::TEXT::TIME AS pta,
                    (xpath('//fcst:Location/@ptd',axml2,ns))[1]::TEXT::TIME AS ptd,
                    (xpath('//fcst:Location/@wta',axml2,ns))[1]::TEXT::TIME AS wta,
                    (xpath('//fcst:Location/@wtd',axml2,ns))[1]::TEXT::TIME AS wtd,
                    (xpath('//fcst:Location/@wtp',axml2,ns))[1]::TEXT::TIME AS wtp,
                    (xpath('//fcst:plat/text()',axml2,ns))[1]::TEXT AS plat,
                    (xpath('//fcst:plat/@platsrc',axml2,ns))[1]::TEXT AS platsrc,
                    (xpath('//fcst:plat/@platsup',axml2,ns))[1]::TEXT::BOOLEAN AS platsup,
                    (xpath('//fcst:plat/@cisPlatsup',axml2,ns))[1]::TEXT::BOOLEAN AS cisplatsup,
                    (xpath('//fcst:length/text()',axml2,ns))[1]::TEXT::INTEGER AS length,
                    (xpath('//fcst:detachFront/text()',axml2,ns))[1]::TEXT::BOOLEAN AS detachfront,
                    (xpath('//fcst:arr/@at',axml2,ns))[1]::TEXT::TIME AS arrat,
                    (xpath('//fcst:arr/@et',axml2,ns))[1]::TEXT::TIME AS arret,
                    (xpath('//fcst:arr/@delayed',axml2,ns))[1]::TEXT::BOOLEAN AS arrdel,
                    (xpath('//fcst:dep/@at',axml2,ns))[1]::TEXT::TIME AS depat,
                    (xpath('//fcst:dep/@et',axml2,ns))[1]::TEXT::TIME AS depet,
                    (xpath('//fcst:dep/@delayed',axml2,ns))[1]::TEXT::BOOLEAN AS depdel,
                    (xpath('//fcst:pass/@at',axml2,ns))[1]::TEXT::TIME AS passat,
                    (xpath('//fcst:pass/@et',axml2,ns))[1]::TEXT::TIME AS passet,
                    (xpath('//fcst:pass/@delayed',axml2,ns))[1]::TEXT::BOOLEAN AS passdel
                INTO arec LIMIT 1;
            
            id2 = darwin.tiploc(arec.tpl);

            -- Calculate delay (if any) & the LDB status as based on same tests
            CASE
                -- train departed
                WHEN arec.depat IS NOT NULL THEN
                    aat = arec.depat;
                    apt = arec.ptd;
                    awt = arec.wtd;
                    aldb = FALSE;
                    aldbdel = FALSE;
                    aterm = FALSE;
                -- train arrived but not departed, may be terminated though
                WHEN arec.arrat IS NOT NULL THEN
                    aat = arec.arrat;
                    apt = arec.pta;
                    awt = arec.wta;
                    aldb = TRUE;
                    aldbdel = FALSE;
                    aterm = arec.wtd IS NULL;
                -- train has passed
                WHEN arec.passat IS NOT NULL THEN
                    aat = arec.passat;
                    apt = NULL;
                    awt = arec.wtp;
                    aldb = FALSE;
                    aterm = FALSE;
                    aldbdel = FALSE;
                -- departs eta
                WHEN arec.depet IS NOT NULL THEN
                    aat = arec.depet;
                    apt = arec.ptd;
                    awt = arec.wtd;
                    aldb = TRUE;
                    aldbdel = arec.depdel;
                    aterm = FALSE;
                -- arrived eta - in this case terminating
                WHEN arec.arret IS NOT NULL THEN
                    aat = arec.arret;
                    apt = arec.pta;
                    awt = arec.wta;
                    aldb = TRUE;
                    aldbdel = arec.arrdel;
                    aterm = arec.wtd IS NULL;
                WHEN arec.passet IS NOT NULL THEN
                    aat = arec.passet;
                    apt = NULL;
                    awt = arec.wtp;
                    aldb = FALSE;
                    aldbdel = arec.passdel;
                    aterm = FALSE;
                ELSE
                    aat = NULL;
                    apt = NULL;
                    awt = NULL;
                    aldb = FALSE;
                    aldbdel = FALSE;
                    aterm = FALSE;
            END CASE;
            
            -- Delay calculation
            adelay = NULL;
            IF aat IS NOT NULL THEN
                IF apt IS NOT NULL THEN
                    adelay = aat - apt;
                ELSE
                    adelay = aat - awt;
                END IF;

                -- Handle delays across midnight
                -- Note: use 24 hours not 1 day as "-23:00:00" + "1 day" becomes "1 day -23:00:00" and not "01:00:00"
                CASE
                    WHEN adelay <= '-18:00:00'::INTERVAL THEN adelay=adelay+'24 hours'::INTERVAL;
                    WHEN adelay >= '18:00:00'::INTERVAL THEN adelay=adelay-'24 hours'::INTERVAL;
                    ELSE adelay=adelay;
                END CASE;
            END IF;

            -- resolve/create the tiploc
            LOOP
                SELECT * INTO rec FROM darwin.forecast_entry WHERE fid=id1 AND tpl=id2;
                IF FOUND THEN
                    UPDATE darwin.forecast_entry
                        SET pta=arec.pta,
                            ptd=arec.ptd,
                            wta=arec.wta,
                            wtd=arec.wtd,
                            wtp=arec.wtp,
                            arr=arec.arrat,
                            dep=arec.depat,
                            pass=arec.passat,
                            etarr=arec.arret,
                            etdep=arec.depet,
                            etpass=arec.passet,
                            etarrdel=arec.arrdel,
                            etdepdel=arec.depdel,
                            etpassdel=arec.passdel,
                            length=arec.length,
                            detachfront=arec.detachfront,
                            delay=adelay,
                            plat=arec.plat,
                            platsup=arec.platsup,
                            cisplatsup=arec.cisplatsup,
                            platsrc=arec.platsrc,
                            ldb=aldb,
                            ldbdel=aldbdel,
                            tm=aat,
                            term=aterm
                        WHERE fid=id1 AND tpl=id2;
                    EXIT;
                ELSE
                    BEGIN
                        INSERT INTO darwin.forecast_entry
                            (
                                fid,tpl,
                                pta,ptd,wta,wtd,wtp,
                                delay,
                                arr,dep,pass,
                                etarr,etdep,etpass,
                                etarrdel,etdepdel,etpassdel,
                                length,
                                detachfront,
                                plat,platsup,cisplatsup,platsrc,
                                ldb,ldbdel,
                                tm,term
                            ) VALUES (
                                id1,id2,
                                arec.pta,arec.ptd,arec.wta,arec.wtd,arec.wtp,
                                adelay,
                                arec.arrat,arec.depat,arec.passat,
                                arec.arret,arec.depet,arec.passet,
                                arec.arrdel,arec.depdel,arec.passdel,
                                arec.length,
                                arec.detachfront,
                                arec.plat,
                                arec.platsup,
                                arec.cisplatsup,
                                arec.platsrc,
                                aldb,aldbdel,
                                aat,aterm
                            );
                    EXCEPTION WHEN unique_violation THEN
                        -- Do nothing, loop & try again.
                    END;
                END IF;
            END LOOP;

        END LOOP;

    END LOOP;
    -- End of forecast

    -- ---------------------------------------------------------------------------
    -- Associations
    FOREACH axml IN ARRAY xpath('//pport:association',pxml,ns)
    LOOP
        SELECT  (xpath('//sched:main/@rid',axml,ns))[1]::TEXT AS mainid,
                (xpath('//sched:main/@pta',axml,ns))[1]::TEXT::TIME AS pta,
                (xpath('//sched:main/@wta',axml,ns))[1]::TEXT::TIME AS wta,
                (xpath('//sched:assoc/@rid',axml,ns))[1]::TEXT AS associd,
                (xpath('//sched:assoc/@ptd',axml,ns))[1]::TEXT::TIME AS ptd,
                (xpath('//sched:assoc/@wtd',axml,ns))[1]::TEXT::TIME AS wtd,
                (xpath('//pport:association/@tiploc',axml,ns))[1]::TEXT AS tpl,
                (xpath('//pport:association/@category',axml,ns))[1]::TEXT AS cat,
                (xpath('//pport:association/@isCancelled',axml,ns))[1]::TEXT::BOOLEAN AS cancelled,
                (xpath('//pport:association/@isDeleted',axml,ns))[1]::TEXT::BOOLEAN AS deleted
            INTO arec LIMIT 1;

        SELECT id INTO id1 FROM darwin.schedule WHERE rid=arec.mainid;
        SELECT id INTO id2 FROM darwin.schedule WHERE rid=arec.associd;
        id3=darwin.tiploc(arec.tpl);
        IF id1 IS NOT NULL AND id2 IS NOT NULL THEN
            LOOP
                SELECT * INTO rec FROM darwin.schedule_assoc WHERE mainid=id1 AND associd=id2;
                IF FOUND THEN
                    UPDATE darwin.schedule_assoc
                        SET tpl=id3,
                            cat=arec.cat,
                            cancelled=arec.cancelled,
                            deleted=arec.deleted,
                            pta=arec.pta,
                            wta=arec.wta,
                            ptd=arec.ptd,
                            wtd=arec.wtd
                        WHERE mainid=id1 AND associd=id2;
                    EXIT;
                ELSE
                    BEGIN
                        INSERT INTO darwin.schedule_assoc
                            (mainid,associd,tpl,cat,cancelled,deleted,pta,wta,ptd,wtd)
                            VALUES (id1,id2,id3,arec.cat,arec.cancelled,arec.deleted,arec.pta,arec.wta,arec.ptd,arec.wtd);
                    EXCEPTION WHEN unique_violation THEN
                        -- Do nothing, loop & try again.
                    END;
                END IF;
            END LOOP;
        END IF;
    END LOOP;

    -- ---------------------------------------------------------------------------
    -- Station messages
    FOREACH axml IN ARRAY xpath('//pport:OW',pxml,ns)
    LOOP
        SELECT  (xpath('//pport:OW/@id',axml,ns))[1]::TEXT::BIGINT AS id,
                (xpath('//pport:OW/@cat',axml,ns))[1]::TEXT AS cat,
                (xpath('//pport:OW/@sev',axml,ns))[1]::TEXT AS sev,
                (xpath('//pport:OW/@suppress',axml,ns))[1]::TEXT::BOOLEAN AS suppress,
                array_to_string(xpath('//pport:OW/msg:Msg',axml,ns),'')::TEXT AS xml
            INTO arec LIMIT 1;

        id1 = arec.id;
        DELETE FROM darwin.message_station WHERE msgid=id1;
        DELETE FROM darwin.message WHERE id=id1;

        INSERT INTO darwin.message (id,cat,sev,suppress,xml,ts)
            VALUES (id1,arec.cat,arec.sev,arec.suppress,arec.xml,ats);

        FOREACH axml2 IN ARRAY xpath('//msg:Station/@crs',axml,ns)
        LOOP
            INSERT INTO darwin.message_station (msgid,crsid) VALUES (id1,darwin.crs(axml2::text));
        END LOOP;
        
    END LOOP;
    
    -- ---------------------------------------------------------------------------
    -- Alarms
    FOREACH axml IN ARRAY xpath('//pport:alarm/alarm:set',pxml,ns)
    LOOP
        SELECT  (xpath('/alarm:set/@id',axml,ns))[1]::TEXT AS id,
                (xpath('name(/alarm:set/*)',axml,ns))[1]::TEXT AS type
            INTO arec LIMIT 1;

        INSERT INTO darwin.alarms (aid,settd,type,xml) VALUES (arec.id,arec.type,ats,array_to_string(axml,''));
    END LOOP;

    FOREACH axml IN ARRAY xpath('//pport:alarm/alarm:clear/text()',pxml,ns)
    LOOP
        UPDATE darwin.alarms SET cleared=true, clearedts=ats WHERE aid=axml::TEXT;
    END LOOP;

    -- ---------------------------------------------------------------------------
    -- Train order
    FOREACH axml IN ARRAY xpath('//pport:trainOrder',pxml,ns)
    LOOP
        SELECT  (xpath('//tord:first/tord:rid/text()',axml,ns))[1]::TEXT AS rid1,
                (xpath('//tord:first/tord:rid/@pta',axml,ns))[1]::TEXT::TIME AS pta1,
                (xpath('//tord:first/tord:rid/@ptd',axml,ns))[1]::TEXT::TIME AS ptd1,
                (xpath('//tord:first/tord:rid/@wta',axml,ns))[1]::TEXT::TIME AS wta1,
                (xpath('//tord:first/tord:rid/@wtd',axml,ns))[1]::TEXT::TIME AS wtd1,
                (xpath('//tord:first/tord:trainID/text()',axml,ns))[1]::TEXT AS tid1,

                (xpath('//tord:second/tord:rid/text()',axml,ns))[1]::TEXT AS rid2,
                (xpath('//tord:second/tord:rid/@pta',axml,ns))[1]::TEXT::TIME AS pta2,
                (xpath('//tord:second/tord:rid/@ptd',axml,ns))[1]::TEXT::TIME AS ptd2,
                (xpath('//tord:second/tord:rid/@wta',axml,ns))[1]::TEXT::TIME AS wta2,
                (xpath('//tord:second/tord:rid/@wtd',axml,ns))[1]::TEXT::TIME AS wtd2,
                (xpath('//tord:second/tord:trainID/text()',axml,ns))[1]::TEXT AS tid2,

                (xpath('//tord:third/tord:rid/text()',axml,ns))[1]::TEXT AS rid3,
                (xpath('//tord:third/tord:rid/@pta',axml,ns))[1]::TEXT::TIME AS pta3,
                (xpath('//tord:third/tord:rid/@ptd',axml,ns))[1]::TEXT::TIME AS ptd3,
                (xpath('//tord:third/tord:rid/@wta',axml,ns))[1]::TEXT::TIME AS wta3,
                (xpath('//tord:third/tord:rid/@wtd',axml,ns))[1]::TEXT::TIME AS wtd3,
                (xpath('//tord:third/tord:trainID/text()',axml,ns))[1]::TEXT AS tid3,

                (xpath('/pport:trainOrder/@tiploc',axml,ns))[1]::TEXT AS tpl,
                (xpath('/pport:trainOrder/@crs',axml,ns))[1]::TEXT AS crs,
                (xpath('/pport:trainOrder/@platform',axml,ns))[1]::TEXT AS plat
            INTO arec LIMIT 1;

        INSERT INTO darwin.trainorder (
                tpl,crs,plat,ts,
                rid1,tid1,pta1,ptd1,wta1,wtd1,
                rid2,tid2,pta2,ptd2,wta2,wtd2,
                rid3,tid3,pta3,ptd3,wta3,wtd3
            ) VALUES (
                darwin.tiploc(arec.tpl),
                darwin.crs(arec.crs),
                arec.plat,
                ats,
                arec.rid1,arec.tid1,arec.pta1,arec.ptd1,arec.wta1,arec.wtd1,
                arec.rid2,arec.tid2,arec.pta2,arec.ptd2,arec.wta2,arec.wtd2,
                arec.rid3,arec.tid3,arec.pta3,arec.ptd3,arec.wta3,arec.wtd3
            );
    END LOOP;

    FOREACH axml IN ARRAY xpath('//pport:alarm/alarm:clear/text()',pxml,ns)
    LOOP
        UPDATE darwin.alarms SET cleared=true, clearedts=ats WHERE aid=axml::TEXT;
    END LOOP;

    -- ---------------------------------------------------------------------------
    -- Deactivations - This must be last otherwise archiving may fail
    FOREACH axml IN ARRAY xpath('//pport:deactivated/@rid',pxml,ns)
    LOOP
        tid=axml::text;

        -- Mark the forecast as deactivated
        UPDATE darwin.forecast SET deactivated=true, ts=ats WHERE rid=tid;

        -- Now archive it
        PERFORM darwin.darwinarchive(tid);
    END LOOP;

END;
$$ LANGUAGE plpgsql;

