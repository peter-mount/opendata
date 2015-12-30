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
public class BasicDistanceCoordinate
        extends BasicCoordinate
        implements DistanceCoordinate
{

    private double distance;

    public BasicDistanceCoordinate( double longitude, double latitude, double distance )
    {
        super( longitude, latitude );
        this.distance = distance;
    }

    @Override
    public void setDistance( double distance )
    {
        this.distance = distance;
    }

    @Override
    public double getDistance()
    {
        return distance;
    }

}
