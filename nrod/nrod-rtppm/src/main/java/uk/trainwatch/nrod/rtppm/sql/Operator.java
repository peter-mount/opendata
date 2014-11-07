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
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author Peter T Mount
 */
public class Operator
{

    public static final SQLFunction<ResultSet, Operator> fromSQL = rs -> new Operator(
            rs.getInt( 1 ),
            rs.getString( 2 ),
            rs.getString( 3 ),
            rs.getString( 4 )
    );

    private final int id;
    private final String operator;
    private final String display;
    private final String hashtag;

    public Operator( int id, String operator, String display, String hashtag )
    {
        this.id = id;
        this.operator = operator;
        this.display = (display == null || display.isEmpty()) ? operator : display;
        this.hashtag = Objects.toString( hashtag, "" );
    }

    public int getId()
    {
        return id;
    }

    public String getOperator()
    {
        return operator;
    }

    public String getDisplay()
    {
        return display;
    }

    public String getHashtag()
    {
        return hashtag;
    }

}
