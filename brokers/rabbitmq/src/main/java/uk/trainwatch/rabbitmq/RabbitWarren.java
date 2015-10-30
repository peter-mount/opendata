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
package uk.trainwatch.rabbitmq;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class RabbitWarren
{

    private static final Map<String, Consumer<String>> CONSUMERS = new ConcurrentHashMap<>();
    
    @Inject
    private Rabbit rabbit;

    @Produces
    @Publisher(value = "")
    public Consumer<String> publish( InjectionPoint ip )
    {
        Publisher p = ip.getAnnotated().getAnnotation( Publisher.class );
        return CONSUMERS.computeIfAbsent( p.value(), rabbit::publishString );
    }
}
