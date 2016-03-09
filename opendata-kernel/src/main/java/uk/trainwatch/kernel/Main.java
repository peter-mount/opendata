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
package uk.trainwatch.kernel;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;

/**
 * Standalone entry point. This boots the CDI environment and initialises the {@link Kernel}
 *
 * @author peter
 */
public class Main
{

    public static void main( String... args )
            throws Exception
    {
        int returnCode;

        try( CDI<Object> cdi = CDI.getCDIProvider().initialize() ) {

            BeanManager beanManager = cdi.getBeanManager();
            Set beans = beanManager.getBeans( Kernel.class );
            Bean<Kernel> bean = beanManager.resolve( beans );
            CreationalContext<Kernel> ctx = beanManager.createCreationalContext( bean );
            Kernel kernel = (Kernel) beanManager.getReference( bean, Kernel.class, ctx );

            kernel.init( Collections.unmodifiableList( Arrays.asList( args ) ) );

            returnCode = kernel.run();
        }

        System.exit( returnCode );
    }
}
