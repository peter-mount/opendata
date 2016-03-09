/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URI;
import javax.json.Json;
import javax.json.JsonReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import uk.trainwatch.util.MapBuilder;

/**
 * Configuration from a remote http server. Authentication is done
 *
 * @author peter
 */
public class HttpConfiguration
        extends ConfigurationWrapper
{

    private Configuration configuration;
    private final URI uri;

    public HttpConfiguration( URI uri )
    {
        this.uri = uri;
    }

    @Override
    protected Configuration getConfiguration()
    {
        if( configuration == null ) {
            try( CloseableHttpClient client = HttpClients.createDefault() ) {
                HttpGet get = new HttpGet( uri );
                get.setHeader( "User-Agent", "Area51 Configuration/1.0" );
                HttpResponse response = client.execute( get );
                switch( response.getStatusLine().getStatusCode() ) {
                    case 200:
                    case 304:
                        try( InputStream is = response.getEntity().getContent() ) {
                            try( JsonReader r = Json.createReader( new InputStreamReader( is ) ) ) {
                                configuration = new MapConfiguration( MapBuilder.fromJsonObject( r.readObject() ).readonly().build() );
                            }
                        }
                    default:
                        configuration = EmptyConfiguration.INSTANCE;
                }
            }
            catch( IOException ex ) {
                throw new UncheckedIOException( ex );
            }
        }
        return configuration;
    }

}
