SET search_path = tpnm;

-- ------------------------------------------------------------
-- Polygons
-- ------------------------------------------------------------
DROP TABLE polyarea;

CREATE TABLE polyarea (
    id      SERIAL NOT NULL,
    name    NAME NOT NULL,
    descr   TEXT NOT NULL,
    PRIMARY KEY(id)
);
ALTER TABLE polyarea OWNER TO rail;

SELECT AddGeometryColumn('tpnm','polyarea','geom',4258,'MULTIPOINT',2);

-- ------------------------------------------------------------
-- Graphic vectors
-- ------------------------------------------------------------
DROP TABLE graphicvector;
CREATE TABLE graphicvector (
    layer   INTEGER NOT NULL,
    x0      INTEGER NOT NULL,
    y0      INTEGER NOT NULL,
    x1      INTEGER NOT NULL,
    y1      INTEGER NOT NULL
);
CREATE INDEX graphicvector_l ON graphicvector(layer);
ALTER TABLE graphicvector OWNER TO rail;

DROP TABLE graphictext;
CREATE TABLE graphictext (
    layer   INTEGER NOT NULL,
    angle   INTEGER NOT NULL,
    text    TEXT NOT NULL,
    x       INTEGER NOT NULL,
    y       INTEGER NOT NULL,
    size    INTEGER NOT NULL
);
ALTER TABLE graphictext OWNER TO rail;

