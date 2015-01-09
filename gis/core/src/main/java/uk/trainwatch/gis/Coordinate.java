/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis;

/**
 *
 * @author peter
 */
public interface Coordinate
{

    /**
     * The longitude in degrees, positive is East.
     * <p>
     * @return
     */
    double getLongitude();

    /**
     * The latitude in degrees, positive is North.
     * <p>
     * @return
     */
    double getLatitude();

}
