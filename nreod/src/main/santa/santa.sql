-- ----------------------------------------------------------------------------------------------------
-- Supporting SQL for santa-darwin.sh
-- ----------------------------------------------------------------------------------------------------

SET search_path = darwin;

-- Ensure we have a dummy TOC
INSERT INTO darwin.toc (code,name,url) VALUES ('ZS','Santa Rail','http://www.noradsanta.org/');

-- Ensure we have the station
SELECT darwin.tiploc('NPLEINT');
SELECT darwin.crs('XNP');

-- Dont worry if one of these two fail, thats fine

-- Update location, usually it appears when NR adds it in their lists
UPDATE darwin.location SET name='North Pole International' WHERE crs=darwin.crs('XNP');

-- In the case where NR hasn't added it, add it ourselves
INSERT INTO darwin.location (tpl,crs,toc,name) VALUES (darwin.tiploc('NPLEINT'),darwin.crs('XNP'),'','North Pole International');
