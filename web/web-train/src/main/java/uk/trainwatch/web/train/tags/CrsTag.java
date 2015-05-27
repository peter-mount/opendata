/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train.tags;

import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.nrod.location.TrainLocation;

/**
 *
 * @author Peter T Mount
 */
public class CrsTag
        extends AbstractLocationTag
{

    @Override
    protected TrainLocation getLocationRef( String value )
    {
        return DarwinReferenceManager.INSTANCE.getLocationRefFromCrs( value );
    }

}
