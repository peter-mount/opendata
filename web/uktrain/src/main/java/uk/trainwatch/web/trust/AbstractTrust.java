/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.trust;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import uk.trainwatch.util.TimeUtils;

/**
 * Base details of a train in Trust
 * <p>
 * @author peter
 */
public class AbstractTrust
        implements Comparable<AbstractTrust>
{

    private final int toc;
    private final String id;
    private final int hashCode;
    private LocalDateTime touched;

    public AbstractTrust( int toc, String id )
    {
        this.toc = toc;
        this.id = id;
        hashCode = this.toc * 19 + Objects.hashCode( this.id );
        touch();
    }

//<editor-fold defaultstate="collapsed" desc="Cache expiry logic">
    /**
     * Any updates to this object must invoke this method to prevent expiry
     */
    protected final synchronized void touch()
    {
        touched = LocalDateTime.now();
    }

    /**
     * touch the object only if t represents a time after the last time it was touched.
     *
     * @param t
     */
    protected final synchronized void touch( long t )
    {
        LocalDateTime now = TimeUtils.getLocalDateTime( t );
        if( touched != null && touched.isBefore( now ) )
        {
            touched = now;
        } else
        {
            touched = LocalDateTime.now();
        }
    }

    /**
     * Used by the expiry algorithm to determine if this entry has expired
     * <p>
     * @return
     */
    final synchronized boolean isExpired( LocalDateTime expiryTime )
    {
        return touched.isBefore( expiryTime );
    }

    final synchronized LocalDateTime getTouched()
    {
        return touched;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Access to train identity">
    public final String getId()
    {
        return id;
    }

    public final int getToc()
    {
        return toc;
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="hashCode/equals fixed to toc & id">
    @Override
    public final int hashCode()
    {
        return hashCode;
    }

    @Override
    public final boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }
        final AbstractTrust other = (AbstractTrust) obj;
        return this.toc == other.toc && Objects.equals( this.id, other.id );
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Comparator, order by when entry was last touched">
    @Override
    public final int compareTo( AbstractTrust o )
    {
        // Use accessor here as it's synchronized
        return -getTouched().
                compareTo( o.getTouched() );
    }
//</editor-fold>
}
