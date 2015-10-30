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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.spi.Bean;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import uk.trainwatch.util.CDIUtils;

/**
 *
 * @author peter
 */
public class MethodAdapter
        implements Job
{

    private static final Logger LOG = Logger.getLogger( MethodAdapter.class.getName() );

    @Override
    public void execute( JobExecutionContext context )
            throws JobExecutionException
    {
        JobDetail d = context.getJobDetail();
        JobDataMap m = d.getJobDataMap();
        String name = m.getString( "bean" );
        Method meth = (Method) m.get( "method" );

        try {
            Bean<Object> bean = CDIUtils.getBean( name );
            Object job = CDIUtils.getInstance( (Bean<Object>) bean, Object.class );

            LOG.log( Level.INFO, () -> "job " + job );

            meth.invoke( job );
        }
        catch( IllegalAccessException |
               IllegalArgumentException |
               InvocationTargetException ex ) {
            LOG.log( Level.SEVERE, ex, () -> "Failed running " + name + " " + meth );
            throw new JobExecutionException( ex, false );
        }
    }

}
