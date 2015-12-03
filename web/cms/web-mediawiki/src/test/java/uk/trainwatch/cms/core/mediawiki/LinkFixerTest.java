/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.cms.core.mediawiki;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.trainwatch.cms.core.Page;

/**
 *
 * @author peter
 */
public class LinkFixerTest
{

    private String badlink;
    private String document;
    private Function<String, Page> pipeline;

    private static String readPage( String name )
            throws IOException
    {
        try( BufferedReader r = new BufferedReader( new InputStreamReader( LinkFixerTest.class.getResourceAsStream( name ) ) ) ) {
            return r.lines().collect( Collectors.joining( "\n" ) );
        }
    }

    @Before
    public void setUp()
            throws IOException
    {
        badlink = readPage( "badlink.html" );
        document = readPage( "document.html" );

        pipeline = s -> {
            Page p = new Page( "test" );
            p.setContent( new ArrayList<>( Arrays.asList( s.split( "\n" ) ) ) );
            return p;
        };

        pipeline = pipeline.andThen( new TitleExtractor() )
                .andThen( new ContentExtractor() )
                .andThen( new CommentStripper() )
                .andThen( new ArticleFormat() )
                .andThen( new LinkFixer() );

        // To enable debugging then add this which will dump the final result to the console
        pipeline = pipeline.andThen( page -> {
            page.getContent().forEach( System.out::println );
            return page;
        } );
    }

    /**
     * Test we strip out bad links.
     * <p>
     * Here we have a page linking to a non-existent page. In this instance we should not have
     * the anchor present
     * <p>
     * @throws Exception
     */
    @Test
    public void testStripBadLink()
            throws Exception
    {
        Page page = pipeline.apply( badlink );
        assertNotNull( page );

        // Fail if we have an edit or red link present
        page.getContent()
                .stream()
                .filter( s -> s.contains( "action=edit" ) || s.contains( "redlink=1" ) )
                .findAny()
                .ifPresent( Assert::fail );
    }

    /**
     * Test we strip out comments
     * <p>
     * Ensure we strip out all comments in a page
     * <p>
     * @throws Exception
     */
    @Test
    public void testStripComments()
            throws Exception
    {
        testStrip( badlink );
//        testStrip( document );
    }

    private void testStrip( String doc )
            throws Exception
    {
        Page page = pipeline.apply( doc );
        assertNotNull( page );

        // Fail if we have the start or end of a comment
        page.getContent()
                .stream()
                .filter( s -> s.contains( "<!--" ) || s.contains( "-->" ) )
                .findAny()
                .ifPresent( Assert::fail );
    }

}
