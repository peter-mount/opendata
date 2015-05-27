/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.nre.darwin.model.ctt.referenceschema.LocationRef;
import uk.trainwatch.nre.darwin.model.ppt.schedules.OPOR;
import uk.trainwatch.nre.darwin.model.ppt.schedules.OR;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;
import uk.trainwatch.nre.darwin.model.util.WorkDeparture;
import uk.trainwatch.nre.darwin.parser.DarwinJaxbContext;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.web.servlet.ApplicationRequest;
import uk.trainwatch.web.train.Train;
import uk.trainwatch.web.train.TrainMovement;
import uk.trainwatch.web.train.servlet.TrainContextListener;

/**
 *
 * @author peter
 */
@WebServlet(name = "TrainSearchServlet", urlPatterns = "/train/search")
public class TrainSearchServlet
        extends AbstractSearchServlet
{

    @Override
    protected void doGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        Map<String, Object> req = request.getRequestScope();
            req.put( "msg", "You must use the form" );
        showHome( request );
    }

    @Override
    protected void doPost( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        Map<String, Object> req = request.getRequestScope();
        Map<String, String> param = request.getParam();

        String station = param.get( "station" );
        if( station == null || station.isEmpty() ) {
            req.put( "msg", "You must select a station" );
            showHome( request );
            return;
        }
        
        try {
            LocalDate date;
            String dateStr = param.get( "date" );
            if( dateStr == null || dateStr.isEmpty() ) {
                date = LocalDate.now();
            }
            else {
                date = LocalDate.parse( dateStr );
            }

            LocalTime time;
            String timeStr = param.get( "time" );
            if( timeStr == null || timeStr.isEmpty() ) {
                time = LocalTime.now();
            }
            else {
                time = LocalTime.parse( timeStr );
            }

            LocalDateTime dt = LocalDateTime.of( date, time );
            search( request, station, dt );
        }
        catch( DateTimeParseException ex ) {
            req.put( "msg", "Your fields are invalid, please check and try again" );
            showHome( request );
        }
    }

    protected void search( ApplicationRequest request, String station, LocalDateTime date )
    {
        Map<String, Object> req = request.getRequestScope();
        TrainLocation loc = TrainLocationFactory.INSTANCE.resolveTrainLocation( station );
        if( loc == null ) {
            req.put( "msg", "The station you have requested is unknown" );
            showHome( request );
        }
        else {
            // Get all trains at this station within 30 minutes of date
            try {
                Collection<Train> trains = search( station, date );

                if( trains.isEmpty() ) {
                    req.put( "msg", "No trains were found" );
                    showHome( request );
                }
                else {
                    req.put( "station", loc );
                    req.put( "searchDate", date );
                    req.put( "trains", trains );

                    request.renderTile( "train.results" );
                }
            }
            catch( SQLException ex ) {
                log( "Search " + loc + " " + date, ex );
                req.put( "msg", "Something unexpected just went wrong, please try again later." );
                showHome( request );
            }
        }
    }

    public Collection<Train> search( String crs, LocalDateTime dt )
            throws SQLException
    {
        // Map CRS to TIPLOC(s)
        Collection<TrainLocation> locs = null;
        if( crs != null && !crs.isEmpty() ) {
            locs = DarwinReferenceManager.INSTANCE.getLocationRefsFromCrs( crs );
        }

        if( locs != null && !locs.isEmpty() ) {
            List<Object> args = new ArrayList<>();
            // Form the SQL allowing for multiple tiplocs per crs
            StringBuilder sb = new StringBuilder().
                    append( "SELECT f.xml FROM darwin.forecast f"
                            + " INNER JOIN darwin.forecast_entry fe ON f.id=fe.fid"
                            + " INNER JOIN darwin.tiploc t ON fe.tpl=t.id"
                            + " WHERE t.tpl IN (" );
            locs.forEach( l -> {
                args.add( l.getTiploc() );
                sb.append( "?," );
            } );
            sb.setLength( sb.length() - 1 );
            String sql = sb.append( ") and f.ssd=?" ).toString();
            args.add( dt.toLocalDate().toString() );

            LocalTime time = dt.toLocalTime();
            try( Connection con = TrainContextListener.getDataSource().getConnection() ) {
                try( PreparedStatement ps = SQL.prepare( con, sql, args.toArray() ) ) {
                    log( ps.toString() );
                    return SQL.stream( ps, SQL.STRING_LOOKUP ).
                            map( DarwinJaxbContext.fromXML ).
                            filter( Pport::isSetUR ).
                            filter( p -> {
                                if( p.getUR().getSchedule().isEmpty() ) {
                                    return false;
                                }
                                Object or = p.getUR().getSchedule().get( 0 ).
                                getOROrOPOROrIP().
                                stream().
                                filter( o -> o instanceof OR || o instanceof OPOR ).
                                findAny().
                                orElse( null );

                                if( or != null ) {
                                    LocalTime t = TimeUtils.getLocalTime( or instanceof OR ? ((OR) or).getPtd() : ((WorkDeparture) or).getWtd() );
                                    if( t != null ) {
                                        return Math.abs( Duration.between( time, t ).getSeconds() ) <= 1800;
                                    }
                                }
                                return false;
                            } ).
                            map( Train::create ).
                            sorted( ( a, b ) -> TrainMovement.compare( a.getMovement( crs ), b.getMovement( crs ) ) ).
                            collect( Collectors.toList() );
                }
            }
        }
        return Collections.emptyList();
    }

}
