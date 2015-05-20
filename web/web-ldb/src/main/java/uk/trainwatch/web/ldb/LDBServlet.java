/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import javax.servlet.annotation.WebServlet;

/**
 * Performance Home
 * <p>
 * @author Peter T Mount
 */
@WebServlet( name = "LDBServlet", urlPatterns = "/ldb/*" )
public class LDBServlet
        extends AbstractLDBViewServlet
{

    @Override
    protected String getServletPrefix()
    {
        return "/ldb/";
    }

    @Override
    protected String getRenderTile()
    {
        return "ldb.info";
    }

}
