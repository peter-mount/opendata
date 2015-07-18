/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.xml;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A simple Json to XML transformer which can handle any size JSON files into XML.
 *
 * It's usually used when a source feed is in JSON and we need to import it natively in the database - as databases usually
 * handle xml better than JSON.
 *
 * @author peter
 */
public class JsonToXmlTransformer
{

    private static final int WRITE_BUFFER_SIZE = 1024;
    private char writeBuffer[];

    private final Reader in;
    private final Writer out;
    private final boolean elementMode;

    /**
     * Constructor with element mode true
     *
     * @param in  Reader
     * @param out Writer
     */
    public JsonToXmlTransformer( Reader in, Writer out )
    {
        this( in, out, true );
    }

    /**
     * elementMode defines if keys in objects are xml attributes or child elements.
     *
     * Set to false if you cannot guarantee ordering when you know an object value can be either another object or a list.
     *
     * If no values are objects or lists then it's 100% safe to use true.
     *
     * @param in          Reader
     * @param out         Writer
     * @param elementMode true for attributes, false for elements
     */
    public JsonToXmlTransformer( Reader in, Writer out, boolean elementMode )
    {
        this.in = in;
        this.out = out;
        this.elementMode = elementMode;
    }

    public void run()
            throws IOException
    {
        run( c ->
        {
            switch( c )
            {
                case '[':
                    list();
                    break;
                case '{':
                    break;
                default:
                    break;
            }
            return true;
        } );
    }

    private void list()
            throws IOException
    {
        list( "L" );
    }

    private void list( String n )
            throws IOException
    {
        attr( null, n, c ->
      {
          switch( c )
          {
              case ']':
                  return false;
              case '[':
                  list();
                  break;
              case '{':
                  object();
                  break;
              default:
                  break;
          }
          return true;
        } );
    }

    private void object()
            throws IOException
    {
        object( "O" );
    }

    private void object( String n )
            throws IOException
    {
        // Add as attribute? First non-attr means rest as elements
        AtomicBoolean attrs = new AtomicBoolean( elementMode );

        out.write( '<' );
        out.write( n );

        run( c ->
        {
            switch( c )
            {
                case '}':
                    return false;

                case '"':
                    key( attrs );
                    break;
                default:
                    break;
            }
            return true;
        } );

        if( attrs.get() )
        {
            out.write( "/>" );
        }
        else
        {
            out.write( "</" );
            out.write( n );
            out.write( '>' );
        }
    }

    private void key( AtomicBoolean attrs )
            throws IOException
    {
        StringBuilder s = new StringBuilder();
        run( c ->
        {
            if( c == '"' )
            {
                String n = s.toString();

                // TfL special. Attributes starting with $ are stripped out
                if( n.startsWith( "$" ) )
                {
                    // They are always strings?
                    // This strips out about 100K from the Tfl Feeds
                    run( c1 -> c1 != '"' );
                    run( c1 -> c1 != '"' );
                }
                else
                {
                    val( attrs, n );
                }
                return false;
            }
            else
            {
                s.append( (char) c );
                return true;
            }
        } );
    }

    /**
     * Write an attribute.
     *
     * If attrs is non null and true then we write it as an attribute. If attrs is null or false then we write it as an element.
     *
     * @param attrs AtomicBoolean
     * @param n     name of element
     * @param t     task to generate element
     * @throws IOException
     */
    private void attr( AtomicBoolean attrs, String n, Task t )
            throws IOException
    {
        if( attrs != null && attrs.get() )
        {
            out.write( ' ' );
            out.write( n );
            out.write( "=\"" );
            run( t );
            out.write( '"' );
        }
        else
        {
            out.write( '<' );
            out.write( n );
            out.write( '>' );
            run( t );
            out.write( "</" );
            out.write( n );
            out.write( '>' );
        }
    }

    /**
     * Set element mode. Once run all attributes are elements.
     *
     * Usually used when we encounter a list or object
     *
     * @param attrs
     * @throws IOException
     */
    private void useElements( AtomicBoolean attrs )
            throws IOException
    {
        if( attrs.getAndSet( false ) )
        {
            out.write( '>' );
        }
    }

    private void val( AtomicBoolean attrs, String n )
            throws IOException
    {
        run( c ->
        {
            switch( c )
            {
                case ':':
                case ' ':
                case '\n':
                case '\r':
                    return true;

                case '"':
                    attr( attrs, n, c1 -> escape(c1) || (c1 != '"' && write( c1 )) );
                    return false;

                // List as the value
                case '[':
                    useElements( attrs );
                    list( n );
                    return false;

                // Object as the value
                case '{':
                    useElements( attrs );
                    object( n );
                    return false;

                // Non quoted value, i.e. number, true, false or null
                default:
                    attr( attrs, n, c1 -> escape( c1 ) || (!(c1 == ',' || c1 == ' ' || c1 == '}') && write( c1 )) );
                    return false;
            }
        } );
    }

    /**
     * Escape the next character if \ is found
     *
     * In lambdas, use {@code (c == '\\' && escape())} as a clause.
     *
     * @param c character to test for escape
     * @return false if not an escape or true if an escape and the end of stream has NOT been reached
     * @throws IOException
     */
    private boolean escape( int c )
            throws IOException
    {
        if( c == '\\' )
        {
            int c1 = in.read();
            return c > -1 && write( c );
        }
        else
        {
            return false;
        }
    }

    /**
     * Wrapper for the {@link Writer#write(int)} method to use when writing values. This method will handle xml escaping of
     * protected characters.
     *
     * @param c
     * @return always true
     * @throws IOException
     */
    private boolean write( int c )
            throws IOException
    {
        switch( c )
        {
            case '&':
                out.write( "&amp;" );
                break;
            case '<':
                out.write( "&lt;" );
                break;
            case '>':
                out.write( "&gt;" );
                break;
            case '"':
                out.write( "&quot;" );
                break;
            case '\'':
                out.write( "&apos;" );
                break;
            default:
                out.write( c );
                break;
        }
        return true;
    }

    private void run( Task t )
            throws IOException
    {
        int c;
        do
        {
            c = in.read();
        } while( c > -1 && t.parse( c ) );
    }

    @FunctionalInterface
    private static interface Task
    {

        boolean parse( int c )
                throws IOException;
    }

}
