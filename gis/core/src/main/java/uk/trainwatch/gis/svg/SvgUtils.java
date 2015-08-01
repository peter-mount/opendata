/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis.svg;

import java.sql.SQLException;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 *
 * @author peter
 */
public class SvgUtils
{

    private static final String SVG_NAMESPACE = "http://www.w3.org/2000/svg";

    /**
     * Append a number to a StringBuilder. Unlike {@link StringBuilder#append(double)} if the result ends with ".0" then that is stripped off meaning an integer
     * is written.
     * <p>
     * @param sb
     * @param v
     */
    public static void append( StringBuilder sb, double v )
    {
        sb.append( v );

        // If we appended '.0' on the end then strip it - saves a lot of space
        int l = sb.length() - 2;
        if( l > 0 && sb.charAt( l ) == '.' && sb.charAt( l + 1 ) == '0' ) {
            sb.setLength( l );
        }

    }

    /**
     * Returns the String value of a double. Unlike {@link String#valueOf(double)} if the string ends with ".0" then it is stripped off returning an integer.
     * <p>
     * @param v
     *          <p>
     * @return
     */
    public static String toString( double v )
    {
        String s = String.valueOf( v );
        return s.endsWith( ".0" ) ? s.substring( 0, s.length() - 2 ) : s;
    }

    public static void writeSvgDocument( XMLStreamWriter w, IntSupplier width, IntSupplier height, SvgRenderer renderer, SvgBounds bounds )
            throws SQLException,
                   XMLStreamException,
                   ClassNotFoundException
    {
        try {
            w.setDefaultNamespace( SVG_NAMESPACE );

            w.writeComment( bounds.toString() );

            w.writeStartElement( SVG_NAMESPACE, "svg" );
            w.writeAttribute( "version", "1.1" );

            w.writeAttribute( "height", String.valueOf( height.getAsInt() ) );
            w.writeAttribute( "width", String.valueOf( width.getAsInt() ) );

            w.writeStartElement( "desc" );
            w.writeAttribute( "style", "-webkit-tap-highlight-color: rgba(0, 0, 0, 0);" );
            w.writeCharacters( "Created by TrainWatch uktra.in" );
            w.writeEndElement();

            w.writeStartElement( "defs" );
            w.writeAttribute( "style", "-webkit-tap-highlight-color: rgba(0, 0, 0, 0);" );
            w.writeEndElement();

            renderer.render( w, bounds );

            w.writeEndElement();
        }
        finally {
            w.writeEndDocument();
            w.close();
        }
    }

    /**
     * Write common attributes to the {@link XMLStreamWriter}
     * <p>
     * @param w        XMLStreamWriter
     * @param cssClass Supplier of css class or null if none
     * @param fill     Supplier of fill or null if none
     * @param stroke   Supplier of stroke or null if none
     * <p>
     * @throws XMLStreamException on failure
     */
    public static void writeSvgAttributes( XMLStreamWriter w,
                                           Supplier<String> cssClass,
                                           Supplier<String> fill,
                                           Supplier<String> stroke )
            throws XMLStreamException
    {

        if( cssClass != null ) {
            w.writeAttribute( "class", cssClass.get() );
        }

        if( fill != null ) {
            w.writeAttribute( "fill", fill.get() );
        }

        if( stroke != null ) {
            w.writeAttribute( "stroke", stroke.get() );
        }
    }

    /**
     * Write an SVG Path element
     * <p>
     * @param w        XMLStreamWriter
     * @param cssClass Supplier of css class or null if none
     * @param fill     Supplier of fill or null if none
     * @param stroke   Supplier of stroke or null if none
     * @param path     Supplier of the path. Must not be null
     */
    public static void writePath( XMLStreamWriter w,
                                  Supplier<String> cssClass,
                                  Supplier<String> fill,
                                  Supplier<String> stroke,
                                  Supplier<String> path
    )
    {
        try {
            w.writeStartElement( "path" );
            writeSvgAttributes( w, cssClass, fill, stroke );
            w.writeAttribute( "d", path.get() );
            w.writeEndElement();
        }
        catch( XMLStreamException ex ) {
            throw new RuntimeException( ex );
        }
    }

    /**
     * Write an SVG text element
     * <p>
     * @param w        XMLStreamWriter
     * @param cssClass Supplier of css class or null if none
     * @param x        Supplier of x coordinate, not null
     * @param y        Supplier of y coordinate, not null
     * @param angle    Supplier of angle. Ignored if null or it returns 0
     * @param fill     Supplier of fill or null if none
     * @param stroke   Supplier of stroke or null if none
     * @param text     Supplier of text, must not be null
     */
    public static void writeText( XMLStreamWriter w,
                                  Supplier<String> cssClass,
                                  DoubleSupplier x,
                                  DoubleSupplier y,
                                  IntSupplier angle,
                                  Supplier<String> fill,
                                  Supplier<String> stroke,
                                  Supplier<String> text
    )
    {
        try {
            w.writeStartElement( "text" );
            writeSvgAttributes( w, cssClass, fill, stroke );
            w.writeAttribute( "x", SvgUtils.toString( x.getAsDouble() ) );
            w.writeAttribute( "y", SvgUtils.toString( y.getAsDouble() ) );

            if( angle != null ) {
                int ang = angle.getAsInt();
                if( ang != 0 ) {
                    w.writeAttribute( "transform", "rotate(" + ang + ")" );
                }
            }
            w.writeCharacters( text.get() );
            w.writeEndElement();
        }
        catch( XMLStreamException ex ) {
            throw new RuntimeException( ex );
        }
    }

    public static void writeCircle( XMLStreamWriter w,
                                    Supplier<String> cssClass,
                                    Supplier<String> fill,
                                    Supplier<String> stroke,
                                    DoubleSupplier x,
                                    DoubleSupplier y,
                                    DoubleSupplier r )
    {
        try {
            w.writeStartElement( "circle" );
            writeSvgAttributes( w, cssClass, fill, stroke );
            w.writeAttribute( "cx", SvgUtils.toString( x.getAsDouble() ) );
            w.writeAttribute( "cy", SvgUtils.toString( y.getAsDouble() ) );
            w.writeAttribute( "r", SvgUtils.toString( r.getAsDouble() ) );
            w.writeEndElement();
        }
        catch( XMLStreamException ex ) {
            throw new RuntimeException( ex );
        }
    }

}
