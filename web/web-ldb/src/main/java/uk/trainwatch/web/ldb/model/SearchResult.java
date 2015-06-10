/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

import java.sql.ResultSet;
import java.time.LocalTime;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
public class SearchResult
        implements Comparable<SearchResult>
{

    public static final SQLFunction<ResultSet, SearchResult> fromSQL = rs -> new SearchResult(
            rs.getString( "rid" ),
            TimeUtils.getLocalTime( rs, "wtm" )
    );

    private final String rid;
    private final LocalTime time;

    private SearchResult( String rid, LocalTime time )
    {
        this.rid = rid;
        this.time = time;
    }

    public String getRid()
    {
        return rid;
    }

    public LocalTime getTime()
    {
        return time;
    }

    @Override
    public int compareTo( SearchResult o )
    {
        return TimeUtils.compareLocalTimeDarwin.compare( time, o.time );
    }

}
