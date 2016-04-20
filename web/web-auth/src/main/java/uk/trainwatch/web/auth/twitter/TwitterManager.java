/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.auth.twitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import twitter4j.TwitterFactory;
import uk.trainwatch.util.config.ConfigurationService;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class TwitterManager
{

    @Inject
    private ConfigurationService configurationService;

    private final Map<String, TwitterFactory> factories = new ConcurrentHashMap<>();

//    public synchronized Twitter newTwitter( String name )
//    {
//        return factories.computeIfAbsent( name, this::newFactory )
//                .getInstance();
//    }
//
//    private TwitterFactory newFactory( String name )
//    {
//        Configuration conf = configurationService.getPrivateConfiguration( "twitter" );
//        return new TwitterFactory( new ConfigurationBuilder()
//                .setOAuthConsumerKey( conf.getString( name + ".consumerKey" ) )
//                .setOAuthConsumerSecret( conf.getString( name + ".consumerSecret" ) )
//                .build() );
//    }
}
