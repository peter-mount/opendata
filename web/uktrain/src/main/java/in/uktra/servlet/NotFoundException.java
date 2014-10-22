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
package in.uktra.servlet;

import com.google.common.base.Objects;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Peter T Mount
 */
public class NotFoundException
        extends HttpResponseException
{

    public NotFoundException()
    {
        super( HttpServletResponse.SC_NOT_FOUND );
    }

    public NotFoundException( String message )
    {
        super( HttpServletResponse.SC_NOT_FOUND, message );
    }

    public NotFoundException( Throwable t )
    {
        super( HttpServletResponse.SC_NOT_FOUND, t );
    }

    public NotFoundException( String message, Throwable t )
    {
        super( HttpServletResponse.SC_NOT_FOUND, message, t );
    }

    public static void assertNotNull( Object v )
    {
        if( v == null )
        {
            throw new NotFoundException();
        }
    }

    public static void assertNotNull( Object v, String msg )
    {
        if( v == null )
        {
            throw new NotFoundException( msg );
        }
    }

    public static void assertEquals( Object a, Object b )
    {
        if( !Objects.equal( a, b ) )
        {
            throw new NotFoundException();
        }
    }

    public static void assertEquals( Object a, Object b, String msg )
    {
        if( !Objects.equal( a, b ) )
        {
            throw new NotFoundException( msg );
        }
    }

    public static void assertNotEqual( Object a, Object b )
    {
        if( Objects.equal( a, b ) )
        {
            throw new NotFoundException();
        }
    }

    public static void assertNotEqual( Object a, Object b, String msg )
    {
        if( Objects.equal( a, b ) )
        {
            throw new NotFoundException( msg );
        }
    }

}
