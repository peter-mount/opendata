/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis.kml;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import javax.xml.stream.XMLStreamException;
import uk.trainwatch.util.xml.UncheckedXMLStreamException;
import uk.trainwatch.util.xml.XMLStreamWriterConsumer;

/**
 *
 * @author peter
 */
public class Schema
        implements KMLObject<Schema>
{

    public static final XMLStreamWriterConsumer<Schema> toXML = ( w, p ) ->
    {
        if( !p.isEmpty() )
        {
            System.out.printf( "\"%s\"\n", p.getName() );
            w.writeStartElement( "Schema" );
            w.writeAttribute( "name", p.getName() );
            w.writeAttribute( "id", p.getName() );

            p.forEach( ( k, v ) ->
            {
                try
                {
                    w.writeStartElement( "SimpleField" );
                    w.writeAttribute( "name", k );
                    w.writeCharacters( v );
                    w.writeEndElement();
                }
                catch( XMLStreamException ex )
                {
                    throw new UncheckedXMLStreamException( ex );
                }
            } );

            w.writeEndElement();
        }
    };

    private String name;
    private final Map<String, String> schema = new HashMap<>();

    public void setName( String name )
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public boolean isEmpty()
    {
        return schema.isEmpty();
    }

    public void clear()
    {
        schema.clear();
    }

    public int size()
    {
        return schema.size();
    }

    public String get( String key )
    {
        return schema.get( key );
    }

    public Set<String> keySet()
    {
        return schema.keySet();
    }

    public String getOrDefault( String key, String defaultValue )
    {
        return schema.getOrDefault( key, defaultValue );
    }

    public void forEach( BiConsumer<? super String, ? super String> action )
    {
        schema.forEach( action );
    }

    public void add( Placemark p )
    {
        p.forEach( ( k, v ) ->
        {
            if( v != null )
            {
                switch( k )
                {
                    case "longitude":
                    case "latitude":
                    case "name":
                        break;

                    default:
                        add( k, v.getClass().
                             getName() );
                        break;
                }
            }
        } );
    }

    public void add( String name, String type )
    {
        Objects.requireNonNull( name );
        Objects.requireNonNull( type );
        // FIXME for now mark long as int? what other types does KML support?
        if( type.equals( Integer.class.getName() )
            || type.equals( Integer.TYPE.getName() )
            || type.equals( Long.class.getName() )
            || type.equals( Long.TYPE.getName() ) )
        {
            schema.put( name, "int" );
        }
        else
        {
            schema.put( name, "string" );
        }
    }

    public void add( ResultSetMetaData meta )
            throws SQLException
    {
        int s = meta.getColumnCount();
        for( int i = 1; i <= s; i++ )
        {
            String n = meta.getColumnName( i );
            if( !"name".equals( n ) && !"longitude".equals( n ) && !"latitude".equals( n ) )
            {
                add( n, meta.getColumnClassName( i ) );
            }
        }
    }
}
