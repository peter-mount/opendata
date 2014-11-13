/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.timetable;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

/**
 *
 * @author Peter T Mount
 */
@WebListener
public class TiplocNames
        implements ServletContextListener
{

    private static volatile Map<String, String> names = null;

    public static String getName( String tiploc )
    {
        return names == null ? null : names.get( tiploc );
    }

    public static void reload()
            throws NamingException,
                   SQLException
    {
        Context ctx = new InitialContext();
        DataSource dataSource = (DataSource) ctx.lookup( "java:/comp/env/jdbc/rail" );

        try( Connection con = dataSource.getConnection(); Statement s = con.createStatement() )
        {
            try( ResultSet rs = s.executeQuery( "SELECT tiploc,tpsdesc FROM timetable.tiploc" ) )
            {
                Map<String, String> map = new ConcurrentHashMap<>();
                while( rs.next() )
                {
                    map.put( rs.getString( 1 ).
                            trim(), rs.getString( 2 ) );
                }
                names = map;
            }
        }
    }

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        try
        {
            reload();
        }
        catch( SQLException |
               NamingException ex )
        {
            throw new RuntimeException( ex );
        }
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
        names = null;
    }

}
