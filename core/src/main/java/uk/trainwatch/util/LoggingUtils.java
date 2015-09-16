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
package uk.trainwatch.util;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 *
 * @author peter
 */
public class LoggingUtils
{

    private static final Logger LOG = Logger.getAnonymousLogger();

    /**
     * Limit all loggers to the specified level - useful to keep a lot of log spam out of the console
     * <p>
     * @param level The required Level
     */
    @SuppressWarnings("UseOfObsoleteCollectionType")
    public static void setLevel( Level level )
    {
        Enumeration<String> en = LogManager.getLogManager().getLoggerNames();
        while( en.hasMoreElements() ) {
            Logger.getLogger( en.nextElement() ).setLevel( level );
        }
    }

    /**
     * Simple banner message
     * <p>
     * @param s
     */
    public static void logBanner( String s )
    {
        char c[] = new char[s.length()];
        Arrays.fill( c, '=' );
        String l = String.valueOf( c );
        LOG.log( Level.INFO, l );
        LOG.log( Level.INFO, s );
        LOG.log( Level.INFO, l );
    }

}
