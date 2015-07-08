/*
 * Copyright 2014 peter.
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
package uk.trainwatch.nrod.rtppm.sql;

import java.io.Serializable;

/**
 * The Operator performance as stored in the database
 *
 * @author peter
 */
public abstract class AbstractOperatorPerformance
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private long id;
    private long date;
    private int operatorId;
    private int run;
    private int ontime;
    private int late;
    private int canceled;
    private int ppm;
    private int rollingPpm;

    public AbstractOperatorPerformance()
    {
    }

    public AbstractOperatorPerformance( long id, long date, int operatorId,
                                        int run,
                                        int ontime, int late, int canceled,
                                        int ppm,
                                        int rollingPpm )
    {
        this.id = id;
        this.date = date;
        this.operatorId = operatorId;
        this.run = run;
        this.ontime = ontime;
        this.late = late;
        this.canceled = canceled;
        this.ppm = ppm;
        this.rollingPpm = rollingPpm;
    }

    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    public long getDate()
    {
        return date;
    }

    public void setDate( long date )
    {
        this.date = date;
    }

    public int getOperatorId()
    {
        return operatorId;
    }

    public void setOperatorId( int operatorId )
    {
        this.operatorId = operatorId;
    }

    public int getRun()
    {
        return run;
    }

    public void setRun( int run )
    {
        this.run = run;
    }

    public int getOntime()
    {
        return ontime;
    }

    public void setOntime( int ontime )
    {
        this.ontime = ontime;
    }

    public int getLate()
    {
        return late;
    }

    public void setLate( int late )
    {
        this.late = late;
    }

    public int getCanceled()
    {
        return canceled;
    }

    public void setCanceled( int canceled )
    {
        this.canceled = canceled;
    }

    public int getPpm()
    {
        return ppm;
    }

    public void setPpm( int ppm )
    {
        this.ppm = ppm;
    }

    public int getRollingPpm()
    {
        return rollingPpm;
    }

    public void setRollingPpm( int rollingPpm )
    {
        this.rollingPpm = rollingPpm;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final AbstractOperatorPerformance other = (AbstractOperatorPerformance) obj;
        return this.id == other.getId();
    }

}
