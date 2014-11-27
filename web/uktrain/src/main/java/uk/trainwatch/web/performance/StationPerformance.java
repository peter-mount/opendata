/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.performance;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.util.Streams;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.web.timetable.ScheduleSQL;

/**
 * Handles the generation of performance figures for a station (if known)
 * <p>
 * @author peter
 */
public class StationPerformance
{

    private static volatile DataSource dataSource;

    private static DataSource getDataSource()
            throws SQLException
    {
        if( dataSource == null )
        {
            synchronized( ScheduleSQL.class )
            {
                if( dataSource == null )
                {
                    try
                    {
                        dataSource = InitialContext.doLookup( "java:/comp/env/jdbc/rail" );
                    }
                    catch( NamingException ex )
                    {
                        throw new SQLException( ex );
                    }
                }
            }
        }
        return dataSource;
    }

    private static Connection getConnection()
            throws SQLException
    {
        return getDataSource().
                getConnection();
    }

    /**
     * Converts a {@link LocalDate} to the format in datetime.dim_date
     * <p>
     * @param date
     *             <p>
     * @return
     */
    public static long getDBDate( LocalDate date )
    {
        return (date.getYear() * 400) + date.getDayOfYear();
    }

    public static long getDBDateStanox( LocalDate date, long stanox )
    {
        return getDBDateStanox( getDBDate( date ), stanox );
    }

    public static long getDBDateStanox( long dbDate, long stanox )
    {
        // Account for occasional error seen in feed
        long s = stanox > 99999 ? (stanox % 100000L) : stanox;
        return (dbDate * 100000) + s;
    }

    public static Map<String, List<StationPerfStat>> getStationPerfStat( LocalDate date, TrainLocation loc )
            throws SQLException
    {
        long stanox = loc.getStanox();
        if( stanox < 1 )
        {
            return Collections.emptyMap();
        }

        long dbDateStanox = getDBDateStanox( date, stanox );
        try( Connection con = getConnection() )
        {
            try( PreparedStatement ps1 = con.prepareStatement( "SELECT * FROM report.perf_stanox_all WHERE dt_stanox=?" );
                 PreparedStatement ps2 = con.prepareStatement(
                         "SELECT * FROM report.perf_stanox_toc p inner join report.dim_operator o on p.operatorid=o.operatorid WHERE dt_stanox=?" );
                 PreparedStatement ps3 = con.prepareStatement(
                         "SELECT * FROM report.perf_stanox_toc_class p inner join report.dim_operator o on p.operatorid=o.operatorid WHERE dt_stanox=?" ) )
            {
                ps1.setLong( 1, dbDateStanox );
                ps2.setLong( 1, dbDateStanox );
                ps3.setLong( 1, dbDateStanox );
                return Streams.concat(
                        SQL.stream( ps1, StationPerfStat.fromSQL_ALL ),
                        SQL.stream( ps2, StationPerfStat.fromSQL_TOC ),
                        SQL.stream( ps3, StationPerfStat.fromSQL_TOC_CLASS )
                ).
                        sorted( StationPerfStat.COMPARATOR ).
                        collect(
                                Collectors.groupingBy( StationPerfStat::getOperator, Collectors.toList()
                                                       //Collectors.groupingBy( StationPerfStat::getTrainClass, Collectors.toList() )
                                )
                        );
            }
        }
    }
}
