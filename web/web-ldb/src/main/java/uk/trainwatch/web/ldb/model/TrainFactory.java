/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

/**
 *
 * @author peter
 */
public class TrainFactory
{

    /**
     * Creates a new empty instance of {@link Train}
     * @param rid Rail ID
     * @return Train
     */
    public static Train newTrainInstance( String rid )
    {
        return new TrainBean( rid );
    }

    /**
     * Creates a new instance of {@link Train} which will lazily perform a cache lookup when first required.
     * 
     * @param rid Rail ID
     * @return Train
     */
    public static Train getTrainProxy( String rid )
    {
        return new TrainProxy( rid );
    }

    /**
     * Creates a new instance of {@link Train} which will lazily perform a cache lookup when first required.
     * 
     * Unlike {@link #getTrainProxy(java.lang.String)} this proxy uses a weak reference to the underlying {@link Train} so is best used for long term storage
     * like a departure board listing.
     * 
     * @param rid Rail ID
     * @return Train
     */
    public static Train getWeakTrainProxy( String rid )
    {
        return new WeakTrainProxy( rid );
    }

}
