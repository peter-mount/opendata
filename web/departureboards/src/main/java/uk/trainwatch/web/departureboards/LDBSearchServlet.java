/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.departureboards;

import java.io.IOException;
import java.io.Writer;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@WebServlet(name = "LDBSearchServlet", urlPatterns = "/search")
public class LDBSearchServlet
        extends AbstractServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        JsonArrayBuilder ary = Json.createArrayBuilder();

        String term = request.getParam().get( "term" );

        if( term != null && !term.isEmpty() ) {
            log( "Searching: " + term );

            DarwinReferenceManager.INSTANCE.searchLocations( term ).
                    peek( l -> log( l.getLocation() ) ).
                    forEach( l -> ary.add( Json.createObjectBuilder().
                                    add( "label", l.getLocation() + " [" + l.getCrs() + "]" ).
                                    add( "value", l.getLocation() ).
                                    add( "crs", l.getCrs() )
                            ) );

        }

        // Allow cache to keep common searches
        request.expiresIn( 1, ChronoUnit.DAYS );
        request.maxAge( 1, ChronoUnit.DAYS );
        request.lastModified( Instant.now() );

        final Writer w = request.getWriter();
        try( JsonWriter jw = Json.createWriter( w ) ) {
            jw.write( ary.build() );
        }
        w.flush();
    }
}
