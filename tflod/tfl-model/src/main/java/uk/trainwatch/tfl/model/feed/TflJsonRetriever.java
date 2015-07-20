/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.model.feed;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.trainwatch.util.xml.JsonToXmlTransformer;

/**
 *
 * @author peter
 */
public class TflJsonRetriever
        implements Supplier<String>
{

    private static final Logger LOG = Logger.getLogger( TflJsonRetriever.class.getName() );
    private final String path;
    private final URL url;

    public TflJsonRetriever( String endPoint, Properties p )
            throws URISyntaxException,
                   MalformedURLException
    {
        path = "https://api.tfl.gov.uk/" + endPoint;
        url = new URI( String.format( "%s?app_id=%s&app_key=%s", path, p.getProperty( "app_id" ), p.getProperty( "app_key" ) ) ).
                toURL();
    }

    @Override
    public String get()
    {
        try {
            LOG.log( Level.INFO, () -> "Connecting to " + path );
            URLConnection con = url.openConnection();
            try( InputStream in = con.getInputStream() ) {
                StringWriter w = new StringWriter();
                new JsonToXmlTransformer( new InputStreamReader( in ), w ).run();
                return w.toString();
            }
        }
        catch( IOException ex ) {
            LOG.log( Level.SEVERE, null, ex );
            return null;
        }
    }

}
