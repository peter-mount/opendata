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
package uk.trainwatch.nrod.rtppm.sql;

import java.sql.ResultSet;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author Peter T Mount
 */
@XmlRootElement(name = "operator")
public class Operator
        implements Comparable<Operator>
{

    public static final SQLFunction<ResultSet, Operator> fromSQL = rs -> new Operator(
            rs.getInt( 1 ),
            rs.getString( 2 ),
            rs.getString( 3 ),
            rs.getString( 4 )
    );

    private int id;
    private String operator;
    private String display;
    private String hashtag;

    public Operator()
    {
    }

    public Operator( int id, String operator, String display, String hashtag )
    {
        this.id = id;
        this.operator = operator;
        this.display = (display == null || display.isEmpty()) ? operator : display;
        this.hashtag = Objects.toString( hashtag, "" );
    }

    @XmlAttribute
    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    @XmlAttribute
    public String getOperator()
    {
        return operator;
    }

    public void setOperator( String operator )
    {
        this.operator = operator;
    }

    @XmlAttribute
    public String getDisplay()
    {
        return display;
    }

    public void setDisplay( String display )
    {
        this.display = display;
    }

    @XmlAttribute
    public String getHashtag()
    {
        return hashtag;
    }

    public void setHashtag( String hashtag )
    {
        this.hashtag = hashtag;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 73 * hash + this.id;
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
        final Operator other = (Operator) obj;
        if( this.id != other.id )
        {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo( Operator o )
    {
        return display.compareTo( o.display );
    }

}
