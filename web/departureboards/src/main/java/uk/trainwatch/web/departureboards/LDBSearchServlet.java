/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.departureboards;

import java.io.IOException;
import java.io.PrintWriter;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;

/**
 *
 * @author peter
 */
@WebServlet( name = "LDBSearchServlet", urlPatterns = "/search" )
public class LDBSearchServlet
        extends HttpServlet
{

    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException,
                   IOException
    {
        JsonArrayBuilder ary = Json.createArrayBuilder();

        String term = req.getParameter( "term" );

        if( term != null && !term.isEmpty() )
        {
            log( "Searching: " + term );

            DarwinReferenceManager.INSTANCE.searchLocations( term ).
                    peek( l-> log(l.getLocation())).
                    forEach( l -> ary.add( Json.createObjectBuilder().
                                    add( "label", l.getLocation() + " [" + l.getCrs() + "]" ).
                                    add( "value", l.getLocation() ).
                                    add( "crs", l.getCrs() )
                            ) );

        }

        final PrintWriter w = resp.getWriter();
        try( JsonWriter jw = Json.createWriter( w ) )
        {
            jw.write( ary.build() );
        }
        w.flush();
    }
}
