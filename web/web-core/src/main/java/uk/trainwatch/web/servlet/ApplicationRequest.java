/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.servlet;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import javax.servlet.ServletContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tiles.Definition;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.request.ApplicationContext;
import org.apache.tiles.request.servlet.ServletRequest;
import org.apache.tiles.request.servlet.ServletUtil;
import uk.trainwatch.util.TimeUtils;

/**
 *
 * @author Peter T Mount
 */
public class ApplicationRequest
        extends ServletRequest
{

    public ApplicationRequest( ApplicationContext applicationContext,
                               HttpServletRequest request,
                               HttpServletResponse response )
    {
        super( applicationContext, request, response );
    }

    public ApplicationRequest( ServletContext servletContext,
                               HttpServletRequest request,
                               HttpServletResponse response )
    {
        this( ServletUtil.getApplicationContext( servletContext ), request, response );
    }

    public ApplicationRequest( javax.servlet.ServletRequest request, ServletResponse response )
    {
        this( ServletUtil.getApplicationContext( ((HttpServletRequest) request).getServletContext() ),
              (HttpServletRequest) request, (HttpServletResponse) response );
    }

    /**
     * Is the request secure
     * <p>
     * @return
     */
    public final boolean isSecure()
    {
        return getRequest().
                isSecure();
    }

    /**
     * Identical to {@link ServletRequest#isUserInRole(java.lang.String) } except this version will only return true if
     * the request is also secure - insecure requests are always unlogged.
     * <p>
     * @param role <p>
     * @return
     */
    @Override
    public boolean isUserInRole( String role )
    {
        return isSecure() && super.isUserInRole( role );
    }

    /**
     * Checks to see if the user is authenticated.
     * <p>
     * Note: This will only return true if we are also https
     * <p>
     * @return
     */
    public final boolean isAuthenticated()
    {
        return isSecure() && getRequest().
                getUserPrincipal() != null;
    }

    public final void redirect( String path )
            throws IOException
    {
        StringBuilder s = new StringBuilder();
        if( isSecure() ) {
            s.append( "https://" ).
                    append( getServerName() );
        }
        s.append( getContextPath() ).
                append( path );

        getResponse().
                sendRedirect( s.toString() );
    }

    public final String getContextPath()
    {
        return getRequest().
                getContextPath();
    }

    public final String getServerName()
    {
        return getRequest().
                getServerName();
    }

    public final String getServletPath()
    {
        return getRequest().
                getServletPath();
    }

    public final String getPathInfo()
    {
        return getRequest().
                getPathInfo();
    }

    public final void sendError( int error )
            throws IOException
    {
        getResponse().
                sendError( error );
    }

    public final void sendError( int error, String text )
            throws IOException
    {
        getResponse().
                sendError( error, text );
    }

    public final void renderTile( String tile )
    {
        TilesAccess.getContainer( getApplicationContext() ).
                render( tile, this );
    }

    public final Definition resolveTile( String tile )
    {
        return TilesAccess.getContainer( getApplicationContext() ).
                getDefinition( tile, this );
    }

    public final void addHeader( String n, String v )
    {
        getResponse().addHeader( n, v );
    }

    public final void addHeader( String n, Instant i )
    {
        getResponse().setDateHeader( n, i.toEpochMilli() );
    }

    public final void addHeader( String n, ZonedDateTime dt )
    {
        addHeader( n, dt.toInstant() );
    }

    public final void addHeader( String n, LocalDateTime dt )
    {
        addHeader( n, dt.atZone( TimeUtils.LONDON ) );
    }

    public final void expires( Instant instant )
    {
        addHeader( "Expires", instant );
    }

    public final void expires( ZonedDateTime dt )
    {
        addHeader( "Expires", dt );
    }

    public final void expires( LocalDateTime dt )
    {
        addHeader( "Expires", dt );
    }

    public final void expiresIn( Duration d )
    {
        expires( LocalDateTime.now().plus( d ) );
    }

    public final void expiresIn( long v, ChronoUnit unit )
    {
        expiresIn( Duration.of( v, unit ) );
    }

    public final void lastModified( Instant instant )
    {
        addHeader( "last-modified", instant );
    }

    public final void lastModified( ZonedDateTime dt )
    {
        addHeader( "last-modified", dt );
    }

    public final void lastModified( LocalDateTime dt )
    {
        addHeader( "last-modified", dt );
    }

    public final void maxAge( Duration d )
    {
        long max = d.getSeconds();
        getResponse().addHeader( "Cache-Control", "public, max-age=" + max + ", s-maxage=" + max + ", no-transform" );
    }

    public final void maxAge( long v, ChronoUnit unit )
    {
        maxAge( Duration.of( v, unit ) );
    }

}
