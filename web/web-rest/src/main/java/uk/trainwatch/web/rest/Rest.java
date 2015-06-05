/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.rest;

import java.lang.annotation.Annotation;
import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import uk.trainwatch.util.TimeUtils;

/**
 *
 * @author peter
 */
public class Rest
{

    private static final Logger log = Logger.getLogger( Rest.class.getName() );

    private final Response.ResponseBuilder builder;
    private List<NewCookie> cookies;
    private CacheControl cacheControl;

    public static Response invoke( RestConsumer c )
    {
        try {
            Rest r = new Rest( Response.ok() );
            c.accept( r );
            return r.build();
        }
        catch( Throwable t ) {
            log.log( Level.SEVERE, null, t );
            return Response.serverError().
                    entity( t.getMessage() ).
                    build();
        }
    }

    private Rest( Response.ResponseBuilder builder )
    {
        this.builder = builder;
    }

    public Response build()
    {
        if( cookies != null && !cookies.isEmpty() ) {
            builder.cookie( cookies.toArray( new NewCookie[cookies.size()] ) );
        }
        if( cacheControl != null ) {
            builder.cacheControl( cacheControl );
        }
        return builder.build();
    }

    public Rest status( int status )
    {
        builder.status( status );
        return this;
    }

    public Rest status( Response.StatusType status )
    {
        builder.status( status );
        return this;
    }

    public Rest status( Response.Status status )
    {
        builder.status( status );
        return this;
    }

    public Rest entity( Object entity )
    {
        builder.entity( entity );
        return this;
    }

    public Rest entity( Object entity, Annotation[] annotations )
    {
        builder.entity( entity, annotations );
        return this;
    }

    public Rest allow( String... methods )
    {
        builder.allow( methods );
        return this;
    }

    public Rest allow( Set<String> methods )
    {
        builder.allow( methods );
        return this;
    }

    public Rest encoding( String encoding )
    {
        builder.encoding( encoding );
        return this;
    }

    public Rest header( String name, Object value )
    {
        builder.header( name, value );
        return this;
    }

    public Rest replaceAll( MultivaluedMap<String, Object> headers )
    {
        builder.replaceAll( headers );
        return this;
    }

    public Rest language( String language )
    {
        builder.language( language );
        return this;
    }

    public Rest language( Locale language )
    {
        builder.language( language );
        return this;
    }

    public Rest type( MediaType type )
    {
        builder.type( type );
        return this;
    }

    public Rest type( String type )
    {
        builder.type( type );
        return this;
    }

    public Rest variant( Variant variant )
    {
        builder.variant( variant );
        return this;
    }

    public Rest contentLocation( URI location )
    {
        builder.contentLocation( location );
        return this;
    }

    public Rest cookie( NewCookie cookie )
    {
        if( cookies == null ) {
            cookies = new ArrayList<>();
        }
        cookies.add( cookie );
        return this;
    }

    public Rest cookie( String name, String value )
    {
        return cookie( new NewCookie( name, value ) );
    }

    public Rest cookie( String name, String value, int maxAge )
    {
        return cookie( name, value, null, maxAge, false );
    }

    public Rest cookie( String name, String value, int maxAge, boolean secure )
    {
        return cookie( name, value, null, maxAge, secure );
    }

    public Rest cookie( String name, String value, String comment, int maxAge, boolean secure )
    {
        return cookie( new NewCookie( new Cookie( name, value ), comment, maxAge, secure ) );
    }

    public Rest expires( Date expires )
    {
        builder.expires( expires );
        return this;
    }

    public Rest expires( Instant i )
    {
        return expires( Date.from( i ) );
    }

    public Rest expires( ZonedDateTime dt )
    {
        return expires( dt.toInstant() );
    }

    public Rest expires( LocalDateTime dt )
    {
        return expires( dt.atZone( TimeUtils.LONDON ) );
    }

    public Rest expiresIn( Duration d )
    {
        return expires( LocalDateTime.now().plus( d ) );
    }

    public Rest expiresIn( long v, ChronoUnit unit )
    {
        return expiresIn( Duration.of( v, unit ) );
    }

    public Rest lastModified( Date lastModified )
    {
        builder.lastModified( lastModified );
        return this;
    }

    public Rest lastModified( Instant i )
    {
        return lastModified( Date.from( i ) );
    }

    public Rest lastModified( ZonedDateTime dt )
    {
        return lastModified( dt.toInstant() );
    }

    public Rest lastModified( LocalDateTime dt )
    {
        return lastModified( dt.atZone( TimeUtils.LONDON ) );
    }

    public Rest location( URI location )
    {
        builder.location( location );
        return this;
    }

    public Rest tag( EntityTag tag )
    {
        builder.tag( tag );
        return this;
    }

    public Rest tag( String tag )
    {
        builder.tag( tag );
        return this;
    }

    public Rest variants( Variant... variants )
    {
        builder.variants( variants );
        return this;
    }

    public Rest variants( List<Variant> variants )
    {
        builder.variants( variants );
        return this;
    }

    public Rest links( Link... links )
    {
        builder.links( links );
        return this;
    }

    public Rest link( URI uri, String rel )
    {
        builder.link( uri, rel );
        return this;
    }

    public Rest link( String uri, String rel )
    {
        builder.link( uri, rel );
        return this;
    }

    private CacheControl getCacheControl()
    {
        if( cacheControl == null ) {
            cacheControl = new CacheControl();
        }
        return cacheControl;
    }

    public Rest mustRevalidate( boolean mustRevalidate )
    {
        getCacheControl().setMustRevalidate( mustRevalidate );
        return this;
    }

    public Rest proxyRevalidate( boolean proxyRevalidate )
    {
        getCacheControl().setProxyRevalidate( proxyRevalidate );
        return this;
    }

    public Rest maxAge( int maxAge )
    {
        getCacheControl().setMaxAge( maxAge );
        getCacheControl().setSMaxAge( maxAge );
        return this;
    }

    public Rest noCache( boolean noCache )
    {
        getCacheControl().setNoCache( noCache );
        return this;
    }

    public Rest noTransform( boolean noTransform )
    {
        getCacheControl().setNoTransform( noTransform );
        return this;
    }

    public Rest noStore( boolean noStore )
    {
        getCacheControl().setNoStore( noStore );
        return this;
    }

}
