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
                (xpath('//O/@platformName',stxml))[1]::TEXT as plat
            INTO srec LIMIT 1;

        -- The station
        astationid = tfl.station(srec.lineid,srec.linename,srec.naptan,srec.stationname);
        aplatid = tfl.platform(astationid,srec.plat);
--if astationid > 100 THEN return; END IF;
    END LOOP;
    -- End train loop

END;
$$ LANGUAGE plpgsql;

