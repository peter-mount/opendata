/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.model.util;

import javax.xml.datatype.XMLGregorianCalendar;
import uk.trainwatch.nre.darwin.model.ppt.schema.Pport;

/**
 * Utility methods added to {@link Pport} to provide additional functionality.
 *
 * You usually use these against the {@link Pport} class not this interface.
 *
 * @author peter
 */
public interface PportUtil
{

    XMLGregorianCalendar getTs();

    String getVersion();

    Pport.UR getUR();

    boolean isSetUR();

    /**
     * Create a clone of this {@link Pport} with just the meta-data defined, no other data.
     *
     * @return
     */
    default Pport cloneMeta()
    {
        Pport p = new Pport();
        p.setTs( getTs() );
        p.setVersion( getVersion() );

        if( isSetUR() ) {
            Pport.UR ur0 = getUR();
            Pport.UR ur1 = new Pport.UR();
            ur1.setRequestID( ur0.getRequestID() );
            ur1.setRequestSource( ur0.getRequestSource() );
            ur1.setUpdateOrigin( ur0.getUpdateOrigin() );
            p.setUR( ur1 );
        }

        return p;
    }

    /**
     * Optionally clone the meta-data of this instance if the supplied instance is null
     *
     * @param p Pport
     * <p>
     * @return p if not null otherwise a clone of this instance
     */
    default Pport cloneMetaIfNull( Pport p )
    {
        return p == null ? cloneMeta() : p;
    }
}
