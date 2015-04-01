/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.workbench;

import uk.trainwatch.web.workbench.trust.Trust;
import uk.trainwatch.web.workbench.trust.TrustCache;
import java.util.stream.Stream;
import uk.trainwatch.util.Streams;

/**
 * Common streams against the collected trust data
 *
 * @author peter
 */
public class WorkbenchStreams
{

    public static Stream<Trust> details( int toc, String id )
    {
        return Streams.ofNullable( TrustCache.INSTANCE.getTrust( toc, id ) );
    }

    /**
     * A stream of activated trains
     *
     * @param toc Train Operator
     * <p>
     * @return Stream
     */
    public static Stream<Trust> activations( int toc )
    {
        return TrustCache.INSTANCE.getStream( toc ).
                filter( Trust::isActivated );
    }

    /**
     * A stream of recent movements
     *
     * @param toc Train Operator
     * <p>
     * @return Stream
     */
    public static Stream<Trust> movements( int toc )
    {
        return TrustCache.INSTANCE.getStream( toc ).
                filter( Trust::isMovement );
    }

    /**
     * A stream of cancellations
     *
     * @param toc Train Operator
     * <p>
     * @return Stream
     */
    public static Stream<Trust> cancellations( int toc )
    {
        return TrustCache.INSTANCE.getStream( toc ).
                filter( Trust::isCancelled );
    }

    /**
     * A stream of delays
     *
     * @param toc Train Operator
     * <p>
     * @return Stream
     */
    public static Stream<Trust> delays( int toc )
    {
        return TrustCache.INSTANCE.getStream( toc ).
                filter( Trust::isDelayed );
    }

    public static Stream<Trust> offroute( int toc )
    {
        return TrustCache.INSTANCE.getStream( toc ).
                filter( Trust::isOffRoute );
    }

    public static Stream<Trust> terminated( int toc )
    {
        return TrustCache.INSTANCE.getStream( toc ).
                filter( Trust::isTerminated );
    }

    public static Stream<Trust> issues( int toc )
    {
        return TrustCache.INSTANCE.getStream( toc ).
                filter( t -> Trust.isCancelled( t )
                             || Trust.isDelayed( t )
                             || Trust.isOffRoute( t )
                );
    }
}
