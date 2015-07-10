-- ----------------------------------------------------------------------
-- TrackerNet
--
-- ----------------------------------------------------------------------

--CREATE SCHEMA tfl;
SET search_path = tfl;

DROP TABLE station;
DROP TABLE crs;

-- ----------------------------------------------------------------------
-- London Underground Station CRS Codes
-- ----------------------------------------------------------------------

CREATE TABLE crs (
    id      SERIAL NOT NULL,
    crs    CHAR(3) NOT NULL,
    name    NAME NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX crs_c ON crs(code);
CREATE INDEX crs_n ON crs(name);

CREATE OR REPLACE FUNCTION tfl.crs (pcrs TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
BEGIN
    IF pcrs IS NULL OR pcrs = '' THEN
        RETURN NULL;
    ELSE
        LOOP
            SELECT * INTO rec FROM tfl.crs WHERE crs=pcrs;
            IF FOUND THEN
                RETURN rec.id;
            END IF;
            BEGIN
                INSERT INTO tfl.crs (crs,name) VALUES (pcrs,pcrs);
                RETURN currval('tfl.crs_id_seq');
            EXCEPTION WHEN unique_violation THEN
                -- Do nothing, loop & try again
            END;
        END LOOP;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- ----------------------------------------------------------------------
-- Links a station to a line
-- ----------------------------------------------------------------------

CREATE TABLE station (
    lineid  INTEGER NOT NULL REFERENCES tfl.line(id),
    crsid   INTEGER NOT NULL REFERENCES tfl.crs(id),
    PRIMARY KEY (lineid,crsid)
);
CREATE INDEX station_l ON station(lineid);
CREATE INDEX station_c ON station(crsid);

-- Returns station(id) for a station on a specific line
CREATE OR REPLACE FUNCTION tfl.station (pline CHAR, pcrs TEXT)
RETURNS INTEGER AS $$
DECLARE
    rec     RECORD;
    rlineid  INTEGER;
    rcrsid   INTEGER;
BEGIN
    IF pline IS NULL OR pline = '' OR pcrs IS NULL OR pcrs = '' THEN
        RETURN NULL;
    END IF;

    rlineid = tfl.line(pline);
    rcrsid = tfl.crs(pcrs);
    
    LOOP;
        SELECT * INTO rec FROM tfl.station WHERE lineid=rlineid AND crsid=rcrsid;
        IF NOT FOUND THEN
            BEGIN
                INSERT INTO tfl.station (lineid,crsid) VALUES (rlineid,rcrsid);

                RETURN rec.crsid;
            EXCEPTION WHEN unique_violation THEN
                -- Do nothing, loop & try again
            END;
        END IF;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

-- Initial Station data

BEGIN;

-- Bakerloo line

SELECT tfl.station('B','BST'); UPDATE tfl.crs SET name='Baker Street' WHERE crs='BST';
SELECT tfl.station('B','CHX'); UPDATE tfl.crs SET name='Charing Cross' WHERE crs='CHX';
SELECT tfl.station('B','ERB'); UPDATE tfl.crs SET name='Edgware Road (Bakerloo)' WHERE crs='ERB';
SELECT tfl.station('B','ELE'); UPDATE tfl.crs SET name='Elephant and Castle' WHERE crs='ELE';
SELECT tfl.station('B','EMB'); UPDATE tfl.crs SET name='Embankment' WHERE crs='EMB';
SELECT tfl.station('B','HSD'); UPDATE tfl.crs SET name='Harlesden' WHERE crs='HSD';
SELECT tfl.station('B','HAW'); UPDATE tfl.crs SET name='Harrow and Wealdstone' WHERE crs='HAW';
SELECT tfl.station('B','KGN'); UPDATE tfl.crs SET name='Kensal Green' WHERE crs='KGN';
SELECT tfl.station('B','KNT'); UPDATE tfl.crs SET name='Kenton' WHERE crs='KNT';
SELECT tfl.station('B','KPK'); UPDATE tfl.crs SET name='Kilburn Park' WHERE crs='KPK';
SELECT tfl.station('B','LAM'); UPDATE tfl.crs SET name='Lambeth North' WHERE crs='LAM';
SELECT tfl.station('B','MDV'); UPDATE tfl.crs SET name='Maida Vale' WHERE crs='MDV';
SELECT tfl.station('B','MYB'); UPDATE tfl.crs SET name='Marylebone' WHERE crs='MYB';
SELECT tfl.station('B','NWM'); UPDATE tfl.crs SET name='North Wembley' WHERE crs='NWM';
SELECT tfl.station('B','OXC'); UPDATE tfl.crs SET name='Oxford Circus' WHERE crs='OXC';
SELECT tfl.station('B','PAD'); UPDATE tfl.crs SET name='Paddington' WHERE crs='PAD';
SELECT tfl.station('B','PIC'); UPDATE tfl.crs SET name='Piccadilly Circus' WHERE crs='PIC';
SELECT tfl.station('B','QPK'); UPDATE tfl.crs SET name='Queen''s Park' WHERE crs='QPK';
SELECT tfl.station('B','RPK'); UPDATE tfl.crs SET name='Regent''s Park' WHERE crs='RPK';
SELECT tfl.station('B','SKT'); UPDATE tfl.crs SET name='South Kenton' WHERE crs='SKT';
SELECT tfl.station('B','SPK'); UPDATE tfl.crs SET name='Stonebridge Park' WHERE crs='SPK';
SELECT tfl.station('B','WAR'); UPDATE tfl.crs SET name='Warwick Avenue' WHERE crs='WAR';
SELECT tfl.station('B','WLO'); UPDATE tfl.crs SET name='Waterloo' WHERE crs='WLO';
SELECT tfl.station('B','WEM'); UPDATE tfl.crs SET name='Wembley Central' WHERE crs='WEM';
SELECT tfl.station('B','WJN'); UPDATE tfl.crs SET name='Willesden Junction' WHERE crs='WJN';

-- Central line

SELECT tfl.station('C','BNK'); UPDATE tfl.crs SET name='Bank' WHERE crs='BNK';
SELECT tfl.station('C','BDE'); UPDATE tfl.crs SET name='Barkingside' WHERE crs='BDE';
SELECT tfl.station('C','BNG'); UPDATE tfl.crs SET name='Bethnal Green' WHERE crs='BNG';
SELECT tfl.station('C','BDS'); UPDATE tfl.crs SET name='Bond Street' WHERE crs='BDS';
SELECT tfl.station('C','BHL'); UPDATE tfl.crs SET name='Buckhurst Hill' WHERE crs='BHL';
SELECT tfl.station('C','CYL'); UPDATE tfl.crs SET name='Chancery Lane' WHERE crs='CYL';
SELECT tfl.station('C','CHG'); UPDATE tfl.crs SET name='Chigwell' WHERE crs='CHG';
SELECT tfl.station('C','DEB'); UPDATE tfl.crs SET name='Debden' WHERE crs='DEB';
SELECT tfl.station('C','EBY'); UPDATE tfl.crs SET name='Ealing Broadway' WHERE crs='EBY';
SELECT tfl.station('C','EAC'); UPDATE tfl.crs SET name='East Acton' WHERE crs='EAC';
SELECT tfl.station('C','EPP'); UPDATE tfl.crs SET name='Epping' WHERE crs='EPP';
SELECT tfl.station('C','FLP'); UPDATE tfl.crs SET name='Fairlop' WHERE crs='FLP';
SELECT tfl.station('C','GHL'); UPDATE tfl.crs SET name='Gants Hill' WHERE crs='GHL';
SELECT tfl.station('C','GRH'); UPDATE tfl.crs SET name='Grange Hill' WHERE crs='GRH';
SELECT tfl.station('C','GFD'); UPDATE tfl.crs SET name='Greenford' WHERE crs='GFD';
SELECT tfl.station('C','HAI'); UPDATE tfl.crs SET name='Hainault' WHERE crs='HAI';
SELECT tfl.station('C','HLN'); UPDATE tfl.crs SET name='Hanger Lane' WHERE crs='HLN';
SELECT tfl.station('C','HOL'); UPDATE tfl.crs SET name='Holborn' WHERE crs='HOL';
SELECT tfl.station('C','HPK'); UPDATE tfl.crs SET name='Holland Park' WHERE crs='HPK';
SELECT tfl.station('C','LAN'); UPDATE tfl.crs SET name='Lancaster Gate' WHERE crs='LAN';
SELECT tfl.station('C','LEY'); UPDATE tfl.crs SET name='Leyton' WHERE crs='LEY';
SELECT tfl.station('C','LYS'); UPDATE tfl.crs SET name='Leytonstone' WHERE crs='LYS';
SELECT tfl.station('C','LST'); UPDATE tfl.crs SET name='Liverpool Street' WHERE crs='LST';
SELECT tfl.station('C','LTN'); UPDATE tfl.crs SET name='Loughton' WHERE crs='LTN';
SELECT tfl.station('C','MAR'); UPDATE tfl.crs SET name='Marble Arch' WHERE crs='MAR';
SELECT tfl.station('C','MLE'); UPDATE tfl.crs SET name='Mile End' WHERE crs='MLE';
SELECT tfl.station('C','NEP'); UPDATE tfl.crs SET name='Newbury Park' WHERE crs='NEP';
SELECT tfl.station('C','NAC'); UPDATE tfl.crs SET name='North Acton' WHERE crs='NAC';
SELECT tfl.station('C','NHT'); UPDATE tfl.crs SET name='Northolt' WHERE crs='NHT';
SELECT tfl.station('C','NHG'); UPDATE tfl.crs SET name='Notting Hill Gate' WHERE crs='NHG';
SELECT tfl.station('C','OXC'); UPDATE tfl.crs SET name='Oxford Circus' WHERE crs='OXC';
SELECT tfl.station('C','PER'); UPDATE tfl.crs SET name='Perivale' WHERE crs='PER';
SELECT tfl.station('C','QWY'); UPDATE tfl.crs SET name='Queensway' WHERE crs='QWY';
SELECT tfl.station('C','RED'); UPDATE tfl.crs SET name='Redbridge' WHERE crs='RED';
SELECT tfl.station('C','ROD'); UPDATE tfl.crs SET name='Roding Valley' WHERE crs='ROD';
SELECT tfl.station('C','RUG'); UPDATE tfl.crs SET name='Ruislip Gardens' WHERE crs='RUG';
SELECT tfl.station('C','SBC'); UPDATE tfl.crs SET name='Shepherd''s Bush' WHERE crs='SBC';
SELECT tfl.station('C','SNB'); UPDATE tfl.crs SET name='Snaresbrook' WHERE crs='SNB';
SELECT tfl.station('C','SRP'); UPDATE tfl.crs SET name='South Ruislip' WHERE crs='SRP';
SELECT tfl.station('C','SWF'); UPDATE tfl.crs SET name='South Woodford' WHERE crs='SWF';
SELECT tfl.station('C','STP'); UPDATE tfl.crs SET name='St Paul''s' WHERE crs='STP';
SELECT tfl.station('C','SFD'); UPDATE tfl.crs SET name='Stratford' WHERE crs='SFD';
SELECT tfl.station('C','THB'); UPDATE tfl.crs SET name='Theydon Bois' WHERE crs='THB';
SELECT tfl.station('C','TCR'); UPDATE tfl.crs SET name='Tottenham Court Road' WHERE crs='TCR';
SELECT tfl.station('C','WAN'); UPDATE tfl.crs SET name='Wanstead' WHERE crs='WAN';
SELECT tfl.station('C','WAC'); UPDATE tfl.crs SET name='West Acton' WHERE crs='WAC';
SELECT tfl.station('C','WRP'); UPDATE tfl.crs SET name='West Ruislip' WHERE crs='WRP';
SELECT tfl.station('C','WCT'); UPDATE tfl.crs SET name='White City' WHERE crs='WCT';
SELECT tfl.station('C','WFD'); UPDATE tfl.crs SET name='Woodford' WHERE crs='WFD';

-- District line

SELECT tfl.station('D','ACT'); UPDATE tfl.crs SET name='Acton Town' WHERE crs='ACT';
SELECT tfl.station('D','ALE'); UPDATE tfl.crs SET name='Aldgate East' WHERE crs='ALE';
SELECT tfl.station('D','BKG'); UPDATE tfl.crs SET name='Barking' WHERE crs='BKG';
SELECT tfl.station('D','BCT'); UPDATE tfl.crs SET name='Barons Court' WHERE crs='BCT';
SELECT tfl.station('D','BEC'); UPDATE tfl.crs SET name='Becontree' WHERE crs='BEC';
SELECT tfl.station('D','BLF'); UPDATE tfl.crs SET name='Blackfriars' WHERE crs='BLF';
SELECT tfl.station('D','BWR'); UPDATE tfl.crs SET name='Bow Road' WHERE crs='BWR';
SELECT tfl.station('D','BBB'); UPDATE tfl.crs SET name='Bromley-by-Bow' WHERE crs='BBB';
SELECT tfl.station('D','CST'); UPDATE tfl.crs SET name='Cannon Street' WHERE crs='CST';
SELECT tfl.station('D','CHP'); UPDATE tfl.crs SET name='Chiswick Park' WHERE crs='CHP';
SELECT tfl.station('D','DGE'); UPDATE tfl.crs SET name='Dagenham East' WHERE crs='DGE';
SELECT tfl.station('D','DGH'); UPDATE tfl.crs SET name='Dagenham Heathway' WHERE crs='DGH';
SELECT tfl.station('D','EBY'); UPDATE tfl.crs SET name='Ealing Broadway' WHERE crs='EBY';
SELECT tfl.station('D','ECM'); UPDATE tfl.crs SET name='Ealing Common' WHERE crs='ECM';
SELECT tfl.station('D','ECT'); UPDATE tfl.crs SET name='Earl''s Court' WHERE crs='ECT';
SELECT tfl.station('D','EHM'); UPDATE tfl.crs SET name='East Ham' WHERE crs='EHM';
SELECT tfl.station('D','EPY'); UPDATE tfl.crs SET name='East Putney' WHERE crs='EPY';
SELECT tfl.station('D','ERD'); UPDATE tfl.crs SET name='Edgware Road (H & C)' WHERE crs='ERD';
SELECT tfl.station('D','EPK'); UPDATE tfl.crs SET name='Elm Park' WHERE crs='EPK';
SELECT tfl.station('D','EMB'); UPDATE tfl.crs SET name='Embankment' WHERE crs='EMB';
SELECT tfl.station('D','FBY'); UPDATE tfl.crs SET name='Fulham Broadway' WHERE crs='FBY';
SELECT tfl.station('D','GRD'); UPDATE tfl.crs SET name='Gloucester Road' WHERE crs='GRD';
SELECT tfl.station('D','GUN'); UPDATE tfl.crs SET name='Gunnersbury' WHERE crs='GUN';
SELECT tfl.station('D','HMD'); UPDATE tfl.crs SET name='Hammersmith (District and Picc)' WHERE crs='HMD';
SELECT tfl.station('D','HST'); UPDATE tfl.crs SET name='High Street Kensington' WHERE crs='HST';
SELECT tfl.station('D','HCH'); UPDATE tfl.crs SET name='Hornchurch' WHERE crs='HCH';
SELECT tfl.station('D','OLY'); UPDATE tfl.crs SET name='Kensington (Olympia)' WHERE crs='OLY';
SELECT tfl.station('D','KEW'); UPDATE tfl.crs SET name='Kew Gardens' WHERE crs='KEW';
SELECT tfl.station('D','MAN'); UPDATE tfl.crs SET name='Mansion House' WHERE crs='MAN';
SELECT tfl.station('D','MLE'); UPDATE tfl.crs SET name='Mile End' WHERE crs='MLE';
SELECT tfl.station('D','MON'); UPDATE tfl.crs SET name='Monument' WHERE crs='MON';
SELECT tfl.station('D','OLY'); UPDATE tfl.crs SET name='Olympia' WHERE crs='OLY';
SELECT tfl.station('D','PGR'); UPDATE tfl.crs SET name='Parsons Green' WHERE crs='PGR';
SELECT tfl.station('D','PLW'); UPDATE tfl.crs SET name='Plaistow' WHERE crs='PLW';
SELECT tfl.station('D','PUT'); UPDATE tfl.crs SET name='Putney Bridge' WHERE crs='PUT';
SELECT tfl.station('D','RCP'); UPDATE tfl.crs SET name='Ravenscourt Park' WHERE crs='RCP';
SELECT tfl.station('D','RMD'); UPDATE tfl.crs SET name='Richmond' WHERE crs='RMD';
SELECT tfl.station('D','SSQ'); UPDATE tfl.crs SET name='Sloane Square' WHERE crs='SSQ';
SELECT tfl.station('D','SKN'); UPDATE tfl.crs SET name='South Kensington' WHERE crs='SKN';
SELECT tfl.station('D','SFS'); UPDATE tfl.crs SET name='Southfields' WHERE crs='SFS';
SELECT tfl.station('D','SJP'); UPDATE tfl.crs SET name='St. James''s Park' WHERE crs='SJP';
SELECT tfl.station('D','STB'); UPDATE tfl.crs SET name='Stamford Brook' WHERE crs='STB';
SELECT tfl.station('D','STG'); UPDATE tfl.crs SET name='Stepney Green' WHERE crs='STG';
SELECT tfl.station('D','TEM'); UPDATE tfl.crs SET name='Temple' WHERE crs='TEM';
SELECT tfl.station('D','THL'); UPDATE tfl.crs SET name='Tower Hill' WHERE crs='THL';
SELECT tfl.station('D','TGR'); UPDATE tfl.crs SET name='Turnham Green' WHERE crs='TGR';
SELECT tfl.station('D','UPM'); UPDATE tfl.crs SET name='Upminster' WHERE crs='UPM';
SELECT tfl.station('D','UPB'); UPDATE tfl.crs SET name='Upminster Bridge' WHERE crs='UPB';
SELECT tfl.station('D','UPY'); UPDATE tfl.crs SET name='Upney' WHERE crs='UPY';
SELECT tfl.station('D','UPK'); UPDATE tfl.crs SET name='Upton Park' WHERE crs='UPK';
SELECT tfl.station('D','VIC'); UPDATE tfl.crs SET name='Victoria' WHERE crs='VIC';
SELECT tfl.station('D','WBT'); UPDATE tfl.crs SET name='West Brompton' WHERE crs='WBT';
SELECT tfl.station('D','WHM'); UPDATE tfl.crs SET name='West Ham' WHERE crs='WHM';
SELECT tfl.station('D','WKN'); UPDATE tfl.crs SET name='West Kensington' WHERE crs='WKN';
SELECT tfl.station('D','WMS'); UPDATE tfl.crs SET name='Westminster' WHERE crs='WMS';
SELECT tfl.station('D','WCL'); UPDATE tfl.crs SET name='Whitechapel' WHERE crs='WCL';
SELECT tfl.station('D','WDN'); UPDATE tfl.crs SET name='Wimbledon' WHERE crs='WDN';
SELECT tfl.station('D','WMP'); UPDATE tfl.crs SET name='Wimbledon Park' WHERE crs='WMP';

-- Hammersmith & City, Circle lines

SELECT tfl.station('H','ALD'); UPDATE tfl.crs SET name='Aldgate' WHERE crs='ALD';
SELECT tfl.station('H','ALE'); UPDATE tfl.crs SET name='Aldgate East' WHERE crs='ALE';
SELECT tfl.station('H','BST'); UPDATE tfl.crs SET name='Baker Street' WHERE crs='BST';
SELECT tfl.station('H','BAR'); UPDATE tfl.crs SET name='Barbican' WHERE crs='BAR';
SELECT tfl.station('H','BKG'); UPDATE tfl.crs SET name='Barking' WHERE crs='BKG';
SELECT tfl.station('H','BLF'); UPDATE tfl.crs SET name='Blackfriars' WHERE crs='BLF';
SELECT tfl.station('H','BWR'); UPDATE tfl.crs SET name='Bow Road' WHERE crs='BWR';
SELECT tfl.station('H','BBB'); UPDATE tfl.crs SET name='Bromley-by-Bow' WHERE crs='BBB';
SELECT tfl.station('H','CST'); UPDATE tfl.crs SET name='Cannon Street' WHERE crs='CST';
SELECT tfl.station('H','EHM'); UPDATE tfl.crs SET name='East Ham' WHERE crs='EHM';
SELECT tfl.station('H','ERD'); UPDATE tfl.crs SET name='Edgware Road (H & C)' WHERE crs='ERD';
SELECT tfl.station('H','EMB'); UPDATE tfl.crs SET name='Embankment' WHERE crs='EMB';
SELECT tfl.station('H','ESQ'); UPDATE tfl.crs SET name='Euston Square' WHERE crs='ESQ';
SELECT tfl.station('H','FAR'); UPDATE tfl.crs SET name='Farringdon' WHERE crs='FAR';
SELECT tfl.station('H','GRD'); UPDATE tfl.crs SET name='Gloucester Road' WHERE crs='GRD';
SELECT tfl.station('H','GPS'); UPDATE tfl.crs SET name='Great Portland Street' WHERE crs='GPS';
SELECT tfl.station('H','HMS'); UPDATE tfl.crs SET name='Hammersmith' WHERE crs='HMS';
SELECT tfl.station('H','HST'); UPDATE tfl.crs SET name='High Street Kensington' WHERE crs='HST';
SELECT tfl.station('H','KXX'); UPDATE tfl.crs SET name='King''s Cross St Pancras' WHERE crs='KXX';
SELECT tfl.station('H','LST'); UPDATE tfl.crs SET name='Liverpool Street' WHERE crs='LST';
SELECT tfl.station('H','MAN'); UPDATE tfl.crs SET name='Mansion House' WHERE crs='MAN';
SELECT tfl.station('H','MLE'); UPDATE tfl.crs SET name='Mile End' WHERE crs='MLE';
SELECT tfl.station('H','MON'); UPDATE tfl.crs SET name='Monument' WHERE crs='MON';
SELECT tfl.station('H','MGT'); UPDATE tfl.crs SET name='Moorgate' WHERE crs='MGT';
SELECT tfl.station('H','PAD'); UPDATE tfl.crs SET name='Paddington' WHERE crs='PAD';
SELECT tfl.station('H','PLW'); UPDATE tfl.crs SET name='Plaistow' WHERE crs='PLW';
SELECT tfl.station('H','SSQ'); UPDATE tfl.crs SET name='Sloane Square' WHERE crs='SSQ';
SELECT tfl.station('H','SKN'); UPDATE tfl.crs SET name='South Kensington' WHERE crs='SKN';
SELECT tfl.station('H','SJP'); UPDATE tfl.crs SET name='St. James''s Park' WHERE crs='SJP';
SELECT tfl.station('H','STG'); UPDATE tfl.crs SET name='Stepney Green' WHERE crs='STG';
SELECT tfl.station('H','TEM'); UPDATE tfl.crs SET name='Temple' WHERE crs='TEM';
SELECT tfl.station('H','THL'); UPDATE tfl.crs SET name='Tower Hill' WHERE crs='THL';
SELECT tfl.station('H','UPK'); UPDATE tfl.crs SET name='Upton Park' WHERE crs='UPK';
SELECT tfl.station('H','VIC'); UPDATE tfl.crs SET name='Victoria' WHERE crs='VIC';
SELECT tfl.station('H','WHM'); UPDATE tfl.crs SET name='West Ham' WHERE crs='WHM';
SELECT tfl.station('H','WMS'); UPDATE tfl.crs SET name='Westminster' WHERE crs='WMS';
SELECT tfl.station('H','WCL'); UPDATE tfl.crs SET name='Whitechapel' WHERE crs='WCL';

-- Jubilee line

SELECT tfl.station('J','BST'); UPDATE tfl.crs SET name='Baker Street' WHERE crs='BST';
SELECT tfl.station('J','BER'); UPDATE tfl.crs SET name='Bermondsey' WHERE crs='BER';
SELECT tfl.station('J','BDS'); UPDATE tfl.crs SET name='Bond Street' WHERE crs='BDS';
SELECT tfl.station('J','CWR'); UPDATE tfl.crs SET name='Canada Water' WHERE crs='CWR';
SELECT tfl.station('J','CWF'); UPDATE tfl.crs SET name='Canary Wharf' WHERE crs='CWF';
SELECT tfl.station('J','CNT'); UPDATE tfl.crs SET name='Canning Town' WHERE crs='CNT';
SELECT tfl.station('J','CPK'); UPDATE tfl.crs SET name='Canons Park' WHERE crs='CPK';
SELECT tfl.station('J','DHL'); UPDATE tfl.crs SET name='Dollis Hill' WHERE crs='DHL';
SELECT tfl.station('J','FRD'); UPDATE tfl.crs SET name='Finchley Road' WHERE crs='FRD';
SELECT tfl.station('J','GPK'); UPDATE tfl.crs SET name='Green Park' WHERE crs='GPK';
SELECT tfl.station('J','KIL'); UPDATE tfl.crs SET name='Kilburn' WHERE crs='KIL';
SELECT tfl.station('J','KBY'); UPDATE tfl.crs SET name='Kingsbury' WHERE crs='KBY';
SELECT tfl.station('J','LON'); UPDATE tfl.crs SET name='London Bridge' WHERE crs='LON';
SELECT tfl.station('J','NEA'); UPDATE tfl.crs SET name='Neasden' WHERE crs='NEA';
SELECT tfl.station('J','NGW'); UPDATE tfl.crs SET name='North Greenwich' WHERE crs='NGW';
SELECT tfl.station('J','QBY'); UPDATE tfl.crs SET name='Queensbury' WHERE crs='QBY';
SELECT tfl.station('J','SWK'); UPDATE tfl.crs SET name='Southwark' WHERE crs='SWK';
SELECT tfl.station('J','SJW'); UPDATE tfl.crs SET name='St John''s Wood' WHERE crs='SJW';
SELECT tfl.station('J','STA'); UPDATE tfl.crs SET name='Stanmore' WHERE crs='STA';
SELECT tfl.station('J','SFD'); UPDATE tfl.crs SET name='Stratford' WHERE crs='SFD';
SELECT tfl.station('J','SWC'); UPDATE tfl.crs SET name='Swiss Cottage' WHERE crs='SWC';
SELECT tfl.station('J','WLO'); UPDATE tfl.crs SET name='Waterloo' WHERE crs='WLO';
SELECT tfl.station('J','WPK'); UPDATE tfl.crs SET name='Wembley Park' WHERE crs='WPK';
SELECT tfl.station('J','WHM'); UPDATE tfl.crs SET name='West Ham' WHERE crs='WHM';
SELECT tfl.station('J','WHD'); UPDATE tfl.crs SET name='West Hampstead' WHERE crs='WHD';
SELECT tfl.station('J','WMS'); UPDATE tfl.crs SET name='Westminster' WHERE crs='WMS';
SELECT tfl.station('J','WLG'); UPDATE tfl.crs SET name='Willesden Green' WHERE crs='WLG';

-- Metropolitan line

SELECT tfl.station('M','ALD'); UPDATE tfl.crs SET name='Aldgate' WHERE crs='ALD';
SELECT tfl.station('M','AME'); UPDATE tfl.crs SET name='Amersham' WHERE crs='AME';
SELECT tfl.station('M','BST'); UPDATE tfl.crs SET name='Baker Street' WHERE crs='BST';
SELECT tfl.station('M','BAR'); UPDATE tfl.crs SET name='Barbican' WHERE crs='BAR';
SELECT tfl.station('M','CLF'); UPDATE tfl.crs SET name='Chalfont and Latimer' WHERE crs='CLF';
SELECT tfl.station('M','CWD'); UPDATE tfl.crs SET name='Chorleywood' WHERE crs='CWD';
SELECT tfl.station('M','CLW'); UPDATE tfl.crs SET name='Colliers Wood' WHERE crs='CLW';
SELECT tfl.station('M','CRX'); UPDATE tfl.crs SET name='Croxley' WHERE crs='CRX';
SELECT tfl.station('M','ETE'); UPDATE tfl.crs SET name='Eastcote' WHERE crs='ETE';
SELECT tfl.station('M','ESQ'); UPDATE tfl.crs SET name='Euston Square' WHERE crs='ESQ';
SELECT tfl.station('M','FAR'); UPDATE tfl.crs SET name='Farringdon' WHERE crs='FAR';
SELECT tfl.station('M','FRD'); UPDATE tfl.crs SET name='Finchley Road' WHERE crs='FRD';
SELECT tfl.station('M','GPS'); UPDATE tfl.crs SET name='Great Portland Street' WHERE crs='GPS';
SELECT tfl.station('M','HOH'); UPDATE tfl.crs SET name='Harrow on the Hill' WHERE crs='HOH';
SELECT tfl.station('M','HDN'); UPDATE tfl.crs SET name='Hillingdon' WHERE crs='HDN';
SELECT tfl.station('M','ICK'); UPDATE tfl.crs SET name='Ickenham' WHERE crs='ICK';
SELECT tfl.station('M','KXX'); UPDATE tfl.crs SET name='King''s Cross St Pancras' WHERE crs='KXX';
SELECT tfl.station('M','LST'); UPDATE tfl.crs SET name='Liverpool Street' WHERE crs='LST';
SELECT tfl.station('M','MPK'); UPDATE tfl.crs SET name='Moor Park' WHERE crs='MPK';
SELECT tfl.station('M','MGT'); UPDATE tfl.crs SET name='Moorgate' WHERE crs='MGT';
SELECT tfl.station('M','NHR'); UPDATE tfl.crs SET name='North Harrow' WHERE crs='NHR';
SELECT tfl.station('M','NWP'); UPDATE tfl.crs SET name='Northwick Park' WHERE crs='NWP';
SELECT tfl.station('M','NWD'); UPDATE tfl.crs SET name='Northwood' WHERE crs='NWD';
SELECT tfl.station('M','NWH'); UPDATE tfl.crs SET name='Northwood Hills' WHERE crs='NWH';
SELECT tfl.station('M','PIN'); UPDATE tfl.crs SET name='Pinner' WHERE crs='PIN';
SELECT tfl.station('M','RLN'); UPDATE tfl.crs SET name='Rayners Lane' WHERE crs='RLN';
SELECT tfl.station('M','RKY'); UPDATE tfl.crs SET name='Rickmansworth' WHERE crs='RKY';
SELECT tfl.station('M','RUI'); UPDATE tfl.crs SET name='Ruislip' WHERE crs='RUI';
SELECT tfl.station('M','RUM'); UPDATE tfl.crs SET name='Ruislip Manor' WHERE crs='RUM';
SELECT tfl.station('M','UXB'); UPDATE tfl.crs SET name='Uxbridge' WHERE crs='UXB';
SELECT tfl.station('M','WAT'); UPDATE tfl.crs SET name='Watford' WHERE crs='WAT';
SELECT tfl.station('M','WPK'); UPDATE tfl.crs SET name='Wembley Park' WHERE crs='WPK';
SELECT tfl.station('M','WHR'); UPDATE tfl.crs SET name='West Harrow' WHERE crs='WHR';

-- Northern Line

SELECT tfl.station('N','ANG'); UPDATE tfl.crs SET name='Angel' WHERE crs='ANG';
SELECT tfl.station('N','ARC'); UPDATE tfl.crs SET name='Archway' WHERE crs='ARC';
SELECT tfl.station('N','BAL'); UPDATE tfl.crs SET name='Balham' WHERE crs='BAL';
SELECT tfl.station('N','BNK'); UPDATE tfl.crs SET name='Bank' WHERE crs='BNK';
SELECT tfl.station('N','BPK'); UPDATE tfl.crs SET name='Belsize Park' WHERE crs='BPK';
SELECT tfl.station('N','BOR'); UPDATE tfl.crs SET name='Borough' WHERE crs='BOR';
SELECT tfl.station('N','BTX'); UPDATE tfl.crs SET name='Brent Cross' WHERE crs='BTX';
SELECT tfl.station('N','BUR'); UPDATE tfl.crs SET name='Burnt Oak' WHERE crs='BUR';
SELECT tfl.station('N','CTN'); UPDATE tfl.crs SET name='Camden Town' WHERE crs='CTN';
SELECT tfl.station('N','CHF'); UPDATE tfl.crs SET name='Chalk Farm' WHERE crs='CHF';
SELECT tfl.station('N','CHX'); UPDATE tfl.crs SET name='Charing Cross' WHERE crs='CHX';
SELECT tfl.station('N','CPC'); UPDATE tfl.crs SET name='Clapham Common' WHERE crs='CPC';
SELECT tfl.station('N','CPN'); UPDATE tfl.crs SET name='Clapham North' WHERE crs='CPN';
SELECT tfl.station('N','CPS'); UPDATE tfl.crs SET name='Clapham South' WHERE crs='CPS';
SELECT tfl.station('N','COL'); UPDATE tfl.crs SET name='Colindale' WHERE crs='COL';
SELECT tfl.station('N','CLW'); UPDATE tfl.crs SET name='Colliers Wood' WHERE crs='CLW';
SELECT tfl.station('N','EFY'); UPDATE tfl.crs SET name='East Finchley' WHERE crs='EFY';
SELECT tfl.station('N','EDG'); UPDATE tfl.crs SET name='Edgware' WHERE crs='EDG';
SELECT tfl.station('N','ELE'); UPDATE tfl.crs SET name='Elephant and Castle' WHERE crs='ELE';
SELECT tfl.station('N','EMB'); UPDATE tfl.crs SET name='Embankment' WHERE crs='EMB';
SELECT tfl.station('N','EUS'); UPDATE tfl.crs SET name='Euston' WHERE crs='EUS';
SELECT tfl.station('N','FYC'); UPDATE tfl.crs SET name='Finchley Central' WHERE crs='FYC';
SELECT tfl.station('N','GGR'); UPDATE tfl.crs SET name='Golders Green' WHERE crs='GGR';
SELECT tfl.station('N','GST'); UPDATE tfl.crs SET name='Goodge Street' WHERE crs='GST';
SELECT tfl.station('N','HMP'); UPDATE tfl.crs SET name='Hampstead' WHERE crs='HMP';
SELECT tfl.station('N','HND'); UPDATE tfl.crs SET name='Hendon Central' WHERE crs='HND';
SELECT tfl.station('N','HBT'); UPDATE tfl.crs SET name='High Barnet' WHERE crs='HBT';
SELECT tfl.station('N','HIG'); UPDATE tfl.crs SET name='Highgate' WHERE crs='HIG';
SELECT tfl.station('N','KEN'); UPDATE tfl.crs SET name='Kennington' WHERE crs='KEN';
SELECT tfl.station('N','KTN'); UPDATE tfl.crs SET name='Kentish Town' WHERE crs='KTN';
SELECT tfl.station('N','KXX'); UPDATE tfl.crs SET name='King''s Cross St Pancras' WHERE crs='KXX';
SELECT tfl.station('N','LSQ'); UPDATE tfl.crs SET name='Leicester Square' WHERE crs='LSQ';
SELECT tfl.station('N','LON'); UPDATE tfl.crs SET name='London Bridge' WHERE crs='LON';
SELECT tfl.station('N','MHE'); UPDATE tfl.crs SET name='Mill Hill East' WHERE crs='MHE';
SELECT tfl.station('N','MGT'); UPDATE tfl.crs SET name='Moorgate' WHERE crs='MGT';
SELECT tfl.station('N','MOR'); UPDATE tfl.crs SET name='Morden' WHERE crs='MOR';
SELECT tfl.station('N','MCR'); UPDATE tfl.crs SET name='Mornington Crescent' WHERE crs='MCR';
SELECT tfl.station('N','OLD'); UPDATE tfl.crs SET name='Old Street' WHERE crs='OLD';
SELECT tfl.station('N','OVL'); UPDATE tfl.crs SET name='Oval' WHERE crs='OVL';
SELECT tfl.station('N','SWM'); UPDATE tfl.crs SET name='South Wimbledon' WHERE crs='SWM';
SELECT tfl.station('N','STK'); UPDATE tfl.crs SET name='Stockwell' WHERE crs='STK';
SELECT tfl.station('N','TBE'); UPDATE tfl.crs SET name='Tooting Bec' WHERE crs='TBE';
SELECT tfl.station('N','TBY'); UPDATE tfl.crs SET name='Tooting Broadway' WHERE crs='TBY';
SELECT tfl.station('N','TCR'); UPDATE tfl.crs SET name='Tottenham Court Road' WHERE crs='TCR';
SELECT tfl.station('N','TOT'); UPDATE tfl.crs SET name='Totteridge and Whetstone' WHERE crs='TOT';
SELECT tfl.station('N','TPK'); UPDATE tfl.crs SET name='Tufnell Park' WHERE crs='TPK';
SELECT tfl.station('N','WST'); UPDATE tfl.crs SET name='Warren Street' WHERE crs='WST';
SELECT tfl.station('N','WLO'); UPDATE tfl.crs SET name='Waterloo' WHERE crs='WLO';
SELECT tfl.station('N','WFY'); UPDATE tfl.crs SET name='West Finchley' WHERE crs='WFY';
SELECT tfl.station('N','WSP'); UPDATE tfl.crs SET name='Woodside Park' WHERE crs='WSP';

-- Picadilly line

SELECT tfl.station('P','ACT'); UPDATE tfl.crs SET name='Acton Town' WHERE crs='ACT';
SELECT tfl.station('P','ALP'); UPDATE tfl.crs SET name='Alperton' WHERE crs='ALP';
SELECT tfl.station('P','AGR'); UPDATE tfl.crs SET name='Arnos Grove' WHERE crs='AGR';
SELECT tfl.station('P','ARL'); UPDATE tfl.crs SET name='Arsenal' WHERE crs='ARL';
SELECT tfl.station('P','BCT'); UPDATE tfl.crs SET name='Barons Court' WHERE crs='BCT';
SELECT tfl.station('P','BOS'); UPDATE tfl.crs SET name='Boston Manor' WHERE crs='BOS';
SELECT tfl.station('P','BGR'); UPDATE tfl.crs SET name='Bounds Green' WHERE crs='BGR';
SELECT tfl.station('P','CRD'); UPDATE tfl.crs SET name='Caledonian Road' WHERE crs='CRD';
SELECT tfl.station('P','CFS'); UPDATE tfl.crs SET name='Cockfosters' WHERE crs='CFS';
SELECT tfl.station('P','COV'); UPDATE tfl.crs SET name='Covent Garden' WHERE crs='COV';
SELECT tfl.station('P','ECM'); UPDATE tfl.crs SET name='Ealing Common' WHERE crs='ECM';
SELECT tfl.station('P','ECT'); UPDATE tfl.crs SET name='Earl’s Court' WHERE crs='ECT';
SELECT tfl.station('P','ETE'); UPDATE tfl.crs SET name='Eastcote' WHERE crs='ETE';
SELECT tfl.station('P','FPK'); UPDATE tfl.crs SET name='Finsbury Park' WHERE crs='FPK';
SELECT tfl.station('P','GRD'); UPDATE tfl.crs SET name='Gloucester Road' WHERE crs='GRD';
SELECT tfl.station('P','GPK'); UPDATE tfl.crs SET name='Green Park' WHERE crs='GPK';
SELECT tfl.station('P','HMD'); UPDATE tfl.crs SET name='Hammersmith (District and Picc)' WHERE crs='HMD';
SELECT tfl.station('P','HTX'); UPDATE tfl.crs SET name='Hatton Cross' WHERE crs='HTX';
SELECT tfl.station('P','HTF'); UPDATE tfl.crs SET name='Heathrow Terminal 4' WHERE crs='HTF';
SELECT tfl.station('P','HRV'); UPDATE tfl.crs SET name='Heathrow Terminal 5' WHERE crs='HRV';
SELECT tfl.station('P','HRC'); UPDATE tfl.crs SET name='Heathrow Terminals 123' WHERE crs='HRC';
SELECT tfl.station('P','HDN'); UPDATE tfl.crs SET name='Hillingdon' WHERE crs='HDN';
SELECT tfl.station('P','HOL'); UPDATE tfl.crs SET name='Holborn' WHERE crs='HOL';
SELECT tfl.station('P','HRD'); UPDATE tfl.crs SET name='Holloway Road' WHERE crs='HRD';
SELECT tfl.station('P','HNC'); UPDATE tfl.crs SET name='Hounslow Central' WHERE crs='HNC';
SELECT tfl.station('P','HNE'); UPDATE tfl.crs SET name='Hounslow East' WHERE crs='HNE';
SELECT tfl.station('P','HNW'); UPDATE tfl.crs SET name='Hounslow West' WHERE crs='HNW';
SELECT tfl.station('P','HPC'); UPDATE tfl.crs SET name='Hyde Park Corner' WHERE crs='HPC';
SELECT tfl.station('P','ICK'); UPDATE tfl.crs SET name='Ickenham' WHERE crs='ICK';
SELECT tfl.station('P','KXX'); UPDATE tfl.crs SET name='King''s Cross St Pancras' WHERE crs='KXX';
SELECT tfl.station('P','KNB'); UPDATE tfl.crs SET name='Knightsbridge' WHERE crs='KNB';
SELECT tfl.station('P','LSQ'); UPDATE tfl.crs SET name='Leicester Square' WHERE crs='LSQ';
SELECT tfl.station('P','MNR'); UPDATE tfl.crs SET name='Manor House' WHERE crs='MNR';
SELECT tfl.station('P','NEL'); UPDATE tfl.crs SET name='North Ealing' WHERE crs='NEL';
SELECT tfl.station('P','NFD'); UPDATE tfl.crs SET name='Northfields' WHERE crs='NFD';
SELECT tfl.station('P','OAK'); UPDATE tfl.crs SET name='Oakwood' WHERE crs='OAK';
SELECT tfl.station('P','OST'); UPDATE tfl.crs SET name='Osterley' WHERE crs='OST';
SELECT tfl.station('P','PRY'); UPDATE tfl.crs SET name='Park Royal' WHERE crs='PRY';
SELECT tfl.station('P','PIC'); UPDATE tfl.crs SET name='Piccadilly Circus' WHERE crs='PIC';
SELECT tfl.station('P','RLN'); UPDATE tfl.crs SET name='Rayners Lane' WHERE crs='RLN';
SELECT tfl.station('P','RUI'); UPDATE tfl.crs SET name='Ruislip' WHERE crs='RUI';
SELECT tfl.station('P','RUM'); UPDATE tfl.crs SET name='Ruislip Manor' WHERE crs='RUM';
SELECT tfl.station('P','RSQ'); UPDATE tfl.crs SET name='Russell Square' WHERE crs='RSQ';
SELECT tfl.station('P','SEL'); UPDATE tfl.crs SET name='South Ealing' WHERE crs='SEL';
SELECT tfl.station('P','SHR'); UPDATE tfl.crs SET name='South Harrow' WHERE crs='SHR';
SELECT tfl.station('P','SKN'); UPDATE tfl.crs SET name='South Kensington' WHERE crs='SKN';
SELECT tfl.station('P','SGT'); UPDATE tfl.crs SET name='Southgate' WHERE crs='SGT';
SELECT tfl.station('P','SHL'); UPDATE tfl.crs SET name='Sudbury Hill' WHERE crs='SHL';
SELECT tfl.station('P','STN'); UPDATE tfl.crs SET name='Sudbury Town' WHERE crs='STN';
SELECT tfl.station('P','TGR'); UPDATE tfl.crs SET name='Turnham Green' WHERE crs='TGR';
SELECT tfl.station('P','TPL'); UPDATE tfl.crs SET name='Turnpike Lane' WHERE crs='TPL';
SELECT tfl.station('P','UXB'); UPDATE tfl.crs SET name='Uxbridge' WHERE crs='UXB';
SELECT tfl.station('P','WGN'); UPDATE tfl.crs SET name='Wood Green' WHERE crs='WGN';

-- Victoria Line

SELECT tfl.station('V','BHR'); UPDATE tfl.crs SET name='Blackhorse Road' WHERE crs='BHR';
SELECT tfl.station('V','BRX'); UPDATE tfl.crs SET name='Brixton' WHERE crs='BRX';
SELECT tfl.station('V','EUS'); UPDATE tfl.crs SET name='Euston' WHERE crs='EUS';
SELECT tfl.station('V','FPK'); UPDATE tfl.crs SET name='Finsbury Park' WHERE crs='FPK';
SELECT tfl.station('V','GPK'); UPDATE tfl.crs SET name='Green Park' WHERE crs='GPK';
SELECT tfl.station('V','HBY'); UPDATE tfl.crs SET name='Highbury and Islington' WHERE crs='HBY';
SELECT tfl.station('V','KXX'); UPDATE tfl.crs SET name='King''s Cross St Pancras' WHERE crs='KXX';
SELECT tfl.station('V','OXC'); UPDATE tfl.crs SET name='Oxford Circus' WHERE crs='OXC';
SELECT tfl.station('V','PIM'); UPDATE tfl.crs SET name='Pimlico' WHERE crs='PIM';
SELECT tfl.station('V','SVS'); UPDATE tfl.crs SET name='Seven Sisters' WHERE crs='SVS';
SELECT tfl.station('V','STK'); UPDATE tfl.crs SET name='Stockwell' WHERE crs='STK';
SELECT tfl.station('V','TTH'); UPDATE tfl.crs SET name='Tottenham Hale' WHERE crs='TTH';
SELECT tfl.station('V','VUX'); UPDATE tfl.crs SET name='Vauxhall' WHERE crs='VUX';
SELECT tfl.station('V','VIC'); UPDATE tfl.crs SET name='Victoria' WHERE crs='VIC';
SELECT tfl.station('V','WAL'); UPDATE tfl.crs SET name='Walthamstow Central' WHERE crs='WAL';
SELECT tfl.station('V','WST'); UPDATE tfl.crs SET name='Warren Street' WHERE crs='WST';

-- Waterloo & City Line

SELECT tfl.station('W','BNK'); UPDATE tfl.crs SET name='Bank' WHERE crs='BNK';
SELECT tfl.station('W','WLO'); UPDATE tfl.crs SET name='Waterloo' WHERE crs='WLO';

-- Commit it, it will fail if something did just now
COMMIT;
