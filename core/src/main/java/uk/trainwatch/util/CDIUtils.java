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

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.inject.spi.InjectionTarget;

/**
 * Some utility methods to support CDI
 * 
 * @author peter
 */
public class CDIUtils
{
    /**
     * Get the current BeanManager instance
     * 
     * @return 
     */
    public static BeanManager getBeanManager() {
        return CDI.current().getBeanManager();
    }
    
    /**
     * Perform injection into a non-CDI managed bean.
     * 
     * An example of this is a JSP Tag
     * 
     * @param <T>
     * @param bean 
     */
    public static <T> void inject( T bean )
    {
            BeanManager beanManager = getBeanManager();
            AnnotatedType<T> annotatedType = beanManager.<T>createAnnotatedType( (Class<T>) bean.getClass() );
            InjectionTarget<T> injectionTarget = beanManager.createInjectionTarget( annotatedType );
            CreationalContext ctx = beanManager.createCreationalContext( null );
            injectionTarget.inject( bean, ctx );
    }
}
