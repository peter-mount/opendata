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

import uk.trainwatch.util.cdi.NamedImpl;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.BeforeShutdown;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import javax.inject.Named;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import uk.trainwatch.util.CDIUtils;

/**
 *
 * @author peter
 */
public class SchedulerExtension
        implements Extension
{

    private static final Logger LOG = Logger.getLogger( SchedulerExtension.class.getName() );

    private Scheduler scheduler;
    private final Set<Class<?>> jobClasses = new HashSet<>();
    private int id = 0, jid;

    void beforeBeanDiscovery( @Observes BeforeBeanDiscovery type )
            throws SchedulerException
    {
        StdSchedulerFactory fact = new StdSchedulerFactory();
        fact.initialize( getClass().getResourceAsStream( "quartz.properties" ) );
        scheduler = fact.getScheduler();
    }

    <T> void findJobs( @Observes @WithAnnotations({Cron.class}) ProcessAnnotatedType<T> pat, BeanManager beanManager )
    {
        // Ensure we are named otherwise job won't fire as we can't locate it
        AnnotatedType<?> type = pat.getAnnotatedType();
        Class<?> clazz = type.getJavaClass();
        CDIUtils.addTypeAnnotation( pat, Named.class, () -> new NamedImpl( "Schedule_" + (id++) ) );

        if( type.isAnnotationPresent( Cron.class ) ) {
            if( Job.class.isAssignableFrom( clazz ) ) {
                jobClasses.add( clazz );
            }
            else {
                throw new UnsupportedOperationException( "@Cron on type must implement Job" );
            }
        }
        else {
            for( AnnotatedMethod<?> meth: type.getMethods() ) {
                if( meth.isAnnotationPresent( Cron.class ) ) {
                    jobClasses.add( clazz );
                }
            }
        }
    }

    void scheduleJobs( @Observes AfterBeanDiscovery afterBeanDiscovery, BeanManager beanManager )
            throws SchedulerException
    {
        for( Class<?> clazz: jobClasses ) {
            for( AnnotatedType<?> type: afterBeanDiscovery.getAnnotatedTypes( clazz ) ) {
                String name = type.getAnnotation( Named.class ).value();
                if( type.isAnnotationPresent( Cron.class ) ) {
                    Cron cron = type.getAnnotation( Cron.class );
                    schedule( cron.value(), name, JobAdapter.class, null );
                }
                else {
                    for( AnnotatedMethod meth: type.getMethods() ) {
                        if( meth.isAnnotationPresent( Cron.class ) ) {
                            Cron cron = meth.getAnnotation( Cron.class );
                            JobDataMap m = new JobDataMap();
                            m.put( "method", meth.getJavaMember() );
                            schedule( cron.value(), name, MethodAdapter.class, m );
                        }
                    }
                }
            }
        }
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

    private void schedule( String expr, String name, Class<? extends Job> jobClass, JobDataMap m )
            throws SchedulerException
    {
        JobDataMap map = m == null ? new JobDataMap() : m;
        map.put( "bean", name );

        String uid = name + ":" + (jid++);

        Trigger trigger = TriggerBuilder.newTrigger()
                .withSchedule( CronScheduleBuilder.cronSchedule( expr ) )
                .withIdentity( uid )
                .build();

        JobDetail detail = JobBuilder.newJob()
                .ofType( jobClass )
                .withIdentity( uid )
                .usingJobData( map )
                .build();

        scheduler.scheduleJob( detail, trigger );

        LOG.log( Level.INFO, () -> "Scheduled " + name + " with cron " + expr );
    }

}
