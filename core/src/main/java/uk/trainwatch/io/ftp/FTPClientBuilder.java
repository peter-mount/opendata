/*
 * Copyright 2015 peter.
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
package uk.trainwatch.io.ftp;

import java.util.function.Consumer;
import uk.trainwatch.util.Consumers;

public class FTPClientBuilder
{

    String proxy = null;
    int proxyPort;
    String proxyUser;
    String proxyPass;

    Consumer<String> consumer;
    boolean printCommands;
    boolean debuggingEnabled;

    int keepAliveTimeout = 0;
    int controlKeepAliveReplyTimeout = 0;
    boolean useEpsvWithIPv4 = false;
    boolean localActive = false;

    boolean binaryTransfer = true;
    boolean listHiddenFiles = false;

    public FTPClientBuilder setLogger( Consumer<String> consumer )
    {
        this.consumer = consumer;
        return this;
    }

    public FTPClientBuilder logger( Consumer<String> consumer )
    {
        this.consumer = Consumers.andThen( this.consumer, consumer );
        return this;
    }

    public FTPClientBuilder enableDebugging()
    {
        debuggingEnabled = true;
        return this;
    }

    public FTPClientBuilder printCommands()
    {
        printCommands = true;
        return this;
    }

    /**
     * HTTP proxy
     * <p>
     * @param proxy
     *              <p>
     * @return
     */
    public FTPClientBuilder proxy( String proxy )
    {
        return proxy( proxy, 80 );
    }

    public FTPClientBuilder proxy( String proxy, int proxyPort )
    {
        this.proxy = proxy;
        this.proxyPort = proxyPort;
        return this;
    }

    public FTPClientBuilder proxyPort( int proxyPort )
    {
        this.proxyPort = proxyPort;
        return this;
    }

    public FTPClientBuilder proxyUser( String proxyUser )
    {
        this.proxyUser = proxyUser;
        return this;
    }

    public FTPClientBuilder proxyPass( String proxyPass )
    {
        this.proxyPass = proxyPass;
        return this;
    }

    /**
     * TCP Keep Alive Timeout
     * <p>
     * @param keepAliveTimeout
     *                         <p>
     * @return
     */
    public FTPClientBuilder keepAliveTimeout( int keepAliveTimeout )
    {
        this.keepAliveTimeout = keepAliveTimeout;
        return this;
    }

    /**
     * TCP Keep Alive Timeout for replies
     * <p>
     * @param controlKeepAliveReplyTimeout
     *                                     <p>
     * @return
     */
    public FTPClientBuilder controlKeepAliveReplyTimeout( int controlKeepAliveReplyTimeout )
    {
        this.controlKeepAliveReplyTimeout = controlKeepAliveReplyTimeout;
        return this;
    }

    public FTPClientBuilder useEpsvWithIPv4()
    {
        useEpsvWithIPv4 = true;
        return this;
    }

    /**
     * Local or remote (behind firewall) server
     * <p>
     * @return
     */
    public FTPClientBuilder localActive()
    {
        localActive = true;
        return this;
    }

    /**
     * Use Passive mode (default) for when behind a firewall
     * <p>
     * @return
     */
    public FTPClientBuilder passive()
    {
        localActive = false;
        return this;
    }

    /**
     * Set binary mode (default)
     * <p>
     * @return
     */
    public FTPClientBuilder binary()
    {
        binaryTransfer = true;
        return this;
    }

    public FTPClientBuilder ascii()
    {
        binaryTransfer = false;
        return this;
    }

    /**
     * List hidden files
     * <p>
     * @param listHiddenFiles
     *                        <p>
     * @return
     */
    public FTPClientBuilder listHiddenFiles()
    {
        listHiddenFiles = true;
        return this;
    }

    public FTPClient build()
    {
        return new DefaultFTPClient( this );
    }

}
