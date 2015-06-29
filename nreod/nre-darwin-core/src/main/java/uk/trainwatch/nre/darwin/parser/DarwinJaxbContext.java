/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.parser;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.JAXBException;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.PportTimetableRef;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.util.xml.JAXBSupport;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class DarwinJaxbContext
{

    /**
     * Mapping function to parse XML into JAXB instances
     */
    public final Function<String, Pport> fromXML()
    {
        return s ->
        {
            if( s == null || s.isEmpty() )
            {
                return null;
            }
            try
            {
                return unmarshall( s );
            } catch( ClassCastException |
                     NullPointerException |
                     JAXBException ex )
            {
                return null;
            }
        };
    }

    public final Function<Pport, String> toXML()
    {
        return p ->
        {
            if( p == null )
            {
                return null;
            }
            try
            {
                return marshall( p );
            } catch( ClassCastException |
                     NullPointerException |
                     JAXBException ex )
            {
                return null;
            }
        };
    }

    private static final Logger log = Logger.getLogger( DarwinJaxbContext.class.getName() );

    private final JAXBSupport jaxb;

    private DarwinJaxbContext()
    {
        try
        {
            log.log( Level.INFO, "Initialising Darwin JAXB" );
            jaxb = new JAXBSupport( 50, Pport.class, PportTimetableRef.class );
        } catch( JAXBException ex )
        {
            Logger.getLogger( DarwinJaxbContext.class.getName() ).log( Level.SEVERE, null, ex );
            throw new IllegalStateException( "Failed to create JAXBContect for Darwin", ex );
        }
    }

    public <T> T unmarshall( String s )
            throws JAXBException
    {
        return jaxb.unmarshall( s );
    }

    public String marshall( Pport p )
            throws JAXBException
    {
        return jaxb.marshallToString( p );
    }

    public <T> T unmarshall( File f )
            throws JAXBException
    {
        return jaxb.unmarshall( f );
    }

    public <T> T unmarshall( Reader r )
            throws JAXBException
    {
        return jaxb.unmarshall( r );
    }

    public <T> T unmarshall( InputStream is )
            throws JAXBException
    {
        return jaxb.unmarshall( is );
    }

    public void marshall( Object v, File f )
            throws JAXBException
    {
        jaxb.marshall( v, f );
    }

    public void marshall( Object v, Writer w )
            throws JAXBException
    {
        jaxb.marshall( v, w );
    }

    public void marshall( Object v, OutputStream os )
            throws JAXBException
    {
        jaxb.marshall( v, os );
    }

    /**
     * @see Pport#cloneMeta()
     * @param orig <p>
     * @return <p>
     * @deprecated now done inside the model
     */
    @Deprecated
    public static Pport duplicate( Pport orig )
    {
        Pport p = new Pport();
        p.setTs( orig.getTs() );
        p.setVersion( orig.getVersion() );

        Pport.UR ur0 = orig.getUR();
        if( ur0 != null )
        {
            Pport.UR ur1 = new Pport.UR();
            ur1.setRequestID( ur0.getRequestID() );
            ur1.setRequestSource( ur0.getRequestSource() );
            ur1.setUpdateOrigin( ur0.getUpdateOrigin() );
            p.setUR( ur1 );
        }

        return p;
    }
}
