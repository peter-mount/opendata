/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.location;

import java.io.Serializable;

/**
 *
 * @author peter
 */
public class TrainLocationID
        extends LocationKey
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    public TrainLocationID( long id )
    {
        super( id );
    }
}
