/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.svgmap.tpnm;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.gis.svg.SvgBounds;

/**
 *
 * @author peter
 */
@WebServlet(name = "TPNMServlet", urlPatterns = "/tpnm/map/refresh")
public class TPNMServlet
        extends HttpServlet
{

    @Inject
    private TPNMWriter writer;

    @Override
    protected void doPost( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException,
                   IOException
    {
        writer.accept(
                new SvgBounds(
                        Double.parseDouble( req.getParameter( "s" ) ),
                        Double.parseDouble( req.getParameter( "x" ) ),
                        Double.parseDouble( req.getParameter( "y" ) ),
                        Integer.parseInt( req.getParameter( "w" ) ),
                        Integer.parseInt( req.getParameter( "h" ) )
                ),
                resp.getWriter() );
    }

}
