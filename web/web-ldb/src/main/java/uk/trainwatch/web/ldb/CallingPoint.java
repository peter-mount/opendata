/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import java.io.Serializable;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.util.Objects;
import javax.json.JsonArray;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
public class CallingPoint
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    public static final SQLFunction<ResultSet, CallingPoint> fromSQL( DarwinReferenceManager darwinReferenceManager )
    {
        return rs -> {
            String tpl = rs.getString( "tpl" );
            return new CallingPoint(
                    tpl,
                    TimeUtils.getLocalTime( rs, "time" ),
                    rs.getBoolean( "report" ),
                    rs.getBoolean( "can" ),
                    darwinReferenceManager.getLocationRefFromTiploc( tpl )
            );
        };
    }

    private final String tpl;
    private final LocalTime time;
    private final boolean report;
    private final boolean canc;
    private final String crs;
    private final String location;

    public CallingPoint( JsonArray o )
    {
        this( o.getString( 0 ), TimeUtils.getLocalTime( o.getString( 1 ) ), false, false, null );
    }

    public CallingPoint( String tpl, LocalTime time, boolean report, boolean canc, TrainLocation loc )
    {
        this.tpl = tpl;
        this.time = time;
        this.report = report;
        this.canc = canc;
        this.crs = loc == null ? null : loc.getCrs();
        this.location = Objects.toString( loc == null ? null : loc.getLocation(), tpl );
    }

    public String getTpl()
    {
        return tpl;
    }

    /**
     * The crs of this location. This may be null if the location doesn't have one.
     *
     * @return
     */
    public String getCrs()
    {
        return crs;
    }

    /**
     * The location name. If there isn't one then this will be the tpl.
     *
     * @return
     */
    public String getLocation()
    {
        return location;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public boolean isReport()
    {
        return report;
    }

    public boolean isCanc()
    {
        return canc;
    }

}
