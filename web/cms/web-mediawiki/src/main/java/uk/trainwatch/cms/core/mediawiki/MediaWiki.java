/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.cms.core.mediawiki;

import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import javax.enterprise.context.ApplicationScoped;
import uk.trainwatch.cms.CmsComponent;
import uk.trainwatch.cms.core.Image;
import uk.trainwatch.cms.core.ImageRetriever;
import uk.trainwatch.cms.core.Page;
import uk.trainwatch.cms.core.PageRetriever;
import uk.trainwatch.rabbitmq.RabbitMQ;

/**
 * The MediaWiki specific modules.
 * <p>
 * The following configuration items need to be true to be enabled on this node:
 * <ul>
 * <li> cms.mediawiki.clean.enabled - Enable node to handle cleaning of mediawiki content</li>
 * <li> cms.mediawiki.retrieve.enabled - Enable node to retrieve content from core media wiki instance</li>
 * </ul>
 * <p>
 * @author peter
 */
@ApplicationScoped
public class MediaWiki
        extends CmsComponent
{

    @Override
    public boolean init()
    {
        boolean ok = super.init();
        if( ok ) {
            retrieveMediaWikiImage();
            retrieveMediaWikiPage();
            cleanMediaWikiPage();
        }
        return ok;
    }

    /**
     * A worker on a shared queue which receives a page as a WireMessage type "CmsPage", clean it up of any artifacts and publishes it back to RabbitMQ
     * with the routing key "cms.store.page".
     * <p>
     * @param wikiUrl
     */
    private void cleanMediaWikiPage()
    {
        if( !getHostBoolean( "mediawiki.clean", false ) ) {
            return;
        }

        // Clean up the page content
        Map<String, Object> properties = RabbitMQ.queueTTL( CMS_TTL );

        // No hostname so we share the queue - only one may clean up a page
        properties.put( RabbitMQ.NO_HOSTNAME, true );

        final Consumer<Page> pagePublisher = rabbit.publish( RabbitMQ.DEFAULT_TOPIC, CMS_STORE_PAGE, Page.write );

        rabbit.queueDurableConsumer( CMS_CLEAN_PAGE, CMS_CLEAN_PAGE, properties,
                                     Page.read
                                     // Log what page we are processing
                                     .andThen( p -> {
                                         LOG.log( Level.INFO, () -> "Processing " + p.getName() );
                                         return p;
                                     } )
                                     // These populate the page
                                     .andThen( new TitleExtractor() )
                                     .andThen( new ContentExtractor() )
                                     // Process the extracted content
                                     .andThen( new CommentStripper() )
                                     .andThen( new ArticleFormat() )
                                     .andThen( new LinkFixer() ),
                                     pagePublisher
        );
    }

    /**
     * A shared queue which one worker will retrieve a page from the core MediaWiki instance.
     * <p>
     * Once retrieved the page will be published as a WireMessage of type "CmsPage" with the routing key "cms.mediawiki.retrieved.page"
     * <p>
     * @param wikiUrl
     */
    private void retrieveMediaWikiPage()
    {
        if( !getHostBoolean( "mediawiki.retrieve", false ) ) {
            return;
        }

        final PageRetriever retriever = new PageRetriever( getHostString( "mediawiki.url" ) );

        // Publisher for retrieved pages
        final Consumer<Page> pagePublisher = rabbit.publish( RabbitMQ.DEFAULT_TOPIC, CMS_CLEAN_PAGE, Page.write );

        // Retrieve page inserts & updates. We need to use parking queues & set a long timeout as we may have a bulk update which can take some time
        Map<String, Object> properties = RabbitMQ.parkQueue( 3000L );
        RabbitMQ.queueTTL( properties, CMS_TTL );

        // No hostname so we share the queue - only one may retrieve
        properties.put( RabbitMQ.NO_HOSTNAME, true );

        rabbit.queueDurableConsumer( CMS_RETRIEVE_PAGE_QUEUE,
                                     "cms.page.insert.#,cms.page.update.#",
                                     properties,
                                     RabbitMQ.toJsonObject.
                                     andThen( Page::new ).
                                     andThen( retriever ),
                                     pagePublisher
        );
    }

    /**
     * A shared queue which one worker will retrieve an image from the core MediaWiki instance
     * <p>
     * Once retrieved the page will be published as a WireMessage of type "CmsImage" with the routing key "cms.mediawiki.retrieved.image"
     * <p>
     * @param wikiUrl
     */
    private void retrieveMediaWikiImage()
    {
        if( !getHostBoolean( "mediawiki.retrieve", false ) ) {
            return;
        }

        final ImageRetriever retriever = new ImageRetriever( getHostString( "mediawiki.url" ) );

        // Publisher to publish the retrieved image, targetted directly to persistence
        final Consumer<Image> imagePublisher = rabbit.publish( RabbitMQ.DEFAULT_TOPIC, CMS_STORE_IMAGE, Image.write );

        // Retrieve image inserts & updates. Again use parking & long timeout
        Map<String, Object> properties = RabbitMQ.parkQueue( 3000L );
        RabbitMQ.queueTTL( properties, CMS_TTL );

        // No hostname so we share the queue - only one may retrieve
        properties.put( RabbitMQ.NO_HOSTNAME, true );

        rabbit.queueDurableConsumer( CMS_RETRIEVE_IMAGE_QUEUE,
                                     "cms.image.insert.#,cms.image.update.#",
                                     properties,
                                     RabbitMQ.toJsonObject.
                                     andThen( Image::new ).
                                     andThen( retriever ),
                                     imagePublisher
        );
    }

}
