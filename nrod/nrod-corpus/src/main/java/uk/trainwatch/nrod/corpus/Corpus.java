/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.corpus;

import java.util.Objects;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLResultSetHandler;

/**
 *
 * @author peter
 */
public class Corpus
{

    public static final SQLResultSetHandler<Corpus> fromSQL = rs -> new Corpus(
            rs.getLong( 1 ),
            SQL.getInt( rs, 2 ),
            SQL.getInt( rs, 3 ),
            rs.getString( 4 ),
            rs.getString( 4 ),
            SQL.getInt( rs, 2 ),
            rs.getString( 4 ),
            rs.getString( 4 )
    );

    private final long id;
    private final Integer stanox;
    private final Integer uic;
    private final String talpha;
    private final String tiploc;
    private final Integer nlc;
    private final String nlcDesc;
    private final String nlcDesc16;

    public Corpus( long id, Integer stanox, Integer uic, String talpha, String tiploc, Integer nlc, String nlcDesc, String nlcDesc16 )
    {
        this.id = id;
        this.stanox = stanox;
        this.uic = uic;
        this.talpha = talpha;
        this.tiploc = tiploc;
        this.nlc = nlc;
        this.nlcDesc = nlcDesc;
        this.nlcDesc16 = nlcDesc16;
    }

    public long getId()
    {
        return id;
    }

    public Integer getStanox()
    {
        return stanox;
    }

    public Integer getUic()
    {
        return uic;
    }

    public String getTalpha()
    {
        return talpha;
    }

    public String getTiploc()
    {
        return tiploc;
    }

    public Integer getNlc()
    {
        return nlc;
    }

    public String getNlcDesc()
    {
        return nlcDesc;
    }

    public String getNlcDesc16()
    {
        return nlcDesc16;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 97 * hash + Objects.hashCode( this.stanox );
        hash = 97 * hash + Objects.hashCode( this.uic );
        hash = 97 * hash + Objects.hashCode( this.talpha );
        hash = 97 * hash + Objects.hashCode( this.tiploc );
        hash = 97 * hash + Objects.hashCode( this.nlc );
        hash = 97 * hash + Objects.hashCode( this.nlcDesc );
        hash = 97 * hash + Objects.hashCode( this.nlcDesc16 );
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        final Corpus other = (Corpus) obj;
        return this.id == other.id
               && Objects.equals( this.stanox, other.stanox )
               && Objects.equals( this.uic, other.uic )
               && Objects.equals( this.talpha, other.talpha )
               && Objects.equals( this.tiploc, other.tiploc )
               && Objects.equals( this.nlc, other.nlc )
               && Objects.equals( this.nlcDesc, other.nlcDesc )
               && Objects.equals( this.nlcDesc16, other.nlcDesc16 );
    }

}
