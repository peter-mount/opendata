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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implements HTTPS Strict Transport Security
 * <p>
 * Note: this is here and not in core as it's usually only valid on secure sites, and those using auth are always secure whilst some based around core are not
 * (yet)
 * <p>
 * For more information: <a href="https://www.chromium.org/hsts">hsts</a>.
 * <p>
 * @author peter
 */
@WebFilter(filterName = "HSTSFilter", urlPatterns = "/*")
public class HSTSFilter
        extends AbstractFilter
{

    // Set expiry to 18 weeks, the minimum if we add ourselves to chromium preload list later
    private static final String VALUE = "max-age=10886400; includeSubDomains";
    private static final String NAME = "Strict-Transport-Security";

    @Override
    protected void doFilter( HttpServletRequest request, HttpServletResponse response, FilterChain chain )
            throws IOException,
                   ServletException
    {
        // Force STS to be on, expiry set to 18 weeks
        response.addHeader( NAME, VALUE );

        chain.doFilter( request, response );
    }

}
