/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.uktra.filter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter which adds the LocalDateTime and derivatives to the request
 * <p>
 * @author Peter T Mount
 */
@WebFilter( filterName = "DateFilter", urlPatterns = "/*" )
public class DateFilter
        extends AbstractFilter
{

    @Override
    protected void doFilter( HttpServletRequest request, HttpServletResponse response, FilterChain chain )
            throws IOException,
                   ServletException
    {
        LocalDateTime now = LocalDateTime.now();
        LocalDate date = now.toLocalDate();

        // LocalDateTime of the request
        request.setAttribute( "now", now );

        // LocalDate of the request
        request.setAttribute( "date", date );

        // The year, used in page footers
        request.setAttribute( "year", date.get( ChronoField.YEAR ) );

        // The page generated time, yyyy-mm-dd hh:mm:ss
        request.setAttribute( "pageGenerated",
                              now.truncatedTo( ChronoUnit.SECONDS ).
                              toString().
                              replace( 'T', ' ' ) );

        chain.doFilter( request, response );
    }

}
