-- ----------------------------------------------------------------------
-- SQL to parse the inbound xml
-- ----------------------------------------------------------------------

--CREATE SCHEMA tfl;
SET search_path = tfl;

CREATE OR REPLACE FUNCTION tfl.prediction (pxml XML )
RETURNS VOID AS $$
DECLARE
    anow        TIMESTAMP WITHOUT TIME ZONE = now();
    stxml       XML;
    srec        RECORD;

    astationid  INTEGER;
    aplatid     INTEGER;
    adestid     INTEGER;
    adue        INTEGER;
BEGIN
    -- Remove all existing predictions
    DELETE FROM tfl.boards;

    -- Loop through each train
    FOREACH stxml IN ARRAY xpath('/L/O',pxml)
    LOOP
        SELECT  (xpath('//O/@naptanId',stxml))[1]::TEXT as naptan,
                (xpath('//O/@stationName',stxml))[1]::TEXT as stationname,
                (xpath('//O/@lineId',stxml))[1]::TEXT as lineid,
                (xpath('//O/@lineName',stxml))[1]::TEXT as linename,
                (xpath('//O/@platformName',stxml))[1]::TEXT as plat,
                (xpath('//O/@vehicleId',stxml))[1]::TEXT as vid,
                (xpath('//O/@direction',stxml))[1]::TEXT as dir,
                (xpath('//O/@towards',stxml))[1]::TEXT as towards,
                (xpath('//O/@operationType',stxml))[1]::TEXT as optype,
                (xpath('//O/@modeName',stxml))[1]::TEXT as mode,
                (xpath('//O/@destinationNaptanId',stxml))[1]::TEXT as destnaptan,
                (xpath('//O/@destinationName',stxml))[1]::TEXT as destname,
                (xpath('//O/@currentLocation',stxml))[1]::TEXT as curloc,
                (xpath('//O/@timeToStation',stxml))[1]::TEXT as due,
                (xpath('//O/@expectedArrival',stxml))[1]::TEXT::TIMESTAMP WITH TIME ZONE as expt
            INTO srec LIMIT 1;

        -- The station
        astationid = tfl.station(srec.lineid,srec.linename,srec.naptan,srec.stationname);
        aplatid = tfl.platform(astationid,srec.plat);

        -- The destination, null if not known
        IF srec.destnaptan IS NULL OR srec.destname IS NULL THEN
            adestid=null;
        ELSE
            adestid = tfl.station(srec.lineid,srec.linename,srec.destnaptan,srec.destname);
        END IF;

        adue = -99;
        IF srec.due = '' THEN
            adue = -1;
        ELSE
            BEGIN
                adue = srec.due::INTEGER;
            EXCEPTION WHEN invalid_text_representation THEN
                adue = -98;
            END;
        END IF;

        INSERT INTO tfl.boards (
                platid,
                vehicleid,
                dir,
                dest, curloc, towards,
                due, expt,
                optype, mode
            ) VALUES (
                aplatid,
                srec.vid,
                srec.dir,
                adestid,
                srec.curloc,
                srec.towards,
                adue, srec.expt,
                srec.optype, srec.mode
            );
    END LOOP;
    -- End train loop

END;
$$ LANGUAGE plpgsql;

