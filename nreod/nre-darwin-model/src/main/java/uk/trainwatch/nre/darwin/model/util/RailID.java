/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.model.util;

/**
 * Interface implemented by JAXB objects that refer to a rid field (Rail ID).
 *
 * Having those objects implement this interface means we can remove a lot of instanceOf and casting to get the rid value,
 * improving code readability and performance.
 *
 * @author peter
 */
public interface RailID
{

    String getRid();

    /**
     * Mapping function to cast a JAXB object to a RailID
     *
     * @param o JAXB Object
     * @return o cast as RailID or null if o does not implement RailID
     */
    static RailID castRailId( Object o )
    {
        return o instanceof RailID ? (RailID) o : null;
    }
}
