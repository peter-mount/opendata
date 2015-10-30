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
package uk.trainwatch.util.cdi;

import java.lang.annotation.Annotation;
import javax.inject.Named;

/**
 * Allows for injection of a Named annotation against a bean
 * <p>
 * @author peter
 */
@SuppressWarnings("AnnotationAsSuperInterface")
public class NamedImpl
        implements Named
{

    private final String value;

    public NamedImpl( String value )
    {
        this.value = value;
    }

    @Override
    public Class<? extends Annotation> annotationType()
    {
        return Named.class;
    }

    @Override
    public String value()
    {
        return value;
    }

}
