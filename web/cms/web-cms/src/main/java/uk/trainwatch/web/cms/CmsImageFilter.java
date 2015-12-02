/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.cms;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Filter which intercepts requests to static images.
 * <p>
 * Originally this was done via apache but we now do this so we can include content from within the application
 * <p>
 * @author peter
 */
@WebFilter(filterName = "CmsImageFilter", urlPatterns = "/*")
public class CmsImageFilter
        implements Filter
{

    private static final String FILE = "/File:";
    private final Pattern pattern = Pattern.compile( "^/images/([0-9a-fA-F]+)/([0-9a-fA-F]+)/(.*)" );

    @Override
    public void init( FilterConfig fc )
            throws ServletException
    {
    }

    @Override
    public void doFilter( ServletRequest req, ServletResponse resp, FilterChain chain )
            throws IOException,
                   ServletException
    {
        HttpServletRequest request = (HttpServletRequest) req;
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        if( contextPath != null && requestURI.startsWith( contextPath ) ) {
            requestURI = requestURI.substring( contextPath.length() );
        }

        if( requestURI.startsWith( "/images/" ) ) {
            Matcher m = pattern.matcher( requestURI );
            if( m.matches() ) {
                request.getRequestDispatcher( "/staticImage/" + m.group( 3 ) ).forward( req, resp );
                return;
            }
        }

        chain.doFilter( req, resp );
    }

    @Override
    public void destroy()
    {
    }

}
