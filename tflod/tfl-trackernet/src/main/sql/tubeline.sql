-- ----------------------------------------------------------------------
-- TrackerNet
--
-- ----------------------------------------------------------------------

--CREATE SCHEMA tfl;
SET search_path = tfl;

DROP TABLE line;

-- ----------------------------------------------------------------------
-- London Underground Line Groups
-- ----------------------------------------------------------------------

CREATE TABLE line (
    id      SERIAL NOT NULL,
    code    CHAR(1) NOT NULL,
    line    NAME NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX line_c ON line(code);
CREATE INDEX line_l ON line(line);

INSERT INTO line (code,line) VALUES ('B','Bakerloo');
INSERT INTO line (code,line) VALUES ('C','Central');
INSERT INTO line (code,line) VALUES ('D','District');
INSERT INTO line (code,line) VALUES ('H','Hammersmith & Circle');
INSERT INTO line (code,line) VALUES ('J','Jubilee');
INSERT INTO line (code,line) VALUES ('M','Metropolitan');
INSERT INTO line (code,line) VALUES ('N','Northern');
INSERT INTO line (code,line) VALUES ('P','Piccadilly');
INSERT INTO line (code,line) VALUES ('V','Victoria');
INSERT INTO line (code,line) VALUES ('W','Waterloo & City');

CREATE OR REPLACE FUNCTION tfl.line (pline TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
BEGIN
    IF pline IS NULL OR pline = '' THEN
        RETURN NULL;
    ELSE
        LOOP
            SELECT * INTO rec FROM tfl.line WHERE code=pline;
            IF FOUND THEN
                RETURN rec.id;
            END IF;
            BEGIN
                INSERT INTO tfl.line (code,line) VALUES (pline,concat('Unknown line ',pline::TEXT));
                RETURN currval('tfl.line_id_seq');
            EXCEPTION WHEN unique_violation THEN
                -- Do nothing, loop & try again
            END;
        END LOOP;
    END IF;
END;
$$ LANGUAGE plpgsql;
