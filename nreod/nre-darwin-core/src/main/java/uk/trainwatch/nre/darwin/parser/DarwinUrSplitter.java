/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.parser;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;

/**
 * A mapping function which will split out the contents of a {@link Pport.UR} message into individual {@link Pport} instances
 * <p>
 * @author peter
 */
public class DarwinUrSplitter
        implements Function<Pport, Stream<Pport>>
{

    /**
     * Note these are ordered by frequency so most common is first
     */
    private static final Collection<Function<Pport, List>> GETTERS = Arrays.asList(
            p -> p.getUR().getTS(),
            p -> p.getUR().getOW(),
            p -> p.getUR().getDeactivated(),
            p -> p.getUR().getAssociation(),
            p -> p.getUR().getSchedule(),
            p -> p.getUR().getTrainAlert(),
            p -> p.getUR().getTrainOrder(),
            p -> p.getUR().getTrackingID(),
            p -> p.getUR().getAlarm()
    );

    @Override
    public Stream<Pport> apply( Pport t )
    {
        Pport.UR ur = t.getUR();
        if( ur != null ) {
            return GETTERS.stream().
                    map( get -> this.<Object>duplicate( t, get ) ).
                    findAny().
                    orElse( Stream.empty() );
        }
        else {
            return Stream.empty();
        }
    }

    private Stream<Pport> duplicate( Pport t, Function<Pport, List> get )
    {
        List l = get.apply( t );
        if( l.isEmpty() ) {
            return Stream.empty();
        }

        return l.stream().
                map( ts -> {
                    Pport p = DarwinJaxbContext.duplicate( t );
                    get.apply( p ).add( ts );
                    return p;
                } );
    }

}
