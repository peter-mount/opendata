/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import uk.trainwatch.nrod.signalmapeditor.MainFrame;
import uk.trainwatch.nrod.signalmapeditor.Project;
import uk.trainwatch.util.Functions;
import uk.trainwatch.util.JsonUtils;

/**
 * A signal map
 * <p>
 * @author peter
 */
public class SignalMap
        extends AbstractMapObject
{

    public static final String PROP_FILE = "map_file";
    public static final String PROP_TITLE = "map_title";
    public static final String PROP_AREA = "map_area";
    public static final String PROP_AUTHOR = "map_author";
    public static final String PROP_VERSION = "map_version";
    public static final String PROP_BERTHS = "map_berths";
    private final Map<String, Berth> berths = new ConcurrentHashMap<>();

    private File file;
    private String title;
    private String area;
    private String author;
    private String version;

    private String notes;

    public static final String PROP_NOTES = "notes";

    public File getFile()
    {
        return file;
    }

    public void setFile( File file )
    {
        File oldFile = this.file;
        this.file = file;
        firePropertyChange( PROP_FILE, oldFile, file );
    }

    /**
     * Get the value of notes
     *
     * @return the value of notes
     */
    public String getNotes()
    {
        return notes;
    }

    /**
     * Set the value of notes
     *
     * @param notes new value of notes
     */
    public void setNotes( String notes )
    {
        String oldNotes = this.notes;
        this.notes = notes;
        firePropertyChange( PROP_NOTES, oldNotes, notes );
    }

    /**
     * Get the value of version
     *
     * @return the value of version
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * Set the value of version
     *
     * @param version new value of version
     */
    public void setVersion( String version )
    {
        String oldVersion = this.version;
        this.version = version;
        firePropertyChange( PROP_VERSION, oldVersion, version );
    }

    /**
     * Get the value of author
     *
     * @return the value of author
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * Set the value of author
     *
     * @param author new value of author
     */
    public void setAuthor( String author )
    {
        String oldAuthor = this.author;
        this.author = author;
        firePropertyChange( PROP_AUTHOR, oldAuthor, author );
    }

    /**
     * Get the value of area
     *
     * @return the value of area
     */
    public String getArea()
    {
        return area;
    }

    /**
     * Set the value of area
     *
     * @param area new value of area
     */
    public void setArea( String area )
    {
        System.out.printf( "area \"%s\" -> \"%s\"\n", this.area, area );

        String oldArea = this.area;
        this.area = area;
        firePropertyChange( PROP_AREA, oldArea, area );
    }

    /**
     * Get the value of title
     *
     * @return the value of title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Set the value of title
     *
     * @param title new value of title
     */
    public void setTitle( String title )
    {
        String oldTitle = this.title;
        this.title = title;
        firePropertyChange( PROP_TITLE, oldTitle, title );
    }

    public Map<String, Berth> getBerths()
    {
        return berths;
    }

    public Berth removeBerth( String id )
    {
        Berth b = berths.remove( id );
        if( b != null ) {
            b.removePropertyChangeListener( this );
            firePropertyChange( PROP_BERTHS, id, null );
        }
        return b;
    }

    public Collection<String> getBerthIds()
    {
        return berths.keySet();
    }

    public Berth newBerth( String id )
    {
        return newBerth( id, 0, 0 );
    }

    public Berth newBerth( String id, int x, int y )
    {
        firePropertyChange( PROP_BERTHS, null, id );
        int y1 = y;
        while( present( x, y1 ) ) {
            y1++;
        }
        return new Berth( id, x, y, this );
    }

    public Berth getBerth( String id )
    {
        return berths.get( id );
    }

    public Berth getOrCreateBerth( String id )
    {
        return berths.computeIfAbsent( id, this::newBerth );
    }

    public Berth getOrCreateBerth( String id, int x, int y )
    {
        return berths.computeIfAbsent( id, k -> newBerth( id, x, y ) );
    }

    public void clear()
    {
        Iterator<Berth> it = berths.values().
                iterator();
        while( it.hasNext() ) {
            it.next().
                    removePropertyChangeListener( this );
            it.remove();
        }
    }

    public void forEach( BiConsumer<? super String, ? super Berth> action )
    {
        berths.forEach( action );
    }

    public Stream<Berth> streamBerths()
    {
        return berths.values().
                stream();
    }

    public boolean present( int x, int y )
    {
        return berths.values().
                stream().
                anyMatch( b -> b.getX() == x && b.getY() == y );
    }

    public Berth getBerthAt( int x, int y )
    {
        return berths.values().
                stream().
                filter( b -> b.getX() == x && b.getY() == y ).
                findAny().
                orElse( null );
    }

    public void addAll( Collection<Berth> c )
    {
        c.forEach( b -> {
            berths.put( b.getId(), b );
            b.addPropertyChangeListener( this );
        } );
        // Dummy notification to notify listeners we have changed
        firePropertyChange( PROP_BERTHS, null, "" );
    }

    public JsonObject toJson()
    {
        JsonArrayBuilder bb = Json.createArrayBuilder();
        forEach( ( i, b ) -> bb.add( Json.createObjectBuilder().
                add( "berth", b.getId() ).
                add( "x", b.getX() ).
                add( "y", b.getY() ).
                add( "next", b.getOutBerths().
                     stream().
                     map( Berth::getId ).
                     collect( Collectors.joining( "," ) ) )
        ) );

        return Json.createObjectBuilder().
                add( "area", Objects.toString( area, "" ) ).
                add( "title", Objects.toString( title, "" ) ).
                add( "author", Objects.toString( author, "" ) ).
                add( "version", Objects.toString( version, "" ) ).
                add( "notes", Objects.toString( notes, "" ) ).
                add( "berths", bb ).
                build();
    }

    public void save()
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
                w.write( JsonUtils.toString.apply( toJson() ) );
                w.flush();
            }

            Project.INSTANCE.setChanged( false );
            MainFrame.setStatus( "Saved " + file );
        }
        catch( IOException ex ) {
            ex.printStackTrace();
        }
    }

    public void open( File file )
    {
        try( Reader r = new FileReader( file ) ) {

            try( JsonReader jr = Json.createReader( r ) ) {
                JsonObject o = (JsonObject) jr.read();
                clear();
                setFile( file );
                setArea( o.getString( "area" ) );
                setAuthor( o.getString( "author" ) );
                setNotes( o.getString( "notes" ) );
                setTitle( o.getString( "title" ) );
                setVersion( o.getString( "version" ) );

                // Read in the berths
                Map<String, Berth> newBerths = o.getJsonArray( "berths" ).
                        stream().
                        map( Functions.castTo( JsonObject.class ) ).
                        map( bo -> new Berth( bo.getString( "berth" ), bo.getInt( "x" ), bo.getInt( "y" ) ) ).
                        collect( Collectors.toMap( Berth::getId, Function.identity() ) );

                // Join them to each other
                o.getJsonArray( "berths" ).
                        stream().
                        map( Functions.castTo( JsonObject.class ) ).
                        filter( bo -> !"".equals( bo.getString( "next" ) ) ).
                        forEach( bo -> {
                            Berth b = newBerths.get( bo.getString( "berth" ) );
                            String next = bo.getString( "next" );
                            for( String id: next.split( "," ) ) {
                                b.join( newBerths.get( id ) );
                            }
                        } );

                // Buld add
                addAll( newBerths.values() );
            }
        }
        catch( IOException ex ) {
            ex.printStackTrace();
        }
    }
}
