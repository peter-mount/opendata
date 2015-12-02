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
import uk.trainwatch.io.IOFunction;
import uk.trainwatch.io.format.DataReader;
import uk.trainwatch.io.format.DataWriter;

/**
 * Base implementation for a {@link WireMessageFormat} that manages String content
 * <p>
 * @author peter
 */
public abstract class StringFormat
        implements WireMessageFormat<String>
{

    @Override
    public final IOFunction<DataReader, String> reader()
    {
        return DataReader::readString;
    }

    @Override
    public final IOBiConsumer<DataWriter, String> writer()
    {
        return DataWriter::writeString;
    }

}
