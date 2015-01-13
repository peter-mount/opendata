/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis.heatmap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import uk.trainwatch.gis.gheat.HeatMap;

/**
 *
 * @author peter
 */
public enum HeatMapManager
{

    INSTANCE;

    private final Map<String, HeatMap> maps = new ConcurrentHashMap<>();

    public HeatMap getHeatMap( String n )
    {
        return maps.get( n );
    }

    public boolean register( String n, HeatMap m )
    {
        return maps.putIfAbsent( n, m ) == null;
    }

    public Collection<String> getMapNames()
    {
        return new ArrayList<>( maps.keySet() );
    }

    public void forEachName( Consumer<String> c )
    {
        maps.keySet().
                forEach( c );
    }

    public void forEachMap( Consumer<HeatMap> c )
    {
        maps.values().
                forEach( c );
    }
}
