/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.svgmap.tpnm;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@WebServlet(name = "TPNMMapServlet", urlPatterns = {"/tpnm/map", "/tpnm/map/*"})
public class TPNMMapServlet
        extends AbstractServlet
{

    private static final Pattern PATTERN = Pattern.compile( "^/([0-9]+)/([0-9.-]+)/([0-9.-]+)$" );

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        String path = request.getPathInfo();
        log( path );
        if( path != null ) {
            Matcher m = PATTERN.matcher( path );
            log( "match " + m.matches() );
            if( m.matches() ) {
                Map<String, Object> req = request.getRequestScope();
                try {
                    req.put( "scale", m.group( 1 ) );

                    req.put( "x", Math.min( Math.max( Double.parseDouble( m.group( 2 ) ), 92844 ), 128509 ) );
                    req.put( "y", Math.min( Math.max( Double.parseDouble( m.group( 3 ) ), -36544 ), 19563 ) );
                    req.entrySet().forEach(
                            e -> log( "Req " + e.getKey() + "=" + e.getValue() )
                    );
                }
                catch( Exception ex ) {
                    log( "Fail", ex );
                    // Invalid so bomb out
                    req.remove( "scale" );
                    req.remove( "x" );
                    req.remove( "y" );
                }
            }
        }

        request.renderTile( "tpnm.map" );
    }

}
