/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.model;

/**
 *
 * @author peter
 */
public class PPM
        implements Comparable<PPM>
{

    private String rag;
    private int value;

    public String getRag()
    {
        return rag;
    }

    public void setRag( String rag )
    {
        this.rag = rag;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue( int value )
    {
        this.value = value;
    }

    @Override
    public int compareTo( PPM o )
    {
        return Integer.compare( value, o.value );
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 41 * hash + (this.rag != null ? this.rag.hashCode() : 0);
        hash = 41 * hash + value;
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( !(obj instanceof PPM) )
        {
            return false;
        }
        final PPM other = (PPM) obj;
        if( (this.rag == null) ? (other.rag != null) : !this.rag.equals( other.rag ) )
        {
            return false;
        }
        if( this.value != other.value )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "PPM{" + "rag=" + rag + ", value=" + value + '}';
    }

}
