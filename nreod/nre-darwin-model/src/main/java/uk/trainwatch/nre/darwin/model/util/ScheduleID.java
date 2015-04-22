/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.model.util;

import java.util.Optional;
import javax.xml.datatype.XMLGregorianCalendar;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;

/**
 * Interface implemented by JAXB objects that refer to a schedule.
 *
 * Having those objects implement this interface means we can remove a lot of instanceOf and casting to get the rid value,
 * improving code readability and performance.
 *
 * @author peter
 */
public interface ScheduleID
        extends RailID
{

    String getUid();

    XMLGregorianCalendar getSsd();

    /**
     * Mapping function to cast a JAXB object to a RailID
     *
     * @param o JAXB Object
     * @return o cast as RailID or null if o does not implement RailID
     */
    static ScheduleID castScheduleId( Object o )
    {
        return o instanceof ScheduleID ? (ScheduleID) o : null;
    }

    /**
     * Resolves a ScheduleID from a {@link Pport}.
     *
     * This will return an instance if the Pport is not null, has a UR element and that element has either a {@link Schedule} or
     * {@link TS} element. If it has more than one then the first Schedule or TS element in that order is used.
     *
     * @param p
     * @return
     */
    static ScheduleID getScheduleID( Pport p )
    {
        if( p == null )
        {
            return null;
        }
        Pport.UR ur = p.getUR();
        if( ur == null )
        {
            return null;
        }
        Optional<ScheduleID> schedule
                = ur.isSetSchedule() ? Optional.of( ur.getSchedule().get( 0 ) )
                        : ur.isSetTS() ? Optional.of( ur.getTS().get( 0 ) )
                                : Optional.empty();
        return schedule.orElse( null );
    }
}
