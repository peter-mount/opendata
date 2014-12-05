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

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
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

    static List<Path> getArgFileList( CommandLine cmd )
    {
        // As commons-cli is pre-generics we have to do this first
        Collection<String> args = cmd.getArgList();

        return args.stream().
                map( File::new ).
                filter( File::exists ).
                filter( File::canRead ).
                map( File::toPath ).
                collect( Collectors.toList() );
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
