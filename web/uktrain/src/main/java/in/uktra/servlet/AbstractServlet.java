/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.uktra.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Peter T Mount
 */
public class AbstractServlet
        extends HttpServlet
{

    @Override
    protected final void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException,
                   IOException
    {
        ApplicationRequest req = new ApplicationRequest( getServletContext(), request, response );
        try
        {
            doGet( req );
        }
        catch( HttpResponseException ex )
        {
            ex.sendError( req );
        }
        catch( Exception ex )
        {
            req.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage() );
            log( ex.getMessage(), ex );
        }
    }

    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.sendError( HttpServletResponse.SC_BAD_REQUEST );
    }

    @Override
    protected final void doPost( HttpServletRequest request, HttpServletResponse response )
            throws ServletException,
                   IOException
    {
        ApplicationRequest req = new ApplicationRequest( getServletContext(), request, response );
        try
        {
            doPost( req );
        }
        catch( HttpResponseException ex )
        {
            ex.sendError( req );
        }
        catch( Exception ex )
        {
            req.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage() );
            log( ex.getMessage(), ex );
        }
    }

    protected void doPost( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.sendError( HttpServletResponse.SC_BAD_REQUEST );
    }
}
