-- ================================================================================
-- the gis.stations table which is loaded from a kml file.
--
-- Note: DO NOT RUN THIS SCRIP AS IS, run just the below definition,
-- then run kmlimport
-- ================================================================================

CREATE TABLE gis.stations (
    id          SERIAL,
    name        TEXT,
    latitude    REAL,
    longitude   REAL,
    tiploc      NAME,
    PRIMARY KEY(id)
);

CREATE INDEX stations_n ON gis.stations(name);
CREATE INDEX stations_ll ON gis.stations(latitude,longitude);
CREATE INDEX stations_t ON gis.stations(tiploc);
ALTER TABLE gis.stations OWNER TO rail;

-- ================================================================================
-- This but of SQL will populate the tiploc field in gis.stations to map it to
-- the timetable.tiploc table.
-- ================================================================================
BEGIN;
UPDATE gis.stations SET tiploc=(SELECT tiploc FROM timetable.tiploc WHERE TRIM(tpsdesc)=TRIM(name) AND crs IS NOT NULL LIMIT 1);
COMMIT;
-- ================================================================================
