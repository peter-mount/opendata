-- ------------------------------------------------------------
-- Signals
-- ------------------------------------------------------------

SET search_path = tpnm;

DROP TABLE security_way;
DROP TABLE securitysection;
DROP TABLE signal;

DROP TABLE directed;

-- Link two nodes as start & end points for something like a signal
CREATE TABLE directed (
    id      SERIAL NOT NULL,
    startid BIGINT NOT NULL,
    endid   BIGINT NOT NULL,
    PRIMARY KEY(id)
);
ALTER TABLE directed OWNER TO rail;

-- TOD add security section here
CREATE TABLE signal (
    id      INTEGER NOT NULL,
    interlockingsysid   INTEGER NOT NULL,
    name                NAME,
    zoneid              INTEGER NOT NULL,
    tmpclosed           BOOLEAN NOT NULL DEFAULT FALSE,
    usesecsectfreeingtm BOOLEAN,
    secsectfreeingtm    INTEGER,
    directedid          BIGINT,-- REFERENCES tpnm.directed(id),
    PRIMARY KEY(id)
);
ALTER TABLE signal OWNER TO rail;

CREATE TABLE securitysection (
    id                  SERIAL NOT NULL,
    signalid            BIGINT,-- NOT NULL REFERENCES signal(id),
    vmax                INTEGER NOT NULL,
    accelerationattail  BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);
ALTER TABLE securitysection OWNER TO rail;

-- security_way - a way linked to a securitysection
CREATE TABLE security_way (
    securitysectionid   INTEGER NOT NULL,-- REFERENCES securitysection(id),
    PRIMARY KEY(id)
) INHERITS (way);
CREATE INDEX security_way_s ON security_way(securitysectionid);
ALTER TABLE security_way OWNER TO rail;

CREATE OR REPLACE FUNCTION tpnm.createsecurityway(psecid BIGINT)
RETURNS BIGINT AS $$
DECLARE
    aid     BIGINT;
    rec     RECORD;
BEGIN
    aid = nextval('tpnm.waypoint_id_seq');
    INSERT INTO tpnm.security_way (id,securitysectionid) VALUES (aid,psecid);
    RETURN aid;
END;
$$ LANGUAGE plpgsql;
