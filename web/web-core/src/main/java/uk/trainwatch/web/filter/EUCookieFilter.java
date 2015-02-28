/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles EU Cookie Compliance by presenting the notice on the first visit to the site.
 * <p>
 * We also represent it after 28 days as thats the expiry of the cookie.
 * <p>
 * <p>
 * @author Peter T Mount
 */
@WebFilter( filterName = "EUCookieFilter", urlPatterns = "/*" )
public class EUCookieFilter
        extends AbstractFilter
{

    private static final String COOKIE_NAME = "euCookie";
    private static final String COOKIE_VALUE = "accepted";
    /**
     * Cookie lasts for 28 days (4 weeks)
     */
    private static final int COOKIE_MAX_AGE = 28 * 86400;

    @Override
    protected void doFilter( HttpServletRequest request, HttpServletResponse response, FilterChain chain )
            throws IOException,
                   ServletException
    {
        boolean hasEUCookie = false;

        Cookie[] cookies = request.getCookies();
        if( cookies != null )
        {
            for( Cookie cookie : cookies )
            {
                hasEUCookie |= COOKIE_NAME.equals( cookie.getName() );
            }
        }

        if( !hasEUCookie )
        {
            Cookie cookie = new Cookie( COOKIE_NAME, COOKIE_VALUE );
            cookie.setMaxAge( COOKIE_MAX_AGE );
            response.addCookie( cookie );
        }

        request.setAttribute( COOKIE_NAME, !hasEUCookie );

        chain.doFilter( request, response );
    }

}
