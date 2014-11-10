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
package uk.trainwatch.app.util;

import java.util.List;
import java.util.function.Consumer;
import javax.json.Json;
import javax.json.JsonStructure;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.rabbitmq.RabbitConnection;
import uk.trainwatch.rabbitmq.RabbitMQ;
import uk.trainwatch.util.app.Utility;

/**
 *
 * @author Peter T Mount
 */
@MetaInfServices(Utility.class)
public class Tweet
        implements Utility {

    private final Options options;

    private RabbitConnection con;
    private String tweetAs;
    private String user;
    private String hash;
    private List<String> texts;

    public Tweet() {
        this.options = new Options().
                addOption(null, "rabbituser", true, "RabbitMQ username").
                addOption(null, "rabbitpassword", true, "RabbitMQ password").
                addOption(null, "rabbithost", true, "RabbitMQ hostname").
                addOption(null, "tweet", true, "tweet the memo as user").
                addOption(null, "user", true, "a51.li user").
                addOption(null, "hash", true, "a51.li hash");
    }

    @Override
    public Options getOptions() {
        return options;
    }

    @Override
    public boolean parseArgs(CommandLine cmd) {
        con = new RabbitConnection(cmd.getOptionValue("rabbituser"),
                cmd.getOptionValue("rabbitpassword"),
                cmd.getOptionValue("rabbithost", "mq.iot.onl"));
        tweetAs = cmd.getOptionValue("tweet");
        user = cmd.getOptionValue("user");
        hash = cmd.getOptionValue("hash");

        texts = cmd.getArgList();

        return true;
    }

    @Override
    public void runUtility()
            throws Exception {
        Consumer<? super JsonStructure> consumer = RabbitMQ.jsonConsumer(con, "twitter.tweet");

        texts.stream().
                map(t -> Json.createObjectBuilder().
                        add("user", user).
                        add("hash", hash).
                        add("tweetAs", tweetAs).
                        add("tweet", t).build()).
                forEach(consumer);

        System.exit(0);
    }

}
