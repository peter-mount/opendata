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
package uk.trainwatch.scheduler;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author peter
 */
public class SchedulerExtension
        implements Extension
{

    private static final Logger LOG = Logger.getLogger( SchedulerExtension.class.getName() );

    private Scheduler scheduler;
    private final Set<Class<Job>> jobClasses = new HashSet<>();

    void beforeBeanDiscovery( @Observes BeforeBeanDiscovery type )
            throws SchedulerException
    {
        StdSchedulerFactory fact = new StdSchedulerFactory();
        fact.initialize( getClass().getResourceAsStream( "quartz.properties" ) );
        scheduler = fact.getScheduler();
    }

    <T> void findJobs( @Observes @WithAnnotations({Cron.class}) ProcessAnnotatedType<T> part, BeanManager beanManager )
    {
        AnnotatedType type = part.getAnnotatedType();
        if( type.isAnnotationPresent( Cron.class ) ) {
            Class<?> clazz = type.getJavaClass();
            if( Job.class.isAssignableFrom( clazz ) ) {
                jobClasses.add( (Class<Job>) clazz );
            }
        }
    }

    void scheduleJobs( @Observes AfterBeanDiscovery afterBeanDiscovery, BeanManager beanManager )
            throws SchedulerException
    {
        scheduleCronJobs();
    }

    void start( @Observes @Initialized(ApplicationScoped.class) Object o )
    {
        try {
            if( !scheduler.isStarted() ) {
                scheduler.start();
            }
        }
        catch( SchedulerException ex ) {
            LOG.log( Level.SEVERE, "Failed to start scheduler", ex );
        }
    }

    void stop( @Observes BeforeShutdown beforeShutdown )
    {
        try {
            if( scheduler.isStarted() ) {
                scheduler.shutdown();
            }
        }
        catch( SchedulerException ex ) {
            LOG.log( Level.SEVERE, null, ex );
        }
    }

    @Produces
    @ApplicationScoped
    Scheduler getScheduler()
    {
        return scheduler;
    }

    private void scheduleCronJobs()
            throws SchedulerException
    {
        for( Class<Job> clazz: jobClasses ) {
            Cron cron = clazz.getAnnotation( Cron.class );
            if( cron != null ) {
                String name = clazz.getName();
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withSchedule( CronScheduleBuilder.cronSchedule( cron.value() ) )
                        .withIdentity( name )
                        .build();

                JobDetail detail = JobBuilder.newJob()
                        .ofType( clazz )
                        .withIdentity( name )
                        .build();

                scheduler.scheduleJob( detail, trigger );

                LOG.log( Level.INFO, () -> "Scheduled " + name + " with cron " + cron.value() );
            }
        }
    }
}
