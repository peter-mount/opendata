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
package uk.trainwatch.tfl.feeds;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.commons.configuration.Configuration;
import uk.trainwatch.tfl.model.feed.TflJsonRetriever;
import uk.trainwatch.util.config.ConfigurationService;

/**
 *
 * @author peter
 */
public abstract class TflFeed
{

    @Inject
    private ConfigurationService configurationService;

    private Supplier<String> retriever;
    private Consumer<String> publisher;

    @PostConstruct
    public void start()
    {
        try {
            Configuration config = configurationService.getPrivateConfiguration( "tfl" );

            retriever = getRetriever( new TflJsonRetriever( getEndPoint(), config.getString( "tfl.app.id" ), config.getString( "tfl.app.key" ) ) );
            publisher = getPublisher();
        }
        catch( URISyntaxException |
               MalformedURLException ex ) {
            throw new RuntimeException( ex );
        }
    }

    protected abstract String getEndPoint();

    protected abstract Consumer<String> getPublisher();

    protected abstract Supplier<String> getRetriever( Supplier<String> s );

    public void retrieve()
    {
        String xml = retriever.get();

        if( xml != null ) {
            publisher.accept( xml );
        }
    }
}
