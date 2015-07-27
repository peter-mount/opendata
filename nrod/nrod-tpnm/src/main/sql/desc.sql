-- ------------------------------------------------------------
-- Main reference types
-- ------------------------------------------------------------

SET search_path = tpnm;

DROP TABLE stationtypedesc;
DROP TABLE stationcategorydesc;
DROP TABLE trackcategorydesc;
DROP TABLE uiccodedesc;

-- <uiccodedesc id="0" text="NOT_SET" vanillatext="-" lastmodified="2012-12-01T08:49:33"></uiccodedesc>


CREATE TABLE uiccodedesc (
    id      INTEGER NOT NULL,
    text    TEXT NOT NULL DEFAULT '',
    vantext TEXT NOT NULL DEFAULT '',
    ts      TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id)
);
ALTER TABLE uiccodedesc OWNER TO rail;

CREATE TABLE trackcategorydesc (
    id      INTEGER NOT NULL,
    text    TEXT NOT NULL DEFAULT '',
    ts      TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id)
);
ALTER TABLE trackcategorydesc OWNER TO rail;

CREATE TABLE stationcategorydesc (
    id      INTEGER NOT NULL,
    text    TEXT NOT NULL DEFAULT '',
    ts      TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id)
);
ALTER TABLE stationcategorydesc OWNER TO rail;

CREATE TABLE stationtypedesc (
    id      INTEGER NOT NULL,
    text    TEXT NOT NULL DEFAULT '',
    ts      TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id)
);
ALTER TABLE stationtypedesc OWNER TO rail;
