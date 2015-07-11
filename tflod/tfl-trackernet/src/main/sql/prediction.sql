-- ----------------------------------------------------------------------
-- SQL to parse the trackernet SQL
-- ----------------------------------------------------------------------

--CREATE SCHEMA tfl;
SET search_path = tfl;

CREATE OR REPLACE FUNCTION tfl.prediction (plinecode TEXT, pxml XML )
RETURNS VOID AS $$
DECLARE
    stxml       XML;
    srec        RECORD;
    acrsid      INTEGER;

    plxml       XML;
    prec        RECORD;
    aplatid     INTEGER;
BEGIN

    -- Loop through each station
    FOREACH stxml IN ARRAY xpath('//S',pxml)
    LOOP
        SELECT  (xpath('//S/@Code',stxml))[1]::TEXT as code,
                (xpath('//S/@N',stxml))[1]::TEXT as name
            INTO srec LIMIT 1;

        -- The station
        acrsid = tfl.station(plinecode,srec.code,srec.name);

        -- Now each platform
        FOREACH plxml IN ARRAY xpath('//P',stxml)
        LOOP
            SELECT  (xpath('//P/@N',plxml))[1]::TEXT as name,
                    (xpath('//P/@Code',plxml))[1]::TEXT::INTEGER as code
                INTO prec LIMIT 1;

            -- The platform
            aplatid = tfl.platform( acrsid, prec.name, prec.code );

        END LOOP;
        -- End platform loop

    END LOOP;
    -- End station loop

END;
$$ LANGUAGE plpgsql;

