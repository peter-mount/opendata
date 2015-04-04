/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.cms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@WebServlet(name = "StaticContentServlet", urlPatterns = "/staticContent/*")
public class StaticContentServlet
        extends AbstractStaticServlet
{

    private static final String ARTICLE_START = "<article id=\"er-article-body\">";
    private static final String ARTICLE_END = "</article>";
    private static final int ARTICLE_END_LENGTH = ARTICLE_END.length();

    private final Pattern titlePattern = Pattern.compile( "<title>(.+)</title>" );

    @Override
    protected void doGet( ApplicationRequest request, final String path )
            throws ServletException,
                   IOException
    {
        File f = new File( baseDirectory, path.substring( 0, 1 ) + "/" + path + "/index.shtml" );
        if( f.exists() && f.isFile() && f.canRead() ) {
            Map<String, Object> req = request.getRequestScope();
            String page = Files.lines( f.toPath() ).collect( Collectors.joining( "\n" ) );

            Matcher m = titlePattern.matcher( page );
            req.put( "pageTitle", m.matches() ? m.group( 1 ) : f.getParentFile().getName() );

            int i = page.indexOf( ARTICLE_START );
            int j = page.indexOf( ARTICLE_END );
            if( i > 0 ) {
                page = page.substring( i, j + ARTICLE_END_LENGTH );
            }
            req.put( "page", page );

            request.renderTile( "cms.page" );
        }
        else {
            request.sendError( HttpServletResponse.SC_NOT_FOUND, path );
        }
    }

}
