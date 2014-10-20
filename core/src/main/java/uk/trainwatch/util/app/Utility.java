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
package uk.trainwatch.util.app;

import java.util.concurrent.Callable;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 *
 * @author peter
 */
public interface Utility
        extends Callable<Void>
{

    default String getName()
    {
        return getClass().
                getSimpleName().
                toLowerCase();
    }

    default Options getOptions()
    {
        return new Options();
    }

    default boolean parseArgs( CommandLine cmd )
    {
        return true;
    }

    @Override
    default Void call()
            throws Exception
    {
        runUtility();
        return null;
    }

    void runUtility()
            throws Exception;
}
