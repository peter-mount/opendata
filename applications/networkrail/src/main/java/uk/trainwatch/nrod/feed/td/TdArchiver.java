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
package uk.trainwatch.nrod.feed.td;

import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import uk.trainwatch.io.DatePathMapper;
import uk.trainwatch.io.FileRecorder;
import uk.trainwatch.util.Consumers;

@ApplicationScoped
public class TdArchiver
        implements Consumer<String>
{

    protected static final Logger LOG = Logger.getLogger( TdArchiver.class.getName() );

    private Consumer<String> logger;

    @PostConstruct
    public void setup()
    {
        Function<String, Path> pathMapper = new DatePathMapper( "/usr/local/networkrail", "td", true );
        logger = Consumers.guard( LOG, FileRecorder.recordTo( pathMapper ) );
    }

    @Override
    public void accept( String t )
    {
        logger.accept( t );
    }

}
