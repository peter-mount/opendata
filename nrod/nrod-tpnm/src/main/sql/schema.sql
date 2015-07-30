-- ------------------------------------------------------------
-- Create & configure the schema
-- ------------------------------------------------------------

--DROP SCHEMA tpnm;
--CREATE SCHEMA tpnm;
--GRANT ALL ON tpnm TO rail;

-- ------------------------------------------------------------
-- Setup postgis
-- ------------------------------------------------------------

-- Ignore if this says it already exists

-- Also this needs to run twice
CREATE EXTENSION postgis;
SET search_path = tpnm;
CREATE EXTENSION postgis;
