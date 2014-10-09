/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.model;

/**
 *
 * @author peter
 */
public class OperatorPPM
        extends AbstractPPM
{

    private int code;
    private String keySymbol;
    private String name;

    public int getCode()
    {
        return code;
    }

    public void setCode( int code )
    {
        this.code = code;
    }

    public String getKeySymbol()
    {
        return keySymbol;
    }

    public void setKeySymbol( String keySymbol )
    {
        this.keySymbol = keySymbol;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @Override
    public int hashCode()
    {
        int hash = super.hashCode();
        hash = 43 * hash + this.code;
        hash = 43 * hash + (this.keySymbol != null ? this.keySymbol.hashCode() : 0);
        hash = 43 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( !super.equals( obj ) )
        {
            return false;
        }
        if( !(obj instanceof OperatorPPM) )
        {
            return false;
        }
        final OperatorPPM other = (OperatorPPM) obj;
        if( this.code != other.code )
        {
            return false;
        }
        if( (this.keySymbol == null) ? (other.keySymbol != null) : !this.keySymbol.equals( other.keySymbol ) )
        {
            return false;
        }
        if( (this.name == null) ? (other.name != null) : !this.name.equals( other.name ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "OperatorPPM{" + super.toString() + ",code=" + code + ", keySymbol=" + keySymbol + ", name=" + name + '}';
    }
}
