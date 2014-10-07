/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonWriter;

/**
 * A set of utility methods to support JSR353
 * <p>
 * @author Peter T Mount
 */
public class JsonUtils
{

    /**
     * Function to convert a {@link JsonValue} to a {@link JsonArray} or null if it's not valid
     */
    public static final Function<? super JsonValue, JsonArray> getArray = v -> v instanceof JsonArray ? (JsonArray) v : null;

    /**
     * Function to convert a {@link JsonValue} to a {@link JsonObject} or null if it's not valid
     */
    public static final Function<? super JsonValue, JsonObject> getObject = v -> v instanceof JsonObject ? (JsonObject) v : null;

    public static final Function<String, JsonStructure> parseJson = JsonUtils::decode;

    /**
     * Function to parse a {@link String} into a {@link JsonObject} or null if it's not valid
     */
    public static final Function<String, JsonObject> parseJsonObject = parseJson.andThen( getObject );

    /**
     * Function to parse a {@link String} into a {@link JsonArray} or null if it's not valid
     */
    public static final Function<String, JsonArray> parseJsonArray = parseJson.andThen( getArray );

    /**
     * Function to convert a {@link JsonStructure} into a String
     */
    public static final Function<? super JsonStructure, String> toString = JsonUtils::encode;

    /**
     * Obtain a {@link JsonStructure} from a String.
     * <p>
     * @param s String
     * <p>
     * @return JsonStructure
     */
    public static JsonStructure decode( String s )
    {
        if( s == null || s.isEmpty() )
        {
            return null;
        }
        final StringReader r = new StringReader( Objects.requireNonNull( s ) );
        try( JsonReader jr = Json.createReader( r ) )
        {
            return jr.read();
        }
    }

    /**
     * Convert a {@link JsonStructure} to a String
     * <p>
     * @param s <p>
     * @return
     */
    public static String encode( JsonStructure s )
    {
        final StringWriter w = new StringWriter();
        try( JsonWriter jw = Json.createWriter( w ) )
        {
            jw.write( s );
            return w.toString();
        }
    }

    /**
     * Convenience to build an empty JsonArray
     * <p>
     * @return
     */
    public static JsonArray emptyArray()
    {
        return Json.createArrayBuilder().
                build();

    }

    /**
     * Get the named JsonArray from a JsonObject. Unlike {@link JsonObject#getJsonArray(java.lang.String)} if null would
     * be returned we return an empty instance
     * <p>
     * @param o    JsonObject
     * @param name name
     * <p>
     * @return JsonArray, never null
     */
    public static JsonArray getJsonArray( JsonObject o, String name )
    {
        JsonArray a = o == null ? null : o.getJsonArray( name );
        return a == null ? emptyArray() : a;
    }

    /**
     * Return a stream from a JsonArray.
     * <p>
     * If the array is null or empty then an empty stream is returned.
     * <p>
     * @param <T> type
     * @param a   array
     * <p>
     * @return Stream, never null
     */
    public static <T extends JsonValue> Stream<T> stream( JsonArray a )
    {
        if( a == null || a.isEmpty() )
        {
            return Stream.empty();
        }
        return (Stream<T>) a.stream();
    }

    /**
     * Return a stream from a named JsonArray within a JsonObject.
     * <p>
     * If the array is null or empty then an empty stream is returned.
     * <p>
     * @param <T> Type
     * @param o   JsonObject
     * @param n   Name of array
     * <p>
     * @return Stream, never null
     */
    public static <T extends JsonValue> Stream<T> stream( JsonObject o, String n )
    {
        return stream( o.getJsonArray( n ) );
    }

    public static int getInt( JsonObject o, String n )
    {
        return getInt( o, n, 0 );
    }

    public static int getInt( JsonObject o, String n, int defaultValue )
    {
        JsonValue v = o.get( n );
        if( v == null )
        {
            return defaultValue;
        }
        switch( v.getValueType() )
        {
            case NUMBER:
                JsonNumber jn = (JsonNumber) v;
                return jn.intValue();
            case STRING:
                JsonString s = (JsonString) v;
                return Integer.parseInt( s.getString() );
            case TRUE:
                return 1;
            case FALSE:
                return 0;
            case NULL:
                return defaultValue;
            default:
                throw new ClassCastException( "Cannot convert " + v.getClass() + " to Integer" );
        }
    }

    public static long getLong( JsonObject o, String n )
    {
        return getLong( o, n, 0L );
    }

    public static long getLong( JsonObject o, String n, long defaultValue )
    {
        JsonValue v = o.get( n );
        if( v == null )
        {
            return defaultValue;
        }
        switch( v.getValueType() )
        {
            case NUMBER:
                JsonNumber jn = (JsonNumber) v;
                return jn.longValue();
            case STRING:
                JsonString s = (JsonString) v;
                return Long.parseLong( s.getString() );
            case TRUE:
                return 1L;
            case FALSE:
                return 0L;
            case NULL:
                return defaultValue;
            default:
                throw new ClassCastException( "Cannot convert " + v.getClass() + " to Long" );
        }
    }

    public static double getDouble( JsonObject o, String n, double defaultValue )
    {
        JsonValue v = o.get( n );
        if( v == null )
        {
            return defaultValue;
        }
        switch( v.getValueType() )
        {
            case NUMBER:
                JsonNumber jn = (JsonNumber) v;
                return jn.doubleValue();
            case STRING:
                JsonString s = (JsonString) v;
                return Double.parseDouble( s.getString() );
            case TRUE:
                return 1.0;
            case FALSE:
                return 0.0;
            case NULL:
                return defaultValue;
            default:
                throw new ClassCastException( "Cannot convert " + v.getClass() + " to Double" );
        }
    }

    public static String getString( JsonObject o, String n )
    {
        return getString( o, n, null );
    }

    public static String getString( JsonObject o, String n, String defaultValue )
    {
        JsonValue v = o.get( n );
        if( v == null )
        {
            return defaultValue;
        }
        switch( v.getValueType() )
        {
            case NUMBER:
                JsonNumber jn = (JsonNumber) v;
                return jn.toString();
            case STRING:
                JsonString s = (JsonString) v;
                return s.getString();
            case TRUE:
                return "true";
            case FALSE:
                return "false";
            case NULL:
                return defaultValue;
            default:
                return v.toString();
        }
    }

    public static boolean getBoolean( JsonObject o, String n )
    {
        return getBoolean( o, n, false );
    }

    public static boolean getBoolean( JsonObject o, String n, boolean defaultValue )
    {
        JsonValue v = o.get( n );
        if( v == null )
        {
            return defaultValue;
        }
        switch( v.getValueType() )
        {
            case NUMBER:
                JsonNumber jn = (JsonNumber) v;
                return !jn.bigIntegerValue().
                        equals( BigInteger.ZERO );
            case STRING:
                JsonString s = (JsonString) v;
                return Boolean.parseBoolean( s.getString() );
            case TRUE:
                return true;
            case FALSE:
                return false;
            case NULL:
                return defaultValue;
            default:
                return Boolean.parseBoolean( v.toString() );
        }
    }

    /**
     * If the JsonObject is not null apply the mapping function to create an object from it.
     * <p>
     * @param <T> Type of final object
     * @param o   JsonObject
     * @param f   mapping function
     * <p>
     * @return resultant object or null if not present.
     */
    public static <T> T computeIfPresent( JsonObject o, Function<JsonObject, T> f )
    {
        return o == null ? null : f.apply( o );
    }

    /**
     * Retrieve a JsonObject from another and if present apply the mapping function to create an object from it.
     * <p>
     * @param <T> Type of final object
     * @param o   JsonObject
     * @param n   name of sub-object
     * @param f   mapping function
     * <p>
     * @return resultant object or null if not present.
     */
    public static <T> T computeIfPresent( JsonObject o, String n, Function<JsonObject, T> f )
    {
        return computeIfPresent( o.getJsonObject( n ), f );
    }

    /**
     * If the JsonObject is not null apply the mapping function to create an object from it.
     * <p>
     * @param <T> Type of final object
     * @param o   JsonObject
     * @param s   supplier of the mapping function
     * <p>
     * @return resultant object or null if not present.
     */
    public static <T> T computeIfPresent( JsonObject o, Supplier<Function<JsonObject, T>> s )
    {
        return o == null ? null : s.get().
                apply( o );
    }

    /**
     * Retrieve a JsonObject from another and if present apply the mapping function to create an object from it.
     * <p>
     * @param <T> Type of final object
     * @param o   JsonObject
     * @param n   name of sub-object
     * @param s   supplier of the mapping function
     * <p>
     * @return resultant object or null if not present.
     */
    public static <T> T computeIfPresent( JsonObject o, String n, Supplier<Function<JsonObject, T>> s )
    {
        return computeIfPresent( o.getJsonObject( n ), s );
    }
}
