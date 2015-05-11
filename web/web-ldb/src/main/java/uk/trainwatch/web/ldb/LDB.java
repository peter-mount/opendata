/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalTime;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
public class LDB
{

    public static final SQLFunction<ResultSet, LDB> fromSQL = rs -> new LDB(
            TimeUtils.getLocalTime( rs, "tm" ),
            rs.getString( "toc" ),
            rs.getString( "destination" ),
            rs.getString( "rid" ),
            rs.getString( "uid" ),
            rs.getInt( "schedule" ),
            // actual arrive/depart
            TimeUtils.getLocalTime( rs, "arr" ),
            TimeUtils.getLocalTime( rs, "dep" ),
            // estimated arrive/depart
            TimeUtils.getLocalTime( rs, "etarr" ),
            TimeUtils.getLocalTime( rs, "etdep" ),
            // timetabled arrive/depart
            TimeUtils.getLocalTime( rs, "pta" ),
            TimeUtils.getLocalTime( rs, "ptd" ),
            // Platform
            rs.getString( "plat" ),
            // Data suppression
            rs.getBoolean( "supp" ),
            rs.getBoolean( "platsup" ),
            rs.getBoolean( "cisplatsup" ),
            // Delay tbi
            TimeUtils.getDuration( rs, "delay" ),
            // Terminated/terminates here
            rs.getBoolean( "term" )
    );

    private final LocalTime time;
    private final boolean terminated;
    private final String toc;
    private final String dest;

    private final String rid;
    private final String uid;
    private final int scheduleId;
    private final LocalTime arr;
    private final LocalTime dep;
    private final LocalTime eta;
    private final LocalTime etd;
    private final LocalTime pta;
    private final LocalTime ptd;
    private final String plat;
    private final boolean sup;
    private final boolean platSup;
    private final boolean cisPlatSup;
    private final Duration delay;

    private LDB(
            LocalTime time,
            String toc,
            String dest,
            String rid, String uid,
            int scheduleId,
            LocalTime arr, LocalTime dep,
            LocalTime eta, LocalTime etd,
            LocalTime pta, LocalTime ptd,
            String plat,
            boolean sup, boolean platSup, boolean cisPlatSup,
            Duration delay,
            boolean terminated )
    {
        this.time = time;
        this.toc = toc;
        this.dest = dest;

        this.rid = rid;
        this.uid = uid;
        this.scheduleId = scheduleId;
        this.arr = arr;
        this.dep = dep;
        this.eta = eta;
        this.etd = etd;
        this.pta = pta;
        this.ptd = ptd;
        this.sup = sup;
        this.plat = plat;
        this.platSup = platSup;
        this.cisPlatSup = cisPlatSup;
        this.delay = delay;
        this.terminated = terminated;
    }

    /**
     * Terminated.
     *
     * If {@link #isArrived()} is true then the train has terminated. If not then it's due to terminate here.
     *
     * @return
     */
    public boolean isTerminated()
    {
        return terminated;
    }

    /**
     * Has this train arrived
     *
     * @return
     */
    public boolean isArrived()
    {
        return arr != null;
    }

    public boolean isOntime()
    {
        return delay.isZero() || Math.abs( delay.getSeconds() ) < 60;
    }

    public LocalTime getTime()
    {
        return time;
    }

    public String getRid()
    {
        return rid;
    }

    public String getUid()
    {
        return uid;
    }

    public LocalTime getArr()
    {
        return arr;
    }

    public LocalTime getDep()
    {
        return dep;
    }

    public LocalTime getEta()
    {
        return eta;
    }

    public LocalTime getEtd()
    {
        return etd;
    }

    public LocalTime getPta()
    {
        return pta;
    }

    public LocalTime getPtd()
    {
        return ptd;
    }

    public String getPlat()
    {
        return plat;
    }

    /**
     * Is this entry suppressed.
     *
     * Licence restriction means that if this returns true then this entry must not be displayed to the general public.
     *
     * @return
     */
    public boolean isSup()
    {
        return sup;
    }

    /**
     * Is this entry public.
     *
     * License restriction means that if this returns false then the entry must not be displayed to the general public.
     *
     * @return
     */
    public boolean isPublic()
    {
        return !isSup();
    }

    /**
     * Platform suppressed.
     *
     * Licence restriction means that if this returns true then the platform must not be displayed.
     *
     * @return
     */
    public boolean isPlatSup()
    {
        return platSup;
    }

    /**
     * Platform suppressed manually from a CIS terminal.
     *
     * Licence restriction means that if this returns true then the platform must not be displayed.
     *
     * @return
     */
    public boolean isCisPlatSup()
    {
        return cisPlatSup;
    }

    /**
     * Can the platform be displayed
     *
     * Licence restriction means that if this returns false (i.e. platSup or cisPlatSup is true) then the platform must not be
     * displayed.
     *
     * @return
     */
    public boolean isDisplayPlatform()
    {
        return !(platSup || cisPlatSup);
    }

    public Duration getDelay()
    {
        return delay;
    }

    public int getScheduleId()
    {
        return scheduleId;
    }

    public String getDest()
    {
        return dest;
    }

    public String getToc()
    {
        return toc;
    }

}
