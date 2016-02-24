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
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.CDI;
import uk.trainwatch.util.CDIUtils;

/**
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

            Bean<Kernel> bean = CDIUtils.getBean( Kernel.class.getName() );
            Kernel kernel = CDIUtils.getInstance( bean, Kernel.class );

            kernel.init( Collections.unmodifiableList( Arrays.asList( args ) ) );

            returnCode = kernel.run();
        }

        System.exit( returnCode );
    }
}
