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
 * It's usually used when a source feed is in JSON and we need to import it natively
 * in the database - as databases usually handle xml better than JSON.
 * @author peter
 */
public class JsonToXmlTransformer
{

    private final Reader in;
    private final Writer out;
    private final boolean elementMode;

    /**
     * Constructor with element mode true
     *
     * @param in          Reader
     * @param out         Writer
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
                    attr( attrs, n, c1 ->
                  {
                      switch( c1 )
                      {
                          case '\\':
                              escape();
                              break;
                          case '"':
                              return false;
                          default:
                              out.write( c1 );
                      }

                      return true;
                    } );
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

                default:
                    attr( attrs, n, c1 ->
                  {
                      switch( c1 )
                      {
                          case ',':
                          case ' ':
                          case '}':
                              return false;
                          default:
                              out.write( c1 );
                              return true;
                      }
                    } );
                    return false;
            }
        } );
    }

    private void escape()
            throws IOException
    {
        run( c ->
        {
            switch( c )
            {
                case '"':
                    out.write( "&quot;" );
                    return false;
                case '\'':
                    out.write( "&apos;" );
                    return false;
                // Handle \n \r etc here?
                default:
                    out.write( c );
            }
            return false;
        } );
    }

    private void run( Task t )
            throws IOException
    {
        int c;
        while( (c = in.read()) > -1 )
        {
            if( !t.parse( c ) )
            {
                return;
            }
        }
    }

    @FunctionalInterface
    private static interface Task
    {

        boolean parse( int c )
                throws IOException;
    }

}
