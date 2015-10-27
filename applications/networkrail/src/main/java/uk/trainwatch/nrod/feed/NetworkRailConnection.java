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
package uk.trainwatch.nrod.feed;

import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.Message;
import uk.trainwatch.apachemq.RemoteActiveMQConnection;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class NetworkRailConnection
{

    private RemoteActiveMQConnection activemq;

    @PostConstruct
    public void start()
    {
        activemq = RemoteActiveMQConnection.getJNDIConnection( "nrod" );
        activemq.start();
    }

    @PreDestroy
    public void stop()
    {
        activemq.stop();
    }

    public <T> void registerTopicConsumer( String topicName, Function<Message, T> mapper, Consumer<T> consumer )
    {
        activemq.registerTopicConsumer( topicName, mapper, consumer );
    }

    public void registerTopicConsumer( String topicName, Consumer<Message> consumer )
    {
        activemq.registerTopicConsumer( topicName, consumer );
    }

}
