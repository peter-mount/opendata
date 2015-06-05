/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.timetable;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import uk.trainwatch.util.sql.KeyValue;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author Peter T Mount
 */
@WebListener
public class TiplocNames
        implements ServletContextListener
{

    private static volatile Map<String, String> names = null;
    private static volatile LocalDateTime lastUpdate = LocalDateTime.MIN;

    private static boolean reloadRequired()
    {
        return lastUpdate.isBefore( LocalDateTime.now().minusHours( 1 ) );
    }

    public static String getName( String tiploc )
    {
        if( names == null || reloadRequired() ) {
            reload();
        }

        return names == null ? null : names.get( tiploc );
    }

    private static synchronized void reload()
    {
        if( names == null || reloadRequired() ) {
            try {
                Context ctx = new InitialContext();
                DataSource dataSource = (DataSource) ctx.lookup( "java:/comp/env/jdbc/rail" );

                try( Connection con = dataSource.getConnection();
                     Statement s = con.createStatement() ) {

                    // The tiplocs from the timetable
                    Map<String, String> map = SQL.stream( s.executeQuery( "SELECT tiploc,tpsdesc FROM timetable.tiploc" ), KeyValue.STRING_STRING ).
                            collect( KeyValue.toMap() );

                    // Overwrite with those from Darwin. Darwin will then take precedence
                    SQL.stream( s.executeQuery( "SELECT t.tpl, l.name"
                                                + " FROM darwin.tiploc t"
                                                + " INNER JOIN darwin.location l ON t.id = l.tpl"
                                                + " INNER JOIN darwin.crs c ON l.crs = c.id" ),
                                KeyValue.STRING_STRING ).
                            forEach( KeyValue.putAll( map ) );

                    names = map;
                    lastUpdate = LocalDateTime.now();
                }
            }
            catch( SQLException |
                   NamingException ex ) {
                throw new RuntimeException( ex );
            }
        }
    }

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        reload();
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
        names = null;
    }

}
