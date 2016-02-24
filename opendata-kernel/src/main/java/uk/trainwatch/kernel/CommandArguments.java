/*
 * Copyright 2016 peter.
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

import java.util.List;

/**
 * Command line arguments sent when the kernel starts up.
 * <p>
 * Objects that need to ensure they are started when the kernel does must Observe this object even if they don't do anything with it. It ensures CDI starts that
 * object up.
 *
 * @author peter
 */
@FunctionalInterface
public interface CommandArguments
{

    List<String> getArguments();
}
