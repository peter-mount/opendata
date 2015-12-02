/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.cms.core;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.json.JsonObject;
import uk.trainwatch.io.message.WireMessage;
import uk.trainwatch.io.message.WireMessageBuilder;
import uk.trainwatch.io.message.WireMessageRegistry;
import uk.trainwatch.util.Streams;

/**
 *
 * @author peter
 */
public class Page
        implements CmsFile
{

    private static final Logger LOG = Logger.getLogger( Page.class.getName() );

    private final String name;
    private String title;
    private List<String> content;

    public static final Function<byte[], Page> read = b -> {
        try {
            WireMessage<Page> page = WireMessageRegistry.INSTANCE.readMessage( b );
            return page == null ? null : page.getContent();
        }
        catch( Exception ex ) {
            LOG.log( Level.SEVERE, null, ex );
            return null;
        }
    };
    
    public static final Function<Page, byte[]> write = page -> {
        try {
            return new WireMessageBuilder<>( PageMessageFormat.TYPE ).content( page ).build();
        }
        catch( Exception ex ) {
            LOG.log( Level.SEVERE, null, ex );
            return null;
        }
    };

    public Page( JsonObject o )
    {
        this.content = Collections.emptyList();
        this.name = this.title = o.getString( "page_title" );
        // These are included from the json but we don't use them now
//        o.getString( "page_id");
//        o.getString( "page_is_new"); // on delete
//        o.getString( "page_touched");
//        o.getString( "page_is_latest"); // on delete
//        o.getString( "schema");
//        o.getString( "table");
    }

    public Page( String name )
    {
        this.name = Objects.requireNonNull( name );
    }

    Page( String name, String title, List<String> content )
    {
        this.name = name;
        this.title = title;
        this.content = content;
    }

    @Override
    public Path toPath( String basePath )
    {
        return Paths.get( basePath, name.substring( 0, 1 ), name, "index.shtml" );
    }

    @Override
    public byte[] toBytes()
    {
        String s = "<!DOCTYPE html>\n<html><head><title>" + title + "</title></head><body>"
                   + lines().collect( Collectors.joining( "\n" ) )
                   + "</body></html>";

        return s.getBytes( Charset.forName( "UTF-8" ) );
    }

    public String getName()
    {
        return name;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public boolean isEmpty()
    {
        return content == null || content.isEmpty();
    }

    public List<String> getContent()
    {
        return content;
    }

    public void setContent( List<String> content )
    {
        this.content = content;
    }

    public Stream<String> lines()
    {
        return Streams.stream( content );
    }
}
