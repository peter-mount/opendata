/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.performance;

import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nrod.rtppm.sql.OperatorDailyPerformance;
import uk.trainwatch.nrod.rtppm.sql.OperatorManager;
import uk.trainwatch.nrod.rtppm.sql.PerformanceManager;

/**
 * Performance Home
 * <p>
 * @author Peter T Mount
 */
@WebServlet( name = "PerfHome", urlPatterns = "/performance/" )
public class HomeServlet
        extends AbstractServlet
{

    @Inject
    private OperatorManager operatorManager;

    @Inject
    private PerformanceManager performanceManager;

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        try
        {
            Map<String, Object> req = request.getRequestScope();

            req.put( "operators", operatorManager.getOperatorMap() );

            Collection<OperatorDailyPerformance> performance = performanceManager.getCurrentPerformance();
            req.put( "performance", performance );
            req.put( "perfdate", LocalDate.now( ZoneId.of( "Europe/London" ) ) );

            request.renderTile( "performance.home" );
        } catch( NoSuchElementException |
                 SQLException ex )
        {
            Logger.getLogger( HomeServlet.class.getName() ).
                    log( Level.SEVERE, null, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage() );
        }
    }

}
