/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.cms.core;

import uk.trainwatch.cms.*;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import javax.enterprise.context.ApplicationScoped;
import uk.trainwatch.rabbitmq.RabbitMQ;

/**
 * The core modules within the cms framework
 * <p>
 * The following configuration items need to be true to be enabled on this node:
 * <ul>
 * <li> cms.store.enabled - Enable node to store cms content. This must be enabled on a node that will serve content</li>
 * </ul>
 * <p>
 * @author peter
 */
@ApplicationScoped
public class CmsCore
        extends CmsComponent
{

    @Override
    public boolean init()
    {
        boolean ok = super.init();
        if( ok ) {
            storeContent();
        }
        return ok;
    }

    /**
     * Handles persistence of pages & images within the local file system
     */
    private void storeContent()
    {
        if( !getHostBoolean( "store", false ) ) {
            return;
        }

        // Store pages
        Map<String, Object> properties = RabbitMQ.queueTTL( CMS_TTL );
        rabbit.queueDurableConsumer( CMS_STORE_PAGE, CMS_STORE_PAGE, properties, Page.read, this::write );

        // Store images
        properties = RabbitMQ.queueTTL( CMS_TTL );
        rabbit.queueDurableConsumer( CMS_STORE_IMAGE, CMS_STORE_IMAGE, properties, Image.read, this::write );
    }

    // Writer to persist to local storage
    private volatile Consumer<CmsFile> writer;

    private void write( CmsFile f )
    {
        if( writer == null ) {
            synchronized( this ) {
                if( writer == null ) {
                    writer = new CmsWriter( getHostString( "basedir" ) );
                }
            }
        }
        try {
            if( f != null ) {
                writer.accept( f );
            }
        }
        catch( Exception ex ) {
            LOG.log( Level.SEVERE, null, ex );
        }
    }

}
