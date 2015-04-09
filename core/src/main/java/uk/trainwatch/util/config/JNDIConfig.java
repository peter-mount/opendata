/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Handles common configuration options
 *
 * @author peter
 */
public enum JNDIConfig
        implements Configuration
{

    INSTANCE;

    private final Map<String, String> config = new ConcurrentHashMap<>();

    @Override
    public String get( String key )
    {
        return config.computeIfAbsent( key, JNDIConfig::doLookup );
    }

    public static <T> T doLookup( String name )
    {
        try
        {
            return InitialContext.doLookup( "java:/comp/env/" + name );
        } catch( NamingException ex )
        {
            Logger.getLogger( JNDIConfig.class.getName() ).log( Level.SEVERE, null, ex );
            return null;
        }
    }
}
