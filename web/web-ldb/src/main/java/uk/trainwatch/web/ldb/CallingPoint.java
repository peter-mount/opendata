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

    public static final SQLFunction<ResultSet, CallingPoint> fromSQL = rs -> new CallingPoint(
            rs.getString( "tpl" ),
            TimeUtils.getLocalTime( rs, "time" ),
            rs.getBoolean( "report" ),
            rs.getBoolean( "can" )
    );

    private final String tpl;
    private final LocalTime time;
    private final boolean report;
    private final boolean canc;

    private CallingPoint( String tpl, LocalTime time, boolean report, boolean canc )
    {
        this.tpl = tpl;
        this.time = time;
        this.report = report;
        this.canc = canc;
    }

    public String getTpl()
    {
        return tpl;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public boolean isReport()
    {
        return report;
    }

    public boolean isCanc()
    {
        return canc;
    }

}
