/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.servlet;

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

    protected final void invoke( HttpServletRequest request, HttpServletResponse response, ServletTask task )
            throws ServletException,
                   IOException
    {
        ApplicationRequest req = new ApplicationRequest( getServletContext(), request, response );
        try {
            task.execute( req );
        }
        catch( HttpResponseException ex ) {
            ex.sendError( req );
        }
        catch( Exception ex ) {
            log( ex.getMessage(), ex );
            req.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage() );
        }
    }

    @Override
    protected final void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException,
                   IOException
    {
        invoke( request, response, this::doGet );
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
        invoke( request, response, this::doPost );
    }

    protected void doPost( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.sendError( HttpServletResponse.SC_BAD_REQUEST );
    }

    @Override
    protected final void doHead( HttpServletRequest request, HttpServletResponse response )
            throws ServletException,
                   IOException
    {
        invoke( request, response, this::doHead );
    }

    protected void doHead( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        doGet( request );
    }

    @Override
    protected final void doPut( HttpServletRequest request, HttpServletResponse response )
            throws ServletException,
                   IOException
    {
        invoke( request, response, this::doPut );
    }

    protected void doPut( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.sendError( HttpServletResponse.SC_BAD_REQUEST );
    }

    @Override
    protected final void doDelete( HttpServletRequest request, HttpServletResponse response )
            throws ServletException,
                   IOException
    {
        invoke( request, response, this::doDelete );
    }

    protected void doDelete( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.sendError( HttpServletResponse.SC_BAD_REQUEST );
    }

    @Override
    protected final void doOptions( HttpServletRequest request, HttpServletResponse response )
            throws ServletException,
                   IOException
    {
        invoke( request, response, this::doOptions );
    }

    protected void doOptions( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.sendError( HttpServletResponse.SC_BAD_REQUEST );
    }

    @Override
    protected final void doTrace( HttpServletRequest request, HttpServletResponse response )
            throws ServletException,
                   IOException
    {
        invoke( request, response, this::doTrace );
    }

    protected void doTrace( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.sendError( HttpServletResponse.SC_BAD_REQUEST );
    }

}
