/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train.tags;

import javax.inject.Inject;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;
import uk.trainwatch.nrod.location.TrainLocation;

/**
 *
 * @author Peter T Mount
 */
public class TiplocTag
        extends AbstractLocationTag
{

    @Inject
    private DarwinReferenceManager darwinReferenceManager;

    @Override
    protected TrainLocation getLocationRef( String value )
    {
        return darwinReferenceManager.getLocationRefFromTiploc( value );
    }

}
