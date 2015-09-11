/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import java.io.Serializable;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.json.JsonArray;
import uk.trainwatch.util.Functions;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
public class LDBTrain
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    public static final SQLFunction<ResultSet, LDBTrain> fromSQL = rs -> new LDBTrain(
            rs.getString( "type" ),
            rs.getString( "dest" ),
            rs.getString( "plat" ),
            rs.getBoolean( "platsup" ),
            rs.getBoolean( "cisplatsup" ),
            TimeUtils.getLocalTime( rs, "pta" ),
            TimeUtils.getLocalTime( rs, "ptd" ),
            TimeUtils.getLocalTime( rs, "etarr" ),
            TimeUtils.getLocalTime( rs, "etdep" ),
            rs.getBoolean( "arrived" ),
            rs.getBoolean( "departed" ),
            rs.getBoolean( "delayed" ),
            rs.getInt( "latereason" ),
            rs.getBoolean( "canc" ),
            rs.getInt( "cancreason" ),
            rs.getBoolean( "term" ),
            rs.getString( "rid" ),
            rs.getInt( "via" ),
            TimeUtils.getLocalTime( rs, "tm" ),
            rs.getString( "callpoint" ),
            rs.getString( "lastreport" ),
            rs.getInt( "length" ),
            rs.getString( "toc" ),
            rs.getString( "assoc" ),
            rs.getString( "assoctpl" ),
            rs.getString( "assoccp" )
    );

    private final LDB.Type type;

    private final String dest;

    private final String plat;
    private final boolean platsup;
    private final boolean cisplatsup;

    private final LocalTime pta;
    private final LocalTime ptd;
    private final LocalTime etarr;
    private final LocalTime etdep;
    private final boolean arrived;
    private final boolean departed;
    private final boolean delayed;
    private final Duration delay;
    private final int latereason;
    private final boolean canc;
    private final int cancreason;
    private final boolean term;

    private final String rid;
    private final int via;

    private final LocalTime tm;

    private final Collection<CallingPoint> callpoint;
    private final CallingPoint lastreport;
    private final int length;
    private final String toc;

    private final String assoc;
    private final String assoctpl;
    private final Collection<CallingPoint> assoccp;

    public LDBTrain( String type, String dest, String plat, boolean platsup, boolean cisplatsup, LocalTime pta, LocalTime ptd, LocalTime etarr, LocalTime etdep,
                     boolean arrived, boolean departed,
                     boolean delayed, int latereason, boolean canc, int cancreason, boolean term, String rid, int via, LocalTime tm,
                     String callpoint,
                     String lastreport,
                     int length, String toc,
                     String assoc, String assoctpl, String assoccp )
    {
        this.type = "D".equals( type ) ? LDB.Type.DARWIN : LDB.Type.TFL;
        this.dest = dest;
        this.plat = plat;
        this.platsup = platsup;
        this.cisplatsup = cisplatsup;
        this.pta = pta;
        this.ptd = ptd;
        this.etarr = etarr;
        this.etdep = etdep;
        this.arrived = arrived;
        this.departed = departed;
        this.delayed = delayed;
        this.latereason = latereason;
        this.canc = canc;
        this.cancreason = cancreason;
        this.term = term;
        this.rid = rid;
        this.via = via;
        this.tm = tm;
        this.length = length;
        this.toc = toc;

        if( ptd != null && etdep != null ) {
            delay = Duration.between( ptd, etdep );
        }
        else if( ptd != null && etdep != null ) {
            delay = Duration.between( pta, etarr );
        }
        else {
            delay = Duration.ZERO;
        }

        this.callpoint = Stream.of( callpoint ).
                map( JsonUtils.parseJsonArray ).
                map( Functions.castTo( JsonArray.class ) ).
                flatMap( Collection::stream ).
                map( Functions.castTo( JsonArray.class ) ).
                map( CallingPoint::new ).
                collect( Collectors.toList() );

        this.lastreport = lastreport == null || lastreport.isEmpty() ? null : new CallingPoint( JsonUtils.parseJsonArray.apply( lastreport ) );

        this.assoc = assoc;
        this.assoctpl = assoctpl;
        this.assoccp = Stream.of( assoccp ).
                map( JsonUtils.parseJsonArray ).
                map( Functions.castTo( JsonArray.class ) ).
                flatMap( Collection::stream ).
                map( Functions.castTo( JsonArray.class ) ).
                map( CallingPoint::new ).
                collect( Collectors.toList() );
    }

    public LDB.Type getType()
    {
        return type;
    }

    public String getDest()
    {
        return dest;
    }

    public String getPlat()
    {
        return plat;
    }

    public boolean isPlatsup()
    {
        return platsup;
    }

    public boolean isCisplatsup()
    {
        return cisplatsup;
    }

    public LocalTime getPta()
    {
        return pta;
    }

    public LocalTime getPtd()
    {
        return ptd;
    }

    public LocalTime getEtarr()
    {
        return etarr;
    }

    public LocalTime getEtdep()
    {
        return etdep;
    }

    public boolean isArrived()
    {
        return arrived;
    }

    public boolean isDeparted()
    {
        return departed;
    }

    public boolean isDelayed()
    {
        return delayed;
    }

    public Duration getDelay()
    {
        return delay;
    }

    public boolean isOntime()
    {
        return delay == null || Math.abs( delay.getSeconds() ) < 60;
    }

    public int getLatereason()
    {
        return latereason;
    }

    public boolean isCanc()
    {
        return canc;
    }

    public int getCancreason()
    {
        return cancreason;
    }

    public boolean isTerm()
    {
        return term;
    }

    public String getRid()
    {
        return rid;
    }

    public int getVia()
    {
        return via;
    }

    public LocalTime getTm()
    {
        return tm;
    }

    public Collection<CallingPoint> getCallpoint()
    {
        return callpoint;
    }

    public CallingPoint getLastreport()
    {
        return lastreport;
    }

    public int getLength()
    {
        return length;
    }

    public String getToc()
    {
        return toc;
    }

    public String getAssoc()
    {
        return assoc;
    }

    public String getAssoctpl()
    {
        return assoctpl;
    }

    public Collection<CallingPoint> getAssoccp()
    {
        return assoccp;
    }

}
