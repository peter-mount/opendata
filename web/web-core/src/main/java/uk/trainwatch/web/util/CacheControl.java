/*
 * Copyright 2014 Peter T Mount.
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
package uk.trainwatch.web.util;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Peter T Mount
 */
public enum CacheControl
{

    /**
     * No caching
     */
    NONE( 0L ),
    /**
     * Cache for 1 hour
     */
    HOUR( 3600000L ),
    /**
     * Cache for 2 hours
     */
    TWO_HOURS( 7300000L ),
    /**
     * Cache for 1 day
     */
    DAY( 86400000L ),
    /**
     * Cache for 1 week
     */
    WEEK( 604800000L ),
    /**
     * Cache for 1 month
     */
    MONTH( 2592000000L ),
    /**
     * Never expires.
     * <p>
     * This is actually 365 days to keep within the specification limit of 1 year
     */
    NEVER( 31536000000L );
    private final long expires;
    private final String control;

    private CacheControl( long expires )
    {
        this.expires = expires;
        // max-age is in seconds and is max age in cache
        // s-maxage is used in some cdn's
        // no-transform hints to intermediary caches not to transform the image
        int maxAge = (int) (expires / 1000L);
        if( maxAge > 0 )
        {
            control = "public, max-age=" + maxAge + ", s-maxage=" + maxAge + ", no-transform";
        }
        else
        {
            control = "public, must-revalidate, no-transform, proxy-revalidate";
        }
    }

    public void addHeaders( HttpServletResponse response )
    {
        response.addHeader( "Cache-Control", control );
        response.addDateHeader( "Expires", System.currentTimeMillis() + expires );
    }

}
