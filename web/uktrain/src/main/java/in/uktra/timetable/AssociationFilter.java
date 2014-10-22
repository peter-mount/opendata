/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.uktra.timetable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import uk.trainwatch.nrod.timetable.cif.record.Association;

/**
 * A filter which takes a {@link List} of {@link Association}'s and produces the current active one
 * <p>
 * @author Peter T Mount
 */
public enum AssociationFilter
        implements Function<List<Association>, Association>,
                   Comparator<Association>
{

    INSTANCE;

    /**
     * Mapping function to get the most recent schedule
     * <p>
     * @param t <p>
     * @return
     */
    @Override
    public Association apply( List<Association> t )
    {
        if( t.isEmpty() )
        {
            return null;
        }
        Collections.sort( t, this );
        return t.get( 0 );
    }

    /**
     * Comparator that orders the most recent runsFrom date first
     * <p>
     * @param a
     * @param b <p>
     * @return
     */
    @Override
    public int compare( Association a, Association b )
    {
        LocalDate da = a.getStartDate();
        LocalDate db = b.getStartDate();
        return da.isAfter( db ) ? -1 : 1;
    }

}
