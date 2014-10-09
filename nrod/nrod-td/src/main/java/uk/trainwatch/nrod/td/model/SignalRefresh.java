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
 * Part of a set of SG messages which update the whole set of signalling data for TD area. Terminated by an SH message.
 * <p>
 * @author Peter T Mount
 */
public class SignalRefresh
        extends SignalMessage
{

    public SignalRefresh()
    {
        super( TDMessageType.SG );
    }

    @Override
    public void accept( TDVisitor v )
    {
        v.visit( this );
    }

}
