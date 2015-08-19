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
public class CRS
        extends LocationKey
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    public CRS( String key )
    {
        super( key );
    }
}
