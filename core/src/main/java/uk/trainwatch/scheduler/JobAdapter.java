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
public class JobAdapter
        implements Job
{

    private static final Logger LOG = Logger.getLogger( JobAdapter.class.getName() );

    @Override
    public void execute( JobExecutionContext context )
            throws JobExecutionException
    {
        JobDetail d = context.getJobDetail();
        JobDataMap m = d.getJobDataMap();
        String name = m.getString( "bean" );
        LOG.log( Level.INFO, () -> "name " + name );

        Bean<?> bean = CDIUtils.getBean( name );
        LOG.log( Level.INFO, () -> "bean " + bean );

        Job job = CDIUtils.getInstance( (Bean<Job>) bean, Job.class );
        LOG.log( Level.INFO, () -> "job " + job );

        job.execute( context );
    }

}
