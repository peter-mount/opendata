/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

/**
 *
 * @author peter
 */
class TrainProxy
        extends AbstractTrainProxy
{

    private static final long serialVersionUID = 1L;

    TrainProxy( String rid )
    {
        super( rid );
    }

    @Override
    protected Train getTrain()
    {
        return getCachedTrain();
    }

}
