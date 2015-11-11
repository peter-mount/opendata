/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.servlet;

import java.io.IOException;
import javax.servlet.ServletException;

/**
 * An implementation of {@link AbstractServlet} which can only be used in a secure request.
 * <p>
 * If an insecure request is received then a redirect will be issued.
 * <p>
 * @author peter
 */
public class SecureServlet
        extends AbstractServlet
{

    @Override
    protected final void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.ifSecureOrRedirect( this::doSecureGet );
    }

    protected void doSecureGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        super.doGet( request );
    }

    @Override
    protected final void doPost( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.ifSecureOrRedirect( this::doSecurePost );
    }

    protected void doSecurePost( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        super.doPost( request );
    }

    @Override
    protected final void doHead( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.ifSecureOrRedirect( this::doSecureHead );
    }

    protected void doSecureHead( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        super.doHead( request );
    }

    @Override
    protected final void doDelete( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.ifSecureOrRedirect( this::doSecureDelete );
    }

    protected void doSecureDelete( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        super.doDelete( request );
    }

    @Override
    protected final void doPut( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.ifSecureOrRedirect( this::doSecurePut );
    }

    protected void doSecurePut( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        super.doPut( request );
    }

    @Override
    protected final void doOptions( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.ifSecureOrRedirect( this::doSecureOptions );
    }

    protected void doSecureOptions( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        super.doOptions( request );
    }

    @Override
    protected final void doTrace( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.ifSecureOrRedirect( this::doSecureTrace );
    }

    protected void doSecureTrace( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        super.doTrace( request );
    }

}
