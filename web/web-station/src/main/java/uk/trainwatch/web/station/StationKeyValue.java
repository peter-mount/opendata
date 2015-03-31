/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.station;

import javax.xml.bind.annotation.XmlRootElement;
import uk.trainwatch.nrod.location.TrainLocation;

/**
 *
 * @author peter
 */
@XmlRootElement
public class StationKeyValue
{

    private final String crs;
    private final String name;

    public StationKeyValue( TrainLocation l )
    {
        this( l.getCrs(), l.getLocation() );
    }

    public StationKeyValue( String crs, String name )
    {
        this.crs = crs;
        this.name = name;
    }

    public String getCrs()
    {
        return crs;
    }

    public String getName()
    {
        return name;
    }

}
