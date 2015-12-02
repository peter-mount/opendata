/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.cms.core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.JsonObject;
import uk.trainwatch.io.message.WireMessage;
import uk.trainwatch.io.message.WireMessageBuilder;
import uk.trainwatch.io.message.WireMessageRegistry;
import uk.trainwatch.web.cms.StaticContentManager;

/**
 *
 * @author peter
 */
public class Image
        implements CmsFile
{

    private static final Logger LOG = Logger.getLogger( Image.class.getName() );
    private final String name;
    private byte[] content;

    public static final Function<byte[], Image> read = b -> {
        try {
            WireMessage<Image> image = WireMessageRegistry.INSTANCE.readMessage( b );
            return image == null ? null : image.getContent();
        }
        catch( Exception ex ) {
            LOG.log( Level.SEVERE, null, ex );
            return null;
        }
    };

    public static final Function<Image, byte[]> write = image -> {
        try {
            return new WireMessageBuilder<>( ImageMessageFormat.TYPE ).content( image ).build();
        }
        catch( Exception ex ) {
            LOG.log( Level.SEVERE, null, ex );
            return null;
        }
    };

    public Image( JsonObject o )
    {
        this.name = o.getString( "filename" );
    }

    Image( String name, byte[] content )
    {
        this.name = name;
        this.content = content;
    }

    @Override
    public Path toPath( String basePath )
    {
        byte b[] = StaticContentManager.INSTANCE.md5( name );
        String p = Integer.toHexString( Byte.toUnsignedInt( b[0] ) );
        if( p.length() == 1 ) {
            p = '0' + p;
        }
        return Paths.get( basePath, "images", p.substring( 0, 1 ), p, name );
    }

    @Override
    public byte[] toBytes()
    {
        return content;
    }

    public String getName()
    {
        return name;
    }

    public byte[] getContent()
    {
        return content;
    }

    public void setContent( byte[] content )
    {
        this.content = content;
    }

}
