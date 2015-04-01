/*
 * Copyright 2015 peter.
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
package uk.trainwatch.apachemq;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.jms.BytesMessage;
import javax.jms.JMSException;

/**
 * Mapping function which accepts a JMS BytesMessage and decompresses the gzipped content into a String
 *
 * @author peter
 */
public class GUnZipBytesMessage
        implements Function<BytesMessage, String>
{

    @Override
    public String apply( BytesMessage t )
    {
        if( t != null )
        {
            try
            {
                long l = t.getBodyLength();
                byte b[] = new byte[(int) l];
                t.readBytes( b );
                try( Reader r = new InputStreamReader( new GZIPInputStream( new ByteArrayInputStream( b ) ) ) )
                {
                    StringBuilder sb = new StringBuilder();
                    char cb[] = new char[1024];
                    int s = r.read( cb );
                    while( s > -1 )
                    {
                        sb.append( cb, 0, s );
                        s = r.read( cb );
                    }
                    return sb.toString();
                }
            } catch( IOException | JMSException ex )
            {
                Logger.getLogger( getClass().getName() ).log( Level.SEVERE, null, ex );
            }
        }

        return null;
    }

}
