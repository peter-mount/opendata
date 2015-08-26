/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;

/**
 *
 * @author peter
 */
public interface LDB
{

    LocalTime getArr();

    int getCancReason();

    String getCurloc();

    Duration getDelay();

    LocalTime getDep();

    String getDest();

    LocalTime getEta();

    LocalTime getEtd();

    long getId();

    CallingPoint getLastReport();

    int getLateReason();

    /**
     * The train length at this location
     *
     * @return number of carriages, 0 for unknown
     */
    int getLength();

    String getOrigin();

    String getPlat();

    Collection<CallingPoint> getPoints();

    LocalTime getPta();

    LocalTime getPtd();

    String getRid();

    int getScheduleId();

    /**
     * The recorded time for this entry as defined by the database.
     *
     * This is the first value in the following sequence that is present: dep, arr, detet, arret, ptd, pta, wtd, wta or wtp.
     *
     * @return
     */
    LocalTime getTime();

    /**
     * Duration from now until the expected time. If the expected time has passed
     * then this returns 0
     * <p>
     * @return Duration, never negative
     */
    Duration getTimeUntil();

    String getToc();

    Timestamp getTs();

    LocalDateTime getTsDT();

    /**
     * The type of entry
     * <p>
     * @return
     */
    Type getType();

    String getUid();

    int getVia();

    /**
     * Has this train arrived.
     *
     * Note to see if the train has arrived but not yet departed use {@link #isOnPlatform()}
     *
     * @return
     */
    boolean isArrived();

    boolean isCanc();

    /**
     * Platform suppressed manually from a CIS terminal.
     *
     * Licence restriction means that if this returns true then the platform must not be displayed.
     *
     * @return
     */
    boolean isCisPlatSup();

    /**
     * Is the service delayed. This will be true if the delay is unknown and "Delayed" should be shown on any display boards.
     *
     * @return
     */
    boolean isDelayUnknown();

    boolean isDelayed();

    /**
     * Has the train departed
     *
     * @return
     */
    boolean isDeparted();

    /**
     * Can the platform be displayed
     *
     * Licence restriction means that if this returns false (i.e. platSup or cisPlatSup is true) then the platform must not be
     * displayed.
     *
     * @return
     */
    boolean isDisplayPlatform();

    boolean isLastReportPresent();

    /**
     * No report. This is defined as having no reported arrival nor departure times
     *
     * @return
     */
    boolean isNoReport();

    /**
     * Is the train on the platform.
     *
     * This is defined as having an arrival but no departure time. However a cancelled, terminated or non-timetabled (working)
     * train will not show regardless of the times.
     *
     * @return
     */
    boolean isOnPlatform();

    /**
     * Is the train on time. This is defined as being within ±1 minute of the timetable,
     *
     * @return
     */
    boolean isOntime();

    /**
     * Platform suppressed.
     *
     * Licence restriction means that if this returns true then the platform must not be displayed.
     *
     * @return
     */
    boolean isPlatSup();

    /**
     * Is this entry public.
     *
     * License restriction means that if this returns false then the entry must not be displayed to the general public.
     *
     * @return
     */
    boolean isPublic();

    /**
     * Is this entry suppressed.
     *
     * Licence restriction means that if this returns true then this entry must not be displayed to the general public.
     *
     * @return
     */
    boolean isSup();

    /**
     * Terminated.
     *
     * If {@link #isArrived()} is true then the train has terminated. If not then it's due to terminate here.
     *
     * @return
     */
    boolean isTerminated();

    /**
     * Is this timetabled. A working train will return false here
     *
     * @return
     */
    boolean isTimetabled();

    void setCanc( boolean canc );
    
    /**
     * The type of this LDB entry
     */
    public static enum Type
    {

        /**
         * Darwin entry, covers mainline
         */
        DARWIN,
        /**
         * TfL entry, covers tube, dlr etc
         */
        TFL
    }
}
