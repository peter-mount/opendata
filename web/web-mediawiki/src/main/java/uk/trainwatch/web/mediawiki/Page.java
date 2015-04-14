/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.mediawiki;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.json.JsonObject;

/**
 *
 * @author peter
 */
public class Page
        implements CmsFile
{

    private final String name;
    private String title;
    private List<String> content;

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
        return content.stream();
    }
}
