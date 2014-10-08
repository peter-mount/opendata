/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 * @author Peter T Mount
 */
public class TimeUtils
{

    public static final ZoneId UTC = ZoneId.of( "UTC" );

    public static final LocalDateTime getLocalDateTime( final long timestamp )
    {
        Instant instant = Instant.ofEpochMilli( timestamp );
        Clock clock = Clock.fixed( instant, UTC );
        return LocalDateTime.now( clock );
    }

}
