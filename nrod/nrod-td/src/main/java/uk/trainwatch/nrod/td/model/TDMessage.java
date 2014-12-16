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
package uk.trainwatch.nrod.td.model;

import java.util.Date;

/**
 * Base class for all TD message types
 * <p>
 * @author Peter T Mount
 */
public abstract class TDMessage
{

    private final TDMessageType msg_type;
    private long time;
    private String areaId;

    public TDMessage( TDMessageType msg_type )
    {
        this.msg_type = msg_type;
    }

    public final TDMessageType getMsg_type()
    {
        return msg_type;
    }

    public final long getTime()
    {
        return time;
    }

    public final Date getDate()
    {
        return new Date( time );
    }

    public final void setTime( long time )
    {
        this.time = time;
    }

    public final String getAreaId()
    {
        return areaId;
    }

    public final void setAreaId( String areaId )
    {
        this.areaId = areaId;
    }

    public abstract void accept( TDVisitor v );
}
