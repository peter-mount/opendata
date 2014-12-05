-- ======================================================================
-- CORPUS (Codes for Operations, Retail & Planning – a Unified Solution)
-- ======================================================================

CREATE SCHEMA reference;
SET search_path = reference;

DROP TABLE corpus;

-- Note TALPHA is really 3ALPHA but thats not a valid column name.
-- 3ALPHA was formally known as CRS (Computer Reservation System) or
-- 
-- Also NLCDESC16 is supposed to be 16 characters but there are entries
-- over this limit, hence it's a varchar(32)

CREATE TABLE corpus (
    id          SERIAL NOT NULL,
    stanox      INTEGER,
    UIC         INTEGER,
    TALPHA      CHAR(3),
    TIPLOC      VARCHAR(10),
    NLC         INTEGER,
    NLCDESC     VARCHAR(64),
    NLCDESC16   VARCHAR(32)
);

CREATE INDEX corpus_s ON corpus(stanox);
CREATE INDEX corpus_u ON corpus(uic);
CREATE INDEX corpus_3 ON corpus(talpha);
CREATE INDEX corpus_t ON corpus(tiploc);
CREATE INDEX corpus_n ON corpus(nlc);

ALTER TABLE corpus OWNER TO rail;
