/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.mediawiki;

import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.config.Configuration;
import uk.trainwatch.util.config.JNDIConfig;

/**
 *
 * @author peter
 */
@WebListener
@ApplicationScoped
public class MediaWikiContextListener
        implements ServletContextListener
{

    private static final Logger LOG = Logger.getLogger( MediaWikiContextListener.class.getName() );

    @Inject
    private Rabbit rabbit;

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        if( JNDIConfig.INSTANCE.getBoolean( "mediawiki.disabled" ) ) {
            return;
        }

        LOG.log( Level.INFO, "Initialising MediaWiki integration" );

        Configuration config = JNDIConfig.INSTANCE;
        final String wikiUrl = config.get( "mediawiki.url" );

        // Writer to persist to local storage
        Consumer<CmsFile> writer = new CmsWriter( config.get( "mediawiki.basedir" ) );

        // Retrieve page inserts & updates. We need to use parking queues & set a long timeout as we may have a bulk update which can take some time
        Map<String, Object> properties = RabbitMQ.parkQueue( 3000L );
        RabbitMQ.queueTTL( properties, 86400000L );

        rabbit.queueDurableConsumer( "cms.page",
                                     "cms.page.insert.#,cms.page.update.#",
                                     properties,
                                     RabbitMQ.toJsonObject.
                                     andThen( Page::new ).
                                     andThen( new PageRetriever( wikiUrl ) ).
                                     andThen( new TitleExtractor() ).
                                     andThen( new ContentExtractor() ).
                                     andThen( new ArticleFormat() ).
                                     andThen( new LinkFixer() ),
                                     writer
        );

        // Retrieve image inserts & updates. Again use parking & long timeout
        properties = RabbitMQ.parkQueue( 3000L );
        RabbitMQ.queueTTL( properties, 86400000L );
        rabbit.queueDurableConsumer( "cms.image",
                                     "cms.image.insert.#,cms.image.update.#",
                                     properties,
                                     RabbitMQ.toJsonObject.
                                     andThen( Image::new ).
                                     andThen( new ImageRetriever( wikiUrl ) ),
                                     writer
        );
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
    }

}
