/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.mediawiki;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.trainwatch.web.cms.StaticContentManager;

/**
 * Retrieves the image from MediaWiki 
 * <p>
 * @author peter
 */
public class ImageRetriever
        implements UnaryOperator<Image>
{

    private static final Logger LOG = Logger.getLogger( ImageRetriever.class.getName() );

    private final String cmsPrefix;

    public ImageRetriever( String cmsPrefix )
    {
        this.cmsPrefix = cmsPrefix;
    }

    @Override
    public Image apply( Image image )
    {
        try {
            LOG.log( Level.INFO, () -> "Retrieving " + image.getName() );
            String realName = cmsPrefix + StaticContentManager.INSTANCE.getRealImagePath( image.getName() );

            LOG.log( Level.INFO, () -> "Retrieving " + realName );
            URL url = new URL( realName );
            URLConnection con = url.openConnection();
            try( InputStream is = con.getInputStream() ) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte b[] = new byte[1024];
                int s = is.read( b );
                while( s > -1 ) {
                    baos.write( b, 0, s );
                    s = is.read( b );
                }
                image.setContent( baos.toByteArray() );
            }

            return image;
        }
        catch( FileNotFoundException ex ) {
            LOG.log( Level.SEVERE, () -> "Could not find " + image.getName() );
            return null;
        }
        catch( IOException ex ) {
            LOG.log( Level.SEVERE, null, ex );
            throw new UncheckedIOException( ex );
        }
    }

}
