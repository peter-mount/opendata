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
package uk.trainwatch.util;

/**
 * A function that taked 3 parameters and returns a single result
 * <p>
 * @author peter
 * @param <A> first type
 * @param <B> second type
 * @param <C> third type
 * @param <T> return type
 */
@FunctionalInterface
public interface TriFunction<A, B, C, T>
{

    T apply( A a, B b, C c );

}
