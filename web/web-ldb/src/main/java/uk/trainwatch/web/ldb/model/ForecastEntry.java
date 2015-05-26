/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalTime;
import java.util.stream.Collectors;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLBiConsumer;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
public class ForecastEntry
        implements Comparable<ForecastEntry>
{

    private static final String SELECT = "SELECT f.fid, t.tpl, f.supp,"
                                         + "f.pta, f.ptd, f.wta, f.wtd, f.wtp,"
                                         + "f.delay,"
                                         + "f.arr, f.dep, f.etarr, f.etdep, f.etpass,"
                                         + "f.plat, f.platsup, f.cisplatsup, f.platsrc,"
                                         + "f.length, f.detachfront,"
                                         + "f.tm"
                                         + " FROM darwin.forecast_entry f"
                                         + " INNER JOIN darwin.tiploc t ON f.tpl=t.id"
                                         + " WHERE f.fid=?";

    public static final SQLFunction<ResultSet, ForecastEntry> fromSQL = rs -> new ForecastEntry(
            rs.getLong( "fid" ),
            rs.getString( "tpl" ),
            rs.getBoolean( "supp" ),
            TimeUtils.getLocalTime( rs, "pta" ),
            TimeUtils.getLocalTime( rs, "ptd" ),
            TimeUtils.getLocalTime( rs, "wta" ),
            TimeUtils.getLocalTime( rs, "wtd" ),
            TimeUtils.getLocalTime( rs, "wtp" ),
            TimeUtils.getDuration( rs, "delay" ),
            TimeUtils.getLocalTime( rs, "arr" ),
            TimeUtils.getLocalTime( rs, "dep" ),
            TimeUtils.getLocalTime( rs, "etarr" ),
            TimeUtils.getLocalTime( rs, "etdep" ),
            TimeUtils.getLocalTime( rs, "etpass" ),
            rs.getString( "plat" ),
            rs.getBoolean( "platsup" ),
            rs.getBoolean( "cisplatsup" ),
            rs.getString( "platsrc" ),
            rs.getInt( "length" ),
            rs.getBoolean( "detachfront" ),
            TimeUtils.getLocalTime( rs, "tm" )
    );

    public static final SQLBiConsumer<Connection, Train> populate = ( c, t ) -> {
        if( t.isForecastPresent() ) {
            try( PreparedStatement ps = SQL.prepare( c, SELECT, t.getForecastId() ) ) {
                t.setForecastEntries( SQL.stream( ps, fromSQL ).
                        sorted().
                        collect( Collectors.toList() )
                );
            }
        }
    };
    
    private final long id;
    private final String tpl;
    private final boolean sup;
    private final LocalTime pta;
    private final LocalTime ptd;
    private final LocalTime wta;
    private final LocalTime wtd;
    private final LocalTime wtp;
    private final Duration delay;
    private final LocalTime arr;
    private final LocalTime dep;
    private final LocalTime etarr;
    private final LocalTime etdep;
    private final LocalTime etpass;
    private final String plat;
    private final boolean platsup;
    private final boolean cisplatsup;
    private final String platsrc;
    private final int length;
    private final boolean detatchfront;
    private final LocalTime tm;

    public ForecastEntry( long id, String tpl, boolean sup, LocalTime pta, LocalTime ptd, LocalTime wta, LocalTime wtd, LocalTime wtp, Duration delay,
                          LocalTime arr, LocalTime dep, LocalTime etarr, LocalTime etdep, LocalTime etpass, String plat, boolean platsup, boolean cisplatsup,
                          String platsrc, int length, boolean detatchfront, LocalTime tm )
    {
        this.id = id;
        this.tpl = tpl;
        this.sup = sup;
        this.pta = pta;
        this.ptd = ptd;
        this.wta = wta;
        this.wtd = wtd;
        this.wtp = wtp;
        this.delay = delay;
        this.arr = arr;
        this.dep = dep;
        this.etarr = etarr;
        this.etdep = etdep;
        this.etpass = etpass;
        this.plat = plat;
        this.platsup = platsup;
        this.cisplatsup = cisplatsup;
        this.platsrc = platsrc;
        this.length = length;
        this.detatchfront = detatchfront;
        this.tm = tm;
    }

    public long getId()
    {
        return id;
    }

    public String getTpl()
    {
        return tpl;
    }

    public boolean isSup()
    {
        return sup;
    }

    public LocalTime getPta()
    {
        return pta;
    }

    public LocalTime getPtd()
    {
        return ptd;
    }

    public LocalTime getWta()
    {
        return wta;
    }

    public LocalTime getWtd()
    {
        return wtd;
    }

    public LocalTime getWtp()
    {
        return wtp;
    }

    public Duration getDelay()
    {
        return delay;
    }

    public LocalTime getArr()
    {
        return arr;
    }

    public LocalTime getDep()
    {
        return dep;
    }

    public LocalTime getEtarr()
    {
        return etarr;
    }

    public LocalTime getEtdep()
    {
        return etdep;
    }

    public LocalTime getEtpass()
    {
        return etpass;
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

    public String getPlatsrc()
    {
        return platsrc;
    }

    public int getLength()
    {
        return length;
    }

    public boolean isDetatchfront()
    {
        return detatchfront;
    }

    public LocalTime getTm()
    {
        return tm;
    }

    @Override
    public int compareTo( ForecastEntry o )
    {
        return TimeUtils.compareLocalTimeDarwin.compare( tm, o.tm );
    }

}
