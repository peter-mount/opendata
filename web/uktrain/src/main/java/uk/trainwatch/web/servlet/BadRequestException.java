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
package uk.trainwatch.web.servlet;

import com.google.common.base.Objects;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Peter T Mount
 */
public class BadRequestException
        extends HttpResponseException
{

    public BadRequestException()
    {
        super( HttpServletResponse.SC_BAD_REQUEST );
    }

    public BadRequestException( String message )
    {
        super( HttpServletResponse.SC_BAD_REQUEST, message );
    }

    public BadRequestException( Throwable t )
    {
        super( HttpServletResponse.SC_BAD_REQUEST, t );
    }

    public BadRequestException( String message, Throwable t )
    {
        super( HttpServletResponse.SC_BAD_REQUEST, message, t );
    }

    public static void assertNotNull( Object v )
    {
        if( v == null )
        {
            throw new BadRequestException();
        }
    }

    public static void assertNotNull( Object v, String msg )
    {
        if( v == null )
        {
            throw new BadRequestException( msg );
        }
    }

    public static void assertEquals( Object a, Object b )
    {
        if( !Objects.equal( a, b ) )
        {
            throw new BadRequestException();
        }
    }

    public static void assertEquals( Object a, Object b, String msg )
    {
        if( !Objects.equal( a, b ) )
        {
            throw new BadRequestException( msg );
        }
    }

    public static void assertNotEqual( Object a, Object b )
    {
        if( Objects.equal( a, b ) )
        {
            throw new BadRequestException();
        }
    }

    public static void assertNotEqual( Object a, Object b, String msg )
    {
        if( Objects.equal( a, b ) )
        {
            throw new BadRequestException( msg );
        }
    }

}
