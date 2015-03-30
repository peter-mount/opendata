-- SQL that references a station to a signal map

CREATE SCHEMA reference;
SET search_path = reference;

DROP TABLE stationsig;

CREATE TABLE stationsig (
       id    	SERIAL NOT NULL,
       crs   	CHAR(3),
       area	CHAR(2),
       segment	INTEGER NOT NULL,
       PRIMARY KEY (crs)
);

CREATE INDEX stationsig_i ON stationsig(id);
CREATE INDEX stationsig_a ON stationsig(area);
CREATE INDEX stationsig_s ON stationsig(segment);
CREATE INDEX stationsig_as ON stationsig(area,segment);

INSERT INTO stationsig(crs,area,segment)
       VALUES ('','A2',2);
