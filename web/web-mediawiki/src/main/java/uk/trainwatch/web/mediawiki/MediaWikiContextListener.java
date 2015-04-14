/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.mediawiki;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.config.Configuration;
import uk.trainwatch.util.config.JNDIConfig;

/**
 *
 * @author peter
 */
@WebListener
public class MediaWikiContextListener
        implements ServletContextListener
{

    private static final Logger LOG = Logger.getLogger( MediaWikiContextListener.class.getName() );

    private RabbitConnection rabbitConnection;

    @Override
    public void contextInitialized( ServletContextEvent sce )
    {
        if( JNDIConfig.INSTANCE.getBoolean( "mediawiki.disabled" ) ) {
            return;
        }

        LOG.log( Level.INFO, "Initialising MediaWiki integration" );

        rabbitConnection = RabbitMQ.createJNDIConnection( "rabbit/uktrain" );

        Configuration config = JNDIConfig.INSTANCE;
        final String wikiUrl = config.get( "mediawiki.url" );

        // Writer to persist to local storage
        Consumer<CmsFile> writer = new CmsWriter( config.get( "mediawiki.basedir" ) );

        // Retrieve page inserts & updates. We need to use parking queues & set a long timeout as we may have a bulk update which can take some time
        Map<String, Object> properties = RabbitMQ.parkQueue( 3000L );
        RabbitMQ.queueTTL( properties, 86400000L );
        RabbitMQ.queueDurableStream( rabbitConnection, "cms.page", "cms.page.insert.#,cms.page.update.#", properties,
                                     s -> s.map( RabbitMQ.toString ).
                                     map( JsonUtils.parseJsonObject ).
                                     map( Page::new ).
                                     map( new PageRetriever( wikiUrl ) ).
                                     filter( Objects::nonNull ).
                                     map( new TitleExtractor() ).
                                     map( new ContentExtractor() ).
                                     map( new ArticleFormat() ).
                                     map( new LinkFixer() ).
                                     forEach( writer )
        );

        // Retrieve image inserts & updates. Again use parking & long timeout
        properties = RabbitMQ.parkQueue( 3000L );
        RabbitMQ.queueTTL( properties, 86400000L );
        RabbitMQ.queueDurableStream( rabbitConnection, "cms.image", "cms.image.insert.#,cms.image.update.#", properties,
                                     s -> s.map( RabbitMQ.toString ).
                                     map( JsonUtils.parseJsonObject ).
                                     map( Image::new ).
                                     map( new ImageRetriever( wikiUrl ) ).
                                     filter( Objects::nonNull ).
                                     forEach( writer )
        );
    }

    @Override
    public void contextDestroyed( ServletContextEvent sce )
    {
        if( rabbitConnection != null ) {
            rabbitConnection.close();
            rabbitConnection = null;
        }
    }

}
