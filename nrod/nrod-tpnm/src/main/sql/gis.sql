-- ------------------------------------------------------------
-- GIS representation of the TPNM model
-- ------------------------------------------------------------

SET search_path = tpnm;

-- ------------------------------------------------------------
-- Node feature
-- ------------------------------------------------------------
DROP TABLE feat_node;
CREATE TABLE feat_node (
    id      BIGINT NOT NULL,
    x       INTEGER NOT NULL,
    y       INTEGER NOT NULL,
    PRIMARY KEY(id)
);
ALTER TABLE feat_node OWNER TO rail;
SELECT AddGeometryColumn('tpnm','feat_node','geom',4258,'POINT',2);

INSERT INTO tpnm.feat_node
    SELECT id, netx::INTEGER, nety::INTEGER, ST_SetSRID(ST_MakePoint(netx, nety),4258)
        FROM tpnm.node;

-- ------------------------------------------------------------
-- Track feature
-- ------------------------------------------------------------

DROP TABLE feat_track;
CREATE TABLE feat_track (
    id      BIGINT,
    name    NAME NOT NULL,
    descr   NAME NOT NULL,
    PRIMARY KEY (id)
);
ALTER TABLE feat_track OWNER TO rail;

SELECT AddGeometryColumn('tpnm','feat_track','geom',4258,'LINESTRING',2);

--INSERT INTO feat_track
--    SELECT  t.id, t.name, t.descr, ST_MakeLine(n.geom)
--        FROM track t
--            INNER JOIN track_way tw ON t.id=tw.trackid
--            INNER JOIN waypoint wp ON tw.id=wp.wayid
--            INNER JOIN feat_node n ON wp.nodeid=n.id
--        GROUP BY t.id;

DELETE FROM feat_track;
WITH
    nodes AS (
        SELECT w.id, wp.seq, n.geom
            FROM feat_node n
                INNER JOIN waypoint wp ON wp.nodeid=n.id
                INNER JOIN track_way w ON w.id=wp.wayid
            ORDER BY w.id,wp.seq
    ),
    lines AS (
        SELECT n.id,ST_MakeLine(n.geom) as geom
            FROM nodes n
            GROUP BY n.id
    )
INSERT INTO feat_track
    SELECT  t.id, t.name, t.descr, ST_MakeLine(g.geom)
        FROM track t
            INNER JOIN track_way w ON t.id=w.trackid
            INNER JOIN lines g ON w.id = g.id
        GROUP BY t.id;

-- ------------------------------------------------------------
-- Signal feature
-- ------------------------------------------------------------

DROP TABLE feat_signal;
CREATE TABLE feat_signal (
    id                  BIGINT NOT NULL,
    signalid            BIGINT NOT NULL,
    interlockingsysid   INTEGER NOT NULL,
    name                NAME,
    zoneid              INTEGER NOT NULL,
    tmpclosed           BOOLEAN NOT NULL,
    vmax                INTEGER NOT NULL,
    accelerationattail  BOOLEAN NOT NULL,
    PRIMARY KEY(id)
);
ALTER TABLE feat_signal OWNER TO rail;

SELECT AddGeometryColumn('tpnm','feat_signal','geom',4258,'LINESTRING',2);

DELETE FROM feat_signal;
WITH
    nodes AS (
        SELECT w.id, wp.seq, n.geom
            FROM feat_node n
                INNER JOIN waypoint wp ON wp.nodeid=n.id
                INNER JOIN security_way w ON w.id=wp.wayid
            ORDER BY w.id,wp.seq
    ),
    lines AS (
        SELECT n.id,ST_MakeLine(n.geom) as geom
            FROM nodes n
            GROUP BY n.id
    )
INSERT INTO feat_signal
    SELECT  ss.id,
            ss.signalid,
            s.interlockingsysid,
            s.name,
            s.zoneid,
            s.tmpclosed,
            ss.vmax,
            ss.accelerationattail,
            g.geom
        FROM securitysection ss
            INNER JOIN security_way w ON ss.id=w.securitysectionid
            INNER JOIN signal s ON ss.signalid=s.id
            INNER JOIN lines g ON w.id = g.id;

-- ------------------------------------------------------------
-- Graphic vectors
-- ------------------------------------------------------------
DROP TABLE feat_graphicvector;

CREATE TABLE feat_graphicvector (
    layer   INTEGER NOT NULL
);
CREATE INDEX feat_graphicvector_il ON graphicvector(id,layer);
CREATE INDEX feat_graphicvector_l ON graphicvector(layer);
ALTER TABLE feat_graphicvector OWNER TO rail;

SELECT AddGeometryColumn('tpnm','feat_graphicvector','geom',4258,'LINESTRING',2);

DELETE FROM feat_graphicvector;
INSERT INTO feat_graphicvector
    SELECT  layer,
            tpnm.ST_MakeLine(
                ST_SetSRID(ST_MakePoint(x0, y0),4258),
                ST_SetSRID(ST_MakePoint(x1, y1),4258)
            )
        FROM graphicvector;

DROP TABLE feat_graphictext;

CREATE TABLE feat_graphictext (
    layer   INTEGER NOT NULL,
    text    TEXT,
    angle   INTEGER,
    size    INTEGER
);
ALTER TABLE feat_graphictext OWNER TO rail;

SELECT AddGeometryColumn('tpnm','feat_graphictext','geom',4258,'POINT',2);

DELETE FROM feat_graphictext;
INSERT INTO feat_graphictext
    SELECT  layer, text, angle, size,
            ST_SetSRID(ST_MakePoint(x, y),4258)
        FROM graphictext;
