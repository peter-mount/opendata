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
public class Schedule
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private static final String SELECT_PATTERN = "SELECT s.id, s.rid, s.uid, s.ssd, s.ts, s.trainid, s.toc, s.cancreason, s.via, o.tpl as origin, d.tpl as dest"
                                                 + " FROM darwin.%s s"
                                                 + " INNER JOIN darwin.tiploc o ON s.origin=o.id"
                                                 + " INNER JOIN darwin.tiploc d ON s.dest=d.id"
                                                 + " WHERE s.rid=?"
                                                 + " ORDER BY s.id DESC";

    public static final String SELECT = String.format( SELECT_PATTERN, "schedule" );
    public static final String SELECT_ARC = String.format( SELECT_PATTERN, "schedulearc" );

    public static final SQLFunction<ResultSet, Schedule> fromSQL = rs -> new Schedule(
            rs.getLong( "id" ),
            rs.getString( "rid" ),
            rs.getString( "uid" ),
            rs.getString( "ssd" ),
            rs.getTimestamp( "ts" ),
            rs.getString( "trainid" ),
            rs.getString( "toc" ),
            rs.getInt( "cancreason" ),
            rs.getInt( "via" ),
            rs.getString( "origin" ),
            rs.getString( "dest" )
    );

    public static final SQLBiConsumer<Connection, Train> populate = ( c, t ) -> {
        try( PreparedStatement ps = SQL.prepare( c, SELECT, t.getRid() ) ) {
            t.setSchedule( SQL.stream( ps, fromSQL ).findAny().orElse( null ) );
        }
    };

    public static final SQLBiConsumer<Connection, Train> populateArc = ( c, t ) -> {
        try( PreparedStatement ps = SQL.prepare( c, SELECT_ARC, t.getRid() ) ) {
            t.setSchedule( SQL.stream( ps, fromSQL ).findAny().orElse( null ) );
            t.setArchived( t.isArchived() || t.isSchedulePresent() );
        }
    };

    private final long id;
    private final String rid;
    private final String uid;
    private final String ssd;
    private final Timestamp ts;
    private final String trainId;
    private final String toc;
    private final int cancReason;
    private final int via;
    private final String origin;
    private final String dest;

    public Schedule( long id, String rid, String uid, String ssd, Timestamp ts, String trainId, String toc, int cancReason, int via, String origin, String dest )
    {
        this.id = id;
        this.rid = rid;
        this.uid = uid;
        this.ssd = ssd;
        this.ts = ts;
        this.trainId = trainId;
        this.toc = toc;
        this.cancReason = cancReason;
        this.via = via;
        this.origin = origin;
        this.dest = dest;
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

    public String getTrainId()
    {
        return trainId;
    }

    public String getToc()
    {
        return toc;
    }

    public int getCancReason()
    {
        return cancReason;
    }

    public int getVia()
    {
        return via;
    }

    public String getOrigin()
    {
        return origin;
    }

    public String getDest()
    {
        return dest;
    }

}
