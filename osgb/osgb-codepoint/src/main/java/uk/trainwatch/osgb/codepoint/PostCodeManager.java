/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.osgb.codepoint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
public enum PostCodeManager
{

    INSTANCE;

    private DataSource dataSource;

    /**
     * Normalise a postcode into the database format.
     *
     * This is crude but follows the rules:
     * <ol>
     * <li>Always starts with a letter</li>
     * <li>Always ends with digit & 2 letters</li>
     * <li>Database requires 7 digits so add a space when needed</li>
     * <li>Special case, W2 is only postcode area with 2 spaces</li>
     * </ol>
     *
     * @param postcode Postcode to normalise
     * @return normalised postcode or null if invalid
     */
    public String normalise( String postcode )
    {

        if( postcode == null || postcode.isEmpty() )
        {
            return null;
        }

        String pc = postcode.toUpperCase();

        int i = pc.indexOf( ' ' );
        String prefix = pc.substring( 0, Math.min( i > 0 ? i : pc.length() - 3, 4 ) );
        if( !(prefix.matches( "[A-Z][0-9A-Z][0-9A-Z]+$" ) || "W2".equals( prefix )) )
        {
            return null;
        }

        if( pc.length() < 3 || !pc.matches( ".*[0-9][A-Z][A-Z]$" ) )
        {
            return null;
        }
        String suffix = pc.substring( pc.length() - 3 );

        return String.format( "%-4.4s%3.3s", prefix, suffix );
    }

    /**
     * Lookup a single postcode
     *
     * @param postcode
     * @return
     */
    public PostCode lookup( String postcode )
    {
        String pc = normalise( postcode );
        if( pc != null && !pc.isEmpty() )
        {
            try( Connection con = dataSource.getConnection() )
            {
                try( PreparedStatement ps = SQL.prepare( con, "SELECT * FROM gis.codepoint WHERE postcode=?", pc ) )
                {
                    return SQL.stream( ps, PostCode.fromSQL ).findAny().orElse( null );
                }
            } catch( SQLException ex )
            {
                Logger.getLogger( PostCodeManager.class.getName() ).log( Level.SEVERE, null, ex );
            }
        }
        return null;
    }

}
