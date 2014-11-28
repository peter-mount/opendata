/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis.kml;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import uk.trainwatch.util.sql.CachingSQLResultSetHandler;
import uk.trainwatch.util.sql.SQLResultSetHandler;
import uk.trainwatch.util.xml.XMLStreamWriterConsumer;

/**
 *
 * @author peter
 */
public class Placemark
        implements KMLObject<Placemark>
{

    public static final SQLResultSetHandler<Placemark> fromSQL = new CachingSQLResultSetHandler<Placemark>()
    {

        @Override
        public Placemark apply( ResultSet t )
                throws SQLException
        {
            Placemark p = new Placemark();
            forEachColumn( t, p::add );
            return p;
        }
    };

    public static final XMLStreamWriterConsumer<Placemark> toXML = ( w, p ) ->
    {
        w.writeStartElement( "Placemark" );
        w.writeElement( "name", p.getName() );

        if( !p.isEmpty() )
        {
            w.writeStartElement( "ExtendedData" );
            p.forEach( w::writeElement );
            w.writeEndElement();
        }

        w.writeStartElement( "Point" );
        w.writeElement( "coordinates", ",", p.getLongitude(), p.getLatitude() );
        w.writeEndElement();

        w.writeEndElement();
    };

    private String name;
    private double longitude;
    private double latitude;
    private Map<String, Object> extendedData;

    public Placemark()
    {
    }

    public final String getName()
    {
        return name;
    }

    public final void setName( String name )
    {
        this.name = name;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude( double latitude )
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude( double longitude )
    {
        this.longitude = longitude;
    }

    public void add( String k, Object v )
    {
        System.out.printf( "add \"%s\" -> \"%s\"\n", k, v );
        String key = k;
        switch( key )
        {
            case "longitude":
                setLongitude( v == null ? 0.0 : ((Number) v).doubleValue() );
                break;

            case "latitude":
                setLatitude( v == null ? 0.0 : ((Number) v).doubleValue() );
                break;

            case "name":
                setName( v == null ? "" : String.valueOf( v ) );
                break;

            default:
                if( extendedData == null )
                {
                    extendedData = new HashMap<>();
                }
                extendedData.put( key, v );
                break;
        }
    }

    public boolean isEmpty()
    {
        return extendedData == null ? true : extendedData.isEmpty();
    }

    public int size()
    {
        return extendedData == null ? 0 : extendedData.size();
    }

    public Object get( String key )
    {
        return extendedData == null ? null : extendedData.get( key );
    }

    public Object remove( String key )
    {
        return extendedData == null ? null : extendedData.remove( key );
    }

    public void clear()
    {
        extendedData = null;
    }

    public Set<String> keySet()
    {
        if( extendedData == null )
        {
            return Collections.emptySet();
        }
        return extendedData.keySet();
    }

    public Object getOrDefault( Object key, Object defaultValue )
    {
        return extendedData == null ? defaultValue : extendedData.getOrDefault( key, defaultValue );
    }

    public void forEach( BiConsumer<? super String, ? super Object> action )
    {
        if( extendedData != null )
        {
            extendedData.forEach( action );
        }
    }

}
