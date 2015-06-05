/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.rest;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

/**
 * Annotation to place on a rest endpoint to define caching constraints for the response
 * <p>
 * @author peter
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache
{

    /**
     * The max age of the response. Defaults to 1
     * <p>
     * @return
     */
    long maxAge() default 1;

    /**
     * The unit of maxAge. Defaults to hours
     * <p>
     * @return
     */
    ChronoUnit unit() default ChronoUnit.HOURS;

    /**
     * If not 0 then the amount of time in unit() from the request that the response should be expired.
     * <p>
     * This will generate the expired header
     * <p>
     * @return
     */
    long expires() default 0;
}
