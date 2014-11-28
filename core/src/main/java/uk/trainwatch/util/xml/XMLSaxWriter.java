/*
 * Copyright 2014 peter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.trainwatch.util.xml;

import java.io.OutputStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Result;

/**
 * A {@link Consumer} which will pass an object to a {@link BiConsumer} for generating XML using the SAX api.
 * <p>
 * By using SAX this is ideal for use with Streams when generating potentially large XML files.
 * <p>
 * @author peter
 * @param <T>
 */
public class XMLSaxWriter<T>
        implements Consumer<T>,
                   XMLStreamWriter,
                   AutoCloseable
{

    private final XMLStreamWriter xmlStreamWriter;
    private final XMLStreamWriterConsumer<T> converter;

    public XMLSaxWriter( XMLStreamWriter w, XMLStreamWriterConsumer<T> converter )
    {
        this.xmlStreamWriter = w;
        this.converter = converter;
    }

    public XMLSaxWriter( OutputStream os, String encoding, XMLStreamWriterConsumer<T> converter )
            throws XMLStreamException
    {
        this( XMLOutputFactory.newInstance().
                createXMLStreamWriter( os, encoding ), converter );
    }

    public XMLSaxWriter( OutputStream os, XMLStreamWriterConsumer<T> converter )
            throws XMLStreamException
    {
        this( XMLOutputFactory.newInstance().
                createXMLStreamWriter( os ), converter );
    }

    public XMLSaxWriter( Writer w, XMLStreamWriterConsumer<T> converter )
            throws XMLStreamException
    {
        this( XMLOutputFactory.newInstance().
                createXMLStreamWriter( w ), converter );
    }

    public XMLSaxWriter( Result r, XMLStreamWriterConsumer<T> converter )
            throws XMLStreamException
    {
        this( XMLOutputFactory.newInstance().
                createXMLStreamWriter( r ), converter );
    }

    public XMLStreamWriter getXMLStreamWriter()
    {
        return xmlStreamWriter;
    }

    @Override
    public void accept( T t )
    {
        try
        {
            converter.accept( this, t );
        }
        catch( XMLStreamException ex )
        {
            throw new UncheckedXMLStreamException( ex );
        }
    }

    public void writeElement( String localName, Object value )
    {
        writeElement( localName, Objects.toString( value, "" ) );
    }

    public void writeElement( String localName, String value )
    {
        try
        {
            xmlStreamWriter.writeStartElement( localName );
            xmlStreamWriter.writeCharacters( value );
            xmlStreamWriter.writeEndElement();
        }
        catch( XMLStreamException ex )
        {
            throw new UncheckedXMLStreamException( ex );
        }
    }

    public void writeElement( String localName, String delimiter, Object... values )
    {
        writeElement( localName,
                      Arrays.stream( values ).
                      filter( Objects::nonNull ).
                      map( String::valueOf ).
                      collect( Collectors.joining( delimiter ) )
        );
    }

    public void writeElement( String localName, int value )
    {
        writeElement( localName, String.valueOf( value ) );
    }

    public void writeElement( String localName, long value )
    {
        writeElement( localName, String.valueOf( value ) );
    }

    public void writeElement( String localName, double value )
    {
        writeElement( localName, String.valueOf( value ) );
    }

    //<editor-fold defaultstate="collapsed" desc="delegate to underlying XMLStreamWriter">
    @Override
    public void writeStartElement( String localName )
            throws XMLStreamException
    {
        xmlStreamWriter.writeStartElement( localName );
    }

    @Override
    public void writeStartElement( String namespaceURI, String localName )
            throws XMLStreamException
    {
        xmlStreamWriter.writeStartElement( namespaceURI, localName );
    }

    @Override
    public void writeStartElement( String prefix, String localName, String namespaceURI )
            throws XMLStreamException
    {
        xmlStreamWriter.writeStartElement( prefix, localName, namespaceURI );
    }

    @Override
    public void writeEmptyElement( String namespaceURI, String localName )
            throws XMLStreamException
    {
        xmlStreamWriter.writeEmptyElement( namespaceURI, localName );
    }

    @Override
    public void writeEmptyElement( String prefix, String localName, String namespaceURI )
            throws XMLStreamException
    {
        xmlStreamWriter.writeEmptyElement( prefix, localName, namespaceURI );
    }

    @Override
    public void writeEmptyElement( String localName )
            throws XMLStreamException
    {
        xmlStreamWriter.writeEmptyElement( localName );
    }

    @Override
    public void writeEndElement()
            throws XMLStreamException
    {
        xmlStreamWriter.writeEndElement();
    }

    @Override
    public void writeEndDocument()
            throws XMLStreamException
    {
        xmlStreamWriter.writeEndDocument();
    }

    @Override
    public void close()
            throws XMLStreamException
    {
        converter.close( this );
        xmlStreamWriter.writeEndDocument();
        xmlStreamWriter.close();
    }

    @Override
    public void flush()
            throws XMLStreamException
    {
        xmlStreamWriter.flush();
    }

    @Override
    public void writeAttribute( String localName, String value )
            throws XMLStreamException
    {
        xmlStreamWriter.writeAttribute( localName, value );
    }

    @Override
    public void writeAttribute( String prefix, String namespaceURI, String localName, String value )
            throws XMLStreamException
    {
        xmlStreamWriter.writeAttribute( prefix, namespaceURI, localName, value );
    }

    @Override
    public void writeAttribute( String namespaceURI, String localName, String value )
            throws XMLStreamException
    {
        xmlStreamWriter.writeAttribute( namespaceURI, localName, value );
    }

    @Override
    public void writeNamespace( String prefix, String namespaceURI )
            throws XMLStreamException
    {
        xmlStreamWriter.writeNamespace( prefix, namespaceURI );
    }

    @Override
    public void writeDefaultNamespace( String namespaceURI )
            throws XMLStreamException
    {
        xmlStreamWriter.writeDefaultNamespace( namespaceURI );
    }

    @Override
    public void writeComment( String data )
            throws XMLStreamException
    {
        xmlStreamWriter.writeComment( data );
    }

    @Override
    public void writeProcessingInstruction( String target )
            throws XMLStreamException
    {
        xmlStreamWriter.writeProcessingInstruction( target );
    }

    @Override
    public void writeProcessingInstruction( String target, String data )
            throws XMLStreamException
    {
        xmlStreamWriter.writeProcessingInstruction( target, data );
    }

    @Override
    public void writeCData( String data )
            throws XMLStreamException
    {
        xmlStreamWriter.writeCData( data );
    }

    @Override
    public void writeDTD( String dtd )
            throws XMLStreamException
    {
        xmlStreamWriter.writeDTD( dtd );
    }

    @Override
    public void writeEntityRef( String name )
            throws XMLStreamException
    {
        xmlStreamWriter.writeEntityRef( name );
    }

    @Override
    public void writeStartDocument()
            throws XMLStreamException
    {
        xmlStreamWriter.writeStartDocument();
    }

    @Override
    public void writeStartDocument( String version )
            throws XMLStreamException
    {
        xmlStreamWriter.writeStartDocument( version );
    }

    @Override
    public void writeStartDocument( String encoding, String version )
            throws XMLStreamException
    {
        xmlStreamWriter.writeStartDocument( encoding, version );
    }

    @Override
    public void writeCharacters( String text )
            throws XMLStreamException
    {
        xmlStreamWriter.writeCharacters( text );
    }

    @Override
    public void writeCharacters( char[] text, int start, int len )
            throws XMLStreamException
    {
        xmlStreamWriter.writeCharacters( text, start, len );
    }

    @Override
    public String getPrefix( String uri )
            throws XMLStreamException
    {
        return xmlStreamWriter.getPrefix( uri );
    }

    @Override
    public void setPrefix( String prefix, String uri )
            throws XMLStreamException
    {
        xmlStreamWriter.setPrefix( prefix, uri );
    }

    @Override
    public void setDefaultNamespace( String uri )
            throws XMLStreamException
    {
        xmlStreamWriter.setDefaultNamespace( uri );
    }

    @Override
    public void setNamespaceContext( NamespaceContext context )
            throws XMLStreamException
    {
        xmlStreamWriter.setNamespaceContext( context );
    }

    @Override
    public NamespaceContext getNamespaceContext()
    {
        return xmlStreamWriter.getNamespaceContext();
    }

    @Override
    public Object getProperty( String name )
            throws IllegalArgumentException
    {
        return xmlStreamWriter.getProperty( name );
    }
    //</editor-fold>
}
