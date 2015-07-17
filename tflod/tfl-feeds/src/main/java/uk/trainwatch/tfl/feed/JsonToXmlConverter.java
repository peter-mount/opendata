/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.feed;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.function.Function;

/**
 * A mapping function that will transform a Json string into XML suitable for importing into PostgreSQL.
 *
 * Although this is generic it's primarily targeted at the TfL rest feeds which consist of a json list followed by one or more
 * objects. In this case we will generate xml with an outer &lt;F&gt; element containing one or more &lt;O&gt; elements for each
 * object.
 *
 *
 * @author peter
 */
public class JsonToXmlConverter
        implements Function<String, String>
{

    /**
     * Transform a Json String into an XML String
     *
     * @param t
     * @return
     */
    @Override
    public String apply( String t )
    {
        StringWriter w = new StringWriter();
        try
        {
            transform( new StringReader( t ), w );
        } catch( IOException ex )
        {
            throw new UncheckedIOException( ex );
        }
        return w.toString();
    }

    /**
     * Transform Json from a {@link Reader} writing the XML to the given {@link Writer}.
     *
     * Note: When this method returns both the Reader and Writer are still open.
     *
     * @param in  Reader containing Json
     * @param out Writer to receive XML
     * @throws IOException
     */
    public static void transform( Reader in, Writer out )
            throws IOException
    {
        State.parse( State.START, in, out );
    }

    public static void main( String... args )
            throws Exception
    {
        try( Writer w = new FileWriter( "/home/peter/test.xml" ) )
        {
            try( FileReader r = new FileReader( "/home/peter/Downloads/Arrivals.json" ) )
            {
                transform( r, w );
            }
        }
    }

    private static enum State
    {

        START
                {

                    @Override
                    public State parse( Reader r, Writer out, StringBuilder name )
                    throws IOException
                    {
                        switch( r.read() )
                        {
                            case -1:
                                return null;

                            case '[':
                                parse( LIST, r, out );
                                break;

                            case '{':
                                parse( OBJECT, r, out );
                                break;
                        }
                        return this;
                    }

                },
        LIST
                {
                    @Override
                    protected void start( Writer out, StringBuilder name )
                    throws IOException
                    {
                        out.write( "<L>" );
                    }

                    @Override
                    protected void end( Writer out, StringBuilder name )
                    throws IOException
                    {
                        out.write( "</L>" );
                    }

                    @Override
                    public State parse( Reader r, Writer out, StringBuilder name )
                    throws IOException
                    {
                        return START.parse( r, out, name ) == null ? null : this;
                    }

                },
        OBJECT
                {
                    @Override
                    protected void start( Writer out, StringBuilder name )
                    throws IOException
                    {
                        out.write( "<O>" );
                    }

                    @Override
                    protected void end( Writer out, StringBuilder name )
                    throws IOException
                    {
                        out.write( "</O>" );
                    }

                    @Override
                    public State parse( Reader r, Writer out, StringBuilder name )
                    throws IOException
                    {
                        switch( r.read() )
                        {
                            case -1:
                            case '}':
                                return null;

                            case '"':
                                parse( State.KEY, r, out );
                                break;

                            default:
                                break;
                        }
                        return this;
                    }

                },
        KEY
                {

                    @Override
                    public State parse( Reader r, Writer out, StringBuilder name )
                    throws IOException
                    {
                        int c;
                        switch( c = r.read() )
                        {
                            case -1:
                            case '}':
                            case ']':
                                return null;

                            case '"':
                                return VAL_SEP;
                            default:
                                name.append( (char) c );
                                return this;
                        }
                    }

                },
        KEY_SKIP
                {

                    @Override
                    public State parse( Reader r, Writer out, StringBuilder name )
                    throws IOException
                    {
                        int c = r.read();
                        if( c == '"' )
                        {
                            return null;
                        }
                        return this;
                    }

                },
        VAL_SKIP
                {

                    @Override
                    public State parse( Reader r, Writer out, StringBuilder name )
                    throws IOException
                    {
                        switch( r.read() )
                        {
                            case -1:
                            case '}':
                            case ',':
                                return null;
                            case '"':
                                return KEY_SKIP;
                            default:
                                return this;
                        }
                    }

                },
        VAL_SEP
                {

                    @Override
                    public State parse( Reader r, Writer out, StringBuilder name )
                    throws IOException
                    {
                        switch( r.read() )
                        {
                            case -1:
                                return null;
                            case ':':
                            case ' ':
                                return this;
                            case '"':
                                return VAL_QUOTE;
                            default:
                                return VAL;
                        }
                    }

                },
        VAL
                {

                    @Override
                    public State parse( Reader r, Writer out, StringBuilder name )
                    throws IOException
                    {
                        int c;
                        switch( c = r.read() )
                        {
                            case -1:
                            case ',':
                                return null;
                            case '[':
                                parse( LIST, r, out );
                                return null;
                            case '}':
                                parse( OBJECT, r, out );
                                return null;
                            default:
                                out.write( c );
                                return this;
                        }
                    }

                },
        VAL_QUOTE
                {
                    @Override
                    protected void start( Writer out, StringBuilder name )
                    throws IOException
                    {
                        out.write( ' ' );
                        out.write( name.toString() );
                        out.write( "=\"" );
                    }

                    @Override
                    protected void end( Writer out, StringBuilder name )
                    throws IOException
                    {
                        out.write( "\"" );
                    }

                    @Override
                    public State parse( Reader r, Writer out, StringBuilder name )
                    throws IOException
                    {
                        int c = r.read();
                        if( c == -1 || c == '"' )
                        {
                            //out.write( '"' );
                            return VAL_ENDQUOTE;
                        }
                        out.write( c );
                        return this;
                    }

                },
        VAL_ENDQUOTE
                {

                    @Override
                    public State parse( Reader r, Writer out, StringBuilder name )
                    throws IOException
                    {
                        switch( r.read() )
                        {
                            case -1:
                            case ',':
                            case '}':
                            case ']':
                                return null;

                            default:
                                return this;
                        }
                    }

                };

        public abstract State parse( Reader r, Writer out, StringBuilder name )
                throws IOException;

        protected void start( Writer out, StringBuilder name )
                throws IOException
        {
        }

        protected void end( Writer out, StringBuilder name )
                throws IOException
        {
        }

        public static void parse( State state, Reader r, Writer out )
                throws IOException
        {
            StringBuilder name = new StringBuilder();

            state.start( out, name );

            State s = state;
            while( s != null )
            {
                s = s.parse( r, out, name );
            }

            state.end( out, name );
        }
    }
}
