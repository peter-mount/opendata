/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.workbench;

import uk.trainwatch.web.workbench.trust.Trust;
import uk.trainwatch.web.workbench.trust.TrustCache;
import java.util.stream.Stream;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import uk.trainwatch.util.Streams;

/**
 * Common streams against the collected trust data
 *
 * @author peter
 */
@ApplicationScoped
public class WorkbenchStreams
{

    @Inject
    private TrustCache trustCache;

    public Stream<Trust> details( int toc, String id )
    {
        return Streams.ofNullable( trustCache.getTrust( toc, id ) );
    }

    /**
     * A stream of activated trains
     *
     * @param toc Train Operator
     * <p>
     * @return Stream
     */
    public Stream<Trust> activations( int toc )
    {
        return trustCache.getStream( toc ).
                filter( Trust::isActivated );
    }

    /**
     * A stream of recent movements
     *
     * @param toc Train Operator
     * <p>
     * @return Stream
     */
    public Stream<Trust> movements( int toc )
    {
        return trustCache.getStream( toc ).
                filter( Trust::isMovement );
    }

    /**
     * A stream of cancellations
     *
     * @param toc Train Operator
     * <p>
     * @return Stream
     */
    public Stream<Trust> cancellations( int toc )
    {
        return trustCache.getStream( toc ).
                filter( Trust::isCancelled );
    }

    /**
     * A stream of delays
     *
     * @param toc Train Operator
     * <p>
     * @return Stream
     */
    public Stream<Trust> delays( int toc )
    {
        return trustCache.getStream( toc ).
                filter( Trust::isDelayed );
    }

    public Stream<Trust> offroute( int toc )
    {
        return trustCache.getStream( toc ).
                filter( Trust::isOffRoute );
    }

    public Stream<Trust> terminated( int toc )
    {
        return trustCache.getStream( toc ).
                filter( Trust::isTerminated );
    }

    public Stream<Trust> issues( int toc )
    {
        return trustCache.getStream( toc ).
                filter( t -> Trust.isCancelled( t )
                             || Trust.isDelayed( t )
                             || Trust.isOffRoute( t )
                );
    }
}
