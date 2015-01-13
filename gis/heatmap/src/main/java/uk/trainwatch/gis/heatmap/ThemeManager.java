package uk.trainwatch.gis.heatmap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public enum ThemeManager
{

    INSTANCE;

    private final Map<String, BufferedImage> dotsList = new ConcurrentHashMap<>();
    private final Map<String, BufferedImage> schemes = new ConcurrentHashMap<>();

    private ThemeManager()
    {
        try {
            init( "dots/dot.properties", dotsList );
            init( "schemes/scheme.properties", schemes );
        }
        catch( IOException |
               UncheckedIOException ex ) {
            Logger.getLogger( ThemeManager.class.getName() ).
                    log( Level.SEVERE, null, ex );
        }

    }

    private void init( String fileName, Map<String, BufferedImage> map )
            throws IOException
    {
        Properties props = new Properties();
        props.load( getClass().
                getResourceAsStream( fileName ) );
        props.forEach( ( n, p ) -> map.computeIfAbsent( (String) n, k -> {
            try {
                return ImageIO.read( getClass().
                        getResourceAsStream( (String) p ) );
            }
            catch( IOException ex ) {
                throw new UncheckedIOException( ex );
            }
        } ) );
    }

    public Collection<String> getDots()
    {
        return dotsList.keySet();
    }

    public BufferedImage getDot( int zoom )
    {
        return dotsList.get( "dot" + zoom + ".png" );
    }

    public Collection<String> getSchemes()
    {
        return schemes.keySet();
    }

    public void forEachScheme( Consumer<String> c )
    {
        getSchemes().
                forEach( c );
    }

    public BufferedImage getScheme( String schemeName )
            throws Exception
    {
        if( !schemes.containsKey( schemeName + ".png" ) ) {
            throw new Exception( "Color scheme '" + schemeName + " could not be found" );
        }
        return schemes.get( schemeName + ".png" );
    }

}
