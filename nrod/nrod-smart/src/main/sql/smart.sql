
-- ======================================================================
-- SMART database describing train berts
-- ======================================================================

CREATE SCHEMA reference;
SET search_path = reference;

DROP TABLE smart;
DROP TABLE smart_area;

-- The Train Describer Areas

CREATE TABLE smart_area (
    id      SERIAL NOT NULL,
    area    char(2) NOT NULL,
    comment text,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX smart_area_a ON smart_area(area);
ALTER TABLE smart_area OWNER TO rail;

CREATE OR REPLACE FUNCTION reference.smart_area( char(2) )
RETURNS BIGINT AS $$
DECLARE
    rec RECORD;
BEGIN
    SELECT * INTO rec FROM reference.smart_area WHERE area = $1;
    IF NOT FOUND THEN
        INSERT INTO reference.smart_area (area) VALUES ($1);
        RETURN currval('reference.smart_area_id_seq');
    END IF;
    RETURN rec.id;
END;
$$ LANGUAGE plpgsql;

-- Train Berth names

CREATE TABLE smart_berth (
    id      SERIAL NOT NULL,
    berth   CHAR(4),
    PRIMARY KEY(id)
);
CREATE UNIQUE INDEX smart_berth_a ON smart_berth(berth);
ALTER TABLE smart_berth OWNER TO rail;

CREATE OR REPLACE FUNCTION reference.smart_berth( char(4) )
RETURNS BIGINT AS $$
DECLARE
    rec RECORD;
BEGIN
    SELECT * INTO rec FROM reference.smart_berth WHERE berth = $1;
    IF NOT FOUND THEN
        INSERT INTO reference.smart_berth (berth) VALUES ($1);
        RETURN currval('reference.smart_berth_id_seq');
    END IF;
    RETURN rec.id;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE smart_line (
    id      SERIAL NOT NULL,
    line   text,
    PRIMARY KEY(id)
);
CREATE UNIQUE INDEX smart_line_a ON smart_line(line);
ALTER TABLE smart_line OWNER TO rail;

CREATE OR REPLACE FUNCTION reference.smart_line( char(4) )
RETURNS BIGINT AS $$
DECLARE
    rec RECORD;
BEGIN
    SELECT * INTO rec FROM reference.smart_line WHERE line = $1;
    IF NOT FOUND THEN
        INSERT INTO reference.smart_line (line) VALUES ($1);
        RETURN currval('reference.smart_line_id_seq');
    END IF;
    RETURN rec.id;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE smart (
    id          SERIAL NOT NULL,
    area        BIGINT NOT NULL REFERENCES reference.smart_area(id),
    fromBerth   BIGINT REFERENCES reference.smart_berth(id),
    toBerth     BIGINT REFERENCES reference.smart_berth(id),
    fromLine    BIGINT REFERENCES reference.smart_line(id),
    toLine      BIGINT REFERENCES reference.smart_line(id),
    berthOffset      INTEGER,
    platform    VARCHAR(16),
    event       INTEGER,
    route       VARCHAR(16),
    stanox      INTEGER,
    stanme      VARCHAR(16),
    steptype    INTEGER,
    comment     TEXT,
    PRIMARY KEY(id)
);

CREATE INDEX smart_fb ON smart(area,fromBerth);
CREATE INDEX smart_tb ON smart(area,toBerth);

ALTER TABLE smart OWNER TO rail;
