/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.function.Predicate;
import uk.trainwatch.util.Streams;
import uk.trainwatch.util.TimeUtils;

/**
 *
 * @author peter
 */
public interface TimetableEntry
{

    String getTpl();

    int getTplid();

    default boolean isSup()
    {
        return false;
    }

    default String getPlat()
    {
        return "";
    }

    default boolean isPlatsup()
    {
        return false;
    }

    default boolean isCisplatsup()
    {
        return false;
    }

    LocalTime getPta();

    LocalTime getPtd();

    LocalTime getWta();

    LocalTime getWtd();

    LocalTime getWtp();

    static LocalTime getTime( TimetableEntry e )
    {
        if( e.getWtd() != null ) {
            return e.getWtd();
        }
        if( e.getWta() != null ) {
            return e.getWta();
        }
        if( e.getWtp() != null ) {
            return e.getWtp();
        }
        return null;
    }

    public static Comparator<TimetableEntry> SORT = ( a, b ) -> TimeUtils.compareLocalTimeDarwin.compare( getTime( a ), getTime( b ) );
    public static Comparator<TimetableEntry> SORT_REVERSE = ( a, b ) -> TimeUtils.compareLocalTimeDarwin.compare( getTime( b ), getTime( a ) );

    public static TimetableEntry findTime( Collection<? extends TimetableEntry> c, Predicate<Integer> tplFilter )
    {
        return Streams.stream( c ).
                filter( e -> tplFilter.test( e.getTplid() ) ).
                findAny().
                orElse( null );
    }
}
