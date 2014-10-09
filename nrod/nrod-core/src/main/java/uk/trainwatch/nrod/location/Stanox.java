/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.location;

/**
 *
 * @author peter
 */
public class Stanox
        extends LocationKey
{

    /**
     * Ensures that the supplied stanox is within the range 0...99999
     * <p>
     * @param stanox stanox
     * <p>
     * @return corrected stanox
     */
    public static long stanox( long stanox )
    {
        if( stanox < 0 )
        {
            return 0;
        }
        if( stanox > 100000 )
        {
            return stanox % 100000;
        }
        return stanox;
    }

    public Stanox( long id )
    {
        super( id );
    }

}
