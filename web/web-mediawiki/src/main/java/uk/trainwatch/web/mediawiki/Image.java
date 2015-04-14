/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.mediawiki;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.json.JsonObject;
import uk.trainwatch.web.cms.StaticContentManager;

/**
 *
 * @author peter
 */
public class Image
        implements CmsFile
{

    private final String name;
    private byte[] content;

    public Image( JsonObject o )
    {
        this.name = o.getString( "filename" );
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
