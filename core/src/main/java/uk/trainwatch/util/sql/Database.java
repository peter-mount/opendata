/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util.sql;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;

/**
 * Annotation to use instead of {@link javax.inject.Inject} to inject a DataSource.
 * 
 * To use instead of {@code @Inject("jdbc.rail")} use {@code @Database("rail") @Inject}.
 * 
 * This then allows us to inject a datasource without JNDI when running in a standalone application.
 * 
 * @author peter
 */
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface Database
{
    @Nonbinding String value();
}
