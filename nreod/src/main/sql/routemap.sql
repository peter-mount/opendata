-- =================================================================
-- Route Map - a dynamic map of tiploc's linked by lines
--
-- We use this to determine if a train has gone off route when
-- we show it on the train detail pages.
--
-- Note This is nothing to do with darwin, it can be used for any
-- graphical data based on tiploc's.
-- =================================================================

CREATE SCHEMA routemap;
ALTER SCHEMA routemap OWNER TO rail;
SET search_path = routemap;

DROP TABLE segment;
DROP TABLE line;
DROP TABLE tiploc;

-- ----------------------------------------------------------------------
-- Normalisation table of tiplocs
-- ----------------------------------------------------------------------
CREATE TABLE tiploc (
    id          SERIAL,
    tpl         varCHAR(16),
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX tiploc_c ON tiploc(tpl);
ALTER TABLE tiploc OWNER TO rail;
ALTER TABLE tiploc_id_seq OWNER TO rail;

CREATE OR REPLACE FUNCTION routemap.tiploc (ptpl TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
BEGIN
    IF ptpl IS NULL OR ptpl = '' THEN
        RETURN null;
    ELSE
        LOOP
            SELECT * INTO rec FROM routemap.tiploc WHERE tpl=ptpl;
            IF FOUND THEN
                RETURN rec.id;
            END IF;
            BEGIN
                INSERT INTO routemap.tiploc (tpl) VALUES (ptpl);
                RETURN currval('routemap.tiploc_id_seq');
            EXCEPTION WHEN unique_violation THEN
                -- Do nothing, loop & try again
            END;
        END LOOP;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------
-- A line on the network
CREATE TABLE line (
    id      SERIAL NOT NULL,
    -- Start tiploc
    stpl    INTEGER NOT NULL REFERENCES tiploc(id),
    -- End tiploc
    etpl    INTEGER NOT NULL REFERENCES tiploc(id),
    -- Size of line
    size    INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

ALTER TABLE line OWNER TO rail;
ALTER SEQUENCE line_id_seq OWNER TO rail;

CREATE INDEX line_se ON line (stpl,etpl);
CREATE INDEX line_s ON line (stpl);
CREATE INDEX line_e ON line (etpl);

-- ----------------------------------------------------------------------
-- A line segment, a unique link between two tiplocs
CREATE TABLE segment (
    id      SERIAL NOT NULL,
    -- Start tiploc
    stpl    INTEGER NOT NULL REFERENCES tiploc(id),
    -- End tiploc
    etpl    INTEGER NOT NULL REFERENCES tiploc(id),
    -- Next segment in a continuous line. Null means end of line
    nid     INTEGER REFERENCES segment(id),
    -- Line this segment belongs to
    lid     INTEGER REFERENCES line(id),
    seq     INTEGER NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

ALTER TABLE segment OWNER TO rail;
ALTER SEQUENCE segment_id_seq OWNER TO rail;

CREATE UNIQUE INDEX segment_se ON segment(stpl,etpl);
CREATE INDEX segment_s ON segment(stpl);
CREATE INDEX segment_e ON segment(etpl);

-- Return segment id linking two tiplocs
CREATE OR REPLACE FUNCTION routemap.segment(pstpl TEXT, petpl TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
    rstpl    BIGINT;
    retpl    BIGINT;
BEGIN
    rstpl = routemap.tiploc(pstpl);
    retpl = routemap.tiploc(petpl);
    
    LOOP
        SELECT * INTO rec FROM routemap.segment WHERE stpl=rstpl AND etpl=retpl;
        IF FOUND THEN
            RETURN rec.id;
        END IF;
        BEGIN
            INSERT INTO routemap.segment (stpl,etpl) VALUES (rstpl,retpl);
            RETURN currval('routemap.segment_id_seq');
        EXCEPTION WHEN unique_violation THEN
            -- Do nothing, loop & try again
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Link two tiplocs returning the lineid the segment belongs to
CREATE OR REPLACE FUNCTION routemap.linktiploc(pstpl TEXT, petpl TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
    seg     RECORD;
    rsid    BIGINT;
    rstpl   BIGINT;
    retpl   BIGINT;
    rlid    BIGINT;
BEGIN
    rstpl = routemap.tiploc(pstpl);
    retpl = routemap.tiploc(petpl);
    rsid = routemap.segment(pstpl,petpl);
    
    LOOP
        -- Get the segment
        SELECT * INTO rec from routemap.segment where id=rsid;
        IF NOT FOUND THEN
            -- Should never happen
            RETURN NULL;
        END IF;

        IF rec.lid IS NOT NULL THEN
            -- Not a new segment so return the existing line id
            RETURN rec.lid;
        ELSE
            -- A new segment so see how we need to handle it

            -- Look for a segment that ends with this line, If not found then it's a totally new line
            SELECT * INTO rec
                FROM routemap.segment
                WHERE etpl=rstpl
                    -- Safety, don't allow us to link backwards
                    AND stpl != retpl
                -- Ensure we only get the oldest line one and limit to 1 result
                ORDER BY lid
                LIMIT 1;
            IF FOUND THEN
                -- One exists so do we append to it or split it?

                IF rec.nid IS NULL THEN
                    -- Segment is at the end of a line so just append to it
                    UPDATE routemap.segment
                        SET nid=rsid
                        WHERE id=rec.id;

                    UPDATE routemap.segment
                        SET lid=rec.lid, seq=rec.seq+1
                        WHERE id=rsid;

                    -- update line stats to account for the new segment
                    UPDATE routemap.line
                        SET etpl=retpl,
                            size=size+1
                        WHERE id=rec.lid;

                    RETURN rec.lid;
                ELSE
                    -- We need to split the line
                    SELECT * INTO seg FROM routemap.line WHERE id=rec.lid;
                    BEGIN
                        -- Create a new line at the split
                        INSERT INTO routemap.line (stpl,etpl,size)
                            VALUES (rec.etpl, seg.etpl, seg.size-rec.seq-1);
                        rlid = currval('routemap.line_id_seq');
                        
                        -- update original line stats to new values
                        UPDATE routemap.line
                            SET size=rec.seq+1,
                                etpl=rec.etpl
                            WHERE id=rec.lid;

                        -- Move segments to the new line
                        UPDATE routemap.segment
                            SET lid=rlid,
                                seq = seq - rec.seq -1
                            WHERE lid=rec.lid AND seq > rec.seq;

                        -- terminate the old line
                        UPDATE routemap.segment
                            SET nid = NULL
                            WHERE id = rec.id;

                        -- Now create the new line for the segment
                        INSERT INTO routemap.line (stpl,etpl,size)
                            VALUES (rstpl,retpl,1);
                        rlid = currval('routemap.line_id_seq');
                        UPDATE routemap.segment
                            SET lid=rlid
                            WHERE id=rsid;

                        RETURN rlid;
                    EXCEPTION WHEN unique_violation THEN
                        -- Do nothing, loop & try again
                    END;
                END IF;
            ELSE
                -- None so a totally new line
                BEGIN
                    INSERT INTO routemap.line (stpl,etpl,size) VALUES (rstpl,retpl,1);
                    rlid = currval('routemap.line_id_seq');
                    UPDATE routemap.segment SET lid=rlid WHERE id=rsid;
                    RETURN rlid;
                EXCEPTION WHEN unique_violation THEN
                    -- Do nothing, loop & try again
                END;
            END IF;
        END IF;
    END LOOP;
    
END;
$$ LANGUAGE plpgsql;

-- Clear the entire map
CREATE OR REPLACE FUNCTION routemap.resetmap()
RETURNS VOID AS $$
BEGIN
    DELETE FROM routemap.segment;
    DELETE FROM routemap.line;
    DELETE FROM routemap.tiploc;
    ALTER SEQUENCE routemap.line_id_seq RESTART WITH 1;
    ALTER SEQUENCE routemap.segment_id_seq RESTART WITH 1;
    ALTER SEQUENCE routemap.tiploc_id_seq RESTART WITH 1;
END;
$$ LANGUAGE plpgsql;

-- Given a list of tiplocs in visiting sequence, return a result set of each tiploc with the associated lineid
-- for that location.
--
-- Special case: The first tiploc will always match the lineid of the second
CREATE OR REPLACE FUNCTION routemap.route( proute TEXT[] )
RETURNS TABLE( tpl TEXT, lid INTEGER) AS $$
DECLARE
    rec     RECORD;
    tpl     TEXT;
    ltpl    TEXT = NULL;
    lid     INTEGER;
    fst     BOOLEAN = true;
BEGIN
    -- Pass 1 ensures the line is linked
    FOREACH tpl IN ARRAY proute
    LOOP
        IF ltpl IS NOT NULL THEN
            lid = routemap.linktiploc(ltpl,tpl);
        END IF;
        ltpl = tpl;
    END LOOP;

    -- Pass 2 generate the result
    -- Note: This may still cause a line id to change part way but it will catch most
    --       cases of where this happens
    ltpl = NULL;
    FOREACH tpl IN ARRAY proute
    LOOP
        IF ltpl IS NOT NULL THEN
            lid = routemap.linktiploc(ltpl,tpl);

            -- Assume first tpl is on same line as second
            IF fst THEN
                RETURN QUERY SELECT ltpl, lid;
                fst = false;
            END IF;

            RETURN QUERY SELECT tpl, lid;
        END IF;
        ltpl = tpl;
    END LOOP;
    RETURN;
END;
$$ LANGUAGE plpgsql;

-- select * from tiploc;
-- 
-- select s.*, t1.tpl, t2.tpl
--     from segment s
--     inner join tiploc t1 on s.stpl=t1.id
--     inner join tiploc t2 on s.etpl=t2.id
--     order by s.lid, s.seq;
-- 
-- select s.*, t1.tpl, t2.tpl
--     from line s
--     inner join tiploc t1 on s.stpl=t1.id
--     inner join tiploc t2 on s.etpl=t2.id
--     order by s.id;
