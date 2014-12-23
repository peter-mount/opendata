/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import uk.trainwatch.nrod.signalmapeditor.Project;
import uk.trainwatch.util.Functions;

/**
 * A signal map
 * <p>
 * @author peter
 */
public class SignalMap
        extends MapObject
{

    public static final String PROP_FILE = "map_file";
    public static final String PROP_TITLE = "map_title";
    public static final String PROP_AREA = "map_area";
    public static final String PROP_AUTHOR = "map_author";
    public static final String PROP_VERSION = "map_version";
    public static final String PROP_NODES = "map_node";
    public static final String PROP_NOTES = "map_notes";
    public static final String PROP_DIMENSION = "map_dimension";

    private final Map<String, Node> nodes = new ConcurrentHashMap<>();

    private File file;
    private String title;
    private String area;
    private String author;
    private String version;
    private String notes;

    private volatile Dimension dimension;

    @Override
    public void propertyChange( PropertyChangeEvent evt )
    {
        // If a coordinate changes then reset dimension
        String n = evt.getPropertyName();
        if( Node.PROP_DIMENSION.equals( evt.getPropertyName() ) ) {
            Dimension d = (Dimension) evt.getNewValue();
            if( d.getWidth() > dimension.getWidth() || d.getHeight() > dimension.getHeight() ) {
                resetDimension();
            }
        }
        super.propertyChange( evt );
    }

    public Dimension getDimension()
    {
        if( dimension == null ) {
            resetDimension();
        }
        return dimension;
    }

    public void resetDimension()
    {
        resetDimension( false );
    }

    public void resetDimension( boolean forceResize )
    {
        Dimension oldDimension = dimension;
        dimension = Project.INSTANCE.getMap().
                streamBerths().
                parallel().
                map( Node::getDimension ).
                reduce( // Use the old as the basis - prevents us from reducing in size. forceResize allows reduction in size
                        oldDimension == null || forceResize ? new Dimension() : oldDimension,
                        // Only replace the accumulator if we know b is outside a on one dimension
                        ( a, b ) -> a.getWidth() >= b.getWidth() && a.getHeight() >= b.getHeight()
                                    ? a
                                    : new Dimension( (int) Math.max( a.getWidth(), b.getWidth() ), (int) Math.max( a.getHeight(), b.getHeight() ) ),
                        // Combine to a new Dimension that will fit both a & b
                        ( a, b ) -> new Dimension( (int) Math.max( a.getWidth(), b.getWidth() ), (int) Math.max( a.getHeight(), b.getHeight() ) )
                );
        firePropertyChange( PROP_DIMENSION, oldDimension, dimension );
    }

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

    public Node removeNode( String id )
    {
        Node b = nodes.remove( id );
        if( b != null ) {
            b.removePropertyChangeListener( this );
            firePropertyChange( PROP_NODES, id, null );
        }
        return b;
    }

    public Collection<String> getBerthIds()
    {
        return nodes.keySet();
    }

    public Berth newBerth( String id )
    {
        return newBerth( id, 0, 0 );
    }

    public Berth newBerth( String id, int x, int y )
    {
        firePropertyChange( PROP_NODES, null, id );
        int y1 = y;
        while( present( x, y1 ) ) {
            y1++;
        }
        return new Berth( id, x, y, this );
    }

    public Berth getBerth( String id )
    {
        Node node = nodes.get( id );
        return node instanceof Berth ? (Berth) node : null;
    }

    public Berth getOrCreateBerth( String id )
    {
        Node node = nodes.computeIfAbsent( id, this::newBerth );
        return node instanceof Berth ? (Berth) node : null;
    }

    public Berth getOrCreateBerth( String id, int x, int y )
    {
        Node node = nodes.computeIfAbsent( id, k -> newBerth( id, x, y ) );
        return node instanceof Berth ? (Berth) node : null;
    }

    public void clear()
    {
        Iterator<Node> it = nodes.values().
                iterator();
        while( it.hasNext() ) {
            it.next().
                    removePropertyChangeListener( this );
            it.remove();
        }
    }

    public void forEach( BiConsumer<? super String, ? super Node> action )
    {
        nodes.forEach( action );
    }

    public Stream<? super Node> streamNodes()
    {
        return nodes.values().
                stream();
    }

    public Stream<Berth> streamBerths()
    {
        return streamNodes().
                map( Functions.castTo( Berth.class ) ).
                filter( Objects::nonNull );
    }

    public Stream<? super LineNode> streamLines()
    {
        return streamNodes().
                map( Functions.castTo( LineNode.class ) ).
                filter( Objects::nonNull );
    }

    public boolean present( int x, int y )
    {
        return nodes.values().
                stream().
                anyMatch( b -> b.getX() == x && b.getY() == y );
    }

    public <T extends Node> T getNodeAt( int x, int y )
    {
        return (T) nodes.values().
                stream().
                filter( b -> b.getX() == x && b.getY() == y ).
                findAny().
                orElse( null );
    }

    public Berth getBerthAt( int x, int y )
    {
        return nodes.values().
                stream().
                map( Functions.castTo( Berth.class ) ).
                filter( Objects::nonNull ).
                filter( b -> b.getX() == x && b.getY() == y ).
                findAny().
                orElse( null );
    }

    public void addAll( Collection<Berth> c )
    {
        c.forEach( b -> {
            nodes.put( b.getId(), b );
            b.addPropertyChangeListener( this );
        } );
        // Dummy notification to notify listeners we have changed
        firePropertyChange( PROP_NODES, null, "" );
    }

    @Override
    public void accept( MapVisitor v )
    {
        v.visit( this );
    }

}
