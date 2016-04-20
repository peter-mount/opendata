/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.cms;

import java.util.logging.Logger;
import javax.inject.Inject;
import org.apache.commons.configuration.Configuration;
import uk.trainwatch.rabbitmq.Rabbit;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.config.ConfigurationService;

/**
 *
 * @author peter
 */
public abstract class CmsComponent
{

    public static final long CMS_TTL = 86400000L;
    /**
     * Queue & Routing Key to store a page locally
     */
    public static final String CMS_STORE_PAGE = "cms.store.page";
    /**
     * Queue & Routing Key to store an image locally
     */
    public static final String CMS_STORE_IMAGE = "cms.store.image";
    /**
     * Queue & Routing key to clean a media wiki page
     */
    public static final String CMS_CLEAN_PAGE = "cms.mediawiki.page.clean";
    /**
     * Queue for retrieving media wiki pages
     */
    public static final String CMS_RETRIEVE_PAGE_QUEUE = "cms.mediawiki.page.retrieve";
    /**
     * Queue for retrieving media wiki images
     */
    public static final String CMS_RETRIEVE_IMAGE_QUEUE = "cms.mediawiki.image.retrieve";

    protected final Logger LOG = Logger.getLogger( getClass().getName() );

    @Inject
    protected Rabbit rabbit;

    @Inject
    private ConfigurationService configurationService;

    private Configuration config;

    private String hostname;

    public boolean getBoolean( String k )
    {
        return config.getBoolean( k, true );
    }

    public boolean getBoolean( String k, boolean defaultValue )
    {
        return config.getBoolean( k, defaultValue );
    }

    public boolean getHostBoolean( String k )
    {
        return getHostBoolean( k + "." + hostname, getBoolean( k ) );
    }

    public boolean getHostBoolean( String k, boolean defaultValue )
    {
        return getBoolean( k + "." + hostname, getBoolean( k, defaultValue ) );
    }

    public String getString( String k )
    {
        return config.getString( k );
    }

    public String getHostString( String k )
    {
        return config.getString( k + "." + hostname, getString( k ) );
    }

    public boolean init()
    {
        hostname = RabbitMQ.getHostname();
//        config = configurationService.getPrivateConfiguration( "cms" );
        return getHostBoolean( "enabled", true );
    }

}
