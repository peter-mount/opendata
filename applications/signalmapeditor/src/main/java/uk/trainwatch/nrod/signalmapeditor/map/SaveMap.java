/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import uk.trainwatch.nrod.signalmapeditor.MainFrame;
import uk.trainwatch.nrod.signalmapeditor.Project;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author peter
 */
public class SaveMap
        implements MapVisitor,
                   Runnable
{

    private final SignalMap map;

    private JsonObjectBuilder json;
    private JsonArrayBuilder berths;
    private JsonArrayBuilder lines;

    public SaveMap( SignalMap map )
    {
        this.map = map;
    }

    @Override
    public void run()
    {
        save( map );
    }

    public JsonObject getJson()
    {
        return json.build();
    }

    public void save( File file, SignalMap map )
    {
        map.accept( this );
        save( file );
    }

    public void save( SignalMap map )
    {
        map.accept( this );
        save( map.getFile() );
    }

    public void save( File file )
    {
        if( file == null ) {
            return;
        }

        try {
            MainFrame.setStatus( "Saving " + file );

            File f = new File( file.getParent(), file.getName() + ".bak" );
            if( f.exists() ) {
                f.delete();
            }
            file.renameTo( f );

            try( FileWriter w = new FileWriter( file ) ) {
                w.write( JsonUtils.toString.apply( json.build() ) );
                w.flush();
            }

            Project.INSTANCE.setChanged( false );
            MainFrame.setStatus( "Saved " + file );
        }
        catch( IOException ex ) {
            ex.printStackTrace();
        }
    }

    @Override
    public void visit( SignalMap map )
    {
        berths = Json.createArrayBuilder();
        lines = Json.createArrayBuilder();

        map.forEach( this );

        json = Json.createObjectBuilder().
                add( "area", Objects.toString( map.getArea(), "" ) ).
                add( "title", Objects.toString( map.getTitle(), "" ) ).
                add( "author", Objects.toString( map.getAuthor(), "" ) ).
                add( "version", Objects.toString( map.getVersion(), "" ) ).
                add( "notes", Objects.toString( map.getNotes(), "" ) ).
                add( "berths", berths ).
                add( "lines", lines );
    }

    @Override
    public void visit( Berth b )
    {
        berths.add( Json.createObjectBuilder().
                add( "berth", b.getId() ).
                add( "x", b.getX() ).
                add( "y", b.getY() ).
                add( "next", b.getOutBerths().
                     stream().
                     map( Berth::getId ).
                     collect( Collectors.joining( "," ) ) )
        );
    }

    @Override
    public void visit( Line l )
    {
    }

    @Override
    public void visit( Points p )
    {
    }

}
