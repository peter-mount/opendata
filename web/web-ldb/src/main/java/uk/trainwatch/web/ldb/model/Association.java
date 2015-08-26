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
public class Association
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private static final String SELECT_SQL = "SELECT"
                                         + " m.rid as main,"
                                         + " a.rid as assoc,"
                                         + " t.tpl as tpl,"
                                         + " r.cat,"
                                         + " r.cancelled,"
                                         + " r.deleted,"
                                         + " r.pta,"
                                         + " r.wta,"
                                         + " r.ptd,"
                                         + " r.wtd"
                                         + " FROM darwin.%s r"
                                         + " INNER JOIN darwin.%s m ON r.mainid=m.id"
                                         + " INNER JOIN darwin.%s a ON r.associd=a.id"
                                         + " INNER JOIN darwin.tiploc t ON r.tpl=t.id"
                                         + " WHERE m.rid=?";

    public static final String SELECT = String.format( SELECT_SQL, "schedule_assoc", "schedule", "schedule" );
    public static final String SELECT_ARC = String.format( SELECT_SQL, "schedule_assocarc", "schedulearc", "schedulearc" );

    public static final SQLFunction<ResultSet, Association> fromSQL = rs -> new Association(
            rs.getString( "main" ),
            rs.getString( "assoc" ),
            rs.getString( "tpl" ),
            rs.getString( "cat" ),
            rs.getBoolean( "cancelled" ),
            rs.getBoolean( "deleted" ),
            TimeUtils.getLocalTime( rs, "pta" ),
            TimeUtils.getLocalTime( rs, "ptd" ),
            TimeUtils.getLocalTime( rs, "wta" ),
            TimeUtils.getLocalTime( rs, "wtd" )
    );

    public static final SQLBiConsumer<Connection, Train> populate = ( c, t ) -> {
        if( t.isSchedulePresent() ) {
            try( PreparedStatement ps = SQL.prepare( c, SELECT, t.getRid() ) ) {
                t.setAssociations( SQL.stream( ps, fromSQL ).
                        sorted().
                        collect( Collectors.toList() )
                );
            }
        }
    };

    public static final SQLBiConsumer<Connection, Train> populateArc = ( c, t ) -> {
        if( t.isForecastPresent() ) {
            try( PreparedStatement ps = SQL.prepare( c, SELECT_ARC, t.getRid() ) ) {
                t.setAssociations( SQL.stream( ps, fromSQL ).
                        sorted().
                        collect( Collectors.toList() )
                );
            }
        }
    };

    private final String main;
    private final String assoc;
    private final String tpl;
    private final String cat;
    private final boolean canc;
    private final boolean del;
    private final LocalTime pta;
    private final LocalTime wta;
    private final LocalTime ptd;
    private final LocalTime wtd;

    public Association( String main, String assoc, String tpl, String cat, boolean canc, boolean del, LocalTime pta, LocalTime wta, LocalTime ptd, LocalTime wtd )
    {
        this.main = main;
        this.assoc = assoc;
        this.tpl = tpl;
        this.cat = cat;
        this.canc = canc;
        this.del = del;
        this.pta = pta;
        this.wta = wta;
        this.ptd = ptd;
        this.wtd = wtd;
    }

    public String getMain()
    {
        return main;
    }

    public String getAssoc()
    {
        return assoc;
    }

    public String getTpl()
    {
        return tpl;
    }

    public String getCat()
    {
        return cat;
    }

    public boolean isCanc()
    {
        return canc;
    }

    public boolean isDel()
    {
        return del;
    }

    public LocalTime getPta()
    {
        return pta;
    }

    public LocalTime getWta()
    {
        return wta;
    }

    public LocalTime getPtd()
    {
        return ptd;
    }

    public LocalTime getWtd()
    {
        return wtd;
    }

}
