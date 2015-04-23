/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.model.util;

import java.util.List;
import uk.trainwatch.nre.darwin.model.ppt.schedules.DT;
import uk.trainwatch.nre.darwin.model.ppt.schedules.OPDT;
import uk.trainwatch.nre.darwin.model.ppt.schedules.OPOR;
import uk.trainwatch.nre.darwin.model.ppt.schedules.OR;

/**
 *
 * @author peter
 */
public interface ScheduleMeta
{

    List<Object> getOROrOPOROrIP();

    boolean isSetOROrOPOROrIP();

    /**
     * Returns the Origin of the schedule.
     * <p>
     * If the schedule contains both a Passenger Origin (OR) and an Operational Origin (OPOR) then we will return the OR instance.
     * <p>
     * @return
     */
    default TplLocation getOrigin()
    {
        if( isSetOROrOPOROrIP() ) {
            return (TplLocation) getOROrOPOROrIP().stream().
                    filter( l -> l instanceof OR || l instanceof OPOR ).
                    // Sort so public origin is first, then operating origin
                    sorted( ( a, b ) -> compare( a, b, OR.class, OPOR.class ) ).
                    findAny().
                    orElse( null );
        }
        else {
            return null;
        }
    }

    /**
     * Returns the Destination of the schedule.
     * <p>
     * If the schedule contains both a Passenger Destination (DT) and an Operational Destination (OPDT) then we will return the DT instance.
     * <p>
     * @return
     */
    default TplLocation getDestination()
    {
        if( isSetOROrOPOROrIP() ) {
            return (TplLocation) getOROrOPOROrIP().stream().
                    filter( l -> l instanceof DT || l instanceof OPDT ).
                    // Sort so public origin is first, then operating origin
                    sorted( ( a, b ) -> compare( a, b, DT.class, OPDT.class ) ).
                    findAny().
                    orElse( null );
        }
        else {
            return null;
        }
    }

    /**
     * Compare two objects.
     * <p>
     * Unlike normal comparison, this applies the following rules:
     * <ol>
     * <li> Both the same then returns 0</li>
     * <li> A is null then 1, or B is null then -1</li>
     * <li> Otherwise it will order them with the one of class rac first (1) or rbc (-1)</li>
     * </ol>
     * The reason behind this is so that we can order based on class, so one class will have higher priority than the other
     * <p>
     * @param <A>
     * @param <B>
     * @param a   Object a
     * @param b   Object b
     * @param rac Class to put first
     * @param rbc Class to put second
     * <p>
     * @return
     */
    static <A, B> int compare( Object a, Object b, Class<A> rac, Class<B> rbc )
    {
        if( a == b ) {
            return 0;
        }
        else if( a == null ) {
            return 1;
        }
        else if( b == null ) {
            return -1;
        }

        Class<?> ac = a.getClass();
        Class<?> bc = b.getClass();
        if( ac == bc ) {
            return 0;
        }
        if( ac == rac ) {
            return 1;
        }
        if( ac == rbc ) {
            return -1;
        }
        return 0;
    }

}
