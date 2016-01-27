/*
 * Copyright 2016 peter.
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
package uk.trainwatch.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.QueueingConsumer;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Consumer which will pass a map message body to a function. If the delivery is expecting a response then the returned map is returned.
 *
 * @author peter
 */
public class RabbitRPCInvoker
        implements Consumer<QueueingConsumer.Delivery>
{

    private static final Logger LOG = Logger.getLogger( RabbitRPCInvoker.class.getName() );
    private final RabbitConnection connection;
    private final UnaryOperator<Map<String, Object>> call;

    public RabbitRPCInvoker( RabbitConnection connection, UnaryOperator<Map<String, Object>> call )
    {
        this.connection = connection;
        this.call = call;
    }

    @Override
    public void accept( QueueingConsumer.Delivery request )
    {
        try {
            Map<String, Object> params = RabbitMQ.fromAMQPTable( request.getBody() );

            Map<String, Object> ret = call.apply( params );

            AMQP.BasicProperties requestProperties = request.getProperties();
            String correlationId = requestProperties.getCorrelationId();
            String replyTo = requestProperties.getReplyTo();
            if( correlationId != null && replyTo != null ) {

                AMQP.BasicProperties replyProperties = new AMQP.BasicProperties.Builder()
                        .correlationId( correlationId )
                        .build();

                connection.getChannel( this )
                        .basicPublish( "", replyTo, replyProperties, RabbitMQ.toAMQPTable( ret ) );
            }
        }
        catch( Throwable ex ) {
            LOG.log( Level.SEVERE, null, ex );
        }
    }

}
