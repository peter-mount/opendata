/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.trust;

import java.util.function.Consumer;
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

    private TrainActivation activation;
    private TrainMovement movement;
    private TrainCancellation cancellation;
    private ChangeOfOrigin changeOfOrigin;
    private ChangeOfIdentity changeOfIdentity;
    private TrainReinstatement reinstatement;

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
//</editor-fold>

    @Override
    public synchronized void accept( TrustMovement t )
    {
        if( t instanceof TrainMovement ) {
            movement = (TrainMovement) t;
        }
        else if( t instanceof TrainActivation ) {
            activation = (TrainActivation) t;
        }
        else if( t instanceof TrainCancellation ) {
            cancellation = (TrainCancellation) t;
        }
        else if( t instanceof ChangeOfOrigin ) {
            changeOfOrigin = (ChangeOfOrigin) t;
        }
        else if( t instanceof ChangeOfIdentity ) {
            changeOfIdentity = (ChangeOfIdentity) t;
        }
        else if( t instanceof TrainReinstatement ) {
            reinstatement = (TrainReinstatement) t;
        }
        touch();
    }

}
