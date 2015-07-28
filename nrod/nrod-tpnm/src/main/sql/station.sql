-- ------------------------------------------------------------
-- Stations, tracks and way's
-- ------------------------------------------------------------

SET search_path = tpnm;

DROP TABLE track;
DROP TABLE station;

-- <station stationid="0" uiccode="0" abbrev="- - -" longname="" commentary="" stdstoppingtime="0" stdconnectiontime="0"
-- stationtype="0" stationcategory="0" uicstationcode="" transportassociation="" x="0.0" y="0.0" easting="0" northing="0"
-- stanox="" lpbflag="" periodid="0" capitalsident="0" nalco="0" nlccheckchr="" pomcpcode="0"
-- crscode="" desccapri="" compulsorystop="false" hasopeninghours="0" lastmodified="2014-07-11T22:59:29">

CREATE TABLE station (
    id                  INTEGER NOT NULL,
    uiccode             INTEGER NOT NULL,
    abbrev              NAME NOT NULL,
    longname            NAME NOT NULL,
    commentary          TEXT NOT NULL,
    stdstoppingtime     INTEGER NOT NULL,
    stdconnectiontime   INTEGER NOT NULL,
    stationtype         INTEGER NOT NULL REFERENCES stationtypedesc(id),
    stationcategory     INTEGER NOT NULL REFERENCES stationcategorydesc(id),
    uicstationcode      NAME NOT NULL,
    transportassociation    NAME NOT NULL,
    x                       INTEGER NOT NULL,
    y                       INTEGER NOT NULL,
    easting                 INTEGER NOT NULL,
    northing                INTEGER NOT NULL,
    stanox                  INTEGER NOT NULL,
    lpbflag                 NAME NOT NULL,
    periodid                INTEGER NOT NULL,   -- ref period
    capitalsident           INTEGER NOT NULL,
    nalco                   INTEGER NOT NULL,
    lastmodified            TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    PRIMARY KEY(id)
);
ALTER TABLE station OWNER TO rail;

--<track trackID="0" name="---" seq="0" longname="" description="" trackcategory="0" effectivelength="0" roplinecode="0"
-- salinecode="0" linepropertytemplateid="0" periodid="0" permissiveWorking="false" directed="false" trackTmpClosed="false">

CREATE TABLE track (
    id                  INTEGER NOT NULL,
    name                NAME NOT NULL,
    seq                 INTEGER NOT NULL,
    descr               NAME NOT NULL,
    trackcat            INTEGER NOT NULL REFERENCES trackcategorydesc(id),
    effectlen           INTEGER NOT NULL,
    roplinecode         INTEGER NOT NULL,
    salinecode          INTEGER NOT NULL,
    lineproptemplateid  INTEGER NOT NULL,
    periodid            INTEGER NOT NULL,
    permissiveWorking   BOOLEAN NOT NULL DEFAULT FALSE,
    directed            BOOLEAN NOT NULL DEFAULT FALSE,
    tempclosed          BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id)
);
ALTER TABLE track OWNER TO rail;

--<station stationid="1" uiccode="70" abbrev="ADLESTN" longname="ADDLESTONE" commentary="" stdstoppingtime="30"
-- stdconnectiontime="0" stationtype="4" stationcategory="3" uicstationcode="00095" transportassociation=""
-- x="0.0" y="0.0" easting="0" northing="0" stanox="86011" lpbflag="" periodid="0" capitalsident="0" nalco="555000"
-- nlccheckchr="R" pomcpcode="0" crscode="ASN" desccapri="ADDLESTONE" compulsorystop="false" hasopeninghours="0"
-- lastmodified="2015-07-13T14:39:37">
--<track trackID="1" name="U-2" seq="0" longname="2" description="U-2 Fast Up" trackcategory="1" effectivelength="0"
-- roplinecode="159" salinecode="746" linepropertytemplateid="38" periodid="0" permissiveWorking="false" directed="true"
-- trackTmpClosed="false">
--<way>
--<point lineid="0" nodeid="301708"></point>
--<point lineid="0" nodeid="263641"></point>
