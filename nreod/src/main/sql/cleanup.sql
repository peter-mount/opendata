-- ----------------------------------------------------------------------
-- Daily cleanup job
--
-- ----------------------------------------------------------------------

SET search_path = darwin;

CREATE OR REPLACE FUNCTION darwin_cleanup()
RETURNS VOID AS $$
DECLARE
    fromDate    DATE;
    rec         RECORD;
BEGIN
    fromDate = now()::DATE;

    -- Remove old messages
    DELETE FROM darwin.message_station WHERE msgid IN (
        SELECT id FROM darwin.message WHERE ts < fromDate
    );
    DELETE FROM darwin.message WHERE ts < fromDate;

    -- Archive old schedules that didn't get deactivated
    FOR rec IN
        SELECT rid FROM darwin.schedule s WHERE s.ssd < fromDate
    UNION
        SELECT rid FROM darwin.forecast f WHERE f.ssd < fromDate
    ORDER BY rid
    LOOP
        PERFORM darwin.darwinarchive(rec.rid);
    END LOOP;

END;
$$ LANGUAGE plpgsql;
