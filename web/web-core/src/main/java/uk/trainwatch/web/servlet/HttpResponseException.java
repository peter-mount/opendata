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

import java.io.IOException;

/**
 *
 * @author Peter T Mount
 */
public class HttpResponseException
        extends RuntimeException
{

    private final int responseCode;

    public HttpResponseException( int responseCode )
    {
        super();
        this.responseCode = responseCode;
    }

    public HttpResponseException( int responseCode, String message )
    {
        super( message );
        this.responseCode = responseCode;
    }

    public HttpResponseException( int responseCode, Throwable cause )
    {
        super( cause );
        this.responseCode = responseCode;
    }

    public HttpResponseException( int responseCode, String message, Throwable cause )
    {
        super( message, cause );
        this.responseCode = responseCode;
    }

    public final void sendError( ApplicationRequest request )
            throws IOException
    {
        String msg = getMessage();
        if( msg == null || msg.isEmpty() )
        {
            request.sendError( responseCode );
        }
        else
        {
            request.sendError( responseCode, msg );
        }
    }
}
