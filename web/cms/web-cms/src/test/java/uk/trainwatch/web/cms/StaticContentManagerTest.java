/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.cms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author peter
 */
public class StaticContentManagerTest
{

    private static final String PREFIX = "PI/";

    @BeforeClass
    public static void setUpClass()
    {
        Properties p = new Properties();
        p.setProperty( "cms.prefix", PREFIX );
        StaticContentManager.INSTANCE.p = p;
    }

    private static String readPage( String name )
            throws IOException
    {
        try( BufferedReader r = new BufferedReader( new InputStreamReader( StaticContentManagerTest.class.getResourceAsStream( name ) ) ) ) {
            return r.lines().collect( Collectors.joining( "\n" ) );
        }
    }

    /**
     * Test that we strip out the cms prefix correctly from links
     * @throws Exception 
     */
    @Test
    public void processPage()
            throws Exception
    {
        String page = readPage( "test1.html" );

        Map<String, Object> req = new HashMap<>();
        StaticContentManager.INSTANCE.processPage( PREFIX, page, req );

        String content = (String) req.get( StaticContentManager.PAGE );
        assertNotNull( content );

        // Main reason for this test, when prefix is /PI it's not removing the prefix from url's
        assertFalse( content.contains( "href=\"/PI/About" ) );
    }

}
