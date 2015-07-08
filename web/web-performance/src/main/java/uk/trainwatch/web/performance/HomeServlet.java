/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.performance;

import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

/**
 * Performance Home
 * <p>
 * @author Peter T Mount
 */
@WebServlet(name = "PerfHome", urlPatterns = {    "/performance","/performance/"
})
public class HomeServlet
        extends AbstractServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.redirect( "/Performance" );
    }

}
