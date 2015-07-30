-- ------------------------------------------------------------
-- Create & configure the schema
-- ------------------------------------------------------------

--DROP SCHEMA tpnm;
--CREATE SCHEMA tpnm;
--GRANT ALL ON tpnm TO rail;

SET search_path = tpnm;

-- ------------------------------------------------------------
-- Setup postgis
-- ------------------------------------------------------------

-- Ignore if this says it already exists
CREATE EXTENSION postgis;
