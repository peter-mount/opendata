/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.cms.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Retrieves the page from MediaWiki returning it's content as a String
 * <p>
 * @author peter
 */
public class PageRetriever
        implements UnaryOperator<Page>
{

    private static final Logger LOG = Logger.getLogger( PageRetriever.class.getName() );

    private final String cmsPrefix;

    public PageRetriever( String cmsPrefix )
    {
        this.cmsPrefix = cmsPrefix;
    }

    @Override
    public Page apply( Page page )
    {
        try {
            LOG.log( Level.INFO, () -> "Retrieving " + page.getName() );
            URL url = new URL( cmsPrefix + "/index.php/" + page.getName() );
            URLConnection con = url.openConnection();
            try( BufferedReader r = new BufferedReader( new InputStreamReader( con.getInputStream() ) ) ) {
                page.setContent( r.lines().collect( Collectors.toList() ) );
            }

            return page;
        }
        catch( FileNotFoundException ex ) {
            LOG.log( Level.SEVERE, () -> "Could not find " + page.getName() );
            return null;
        }
        catch( IOException ex ) {
            LOG.log( Level.SEVERE, () -> "Failed for" + page.getName() );
            return null;
        }
    }

}
