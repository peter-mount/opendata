/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import java.sql.ResultSet;
import java.time.LocalTime;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
public class CallingPoint
{

    public static final SQLFunction<ResultSet, CallingPoint> fromSQL = rs -> new CallingPoint( rs.getString( "tpl" ), TimeUtils.getLocalTime( rs, "time" ) );

    private final String tpl;
    private final LocalTime time;

    private CallingPoint( String tpl, LocalTime time )
    {
        this.tpl = tpl;
        this.time = time;
    }

    public String getTpl()
    {
        return tpl;
    }

    public LocalTime getTime()
    {
        return time;
    }

}
