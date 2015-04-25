-- ----------------------------------------------------------------------
-- Darwin Analyser
--
-- This set of SQL parses the raw Pport xml directly into the database
--
-- ----------------------------------------------------------------------

--CREATE SCHEMA darwin;
SET search_path = darwin;

DROP TABLE forecast_entry;
DROP TABLE forecast;

DROP TABLE schedule_entry;
DROP TABLE schedule;

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
-- Schedule
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

-- ----------------------------------------------------------------------
-- Train forecast
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

DROP TABLE log;
CREATE TABLE log (t TEXT);
ALTER TABLE log OWNER TO rail;

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
        UPDATE darwin.forecast SET deactivated=true, ts=ats WHERE rid=axml::text;
    END LOOP;

END;
$$ LANGUAGE plpgsql;
