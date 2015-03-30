/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.trust;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import uk.trainwatch.nrod.location.TrainLocation;
import uk.trainwatch.nrod.location.TrainLocationFactory;
import uk.trainwatch.nrod.timetable.model.Schedule;
import uk.trainwatch.nrod.trust.model.ChangeOfIdentity;
import uk.trainwatch.nrod.trust.model.ChangeOfOrigin;
import uk.trainwatch.nrod.trust.model.TrainActivation;
import uk.trainwatch.nrod.trust.model.TrainCancellation;
import uk.trainwatch.nrod.trust.model.TrainMovement;
import uk.trainwatch.nrod.trust.model.TrainReinstatement;
import uk.trainwatch.nrod.trust.model.TrustMovement;

/**
 * The details observed about a train in trust
 * <p>
 * @author peter
 */
public class Trust
        extends AbstractTrust
        implements Consumer< TrustMovement>
{

    private TrainLocation location;
    private TrainActivation activation;
    private TrainMovement movement;
    private TrainCancellation cancellation;
    private ChangeOfOrigin changeOfOrigin;
    private ChangeOfIdentity changeOfIdentity;
    private TrainReinstatement reinstatement;
    private TrainStatus status = TrainStatus.INITIAL;
    private Schedule schedule;
    private boolean scheduleFail;
    private final Collection<TrainMovement> movements = new ArrayList<>();

    public Trust( int toc, String id )
    {
        super( toc, id );
    }

    //<editor-fold defaultstate="collapsed" desc="Accessors">
    public synchronized TrainActivation getActivation()
    {
        return activation;
    }

    public synchronized TrainMovement getMovement()
    {
        return movement;
    }

    public synchronized TrainCancellation getCancellation()
    {
        return cancellation;
    }

    public synchronized ChangeOfOrigin getChangeOfOrigin()
    {
        return changeOfOrigin;
    }

    public synchronized ChangeOfIdentity getChangeOfIdentity()
    {
        return changeOfIdentity;
    }

    public synchronized TrainReinstatement getReinstatement()
    {
        return reinstatement;
    }

    public synchronized TrainStatus getStatus()
    {
        return status;
    }

    public synchronized TrainLocation getLocation()
    {
        return location;
    }

    public synchronized Schedule getSchedule()
    {
        return schedule;
    }

    public synchronized void setSchedule( Schedule schedule )
    {
        this.schedule = schedule;
        scheduleFail = schedule == null;
    }

//</editor-fold>
    public synchronized boolean hasMovement()
    {
        return movement != null;
    }

    public synchronized long getDelay()
    {
        return movement == null ? 0L : movement.getDelay();
    }

    private void touch( long t, long s )
    {
        touch( t );
        location = TrainLocationFactory.INSTANCE.getTrainLocationByStanox( s );
    }

    @Override
    public synchronized void accept( TrustMovement t )
    {
        if( t instanceof TrainMovement ) {
            movement = (TrainMovement) t;
            movements.add( movement );
            status = TrainStatus.getByDelay( movement.getDelay() );
            touch( movement.getActual_timestamp(), movement.getLoc_stanox() );
        }
        else if( t instanceof TrainActivation ) {
            activation = (TrainActivation) t;
            status = TrainStatus.ACTIVATED;
            touch( activation.getCreation_timestamp(), activation.getSched_origin_stanox() );
        }
        else if( t instanceof TrainCancellation ) {
            cancellation = (TrainCancellation) t;
            status = TrainStatus.CANCELLED;
            touch( cancellation.getCanx_timestamp(), cancellation.getLoc_stanox() );
        }
        else if( t instanceof ChangeOfOrigin ) {
            changeOfOrigin = (ChangeOfOrigin) t;
            touch( changeOfOrigin.getCoo_timestamp(), changeOfOrigin.getLoc_stanox() );
        }
        else if( t instanceof ChangeOfIdentity ) {
            changeOfIdentity = (ChangeOfIdentity) t;
            touch( changeOfIdentity.getEvent_timestamp() );
        }
        else if( t instanceof TrainReinstatement ) {
            reinstatement = (TrainReinstatement) t;
            touch( reinstatement.getReinstatement_timestamp(), reinstatement.getLoc_stanox() );
        }
    }

    public Collection<TrainMovement> getMovements()
    {
        return movements;
    }

    /**
     * Has a train got an activation record?
     *
     * @param t
     *          <p>
     * @return
     */
    public static boolean isActivated( Trust t )
    {
        return t != null && t.getActivation() != null;
    }

    public static boolean isScheduled( Trust t )
    {
        return t != null && t.getSchedule() != null;
    }

    public synchronized boolean isScheduleFail()
    {
        return scheduleFail;
    }

    /**
     * Has a train got a movement record?
     *
     * @param t
     *          <p>
     * @return
     */
    public static boolean isMovement( Trust t )
    {
        return t != null && t.getMovement() != null;
    }

    /**
     * Has a train got a cancellation record?
     *
     * @param t
     *          <p>
     * @return
     */
    public static boolean isCancelled( Trust t )
    {
        return t != null && t.getCancellation() != null;
    }

    /**
     * Is a train delayed?
     *
     * Note: Delayed here is >=5m but we put an upper limit as delay can appear to be 45 years for when they are off route
     *
     * @param t
     *          <p>
     * @return
     */
    public static boolean isDelayed( Trust t )
    {
        if( !isMovement( t ) ) {
            return false;
        }
        long delay = t.getDelay();
        return delay >= 300 && delay < 86400;
    }

    public static boolean isOffRoute( Trust t )
    {
        if( !isMovement( t ) ) {
            return false;
        }
        TrainMovement m = t.getMovement();
        return m.isOffroute_ind();
    }

    public static boolean isTerminated( Trust t )
    {
        if( !isMovement( t ) ) {
            return false;
        }
        TrainMovement m = t.getMovement();
        return m.isTrain_terminated();
    }

}
