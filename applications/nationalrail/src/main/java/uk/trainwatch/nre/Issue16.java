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
package uk.trainwatch.nre;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

/**
 * A temporary hack to fix Issue 16 where the NRE push port feed suddenly stops running.
 * <p>
 * So here if we have no messages after 3 minutes we then die and restart.
 * <p>
 * We use 3 minutes as NRE will queue for 5 minutes
 * <p>
 * @author peter
 */
public final class Issue16
        extends TimerTask
        implements Consumer<String>
{

    private LocalDateTime lastMessage;
    private final Timer timer;

    @SuppressWarnings("LeakingThisInConstructor")
    public Issue16()
    {
        lastMessage = LocalDateTime.now();
        timer = new Timer( true );
        timer.schedule( this, 60000L, 60000L );
    }

    private synchronized LocalDateTime getLastMessage()
    {
        return lastMessage;
    }

    @Override
    public synchronized void accept( String t )
    {
        lastMessage = LocalDateTime.now();
    }

    @Override
    public void run()
    {
        Duration dur = Duration.between( getLastMessage(), LocalDateTime.now() );
        if( dur.getSeconds( ) >= 180 ) {
            System.exit( 999 );
        }
    }

}
