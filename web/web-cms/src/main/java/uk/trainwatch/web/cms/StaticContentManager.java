/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.cms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.web.util.CacheControl;
import uk.trainwatch.web.util.ImageUtils;

/**
 * Common code used by both the servlets and tags
 * <p>
 * @author peter
 */
public enum StaticContentManager
{

    INSTANCE;

    private static final String FILE = "File:";
    private static final int FILE_LENGTH = FILE.length();

    private static final String ARTICLE_START = "<article id=\"er-article-body\">";
    private static final String ARTICLE_END = "</article>";
    private static final int ARTICLE_END_LENGTH = ARTICLE_END.length();
    /**
     * Request attribute holding the page content
     */
    public static final String PAGE = "page";
    /**
     * Request attribute holding the page title
     */
    public static final String PAGE_TITLE = "pageTitle";
    /**
     * Request attribute holding the File for the page.
     */
    public static final String PAGE_FILE = "pageFile";
    // FIXME remove this hardcoding
    protected final File baseDirectory = new File( "/var/www/uktra.in" );
    /**
     * Pattern to extract page title
     */
    private final Pattern titlePattern = Pattern.compile( "<title>(.+)</title>" );

    /**
     * Retrieve a static page from the cms
     * <p>
     * @param path Page name
     * @param req  Map to store the page
     * <p>
     * @return true if the page was found & rendered, false if not found
     * <p>
     * @throws IOException If the page could not be read
     */
    public boolean getPage( String path, Map<String, Object> req )
            throws IOException
    {
        File f = new File( baseDirectory, path.substring( 0, 1 ) + "/" + path + "/index.shtml" );
        if( f.exists() && f.isFile() && f.canRead() ) {
            String page = Files.lines( f.toPath() ).collect( Collectors.joining( "\n" ) );

            Matcher m = titlePattern.matcher( page );
            req.put( PAGE_TITLE, m.matches() ? m.group( 1 ) : f.getParentFile().getName() );

            int i = page.indexOf( ARTICLE_START );
            int j = page.indexOf( ARTICLE_END );
            if( i > 0 ) {
                page = page.substring( i, j + ARTICLE_END_LENGTH );
            }

            // Fix external links in the cms that points to it so we point to the real site
            page=page.replaceAll( "//((.+?trainwatch\\.im)|(.+?\\.uktra\\.in)|(uktra\\.in))/", "/");

            req.put( PAGE, page );

            req.put( PAGE_FILE, f );

            return true;
        }
        else {
            return false;
        }
    }

    public boolean getImage( String path, HttpServletResponse response )
            throws IOException
    {
        File f = new File( baseDirectory, getRealImagePath( path ) );
        if( f.exists() && f.isFile() && f.canRead() ) {
            ImageUtils.sendFile( f.toPath(), CacheControl.TWO_HOURS, response );
            return true;
        }
        else {
            response.sendError( HttpServletResponse.SC_NOT_FOUND, path );
            return false;
        }
    }

    /**
     * Get the real image path of an image.
     * <p>
     * This is based on MediaWiki's algorithm, given an image name it gets the MD5 of that name, takes the first 2 hex digits, and uses them
     * to form the path.
     * <p>
     * So "Apple.jpg" has the first 2 digits 2b so the path is "/images/2/2b/Apple.jpg"
     * <p>
     * The reasoning behind this is to prevent directories becoming large as that does have a performance hit. Using the MD5 just spreads images across
     * directories
     * more evenly than say using the first characters of the filename.
     * <p>
     * @param s File name.
     * <p>
     * @return image path relative to the site
     */
    public String getRealImagePath( String s )
    {
        String name = s == null ? "" : s;
        if( name.startsWith( FILE ) ) {
            name = name.substring( FILE_LENGTH );
        }
        byte b[] = md5( name );
        String p = Integer.toHexString( Byte.toUnsignedInt( b[0] ) );
        if( p.length() == 1 ) {
            p = '0' + p;
        }
        return String.join( "/", "/images", p.substring( 0, 1 ), p, name );
    }

    /**
     * Get the MD5 of a string
     * <p>
     * @param s
     *          <p>
     * @return
     */
    public byte[] md5( String s )
    {
        try {
            return MessageDigest.getInstance( "MD5" ).digest( s.getBytes() );
        }
        catch( NoSuchAlgorithmException ex ) {
            Logger.getLogger( StaticContentManager.class.getName() ).log( Level.SEVERE, null, ex );
            throw new IllegalStateException( ex );
        }
    }
}
