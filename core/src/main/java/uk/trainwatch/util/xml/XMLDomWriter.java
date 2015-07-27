/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.xml;

import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.function.Consumer;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.w3c.dom.ls.LSSerializerFilter;

/**
 * A {@link Consumer} which will pass {@link Node}'s to a {@link LSSerializer}
 * <p>
 * @author peter
 */
public class XMLDomWriter
        implements Consumer<Node>
{

    private final LSOutput output;
    private final LSSerializer serializer;

    private XMLDomWriter()
            throws ClassNotFoundException,
                   InstantiationException,
                   IllegalAccessException
    {
        DOMImplementationRegistry reg = DOMImplementationRegistry.newInstance();
        DOMImplementation impl = reg.getDOMImplementation( "XML 3.0" );
        DOMImplementationLS domImplLs = DOMImplementationLS.class.cast( impl.getFeature( "LS", "3.0" ) );
        output = domImplLs.createLSOutput();
        serializer = domImplLs.createLSSerializer();
    }

    /**
     * Construct with output going to an {@link OutputStream}
     * <p>
     * @param os OutputStream
     * <p>
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public XMLDomWriter( OutputStream os )
            throws ClassNotFoundException,
                   InstantiationException,
                   IllegalAccessException
    {
        this();
        output.setByteStream( os );
    }

    /**
     * Construct with output going to a {@link Writer}
     * <p>
     * @param w Writer
     * <p>
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public XMLDomWriter( Writer w )
            throws ClassNotFoundException,
                   InstantiationException,
                   IllegalAccessException
    {
        this();
        output.setCharacterStream( w );
    }

    //<editor-fold defaultstate="collapsed" desc="LSOutput delegates">
    public Writer getCharacterStream()
    {
        return output.getCharacterStream();
    }

    public void setCharacterStream( Writer characterStream )
    {
        output.setCharacterStream( characterStream );
    }

    public OutputStream getByteStream()
    {
        return output.getByteStream();
    }

    public void setByteStream( OutputStream byteStream )
    {
        output.setByteStream( byteStream );
    }

    public String getSystemId()
    {
        return output.getSystemId();
    }

    public void setSystemId( String systemId )
    {
        output.setSystemId( systemId );
    }

    public String getEncoding()
    {
        return output.getEncoding();
    }

    public void setEncoding( String encoding )
    {
        output.setEncoding( encoding );
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LSSerializer delegates">
    public String getNewLine()
    {
        return serializer.getNewLine();
    }

    public void setNewLine( String newLine )
    {
        serializer.setNewLine( newLine );
    }

    public LSSerializerFilter getFilter()
    {
        return serializer.getFilter();
    }

    public void setFilter( LSSerializerFilter filter )
    {
        serializer.setFilter( filter );
    }
    //</editor-fold>

    @Override
    public void accept( Node t )
    {
        serializer.write( t, output );
    }

    public static String toXml( Node t )
    {
        try
        {
            StringWriter w = new StringWriter();
            new XMLDomWriter( w ).accept( t );
            return w.toString();
        } catch( ClassNotFoundException | InstantiationException | IllegalAccessException ex )
        {
            return null;
        }
    }
}
