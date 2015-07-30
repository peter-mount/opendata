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
SELECT AddGeometryColumn('tpnm','feat_node','geom',-1,'POINT',2);

INSERT INTO tpnm.feat_node
    SELECT id, netx::INTEGER, nety::INTEGER, ST_MakePoint(netx, nety)
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

SELECT AddGeometryColumn('tpnm','feat_track','geom',-1,'LINESTRING',2);

INSERT INTO feat_track
    SELECT  t.id, t.name, t.descr, ST_MakeLine(n.geom)
        FROM track t
        INNER JOIN track_way tw ON t.id=tw.trackid
        INNER JOIN waypoint wp ON tw.id=wp.wayid
        INNER JOIN feat_node n ON wp.nodeid=n.id
        GROUP BY t.id;
