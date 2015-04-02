/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.parser;

import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public static final Function<String, Pport> fromXML = s ->
    {
        if( s == null || s.isEmpty() )
        {
            return null;
        }
        try
        {
            return INSTANCE.unmarshall( s );
        } catch( ClassCastException | NullPointerException | JAXBException ex )
        {
            return null;
        }
    };

    private final String PACKAGES[] =
    {
        "uk.trainwatch.nre.darwin.model.ctt.referenceschema",
        "uk.trainwatch.nre.darwin.model.ctt.schema",
        "uk.trainwatch.nre.darwin.model.ppt.alarms",
        "uk.trainwatch.nre.darwin.model.ppt.commontypes",
        "uk.trainwatch.nre.darwin.model.ppt.forecasts",
        "uk.trainwatch.nre.darwin.model.ppt.schedules",
        "uk.trainwatch.nre.darwin.model.ppt.schema",
        "uk.trainwatch.nre.darwin.model.ppt.stationmessages",
        "uk.trainwatch.nre.darwin.model.ppt.status",
        "uk.trainwatch.nre.darwin.model.ppt.tddata",
        "uk.trainwatch.nre.darwin.model.ppt.trainalerts",
        "uk.trainwatch.nre.darwin.model.ppt.trainorder"
    };
    private final Logger log = Logger.getLogger( DarwinJaxbContext.class.getName() );

    private final JAXBSupport jaxb;

    private DarwinJaxbContext()
    {
        try
        {
            log.log( Level.INFO, "Initialising Darwin JAXB" );
            jaxb = new JAXBSupport( PACKAGES );
        } catch( JAXBException ex )
        {
            Logger.getLogger( DarwinJaxbContext.class.getName() ).log( Level.SEVERE, null, ex );
            throw new IllegalStateException( "Failed to create JAXBContect for Darwin", ex );
        }
    }

    public Pport unmarshall( String s )
            throws JAXBException
    {
        return jaxb.unmarshall( s );
    }
}
