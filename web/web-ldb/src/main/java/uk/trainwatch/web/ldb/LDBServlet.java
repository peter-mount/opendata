/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import uk.trainwatch.web.servlet.AbstractServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQL;

/**
 * Performance Home
 * <p>
 * @author Peter T Mount
 */
@WebServlet( name = "LDBServlet", urlPatterns = "/ldb/*" )
public class LDBServlet
        extends AbstractServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
            IOException
    {
        String crs = request.getPathInfo().substring( 1 ).toUpperCase();

        TrainLocation loc = TrainLocationFactory.INSTANCE.getTrainLocationByCrs( crs );
        if( loc == null )
        {
            // See if they have used an alternate code
            loc = TrainLocationFactory.INSTANCE.resolveTrainLocation( crs );

            if( loc == null )
            {
                request.sendError( HttpServletResponse.SC_NOT_FOUND );
            } else
            {
                // Redirect to the correct page
                request.getResponse().
                        sendRedirect( "/ldb/" + loc.getCrs() );
            }
        } else
        {
            show( request, loc );
        }
    }

    private void show( ApplicationRequest request, TrainLocation loc )
            throws ServletException,
            IOException
    {
        Map<String, Object> req = request.getRequestScope();
        req.put( "location", loc );
        req.put( "pageTitle", loc.getLocation() );
        try
        {
            LocalTime time = TimeUtils.getLondonDateTime().toLocalTime();
            String otherTime = request.getParam().get( "t" );
            if( otherTime != null )
            {
                time = LocalTime.parse( otherTime );
            }

            getDepartures( req, loc, time );

            request.renderTile( "ldb.info" );
        } catch( SQLException ex )
        {
            log( "show " + loc, ex );
            request.sendError( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        }
    }

    /**
     * Timetabled departures
     * <p>
     * @param req
     * @param loc <p>
     * @throws SQLException
     */
    private void getDepartures( Map<String, Object> req, TrainLocation loc, LocalTime time )
            throws SQLException
    {
        // FIXME allow to be shown whilst at the platform. However this might cause issues
        // with a train sitting at the platform but not on the boards?
        LocalTime timeAfter = time.minusMinutes( 2 );
        LocalTime timeBefore = timeAfter.plusHours( 1 );
        boolean midnight = time.getHour() > 20;
        req.put( "midnight", midnight );

        try( Connection con = LDBContextListener.getDataSource().getConnection() )
        {
            // must have a working departure
            // order by first of actual departure, estimated then working departure
            List<LDB> departures;
            try( PreparedStatement ps = SQL.prepare( con,
                    "SELECT *,"
                    + "   s.toc AS toc,"
                    + "   d.name AS destination"
                    //+ " COALESCE(fe.dep,fe.etdep,fe.wtd,fe.arr,fe.etarr,fe.wta) as time,"
                    + " FROM darwin.forecast f"
                    + " INNER JOIN darwin.schedule s ON f.schedule=s.id"
                    + " INNER JOIN darwin.location d ON s.dest=d.tpl"
                    + " INNER JOIN darwin.forecast_entry fe ON f.id=fe.fid"
                    + " INNER JOIN darwin.location l ON fe.tpl=l.tpl"
                    + " INNER JOIN darwin.crs c ON l.crs=c.id"
                    // Only for this station
                    + " WHERE c.crs=?"
                    // Flagged for display
                    + " AND fe.ldb=TRUE"
                    // all non-passes required so we can use Terminates/Starts here etc
                    + " AND fe.wtp IS NULL",
                    // Order by the first valid time
                    //+ " ORDER BY COALESCE(fe.dep,fe.etdep,fe.wtd)",
                    loc.getCrs() ) )
            {
                departures = SQL.stream( ps, LDB.fromSQL ).
                        // Filter only public entries
                        filter( LDB::isPublic ).
                        // Filter out those out of range, accounting for midnight
                        filter( l ->
                                {
                                    boolean r = l.getTime().isAfter( timeAfter );
                                    boolean b = l.getTime().isBefore( timeBefore );

                                    return midnight ? r | b : r & b;
                        } ).
                        // Sort to Darwin rules, accounts for midnight
                        sorted( ( a, b ) -> TimeUtils.compareLocalTimeDarwin.compare( a.getTime(), b.getTime() ) ).
                        collect( Collectors.toList() );
            }

            req.put( "departures", departures );
        }
    }

}
