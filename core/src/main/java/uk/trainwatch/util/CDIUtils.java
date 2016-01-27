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

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.inject.Named;
import uk.trainwatch.util.cdi.MutableAnnotatedType;
import uk.trainwatch.util.cdi.NamedImpl;

/**
 * Some utility methods to support CDI
 * <p>
 * @author peter
 */
public class CDIUtils
{

    private static final AtomicInteger id = new AtomicInteger();

    /**
     * Get the current BeanManager instance
     * <p>
     * @return
     */
    public static BeanManager getBeanManager()
    {
        return CDI.current().getBeanManager();
    }

    /**
     * Perform injection into a non-CDI managed bean.
     * <p>
     * An example of this is a JSP Tag
     * <p>
     * @param <T>
     * @param bean
     */
    public static <T> T inject( T bean )
    {
        BeanManager beanManager = getBeanManager();
        AnnotatedType<T> annotatedType = beanManager.<T>createAnnotatedType( (Class<T>) bean.getClass() );
        InjectionTarget<T> injectionTarget = beanManager.createInjectionTarget( annotatedType );
        CreationalContext ctx = beanManager.createCreationalContext( null );
        injectionTarget.inject( bean, ctx );
        return bean;
    }

    public static <T> Bean<T> getBean( String name )
    {
        BeanManager beanManager = getBeanManager();
        Set beans = beanManager.getBeans( name );
        return beanManager.resolve( beans );
    }

    public static <T> Bean<T> getBean( Type type, Annotation... qualifiers )
    {
        BeanManager beanManager = getBeanManager();
        Set beans = beanManager.getBeans( type, qualifiers );
        return beanManager.resolve( beans );
    }

    public static <T> T getInstance( Bean<T> bean, Class<T> clazz )
    {
        BeanManager beanManager = getBeanManager();
        CreationalContext<T> ctx = beanManager.createCreationalContext( bean );
        return (T) beanManager.getReference( bean, clazz, ctx );
    }

    /**
     * Ensure we have an annotation present
     * <p>
     * @param <T>
     * @param pat
     * @param clazz
     * @param s
     *              <p>
     * @return
     */
    public static <T> AnnotatedType<T> addTypeAnnotation( ProcessAnnotatedType<T> pat, Class<? extends Annotation> clazz, Supplier<Annotation> s )
    {
        AnnotatedType<T> t = pat.getAnnotatedType();
        if( !t.isAnnotationPresent( clazz ) ) {
            MutableAnnotatedType<T> mat;
            if( t instanceof MutableAnnotatedType ) {
                mat = (MutableAnnotatedType<T>) t;
            }
            else {
                mat = new MutableAnnotatedType<>( t );
                pat.setAnnotatedType( mat );
            }
            mat.add( s.get() );
            return mat;
        }
        return t;
    }

    /**
     * Ensures that a type is named ensuring we can look it up by name at some future date.
     *
     * @param <T> Type
     * @param pat ProcessAnnotatedType
     */
    public static <T> void ensureNamed( ProcessAnnotatedType<T> pat )
    {
        CDIUtils.addTypeAnnotation( pat, Named.class, () -> new NamedImpl( "Unique_Object_Ref_" + id.incrementAndGet() ) );
    }

    /**
     * Is this type an interface
     *
     * @param <T>
     * @param pat
     *
     * @return
     */
    public static <T> boolean isInterface( ProcessAnnotatedType<T> pat )
    {
        return pat.getAnnotatedType().getJavaClass().isInterface();
    }

    public static <T> void forEachType( ProcessAnnotatedType<T> pat, Class<? extends Annotation> annotationType,
                                        Consumer<? super AnnotatedType<? extends Object>> c )
    {
        AnnotatedType<?> type = pat.getAnnotatedType();
        if( type.isAnnotationPresent( annotationType ) ) {
            c.accept( type );
        }
    }

    /**
     * For each method in a type that has a specific annotation pass the class and method to a consumer
     *
     * @param <T>
     * @param pat
     * @param annotationType
     * @param c
     */
    public static <T> void forEachMethod( ProcessAnnotatedType<T> pat, Class<? extends Annotation> annotationType,
                                          BiConsumer<Class<?>, ? super AnnotatedMethod<? extends Object>> c )
    {
        AnnotatedType<?> type = pat.getAnnotatedType();
        Class<?> clazz = type.getJavaClass();
        type.getMethods()
                .stream().
                filter( meth -> meth.isAnnotationPresent( annotationType ) ).
                forEach( m -> c.accept( clazz, m ) );
    }
}
