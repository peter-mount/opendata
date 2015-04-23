/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.train.tags;

import uk.trainwatch.nre.darwin.model.ctt.referenceschema.LocationRef;
import uk.trainwatch.nre.darwin.reference.DarwinReferenceManager;

/**
 *
 * @author Peter T Mount
 */
public class CrsTag
        extends AbstractLocationTag
{

    @Override
    protected LocationRef getLocationRef( String value )
    {
        return DarwinReferenceManager.INSTANCE.getLocationRefFromTiploc( value );
    }

}
