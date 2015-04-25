-- ----------------------------------------------------------------------
-- Darwin Analyser
--
-- This set of SQL parses the raw Pport xml directly into the database
--
-- ----------------------------------------------------------------------

--CREATE SCHEMA darwin;
SET search_path = darwin;

DROP TABLE trainorder;

DROP TABLE alarms;

DROP TABLE message_station;
DROP TABLE message;

DROP TABLE forecast_entry;
DROP TABLE forecast;

DROP TABLE forecast_entryarc;
DROP TABLE forecastarc;

DROP TABLE schedule_assoc;
DROP TABLE schedule_entry;
DROP TABLE schedule;

DROP TABLE schedule_assocarc;
DROP TABLE schedule_entryarc;
DROP TABLE schedulearc;

DROP TABLE crs;
DROP TABLE tiploc;

-- ----------------------------------------------------------------------
-- Normalisation table of tiplocs
-- ----------------------------------------------------------------------
CREATE TABLE tiploc (
    id          SERIAL,
    tpl         varCHAR(16),
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX tiploc_c ON tiploc(tpl);
ALTER TABLE tiploc OWNER TO rail;
ALTER TABLE tiploc_id_seq OWNER TO rail;

CREATE OR REPLACE FUNCTION darwin.tiploc (ptpl TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
BEGIN
    LOOP
        SELECT * INTO rec FROM darwin.tiploc WHERE tpl=ptpl;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO darwin.tiploc (tpl) VALUES (ptpl);
            RETURN currval('darwin.tiploc_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------
-- Normalisation table of crs
-- ----------------------------------------------------------------------
CREATE TABLE crs (
    id          SERIAL,
    crs         CHAR(3),
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX crs_c ON crs(crs);
ALTER TABLE crs OWNER TO rail;
ALTER TABLE crs_id_seq OWNER TO rail;

CREATE OR REPLACE FUNCTION darwin.crs (pcrs TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
BEGIN
    LOOP
        SELECT * INTO rec FROM darwin.crs WHERE crs=pcrs;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO darwin.crs (crs) VALUES (pcrs);
            RETURN currval('darwin.crs_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------
-- Schedule
--
-- This comprises two sets of tables, the active one forecast and
-- an archive table forecastarc. The second set of tables are used when
-- a train is deactivated.
--
-- This is so that the live updates operate on a smaller table, keeping
-- performance up.
-- ----------------------------------------------------------------------

CREATE TABLE schedule (
    id          SERIAL,
    rid         VARCHAR(16) NOT NULL,
    uid         VARCHAR(16) NOT NULL,
    ssd         DATE,
    ts          TIMESTAMP WITH TIME ZONE,
    trainId     CHAR(4),
    toc         CHAR(2),
    origin      INTEGER NOT NULL REFERENCES tiploc(id),
    dest        INTEGER NOT NULL REFERENCES tiploc(id),
    PRIMARY KEY(id)
);
CREATE UNIQUE INDEX schedule_r ON schedule(rid);
CREATE INDEX schedule_t ON schedule(ts);
CREATE INDEX schedule_h ON schedule(trainId);
CREATE INDEX schedule_sh ON schedule(ssd,trainId);
CREATE INDEX schedule_so ON schedule(ssd,toc);
CREATE INDEX schedule_o ON schedule(toc);
ALTER TABLE schedule OWNER TO rail;
ALTER TABLE schedule_id_seq OWNER TO rail;

CREATE TABLE schedulearc (
    id          BIGINT NOT NULL,
    rid         VARCHAR(16) NOT NULL,
    uid         VARCHAR(16) NOT NULL,
    ssd         DATE,
    ts          TIMESTAMP WITH TIME ZONE,
    trainId     CHAR(4),
    toc         CHAR(2),
    origin      INTEGER NOT NULL REFERENCES tiploc(id),
    dest        INTEGER NOT NULL REFERENCES tiploc(id),
    PRIMARY KEY(id)
);
CREATE UNIQUE INDEX schedulearc_r ON schedulearc(rid);
CREATE INDEX schedulearc_t ON schedulearc(ts);
CREATE INDEX schedulearc_h ON schedulearc(trainId);
CREATE INDEX schedulearc_sh ON schedulearc(ssd,trainId);
CREATE INDEX schedulearc_so ON schedulearc(ssd,toc);
CREATE INDEX schedulearc_o ON schedulearc(toc);
ALTER TABLE schedulearc OWNER TO rail;

CREATE TABLE schedule_entry (
    id          SERIAL,
    schedule    BIGINT NOT NULL REFERENCES schedule(id),
    type        NAME,
    tpl         INTEGER NOT NULL REFERENCES tiploc(id),
    pta         TIME,
    ptd         TIME,
    wta         TIME,
    wtd         TIME,
    wtp         TIME,
    act         NAME,
    PRIMARY KEY(id)
);
CREATE INDEX schedule_entry_s ON schedule_entry(schedule);
ALTER TABLE schedule_entry OWNER TO rail;
ALTER TABLE schedule_entry_id_seq OWNER TO rail;

CREATE TABLE schedule_entryarc (
    id          BIGINT NOT NULL,
    schedule    BIGINT NOT NULL REFERENCES schedulearc(id),
    type        NAME,
    tpl         INTEGER NOT NULL REFERENCES tiploc(id),
    pta         TIME,
    ptd         TIME,
    wta         TIME,
    wtd         TIME,
    wtp         TIME,
    act         NAME,
    PRIMARY KEY(id)
);
CREATE INDEX schedule_entryarc_s ON schedule_entryarc(schedule);
ALTER TABLE schedule_entryarc OWNER TO rail;

CREATE TABLE schedule_assoc (
    mainid      BIGINT NOT NULL REFERENCES schedule(id),
    associd     BIGINT NOT NULL REFERENCES schedule(id),
    tpl         INTEGER NOT NULL REFERENCES tiploc(id),
    cat         CHAR(2),
    cancelled   BOOLEAN DEFAULT false,
    deleted     BOOLEAN DEFAULT false,
    -- main arrival
    pta         TIME,
    wta         TIME,
    -- assoc depart
    ptd         TIME,
    wtd         TIME,
    PRIMARY KEY (mainid,associd)
);
CREATE INDEX schedule_assoc_t ON schedule_assoc(tpl);
CREATE INDEX schedule_assoc_m ON schedule_assoc(mainid);
CREATE INDEX schedule_assoc_a ON schedule_assoc(associd);
ALTER TABLE schedule_assoc OWNER TO rail;

CREATE TABLE schedule_assocarc (
    mainid      BIGINT NOT NULL REFERENCES schedulearc(id),
    associd     BIGINT NOT NULL REFERENCES schedulearc(id),
    tpl         INTEGER NOT NULL REFERENCES tiploc(id),
    cat         CHAR(2),
    cancelled   BOOLEAN DEFAULT false,
    deleted     BOOLEAN DEFAULT false,
    -- main arrival
    pta         TIME,
    wta         TIME,
    -- assoc depart
    ptd         TIME,
    wtd         TIME,
    PRIMARY KEY (mainid,associd)
);
CREATE INDEX schedule_assocarc_t ON schedule_assocarc(tpl);
CREATE INDEX schedule_assocarc_m ON schedule_assocarc(mainid);
CREATE INDEX schedule_assocarc_a ON schedule_assocarc(associd);
ALTER TABLE schedule_assocarc OWNER TO rail;

-- ----------------------------------------------------------------------
-- Train forecast
--
-- This comprises two sets of tables, the active one forecast and
-- an archive table forecastarc. The second set of tables are used when
-- a train is deactivated.
--
-- This is so that the live updates operate on a smaller table, keeping
-- performance up.
-- ----------------------------------------------------------------------

CREATE TABLE forecast (
    id          SERIAL,
    rid         VARCHAR(16) NOT NULL,
    uid         VARCHAR(16) NOT NULL,
    ssd         DATE,
    ts          TIMESTAMP WITH TIME ZONE,
    activated   BOOLEAN DEFAULT false,
    deactivated BOOLEAN DEFAULT false,
    schedule    BIGINT REFERENCES schedule(id),
    PRIMARY KEY(id)
);
CREATE UNIQUE INDEX forecast_r ON forecast(rid);
CREATE INDEX forecast_t ON forecast(ts);
ALTER TABLE forecast OWNER TO rail;
ALTER TABLE forecast_id_seq OWNER TO rail;

CREATE TABLE forecastarc (
    id          BIGINT NOT NULL,
    rid         VARCHAR(16) NOT NULL,
    uid         VARCHAR(16) NOT NULL,
    ssd         DATE,
    ts          TIMESTAMP WITH TIME ZONE,
    activated   BOOLEAN DEFAULT false,
    deactivated BOOLEAN DEFAULT false,
    schedulearc    BIGINT REFERENCES schedulearc(id),
    PRIMARY KEY(id)
);
CREATE UNIQUE INDEX forecastarc_r ON forecastarc(rid);
CREATE INDEX forecastarc_t ON forecastarc(ts);
ALTER TABLE forecastarc OWNER TO rail;

-- ----------------------------------------------------------------------
-- Maps a forecast to the tiploc & contains the forecast details
-- needed by ldb
-- ----------------------------------------------------------------------
CREATE TABLE forecast_entry (
    fid         BIGINT NOT NULL REFERENCES forecast(id),
    tpl         INTEGER NOT NULL REFERENCES tiploc(id),
    -- Suppress this entry?
    supp        BOOLEAN DEFAULT false,
    -- From one of the figures
    arr         TIME,
    dep         TIME,
    pass        TIME,
    -- Platform
    plat        VARCHAR(4),
    platsup     BOOLEAN DEFAULT false,
    --
    PRIMARY KEY (fid,tpl)
);
CREATE UNIQUE INDEX forecast_entry_ft ON forecast_entry(fid,tpl);
CREATE INDEX forecast_entry_f ON forecast_entry(fid);
CREATE INDEX forecast_entry_t ON forecast_entry(tpl);
ALTER TABLE forecast_entry OWNER TO rail;

CREATE TABLE forecast_entryarc (
    fid         BIGINT NOT NULL REFERENCES forecastarc(id),
    tpl         INTEGER NOT NULL REFERENCES tiploc(id),
    -- Suppress this entry?
    supp        BOOLEAN DEFAULT false,
    -- From one of the figures
    arr         TIME,
    dep         TIME,
    pass        TIME,
    -- Platform
    plat        VARCHAR(4),
    platsup     BOOLEAN DEFAULT false,
    --
    PRIMARY KEY (fid,tpl)
);
CREATE UNIQUE INDEX forecast_entryarc_ft ON forecast_entryarc(fid,tpl);
CREATE INDEX forecast_entryarc_f ON forecast_entryarc(fid);
CREATE INDEX forecast_entryarc_t ON forecast_entryarc(tpl);
ALTER TABLE forecast_entryarc OWNER TO rail;

DROP TABLE log;
CREATE TABLE log (t TEXT);
ALTER TABLE log OWNER TO rail;

-- ----------------------------------------------------------------------
-- Station messages
-- ----------------------------------------------------------------------

CREATE TABLE message (
    id          BIGINT NOT NULL,
    cat         VARCHAR(16) NOT NULL,
    sev         VARCHAR(16) NOT NULL,
    suppress    BOOLEAN,
    xml         TEXT,
    ts          TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);
CREATE INDEX message_t ON message(ts);
ALTER TABLE message OWNER TO rail;

-- Link message to stations

CREATE TABLE message_station (
    msgid   BIGINT NOT NULL REFERENCES message(id),
    crsid   BIGINT NOT NULL REFERENCES crs(id)
);
CREATE UNIQUE INDEX message_station_mc ON message_station(msgid,crsid);
CREATE INDEX message_station_m ON message_station(msgid);
CREATE INDEX message_station_c ON message_station(crsid);
ALTER TABLE message_station OWNER TO rail;

-- ----------------------------------------------------------------------
-- Alarms
-- ----------------------------------------------------------------------

CREATE TABLE alarms (
    id          SERIAL NOT NULL,
    aid         NAME,
    -- When the alarm was set
    setts       TIMESTAMP WITH TIME ZONE,
    -- has it been cleared and when
    cleared     BOOLEAN DEFAULT false,
    clearedts   TIMESTAMP WITH TIME ZONE,
    -- type of alarm
    type        NAME,
    -- XML of the alarm
    xml         TEXT,
    PRIMARY KEY (id)
);
ALTER TABLE alarms OWNER TO rail;

-- ----------------------------------------------------------------------
-- Train Order
-- ----------------------------------------------------------------------
CREATE TABLE trainorder (
    id          SERIAL NOT NULL,
    tpl         INTEGER REFERENCES tiploc(id),
    crs         INTEGER REFERENCES crs(id),
    plat        NAME,
    ts          TIMESTAMP WITH TIME ZONE,
    -- first
    rid1        VARCHAR(16),
    tid1        CHAR(4),
    pta1        TIME,
    wta1        TIME,
    ptd1        TIME,
    wtd1        TIME,
    -- second
    rid2        VARCHAR(16),
    tid2        CHAR(4),
    pta2        TIME,
    wta2        TIME,
    ptd2        TIME,
    wtd2        TIME,
    -- third
    rid3        VARCHAR(16),
    tid3        CHAR(4),
    pta3        TIME,
    wta3        TIME,
    ptd3        TIME,
    wtd3        TIME,
    PRIMARY KEY(id)
);
CREATE INDEX trainorder_t ON trainorder(tpl);
CREATE INDEX trainorder_c ON trainorder(crs);
CREATE INDEX trainorder_r1 ON trainorder(rid1);
CREATE INDEX trainorder_t1 ON trainorder(tid1);
CREATE INDEX trainorder_r2 ON trainorder(rid2);
CREATE INDEX trainorder_t2 ON trainorder(tid2);
CREATE INDEX trainorder_r3 ON trainorder(rid3);
CREATE INDEX trainorder_t3 ON trainorder(tid3);
ALTER TABLE trainorder OWNER TO rail;

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
    -- record used in db updates
    rec         RECORD;
    -- XML from xpath & used in loops
    arxml       XML[];
    axml        XML;
    axml2       XML;
    --
    tid         TEXT;
    -- ID's
    id1         BIGINT;
    id2         BIGINT;
    id3         BIGINT;
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
                (xpath('//sched:OPDT/@tpl',axml,ns))[1]::TEXT AS wdt
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
        
        -- Create/update the schedule table
        LOOP
            SELECT id INTO rec FROM darwin.schedule WHERE rid=arec.rid;
            IF FOUND THEN
                UPDATE darwin.schedule SET ts = ats WHERE rid=arec.rid;
                id1 = rec.id;
                EXIT;
            ELSE
                BEGIN
                    INSERT INTO darwin.schedule (rid,uid,ssd,trainid,toc,ts,origin,dest)
                        VALUES (arec.rid,arec.uid,arec.ssd,arec.trainid,arec.toc,ats,id2,id3);
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
            SELECT  (xpath('name(/*)',axml2,ns))[1]::TEXT AS type,
                    (xpath('//@tpl',axml2,ns))[1]::TEXT AS tpl,
                    (xpath('//@act',axml2,ns))[1]::TEXT AS act,
                    (xpath('//@pta',axml2,ns))[1]::TEXT::TIME AS pta,
                    (xpath('//@ptd',axml2,ns))[1]::TEXT::TIME AS ptd,
                    (xpath('//@wta',axml2,ns))[1]::TEXT::TIME AS wta,
                    (xpath('//@wtd',axml2,ns))[1]::TEXT::TIME AS wtd,
                    (xpath('//@wtp',axml2,ns))[1]::TEXT::TIME AS wtp
                INTO arec LIMIT 1;
            INSERT INTO darwin.schedule_entry (schedule,type,tpl,pta,ptd,wta,wtd,wtp,act)
                VALUES (id1,arec.type,darwin.tiploc(arec.tpl),arec.pta,arec.ptd,arec.wta,arec.wtd,arec.wtp,arec.act);
            
        END LOOP;

    END LOOP;
    -- End of schedules
--            INSERT INTO darwin.log VALUES (arec.type);

    -- ---------------------------------------------------------------------------
    -- Forecasts
    FOREACH axml IN ARRAY xpath('//pport:TS',pxml,ns)
    LOOP
        -- The outer TS element
        SELECT  (xpath('//pport:TS/@rid',axml,ns))[1]::TEXT AS rid,
                (xpath('//pport:TS/@uid',axml,ns))[1]::TEXT AS uid,
                (xpath('//pport:TS/@ssd',axml,ns))[1]::TEXT::DATE AS ssd
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

        -- Now the forecast table entries
        -- FIXME this won't handle instances of a tiploc being visited twice right now

        -- Update the forecast_entry table
        FOREACH axml2 IN ARRAY xpath('//fcst:Location',axml,ns)
        LOOP
            -- extract data from each location element
            SELECT  (xpath('//fcst:Location/@tpl',axml,ns))[1]::text AS tpl,
                    (xpath('//fcst:plat/text()',axml,ns))[1]::text AS plat,
                    (xpath('//fcst:plat/@platsup',axml,ns))[1]::text::boolean AS platsup
                INTO arec LIMIT 1;

            id2 = darwin.tiploc(arec.tpl);

            -- resolve/create the tiploc
            LOOP
                SELECT * INTO rec FROM darwin.forecast_entry WHERE fid=id1 AND tpl=id2;
                IF FOUND THEN
                    UPDATE darwin.forecast_entry
                        SET plat=arec.plat,
                            platsup=arec.platsup
                        WHERE fid=id1 AND tpl=id2;
                    EXIT;
                ELSE
                    BEGIN
                        INSERT INTO darwin.forecast_entry
                            (fid,tpl,plat,platsup)
                            VALUES (id1,id2,
                                arec.plat,
                                arec.platsup
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
    -- Deactivations - here we don't worry if we dont have an entry
    FOREACH axml IN ARRAY xpath('//pport:deactivated/@rid',pxml,ns)
    LOOP
        tid=axml::text;

        -- Mark the forecast as deactivated
        UPDATE darwin.forecast SET deactivated=true, ts=ats WHERE rid=tid;
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

    END LOOP;

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

        INSERT INTO darwin.alarm (aid,settd,type,xml) VALUES (arec.id,arec.type,ats,array_to_string(axml,''));
    END LOOP;

    FOREACH axml IN ARRAY xpath('//pport:alarm/alarm:clear/text()',pxml,ns)
    LOOP
        UPDATE darwin.alarm SET cleared=true, clearedts=ats WHERE aid=axml::TEXT;
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
        UPDATE darwin.alarm SET cleared=true, clearedts=ats WHERE aid=axml::TEXT;
    END LOOP;


END;
$$ LANGUAGE plpgsql;
