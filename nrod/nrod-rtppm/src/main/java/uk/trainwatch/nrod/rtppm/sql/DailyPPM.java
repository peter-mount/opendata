/*
 * Copyright 2014 peter.
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
package uk.trainwatch.nrod.rtppm.sql;

import java.sql.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author peter
 */
@XmlRootElement(name = "ppm")
public class DailyPPM
        implements Comparable<DailyPPM>
{

    private long timestamp;
    private int id;
    private int value;

    public DailyPPM()
    {
    }

    public DailyPPM( long timestamp, int id, int value )
    {
        this.timestamp = timestamp;
        this.id = id;
        this.value = value;
    }

    public DailyPPM( Date date, int id, int value )
    {
        this( date.getTime(), id, value );
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp( long timestamp )
    {
        this.timestamp = timestamp;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue( int value )
    {
        this.value = value;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 53 * hash + (int) (this.timestamp ^ (this.timestamp >>> 32));
        hash = 53 * hash + this.id;
        hash = 53 * hash + this.value;
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        if( getClass() != obj.getClass() )
        {
            return false;
        }
        final DailyPPM other = (DailyPPM) obj;
        if( this.timestamp != other.timestamp )
        {
            return false;
        }
        if( this.id != other.id )
        {
            return false;
        }
        if( this.value != other.value )
        {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo( DailyPPM o )
    {
        int r = Integer.compare( id, o.id );
        if( r == 0 )
        {
            r = Long.compare( timestamp, o.timestamp );
        }
        return r;
    }

}
