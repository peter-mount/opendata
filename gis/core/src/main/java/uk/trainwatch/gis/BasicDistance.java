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
public class BasicDistance implements Distance
{

    final double distance;

    public BasicDistance( double distance )
    {
        this.distance = distance;
    }

    @Override
    public double getDistance()
    {
        return distance;
    }

}
