/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.cms;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 * Filter which intercepts requests to static content.
 * <p>
 * Originally this was done via apache but we now do this so we can include content from within the application
 * <p>
 * @author peter
 */
@WebFilter(filterName = "CmsPageFilter", urlPatterns = "/*")
public class CmsPageFilter
        implements Filter
{

    private static final String FILE = "/File:";
    private static final String STATIC_CONTENT = "/staticContent";
    private static final String STATIC_IMAGE = "/staticImage";

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

        if( requestURI.startsWith( FILE ) ) {
            request.getRequestDispatcher(STATIC_IMAGE + requestURI ).forward( req, resp );
        }
        else if( requestURI.length() > 2 && Character.isUpperCase( requestURI.charAt( 1 ) ) ) {
            request.getRequestDispatcher(STATIC_CONTENT + requestURI ).forward( req, resp );
        }
        else {
            chain.doFilter( req, resp );
        }
    }

    @Override
    public void destroy()
    {
    }

}
