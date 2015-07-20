-- ----------------------------------------------------------------------
-- Tables to allow us to link mainline & tfl stations together.
--
-- For mainline, this will allow us to link from a mainline station
-- to a TfL station.
--
-- For example: VIC to Victoria tube.
--
-- Note: This is manually created as there's no (as of 2015/7/20) identifyiable
-- way of doing this progmatically.
--
-- Also some are additional, for example CHX -> Charing Cross but I also
-- link CHX to Embankment as it's also next door to that station.
--
-- ----------------------------------------------------------------------

--CREATE SCHEMA reference;
SET search_path = tfl;

DROP TABLE station_crs;

-- ----------------------------------------------------------------------
-- Link to Mainline Station
--
-- Note: It's valid for n-n relationship here
-- e.g. Kings X & St Pancras has 2 mainline stations
--      but CHX has 2 underground (Charing X & Embankment)
-- ----------------------------------------------------------------------
CREATE TABLE station_crs (
    id      SERIAL NOT NULL,
    stationid   INTEGER NOT NULL REFERENCES tfl.station(id),
    crs     CHAR(3) NOT NULL,
    PRIMARY KEY (id)
);
CREATE INDEX station_crs_s ON station_crs(stationid);
CREATE INDEX station_crs_c ON station_crs(crs);
GRANT ALL ON station_crs TO rail;

CREATE OR REPLACE FUNCTION tfl.linkcrs( pstation INTEGER, pcrs TEXT )
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
BEGIN
    LOOP
        SELECT * INTO rec FROM tfl.station_crs WHERE stationid=pstation AND crs=pcrs;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO tfl.station_crs (stationid,crs) VALUES (pstation,pcrs);
            RETURN currval('tfl.station_crs_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------
-- Link known stations
-- Here pname is the TfL name, pcrs is the Darwin CRS code
CREATE OR REPLACE FUNCTION tfl.populate( pname TEXT, pcrs TEXT )
RETURNS void AS $$
DECLARE
    rec         RECORD;
    astationid  INTEGER;
BEGIN
    FOR rec IN SELECT * FROM tfl.station WHERE name = pname
    LOOP
        PERFORM tfl.linkcrs(rec.id,pcrs);
    END LOOP;
END;
$$ LANGUAGE plpgsql;

select tfl.populate('Barbican','ZBB');                      -- darwin has this as Barbican Underground
select tfl.populate('Balham','BAL');
select tfl.populate('Brixton','BRX');
select tfl.populate('Blackfriars','BFR');

select tfl.populate('Cannon Street','CST');

select tfl.populate('Elephant & Castle','EPH');             -- Mainline
select tfl.populate('Elephant & Castle','ZEL');             -- Underground
select tfl.populate('Embankment','CHX');
select tfl.populate('Euston','EUS');

select tfl.populate('Farringdon','ZFD');                    -- darwin has this as Farringdon Underground

-- Kings Cross St Pancras is 1 underground with 2 mainline
select tfl.populate('King''s Cross St. Pancras','KGX');
select tfl.populate('King''s Cross St. Pancras','STP');

select tfl.populate('Charing Cross','CHX');
select tfl.populate('Clapham Junction','CLJ');

select tfl.populate('Kensington High Streen','ZHS');        -- darwin has this but Circle/District only
select tfl.populate('Kensington Olympia','KPA');            -- Overground & Limited District

select tfl.populate('Limehouse','LHS');
select tfl.populate('Liverpool Street','LST');
select tfl.populate('London Bridge','LBG');

select tfl.populate('Marylebone','MYB');
select tfl.populate('Moorgate','MOG');                      -- darwin mainline
select tfl.populate('Moorgate','ZMG');                      -- darwin underground

select tfl.populate('Paddington','PAD');

select tfl.populate('Shadwell','SDE');
select tfl.populate('Southwark','WAE');                     -- Waterloo East
select tfl.populate('Stratford','SRA');                     -- Stratford (London)
select tfl.populate('Stratford International','SFA');       -- Stratford International

select tfl.populate('Tower Hill','FST');                    -- Fenchurch St

select tfl.populate('Vauxhall','VXH');
select tfl.populate('Victoria','VIC');

select tfl.populate('Waterloo','WAT');
select tfl.populate('West Brompton','WBP');
select tfl.populate('West Ham','WEH');
select tfl.populate('West Hampstead','WHD');                -- WHP is thameslink same one?
select tfl.populate('Whitechapel','ZWL');
select tfl.populate('Wimbledon','WIM');

DROP FUNCTION tfl.populate(TEXT,TEXT);

-- ----------------------------------------------------------------------
