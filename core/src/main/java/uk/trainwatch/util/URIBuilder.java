/*
 * Copyright 2016 peter.
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
package uk.trainwatch.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author peter
 */
public interface URIBuilder
{

    URIBuilder scheme( String s );

    URIBuilder authority( String s );

    URIBuilder userInfo( String s );

    URIBuilder schemeSpecificPart( String s );

    URIBuilder host( String s );

    URIBuilder path( String s );

    QueryBuilder query();

    URIBuilder query( String s );

    URIBuilder fragment( String s );

    URIBuilder port( int p );

    URI build()
            throws URISyntaxException;

    static interface QueryBuilder
    {

        QueryBuilder add( String name, String value );

        default QueryBuilder add( String name, int value )
        {
            return add( name, String.valueOf( value ) );
        }

        default QueryBuilder add( String name, long value )
        {
            return add( name, String.valueOf( value ) );
        }

        default QueryBuilder add( String name, boolean value )
        {
            return add( name, String.valueOf( value ) );
        }

        URIBuilder endQuery();

    }

    static URIBuilder createHTTP()
    {
        return create().scheme( "http" );
    }

    static URIBuilder createHTTPS()
    {
        return create().scheme( "https" );
    }

    static URIBuilder create( String uri )
            throws URISyntaxException
    {
        return create( new URI( uri ) );
    }

    static URIBuilder create( URL url )
            throws URISyntaxException
    {
        return create( url.toURI() );
    }

    static URIBuilder create( URI uri )
    {
        return create()
                .authority( uri.getAuthority() )
                .fragment( uri.getFragment() )
                .host( uri.getHost() )
                .path( uri.getPath() )
                .port( uri.getPort() )
                .scheme( uri.getScheme() )
                .schemeSpecificPart( uri.getSchemeSpecificPart() )
                .userInfo( uri.getUserInfo() );
    }

    static URIBuilder create()
    {
        return new URIBuilder()
        {
            private String scheme;
            private String authority;
            private String userInfo;
            private String ssp;
            private String host;
            private String path;
            private String query;
            private String fragment;
            private Integer port;

            @Override
            public URIBuilder scheme( String s )
            {
                scheme = s;
                return this;
            }

            @Override
            public URIBuilder authority( String s )
            {
                authority = s;
                return this;
            }

            @Override
            public URIBuilder userInfo( String s )
            {
                userInfo = s;
                return this;
            }

            @Override
            public URIBuilder schemeSpecificPart( String s )
            {
                ssp = s;
                return this;
            }

            @Override
            public URIBuilder host( String s )
            {
                host = s;
                return this;
            }

            @Override
            public URIBuilder path( String s )
            {
                path = s;
                return this;
            }

            @Override
            public QueryBuilder query()
            {
                URIBuilder b = this;
                final List<String> params = new ArrayList<>();

                return new QueryBuilder()
                {

                    @Override
                    public QueryBuilder add( String name, String value )
                    {
                        Objects.requireNonNull( name, "Name must not be null" );
                        try {
                            if( value == null ) {
                                params.add( URLEncoder.encode( name, "UTF-8" ) );
                            }
                            else {
                                params.add( URLEncoder.encode( name, "UTF-8" ) + "=" + URLEncoder.encode( value, "UTF-8" ) );
                            }
                            return this;
                        }
                        catch( UnsupportedEncodingException ex ) {
                            throw new IllegalArgumentException( name + "=" + value, ex );
                        }
                    }

                    @Override
                    public URIBuilder endQuery()
                    {
                        if( !params.isEmpty() ) {
                            String q = params.stream().collect( Collectors.joining( "&" ) );
                            query = query == null ? q : (query + "&" + q);
                        }
                        return b;
                    }
                };
            }

            @Override
            public URIBuilder query( String s )
            {
                query = s;
                return this;
            }

            @Override
            public URIBuilder fragment( String s )
            {
                fragment = s;
                return this;
            }

            @Override
            public URIBuilder port( int p )

            {
                if( p < -1 || p == 0 || p > 65535 ) {
                    throw new IllegalArgumentException( "Port " + p );
                }

                port = p;
                return this;
            }

            @Override
            public URI build()
                    throws URISyntaxException
            {
                if( ssp != null ) {
                    return new URI( Objects.requireNonNull( scheme, "scheme" ),
                                    Objects.requireNonNull( ssp, "ssp" ),
                                    Objects.requireNonNull( fragment, "fragment" ) );
                }

                if( authority != null ) {
                    return new URI( Objects.requireNonNull( scheme, "scheme" ),
                                    Objects.requireNonNull( authority, "authority" ),
                                    path,
                                    query,
                                    fragment );
                }
                return new URI( Objects.requireNonNull( scheme, "scheme" ),
                                userInfo,
                                Objects.requireNonNull( host, "host" ),
                                port == null ? -1 : port,
                                path,
                                query,
                                fragment );
            }

        };
    }
}
