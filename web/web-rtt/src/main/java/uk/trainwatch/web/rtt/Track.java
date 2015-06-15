/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.rtt;

import java.time.LocalTime;
import java.util.Comparator;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.web.ldb.model.ForecastEntry;
import uk.trainwatch.web.ldb.model.ScheduleEntry;
import uk.trainwatch.web.ldb.model.TimetableEntry;
import uk.trainwatch.web.ldb.model.Train;

/**
 *
 * @author peter
 */
public class Track
        implements Comparable<Track>
{

    private final String tpl;
    private final boolean cancelled;
    private final boolean pass;
    private boolean past;
    private final boolean scheduled;
    private int row;
    private int col;
    private final LocalTime time;

    public Track( Train t, ForecastEntry fe )
    {
        this.tpl = fe.getTpl();
        time = TimetableEntry.getTime( fe );

        ScheduleEntry se = fe.getScheduleEntry();

        scheduled = se != null;
        cancelled = se != null && se.isCan();

        // Pass if we have a working pass time
        pass = fe.getWtp() != null;

        // past if we have an observed time
        past = fe.getArr() != null || fe.getDep() != null || fe.getPass() != null;

    }

    public LocalTime getTime()
    {
        return time;
    }

    public boolean isPast()
    {
        return past;
    }

    Track setPast() {
        past=true;
        return this;
    }
    
    public int getCol()
    {
        return col;
    }

    Track setCol( int col )
    {
        this.col = col;
        return this;
    }

    public int getRow()
    {
        return row;
    }

    Track setRow( int row )
    {
        this.row = row;
        return this;
    }

    @Override
    public String toString()
    {
        return "Track{" + "tpl=" + tpl + ", cancelled=" + cancelled + ", pass=" + pass + ", past=" + past + ", scheduled=" + scheduled + ", row=" + row + ", col=" + col + ", time=" + time + '}';
    }

    public String getTpl()
    {
        return tpl;
    }

    public boolean isCancelled()
    {
        return cancelled;
    }

    public boolean isPass()
    {
        return pass;
    }

    public boolean isScheduled()
    {
        return scheduled;
    }
    

    public JsonObjectBuilder toBuilder()
    {
        return Json.createObjectBuilder().
                add( "tpl", tpl ).
                add( "time", time.toSecondOfDay() ).
                add( "x", col ).
                add( "y", row ).
                add( "scheduled", scheduled ).
                add( "cancelled", cancelled ).
                add( "pass", pass ).
                add( "past", past );
    }

    @Override
    public int compareTo( Track o )
    {
        return TimeUtils.compareLocalTimeDarwin.compare( time, o.time );
    }

    static final Comparator<Track> reverseSort = ( a, b ) -> TimeUtils.compareLocalTimeDarwin.compare( b.time, a.time );
}
