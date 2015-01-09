/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis;

import java.util.Comparator;

/**
 *
 * @author peter
 */
public interface Distance
{

    /**
     * The distance in some common unit
     * <p>
     * @return
     */
    double getDistance();

    /**
     * Set the distance.
     * <p>
     * If an object is immutable this can be left out as the default implementation does nothing.
     * <p>
     * @param distance
     */
    default void setDistance( double distance )
    {
    }

    /**
     * Comparator to compare Distances
     */
    static final Comparator<Distance> COMPARATOR = ( a, b ) -> Double.compare( a.getDistance(), b.getDistance() );

}
