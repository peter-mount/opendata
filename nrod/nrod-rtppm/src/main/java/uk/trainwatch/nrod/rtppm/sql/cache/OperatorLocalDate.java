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
package uk.trainwatch.nrod.rtppm.sql.cache;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import uk.trainwatch.nrod.rtppm.sql.Operator;

/**
 *
 * @author peter
 */
public class OperatorLocalDate
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private int id;
    private LocalDate date;

    public OperatorLocalDate()
    {
    }

    public OperatorLocalDate( int id, LocalDate date )
    {
        this.id = id;
        this.date = date;
    }

    public OperatorLocalDate( LocalDate date )
    {
        this( Integer.MIN_VALUE, date );
    }

    public OperatorLocalDate( Operator o, LocalDate date )
    {
        this( o.getId(), date );
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public void setDate( LocalDate date )
    {
        this.date = date;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode( this.date );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final OperatorLocalDate other = (OperatorLocalDate) obj;
        return id != other.getId() && date.equals( other.getDate() );
    }

}
