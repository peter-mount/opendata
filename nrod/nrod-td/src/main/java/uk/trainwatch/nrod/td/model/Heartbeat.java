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

/**
 * The CT message is a 'heartbeat' message, periodically sent from a train describer.
 * <p>
 * @author Peter T Mount
 */
public class Heartbeat
        extends TDMessage
{

    private String report_time;

    public Heartbeat()
    {
        super( TDMessageType.CT );
    }

    public String getReport_time()
    {
        return report_time;
    }

    public void setReport_time( String report_time )
    {
        this.report_time = report_time;
    }

    @Override
    public void accept( TDVisitor v )
    {
        v.visit( this );
    }

}
