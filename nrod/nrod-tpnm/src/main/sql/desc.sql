-- ------------------------------------------------------------
-- Main reference types
-- ------------------------------------------------------------

SET search_path = tpnm;

DROP TABLE stationtypedesc;
DROP TABLE stationcategorydesc;
DROP TABLE trackcategorydesc;
DROP TABLE uiccodedesc;

DROP TABLE textref;

CREATE TABLE textref (
    id      INTEGER NOT NULL,
    text    TEXT NOT NULL DEFAULT '',
    ts      TIMESTAMP WITHOUT TIME ZONE,
    PRIMARY KEY (id)
);
ALTER TABLE textref OWNER TO rail;

-- <uiccodedesc id="0" text="NOT_SET" vanillatext="-" lastmodified="2012-12-01T08:49:33"></uiccodedesc>

CREATE TABLE uiccodedesc (
    vantext TEXT NOT NULL DEFAULT '',
    PRIMARY KEY (id)
) INHERITS (textref);
ALTER TABLE uiccodedesc OWNER TO rail;

CREATE TABLE trackcategorydesc (PRIMARY KEY (id)) INHERITS (textref);
ALTER TABLE trackcategorydesc OWNER TO rail;

CREATE TABLE stationcategorydesc (PRIMARY KEY (id)) INHERITS (textref);
ALTER TABLE stationcategorydesc OWNER TO rail;

CREATE TABLE stationtypedesc (PRIMARY KEY (id)) INHERITS (textref);
ALTER TABLE stationtypedesc OWNER TO rail;

-- TODO period
