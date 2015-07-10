/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.trackernet.cache;

import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.cache.annotation.CacheDefaults;
import javax.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

/**
 *
 * @author peter
 */
@ApplicationScoped
@CacheDefaults( cacheName = "TrackerNetStationCache" )
public class TrackerNetStationCache
{

    private static final Logger LOG = Logger.getLogger( TrackerNetStationCache.class.getName() );

    @Resource(name = "jdbc/rail")
    private DataSource dataSource;

}
