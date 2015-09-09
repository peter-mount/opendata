/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

import java.lang.ref.WeakReference;
import uk.trainwatch.web.ldb.cache.TrainCache;

/**
 *
 * @author peter
 */
class WeakTrainProxy
        extends AbstractTrainProxy
{

    private static final long serialVersionUID = 1L;
    private WeakReference<Train> train;

    WeakTrainProxy( String rid )
    {
        super( rid );
    }

    @Override
    protected Train getCachedTrain()
    {
        Train t = super.getCachedTrain();
        if( t == null ) {
            train = null;
        }
        else {
            train = new WeakReference<>( t );
        }
        return t;
    }

    @Override
    protected Train getTrain()
    {
        return train == null || train.get() == null ? getCachedTrain() : train.get();
    }

}
