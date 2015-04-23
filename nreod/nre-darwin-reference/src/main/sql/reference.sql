-- ----------------------------------------------------------------------
-- Darwin Reference Data
-- ----------------------------------------------------------------------

--CREATE SCHEMA darwin;
SET search_path = darwin;

-- ----------------------------------------------------------------------
-- Darwin Location table
-- ----------------------------------------------------------------------

DROP TABLE location;

CREATE TABLE location (
    id      SERIAL NOT NULL,
    tpl     VARCHAR(16) NOT NULL,
    crs     CHAR(3),
    toc     TEXT,
    name    NAME NOT NULL,
    PRIMARY KEY (tpl)
);

CREATE INDEX location_i ON location(id);
CREATE INDEX location_c ON location(crs);
CREATE INDEX location_n ON location(name);

ALTER TABLE location OWNER TO rail;
ALTER SEQUENCE location_id_seq OWNER TO rail;

-- ----------------------------------------------------------------------
-- Darwin Late Running Reasons
-- ----------------------------------------------------------------------

DROP TABLE latereason;

CREATE TABLE latereason (
    id      INTEGER NOT NULL,
    name    TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX latereason_i ON latereason(id);

ALTER TABLE latereason OWNER TO rail;

-- ----------------------------------------------------------------------
-- Darwin Late Running Reasons
-- ----------------------------------------------------------------------

DROP TABLE cancreason;

CREATE TABLE cancreason (
    id      INTEGER NOT NULL,
    name    TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX cancreason_i ON cancreason(id);

ALTER TABLE cancreason OWNER TO rail;

-- ----------------------------------------------------------------------
-- Darwin CIS source code
-- ----------------------------------------------------------------------

DROP TABLE cissource;

CREATE TABLE cissource (
    id      SERIAL NOT NULL,
    code    CHAR(4) NOT NULL,
    name    TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX cissource_i ON cissource(id);
CREATE UNIQUE INDEX cissource_c ON cissource(code);
CREATE INDEX cissource_n ON cissource(name);

ALTER TABLE cissource OWNER TO rail;

-- ----------------------------------------------------------------------
-- Darwin tocs
-- ----------------------------------------------------------------------

DROP TABLE toc;

CREATE TABLE toc (
    id      SERIAL NOT NULL,
    code    CHAR(2) NOT NULL,
    name    TEXT NOT NULL,
    -- url of the toc? seems unused at
    url TEXT,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX toc_i ON toc(id);
CREATE UNIQUE INDEX toc_c ON toc(code);
CREATE INDEX toc_n ON toc(name);

ALTER TABLE toc OWNER TO rail;

-- ----------------------------------------------------------------------
-- Darwin via
-- ----------------------------------------------------------------------

DROP TABLE via;

CREATE TABLE via (
    id      SERIAL NOT NULL,
    -- CRS of station where via is defined
    at   CHAR(3) NOT NULL,
    -- tiploc of destination for this via to be valid
    dest    NAME NOT NULL,
    -- Journey must call at this tiploc for via text to be valid
    loc1    NAME NOT NULL,
    -- if defined, Journey must call at this tiploc after loc1 for via text to be valid
    loc2    NAME,
    -- Text to display if journey matches
    text    TEXT NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX via_i ON via(id);
CREATE INDEX via_a ON via(at);
CREATE INDEX via_d ON via(dest);
CREATE INDEX via_l1 ON via(loc1);
CREATE INDEX via_l2 ON via(loc2);
CREATE INDEX via_i1 ON via(at,dest,loc1);
CREATE INDEX via_i2 ON via(at,dest,loc1,loc2);

ALTER TABLE via OWNER TO rail;

