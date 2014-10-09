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
package uk.trainwatch.util;

import java.time.LocalTime;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Peter T Mount
 */
public class TimeUtilsTest
{

    @Test
    public void testGetLocalTime_hhmmss()
    {
        assertEquals( LocalTime.of( 0, 0, 0 ), TimeUtils.getLocalTime( "000000" ) );
        assertEquals( LocalTime.of( 0, 0, 10 ), TimeUtils.getLocalTime( "000010" ) );
        assertEquals( LocalTime.of( 0, 10, 0 ), TimeUtils.getLocalTime( "001000" ) );
        assertEquals( LocalTime.of( 10, 0, 0 ), TimeUtils.getLocalTime( "100000" ) );
        assertEquals( LocalTime.of( 23, 59, 59 ), TimeUtils.getLocalTime( "235959" ) );
    }

    @Test
    public void testGetLocalTime_hhmm()
    {
        assertEquals( LocalTime.of( 0, 0 ), TimeUtils.getLocalTime( "0000" ) );
        assertEquals( LocalTime.of( 0, 0 ), TimeUtils.getLocalTime( "0000" ) );
        assertEquals( LocalTime.of( 0, 10 ), TimeUtils.getLocalTime( "0010" ) );
        assertEquals( LocalTime.of( 10, 0 ), TimeUtils.getLocalTime( "1000" ) );
        assertEquals( LocalTime.of( 23, 59 ), TimeUtils.getLocalTime( "2359" ) );
    }

    @Test
    public void testGetLocalTime_hh_mm_ss()
    {
        assertEquals( LocalTime.of( 0, 0, 0 ), TimeUtils.getLocalTime( "00:00:00" ) );
        assertEquals( LocalTime.of( 0, 0, 10 ), TimeUtils.getLocalTime( "00:00:10" ) );
        assertEquals( LocalTime.of( 0, 10, 0 ), TimeUtils.getLocalTime( "00:10:00" ) );
        assertEquals( LocalTime.of( 10, 0, 0 ), TimeUtils.getLocalTime( "10:00:00" ) );
        assertEquals( LocalTime.of( 23, 59, 59 ), TimeUtils.getLocalTime( "23:59:59" ) );
    }

    @Test
    public void testGetLocalTime_hh_mm()
    {
        assertEquals( LocalTime.of( 0, 0 ), TimeUtils.getLocalTime( "00:00" ) );
        assertEquals( LocalTime.of( 0, 0 ), TimeUtils.getLocalTime( "00:00" ) );
        assertEquals( LocalTime.of( 0, 10 ), TimeUtils.getLocalTime( "00:10" ) );
        assertEquals( LocalTime.of( 10, 0 ), TimeUtils.getLocalTime( "10:00" ) );
        assertEquals( LocalTime.of( 23, 59 ), TimeUtils.getLocalTime( "23:59" ) );
    }

}
