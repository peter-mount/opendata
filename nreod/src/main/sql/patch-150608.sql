-- =======================================================
-- Patch to fix wrong column name in forecast archives
-- caused by a typo in the original darwin.sql script.
--
-- Peter Mount 2015 June 8
-- =======================================================

SET search_path = darwin;

BEGIN;

ALTER TABLE forecastarc ADD COLUMN schedule BIGINT;
ALTER TABLE forecastarc DROP COLUMN schedulearc;

COMMIT;

