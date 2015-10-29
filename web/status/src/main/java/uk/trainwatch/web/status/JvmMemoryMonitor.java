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
package uk.trainwatch.web.status;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import uk.trainwatch.util.DaemonThreadFactory;
import uk.trainwatch.util.counter.BoundedDeque;
import uk.trainwatch.util.counter.MinMaxMonitor;
import uk.trainwatch.util.counter.RateStatistics;

/**
 * Background process to record the memory statistics
 * <p>
 * @author peter
 */
@ApplicationScoped
public class JvmMemoryMonitor
{

    @Inject
    private RateStatistics rateStatistics;

    public void start()
    {
        final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

        schedule( memoryMXBean::getHeapMemoryUsage, "jvm.heap" );
        schedule( memoryMXBean::getNonHeapMemoryUsage, "jvm.nonheap" );
    }

    private MinMaxMonitor getMonitor( String prefix )
    {
        MinMaxMonitor m = new MinMaxMonitor( rateStatistics.getConsumer( prefix + ".min", BoundedDeque::getLastValue ),
                                             rateStatistics.getConsumer( prefix + ".max", BoundedDeque::getLastValue ) );
        m.accept( 0L );
        return m;
    }

    private void schedule( Supplier<MemoryUsage> s, String prefix )
    {
        MinMaxMonitor committed = getMonitor( prefix + ".committed" );
        MinMaxMonitor used = getMonitor( prefix + ".used" );
        MinMaxMonitor max = getMonitor( prefix + ".max" );
        MinMaxMonitor init = getMonitor( prefix + ".init" );

        DaemonThreadFactory.INSTANCE.scheduleAtFixedRate( () -> {
            MemoryUsage m = s.get();
            committed.accept( m.getCommitted() );
            used.accept( m.getUsed() );
            max.accept( m.getMax() );
            init.accept( m.getInit() );
        }, 1L, 1L, TimeUnit.SECONDS );
    }

}
