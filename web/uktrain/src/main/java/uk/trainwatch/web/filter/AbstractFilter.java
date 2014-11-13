/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Peter T Mount
 */
public abstract class AbstractFilter
        implements Filter
{

    @Override
    public void init( FilterConfig filterConfig )
            throws ServletException
    {
    }

    @Override
    public void destroy()
    {
    }

    @Override
    public final void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
            throws IOException,
                   ServletException
    {
        doFilter( (HttpServletRequest) request, (HttpServletResponse) response, chain );
    }

    protected abstract void doFilter( HttpServletRequest request, HttpServletResponse response, FilterChain chain )
            throws IOException,
                   ServletException;
}
