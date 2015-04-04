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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 *
 * @author peter
 */
@WebServlet(name = "StaticImageServlet", urlPatterns = "/staticImage/*")
public class StaticImageServlet
        extends AbstractStaticServlet
{

    @Override
    protected void doGet( ApplicationRequest request, final String path )
            throws ServletException,
                   IOException
    {
        try {
            byte b[] = MessageDigest.getInstance( "MD5" ).digest( path.getBytes() );
            String p = Integer.toHexString( Byte.toUnsignedInt( b[0] ) );
            if( p.length() == 1 ) {
                p = '0' + p;
            }
            p = String.join( "/", "/images", p.substring( 0, 1 ), p, path );
            
            System.out.println(p);

            File f = new File( baseDirectory, p );
            if( f.exists() && f.isFile() && f.canRead() ) {
                request.getResponse().setContentLength( (int) f.length() );
                Files.copy( f.toPath(), request.getOutputStream() );
            }
            else {
                request.sendError( HttpServletResponse.SC_NOT_FOUND, path );
            }
        }
        catch( NoSuchAlgorithmException ex ) {
            Logger.getLogger( StaticImageServlet.class.getName() ).log( Level.SEVERE, null, ex );
        }
    }

}
