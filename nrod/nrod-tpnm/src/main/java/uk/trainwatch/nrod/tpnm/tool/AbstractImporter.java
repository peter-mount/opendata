/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.tool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 * @param <T> Type to accept
 * @param <V> Type to process
 */
public abstract class AbstractImporter<T, V extends T>
        implements SQLConsumer<T>
{

    @SuppressWarnings("NonConstantLogger")
    protected final Logger log = Logger.getLogger( getClass().getName() );

    protected final Connection con;
    private final String name;
    private final int commit;
    private int count;

    public AbstractImporter( Connection con )
    {
        this( con, 0 );
    }

    public AbstractImporter( Connection con, int commit )
    {
        this.con = con;
        this.name = getClass().getSimpleName();
        this.commit = commit;
    }

    public AbstractImporter( Connection con, String name )
    {
        this( con, 0, name );
    }

    public AbstractImporter( Connection con, int commit, String name )
    {
        this.con = con;
        this.name = name;
        this.commit = commit;
    }

    @Override
    public final void accept( T t )
            throws SQLException
    {
        if( t == null ) {
            return;
        }

        if( count == 0 ) {
            log.log( Level.INFO, () -> "Beginning " + name );
        }

        count++;

        process( (V) t );

        if( commit > 0 && (count % commit) == 0 ) {
            log.log( Level.INFO, "Committing..." );
            con.commit();
        }
    }

    protected abstract void process( V v )
            throws SQLException;

    public final int getCount()
    {
        return count;
    }

    protected final boolean isFirst()
    {
        return count == 1;
    }

}
