/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.uktra.timetable;

import in.uktra.servlet.AbstractServlet;
import in.uktra.servlet.ApplicationRequest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.sql.DataSource;

/**
 * TimeTable home
 * <p>
 * @author Peter T Mount
 */
public abstract class AbstractSearchServlet
        extends AbstractServlet
{

    private DataSource dataSource;

    protected final DataSource getDataSource()
    {
        return dataSource;
    }

    protected final Connection getConnection()
            throws SQLException
    {
        return dataSource.getConnection();
    }

    @Override
    public void init( ServletConfig config )
            throws ServletException
    {
        super.init( config );
        try
        {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup( "java:/comp/env/jdbc/rail" );
        }
        catch( NamingException ex )
        {
            throw new ServletException( ex );
        }

    }

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        doSearch( request );
    }

    @Override
    protected void doPost( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        doSearch( request );
    }

    protected abstract void doSearch( ApplicationRequest request )
            throws ServletException,
                   IOException;

}
