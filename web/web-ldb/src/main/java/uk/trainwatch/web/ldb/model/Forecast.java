/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLBiConsumer;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
public class Forecast
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    public static final String SELECT = "SELECT * FROM darwin.getForecast(?)";

    public static final SQLFunction<ResultSet, Forecast> fromSQL = rs -> new Forecast(
            rs.getLong( "id" ),
            rs.getString( "rid" ),
            rs.getString( "uid" ),
            rs.getString( "ssd" ),
            rs.getTimestamp( "ts" ),
            rs.getInt( "latereason" ),
            rs.getBoolean( "activated" ),
            rs.getBoolean( "deactivated" ),
            rs.getLong( "schedule" ),
            rs.getBoolean( "archived")
    );

    public static final SQLBiConsumer<Connection, Train> populate = ( c, t ) -> {
        try( PreparedStatement ps = SQL.prepare( c, SELECT, t.getRid() ) ) {
            t.setForecast( SQL.stream( ps, fromSQL ).findAny().orElse( null ) );
        }
    };

    private final long id;
    private final String rid;
    private final String uid;
    private final String ssd;
    private final Timestamp ts;
    private final int lateReason;
    private final boolean activated;
    private final boolean deactivated;
    private final long schedule;
    private final boolean archived;

    public Forecast( long id, String rid, String uid, String ssd, Timestamp ts, int lateReason, boolean activated,
                     boolean deactivated, long schedule, boolean archived )
    {
        this.id = id;
        this.rid = rid;
        this.uid = uid;
        this.ssd = ssd;
        this.ts = ts;
        this.lateReason = lateReason;
        this.activated = activated;
        this.deactivated = deactivated;
        this.schedule = schedule;
        this.archived = archived;
    }

    public long getId()
    {
        return id;
    }

    public String getRid()
    {
        return rid;
    }

    public String getUid()
    {
        return uid;
    }

    public String getSsd()
    {
        return ssd;
    }

    public Timestamp getTs()
    {
        return ts;
    }

    public int getLateReason()
    {
        return lateReason;
    }

    public boolean isActivated()
    {
        return activated;
    }

    public boolean isDeactivated()
    {
        return deactivated || archived;
    }

    public boolean isArchived()
    {
        return archived;
    }

    public long getSchedule()
    {
        return schedule;
    }

}
