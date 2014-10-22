/*
 * Copyright 2014 Peter T Mount.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package in.uktra.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * This filter class removes any whitespace from the response. It actually trims all leading and trailing spaces or tabs
 * and newlines before writing to the response stream. This will greatly save the network bandwith, but this will make
 * the source of the response more hard to read.
 * <p>
 * This filter should be configured in the web.xml as follows:
 * <pre>
 * &lt;filter&gt;
 *     &lt;description&gt;
 *         This filter class removes any whitespace from the response. It actually trims all
 *         leading and trailing spaces or tabs and newlines before writing to the response stream.
 *         This will greatly save the network bandwith, but this will make the source of the
 *         response more hard to read.
 *     &lt;/description&gt;
 *     &lt;filter-name&gt;whitespaceFilter&lt;/filter-name&gt;
 *     &lt;filter-class&gt;net.balusc.webapp.WhitespaceFilter&lt;/filter-class&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 *     &lt;filter-name&gt;whitespaceFilter&lt;/filter-name&gt;
 *     &lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 * <p>
 * @author BalusC
 * @link http://balusc.blogspot.com/2007/12/whitespacefilter.html
 * @author Peter Mount reimplemented from scratch to use a custom writer which does the filtering.
 */
@WebFilter( displayName = "WhitespaceFilter", value = "/*" )
public class WhitespaceFilter
        implements Filter
{

    @Override
    public void init( FilterConfig config )
            throws ServletException
    {
        //
    }

    @Override
    public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain )
            throws IOException,
                   ServletException
    {
        if( response instanceof HttpServletResponse )
        {
            HttpServletResponse httpres = (HttpServletResponse) response;
            chain.doFilter( request, wrapResponse( httpres, createTrimWriter( httpres ) ) );
        }
        else
        {
            chain.doFilter( request, response );
        }
    }

    @Override
    public void destroy()
    {
    }

    private static PrintWriter createTrimWriter( final HttpServletResponse response )
            throws IOException
    {
        return new PrintWriter( new WhitespaceFilterWriter(
                new OutputStreamWriter( response.getOutputStream(), "UTF-8" ) ), true );
    }

    private static HttpServletResponse wrapResponse( final HttpServletResponse response, final PrintWriter writer )
    {
        return new HttpServletResponseWrapper( response )
        {
            @Override
            public PrintWriter getWriter()
                    throws IOException
            {
                return writer;
            }
        };
    }

}
