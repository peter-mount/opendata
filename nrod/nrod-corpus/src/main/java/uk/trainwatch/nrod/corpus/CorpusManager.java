/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.corpus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import uk.trainwatch.util.sql.SQL;

/**
 * Handles access to Corpus
 * <p>
 * @author peter
 */
public enum CorpusManager
{

    INSTANCE;
    private DataSource dataSource;

    void setDataSource( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    /**
     * Perform a lookup from CORPUS for a stanox.
     * <p>
     * This may return more than one entry for the same location.
     * <p>
     * @param stanox
     *               <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    public Collection<Corpus> getStanox( int stanox )
            throws SQLException
    {
        return lookup( "stanox", stanox );
    }

    public Collection<Corpus> getNLC( int nlc )
            throws SQLException
    {
        return lookup( "nlc", nlc );
    }

    public Collection<Corpus> getUIC( int uic )
            throws SQLException
    {
        return lookup( "uic", uic );
    }

    public Collection<Corpus> get3Alpha( String talpha )
            throws SQLException
    {
        if( talpha == null || talpha.length() != 3 )
        {
            return Collections.emptyList();
        }
        return lookup( "talpha", talpha.toUpperCase() );
    }

    public Collection<Corpus> getTiploc( String tiploc )
            throws SQLException
    {
        if( tiploc == null )
        {
            return Collections.emptyList();
        }
        return lookup( "tiploc", tiploc.toUpperCase() );
    }

    private Collection<Corpus> lookup( String field, Object value )
            throws SQLException
    {
        if( value == null || "".equals( value ) )
        {
            return Collections.emptyList();
        }
        try( Connection con = dataSource.getConnection() )
        {
            try( PreparedStatement ps = con.prepareStatement( "SELECT * FROM reference.corpus WHERE " + field + "=?" ) )
            {
                ps.setObject( 1, value );
                return SQL.stream( ps, Corpus.fromSQL ).
                        collect( Collectors.toList() );
            }
        }

    }
}
