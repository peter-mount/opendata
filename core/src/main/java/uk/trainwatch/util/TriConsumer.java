/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

/**
 * A consumer that accepts three parameters
 * <p>
 * @author peter
 * @param <A> First parameter
 * @param <B> Second parameter
 * @param <C> Third parameter
 */
@FunctionalInterface
public interface TriConsumer<A, B, C>
{

    void accept( A e, B c, C v );

}
