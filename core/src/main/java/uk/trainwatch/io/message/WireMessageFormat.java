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
package uk.trainwatch.io.message;

import uk.trainwatch.io.IOBiConsumer;
import uk.trainwatch.io.format.DataReader;
import uk.trainwatch.io.format.DataWriter;
import uk.trainwatch.io.IOFunction;

/**
 *
 * @author peter
 */
public interface WireMessageFormat<T>
{

    /**
     * The unique type of this message
     * <p>
     * @return
     */
    String getType();

    /**
     * Reader for this content
     * <p>
     * @return
     */
    IOFunction<DataReader, T> reader();

    /**
     * Writer for this content
     * <p>
     * @return
     */
    IOBiConsumer<DataWriter, T> writer();

}
