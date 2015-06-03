-- =================================================================
-- patch to add delayed attribution to forecast entries
--
-- 2015 June 3
-- =================================================================

ALTER TABLE forecast_entryarc
    ADD COLUMN etarrdel boolean;

ALTER TABLE forecast_entryarc
    ADD COLUMN etdepdel boolean;

ALTER TABLE forecast_entryarc
    ADD COLUMN etpassdel boolean;

ALTER TABLE forecast_entryarc
    ADD COLUMN ldbdel boolean;

ALTER TABLE forecast_entry
    ADD COLUMN etarrdel boolean;

ALTER TABLE forecast_entry
    ADD COLUMN etdepdel boolean;

ALTER TABLE forecast_entry
    ADD COLUMN etpassdel boolean;

ALTER TABLE forecast_entry
    ADD COLUMN ldbdel boolean;

