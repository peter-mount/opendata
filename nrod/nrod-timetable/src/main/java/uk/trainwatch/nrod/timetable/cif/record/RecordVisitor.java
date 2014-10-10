/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

/**
 * A visitor to {@link Record}.
 * <p>
 * The order of the methods in this class are in the order they appear in the CIF End User Specification document which
 * also indicates the order they appear in the CIF file.
 * <p>
 * @author Peter T Mount
 */
public interface RecordVisitor
{

    /**
     * The Header is the first record of a CIF file
     * <p>
     * @param h
     */
    default void visit( Header h )
    {
    }

    /**
     * Insert a tiploc
     * <p>
     * @param i
     */
    default void visit( TIPLOCInsert i )
    {
    }

    /**
     * Amend a tiploc
     * <p>
     * @param a
     */
    default void visit( TIPLOCAmend a )
    {
    }

    /**
     * Delete a tiploc
     * <p>
     * @param d
     */
    default void visit( TIPLOCDelete d )
    {
    }

    /**
     * Association between two trains
     * <p>
     * @param a
     */
    default void visit( Association a )
    {
    }

    /**
     * Basic schedule for a train
     * <p>
     * @param s
     */
    default void visit( BasicSchedule s )
    {
    }

    /**
     * Extra information for a schedule
     * <p>
     * @param e
     */
    default void visit( BasicScheduleExtras e )
    {
    }

    /**
     * Details about the origin (start) of the schedule
     * <p>
     * @param ol
     */
    default void visit( OriginLocation ol )
    {
    }

    /**
     * Details about a location between the origin and terminating location
     * <p>
     * @param il
     */
    default void visit( IntermediateLocation il )
    {
    }

    /**
     * Changes made enroute
     * <p>
     * @param c
     */
    default void visit( ChangesEnRoute c )
    {
    }

    /**
     * The terminating (end) location of the schedule
     * <p>
     * @param tl
     */
    default void visit( TerminatingLocation tl )
    {
    }

    /**
     * The trainer record is the last record in the CIF file
     * <p>
     * @param t
     */
    default void visit( TrailerRecord t )
    {
    }

    /**
     * Train Specific note. This is currently unused
     * <p>
     * @param n
     */
    default void visit( TrainNote n )
    {
    }

    /**
     * Location Specific Note. This is currently unused
     * <p>
     * @param n
     */
    default void visit( LocationNote n )
    {
    }
}
