/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.model.util;

/**
 * Interface implemented by JAXB objects that refer to a tpl field (TIPLOC).
 *
 * Having those objects implement this interface means we can remove a lot of instanceOf and casting to get the tpl value,
 * improving code readability and performance.
 *
 * @author peter
 */
public interface TplLocation
{

    /**
     * The TIPLOC
     *
     * @return
     */
    String getTpl();

    /**
     * Mapping function to cast a JAXB object to a TplLocation
     *
     * @param o JAXB Object
     * @return o cast as TplLocation or null if o does not implement TplLocation
     */
    static TplLocation castTplLocation( Object o )
    {
        return o instanceof TplLocation ? (TplLocation) o : null;
    }
}
