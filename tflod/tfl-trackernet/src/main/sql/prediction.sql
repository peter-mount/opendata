-- ----------------------------------------------------------------------
-- SQL to parse the trackernet SQL
-- ----------------------------------------------------------------------

--CREATE SCHEMA tfl;
SET search_path = tfl;

CREATE OR REPLACE FUNCTION tfl.prediction (plinecode TEXT, pxml XML )
RETURNS VOID AS $$
DECLARE
    anow        TIMESTAMP WITHOUT TIME ZONE = now();
    stxml       XML;
    srec        RECORD;
    acrsid      INTEGER;

    plxml       XML;
    prec        RECORD;
    aplatid     INTEGER;
    adue        INTEGER;

    txml        XML;
    trec        RECORD;
BEGIN
    anow = (xpath('//Time/@TimeStamp',pxml))[1]::TEXT::TIMESTAMP WITHOUT TIME ZONE;

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

            -- Wipe the display boards for this platform
            DELETE FROM tfl.boards WHERE crsid=acrsid AND platid=aplatid;
            
            -- Process each train
            FOREACH txml IN ARRAY xpath('//T',plxml)
            LOOP
                SELECT  (xpath('//T/@S',txml))[1]::TEXT as trainset,
                        (xpath('//T/@T',txml))[1]::TEXT::INTEGER as tripno,
                        (xpath('//T/@C',txml))[1]::TEXT as due,
                        (xpath('//T/@L',txml))[1]::TEXT as location,
                        (xpath('//T/@D',txml))[1]::TEXT::INTEGER as destcode,
                        (xpath('//T/@DE',txml))[1]::TEXT as destination
                    INTO trec LIMIT 1;

                -- When the train is due, from mm:ss to seconds. Not time as mm can be >59 here
                adue = -99;
                IF trec.due = 'due' THEN
                    -- '-' indicates that the train is on the platform
                    adue = -1;
                ELSIF trec.due = '-' THEN
                    -- '-' indicates that the train is on the platform
                    adue = -2;
                ELSE
                    BEGIN
                        adue = (substring(trec.due FROM '^[0-9]+')::INTEGER*60)+substring(trec.due FROM '[0-9]+$')::INTEGER;
                    EXCEPTION WHEN invalid_text_representation THEN
                        -- Invalid so set to unknown
                        adue = -99;
                    END;
                END IF;
                -- If adue is still null then use -98 can happen if due holds unknown txt
                IF adue IS NULL THEN
                    adue = -98;
                END IF;

                -- Add the train to the boards
                INSERT INTO tfl.boards (
                        crsid, platid, tm,
                        trainset,tripno,
                        due,
                        location,
                        dest, destination
                    ) VALUES (
                        acrsid, aplatid, anow,
                        trec.trainset, trec.tripno,
                        adue,
                        trec.location,
                        trec.destcode, trec.destination
                    );

            END LOOP;

        END LOOP;
        -- End platform loop

    END LOOP;
    -- End station loop

END;
$$ LANGUAGE plpgsql;

