/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.uktra.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple servlet which renders a specific tile.
 * <p>
 * To implement simply extend this class and annotate it with the required urlPatterns.
 * <p>
 * Also add initParams where each one's name is the pattern and the value the tile to render for that page.
 * <p>
 * @author Peter T Mount
 */
public abstract class AbstractHomeServlet
        extends AbstractServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        // Get the tile name from the init parameters based on the servlet path.
        //
        // Sanity check: if null then the initparam for that path is missing from the annotations above
        //
        String tile = getInitParameter( request.getServletPath() );
        if( tile == null )
        {
            request.sendError( HttpServletResponse.SC_NOT_FOUND );
        }
        else
        {
            request.renderTile( tile );
        }
    }

}
