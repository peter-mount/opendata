/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.util.function.Supplier;

/**
 *
 * @author Peter T Mount
 */
@FunctionalInterface
public interface BlockingSupplier<T>
        extends Supplier<T>
{

    /**
     * Called when the background thread has shutdown.
     * <p>
     * An implementation can use this to mark itself as invalid and clear up any resources
     */
    default void setInvalid()
    {
    }
}
