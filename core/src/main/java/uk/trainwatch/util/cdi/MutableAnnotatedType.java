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
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;

/**
 * A delegate for {@link AnnotatedType} that allows for the underlying annotations to be modified
 * <p>
 * @author peter
 */
public class MutableAnnotatedType<X>
        implements AnnotatedType<X>
{

    private final AnnotatedType<X> original;
    Set<Annotation> annotations;
    
    public MutableAnnotatedType( AnnotatedType<X> original )
    {
        this.original = original;
        annotations = new HashSet<>( original.getAnnotations() );
    }

    public MutableAnnotatedType add( Annotation a )
    {
        annotations.add( a );
        return this;
    }

    @Override
    public Class<X> getJavaClass()
    {
        return original.getJavaClass();
    }

    @Override
    public Set<AnnotatedConstructor<X>> getConstructors()
    {
        return original.getConstructors();
    }

    @Override
    public Set<AnnotatedMethod<? super X>> getMethods()
    {
        return original.getMethods();
    }

    @Override
    public Set<AnnotatedField<? super X>> getFields()
    {
        return original.getFields();
    }

    @Override
    public Type getBaseType()
    {
        return original.getBaseType();
    }

    @Override
    public Set<Type> getTypeClosure()
    {
        return original.getTypeClosure();
    }

    @Override
    public <T extends Annotation> T getAnnotation( Class<T> annotationType )
    {
        return original.getAnnotation( annotationType );
    }

    @Override
    public Set<Annotation> getAnnotations()
    {
        return annotations;
    }

    @Override
    public boolean isAnnotationPresent(
            Class<? extends Annotation> annotationType )
    {
        return original.isAnnotationPresent( annotationType );
    }

}
