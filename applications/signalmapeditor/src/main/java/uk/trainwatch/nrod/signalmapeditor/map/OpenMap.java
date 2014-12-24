/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import uk.trainwatch.util.Functions;

/**
 *
 * @author peter
 */
public class OpenMap
        implements Runnable
{

    private final SignalMap map;

    public OpenMap( SignalMap map )
    {
        this.map = map;
    }

    @Override
    public void run()
    {
        try( Reader r = new FileReader( map.getFile() ) ) {

            try( JsonReader jr = Json.createReader( r ) ) {
                JsonObject o = (JsonObject) jr.read();
                map.clear();
                map.setArea( o.getString( "area" ) );
                map.setAuthor( o.getString( "author" ) );
                map.setNotes( o.getString( "notes" ) );
                map.setTitle( o.getString( "title" ) );
                map.setVersion( o.getString( "version" ) );

                // Read in the berths
                Map<String, Node> newBerths = o.getJsonArray( "berths" ).
                        stream().
                        map( Functions.castTo( JsonObject.class ) ).
                        map( bo -> new Berth( bo.getString( "berth" ), bo.getInt( "x" ), bo.getInt( "y" ) ) ).
                        collect( Collectors.toMap( Berth::getId, Function.identity() ) );

                // Lines - new format
                o.getJsonArray( "lines" ).
                        stream().
                        map( Functions.castTo( JsonObject.class ) ).
                        filter( bo -> "Line".equals( bo.getString( "type" ) ) ).
                        forEach( bo -> {
                            Berth from = (Berth) newBerths.get( bo.getString( "from" ) );
                            Berth to = (Berth) newBerths.get( bo.getString( "to" ) );
                            Line l = new Line( from, to );
                            newBerths.put( l.getId(), l );
                            from.join( l );
                        } );

                // Buld add
                map.addAll( newBerths.values() );
            }
        }
        catch( IOException ex ) {
            ex.printStackTrace();
        }
    }

}
