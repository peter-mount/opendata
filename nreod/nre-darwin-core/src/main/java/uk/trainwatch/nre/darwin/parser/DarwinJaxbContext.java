/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.parser;

import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.xml.bind.JAXBException;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.util.xml.JAXBSupport;

/**
 *
 * @author peter
 */
public enum DarwinJaxbContext
{

    INSTANCE;

    /**
     * Mapping function to parse XML into JAXB instances
     */
    public static final Function<String, Pport> fromXML = s -> {
        if( s == null || s.isEmpty() ) {
            return null;
        }
        try {
            return INSTANCE.unmarshall( s );
        }
        catch( ClassCastException |
               NullPointerException |
               JAXBException ex ) {
            return null;
        }
    };

    public static final Function<Pport, String> toXML = p -> {
        if( p == null ) {
            return null;
        }
        try {
            return INSTANCE.marshall( p );
        }
        catch( ClassCastException |
               NullPointerException |
               JAXBException ex ) {
            return null;
        }
    };

    /**
     * A common instance of {@link DarwinUrSplitter}
     */
    public static final Function<Pport, Stream<Pport>> messageSplitter = new DarwinUrSplitter();

    private final Logger log = Logger.getLogger( DarwinJaxbContext.class.getName() );

    private final JAXBSupport jaxb;

    private DarwinJaxbContext()
    {
        try {
            log.log( Level.INFO, "Initialising Darwin JAXB" );
            jaxb = new JAXBSupport( 50, Pport.class );
        }
        catch( JAXBException ex ) {
            Logger.getLogger( DarwinJaxbContext.class.getName() ).log( Level.SEVERE, null, ex );
            throw new IllegalStateException( "Failed to create JAXBContect for Darwin", ex );
        }
    }

    public Pport unmarshall( String s )
            throws JAXBException
    {
        return jaxb.unmarshall( s );
    }

    public String marshall( Pport p )
            throws JAXBException
    {
        return jaxb.marshallToString( p );
    }

    public static Pport duplicate( Pport orig )
    {
        Pport p = new Pport();
        p.setTs( orig.getTs() );
        p.setVersion( orig.getVersion() );

        Pport.UR ur0 = orig.getUR();
        if( ur0 != null ) {
            Pport.UR ur1 = new Pport.UR();
            ur1.setRequestID( ur0.getRequestID() );
            ur1.setRequestSource( ur0.getRequestSource() );
            ur1.setUpdateOrigin( ur0.getUpdateOrigin() );
            p.setUR( ur1 );
        }

        return p;
    }
}
