-- Create a social user with the requested user name
CREATE OR REPLACE FUNCTION createSocialUser( pname TEXT,pfirst NAME,plast NAME)
RETURNS NAME
AS $$
DECLARE
    nam TEXT;
BEGIN
    nam = pname;
    LOOP
        BEGIN
            INSERT INTO users (username,pass,enabled,locked,email,homepage,first,last,name,realm)
                VALUES ( nam, 'N/A', true, false, '', '', pfirst, plast, pname, 1);
            RETURN nam;
        EXCEPTION WHEN unique_violation THEN
            -- Generate random name and loop
            nam = concat(pname , trunc(random()*10000));
        END;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION createTwitterUser( pid BIGINT, pscreenname TEXT, twitter bytea )
RETURNS NAME
AS $$
DECLARE
    rec RECORD;
    twr RECORD;
    nam TEXT;
BEGIN
    SELECT u.* INTO rec FROM users u INNER JOIN user_social su ON u.id=su.userid WHERE su.netid=1 AND su.remoteid = pid::text;
    IF FOUND THEN
        UPDATE user_social
            SET data = twitter
            WHERE remoteid=pid::text AND userid=rec.id AND netid=1;
        RETURN rec.username;
    END IF;

    nam = createSocialUser(pscreenname,pscreenname,'');
    SELECT * INTO rec FROM users WHERE username=nam;
    INSERT INTO user_social (userid,netid,remoteid,data) VALUES (rec.id,1,pid::text,twitter);
    RETURN nam;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION linkTwitterUser( pname TEXT, pid BIGINT, pscreenname TEXT, twitter bytea )
RETURNS NAME
AS $$
DECLARE
    rec RECORD;
    twr RECORD;
    nam TEXT;
BEGIN
    SELECT * INTO rec FROM users WHERE username=pname;
    IF FOUND THEN
        SELECT * INTO twr FROM user_social WHERE userid=rec.id;
        IF NOT FOUND THEN
            INSERT INTO user_social (userid,netid,remoteid,data) VALUES (rec.id,1,pid::text,twitter);
        END IF;
        RETURN rec.username;
    END IF;

    RETURN NULL;
END;
$$ LANGUAGE plpgsql;
