/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.web.servlet.ApplicationRequest;

/**
 * Performance Home
 * <p>
 * @author Peter T Mount
 */
@WebServlet( name = "MobileLDBServlet", urlPatterns = "/mldb/*" )
public class MobileLDBServlet
        extends AbstractLDBViewServlet
{

    @Override
    protected String getServletPrefix()
    {
        return "/mldb/";
    }

    @Override
    protected String getRenderTile()
    {
        return "ldb.mobile";
    }

    @Override
    protected void show( ApplicationRequest request, TrainLocation loc )
            throws ServletException,
            IOException
    {
        request.getRequestScope().put( "location", loc);
        request.renderTile( getRenderTile() );
    }

}
