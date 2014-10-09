/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import javax.json.JsonObject;

/**
 *
 * @author Peter T Mount
 */
public enum TrustMovementType
{

    ACTIVATION( 1, TrainActivationFactory.INSTANCE ),
    CANCELLATION( 2, TrainCancellationFactory.INSTANCE ),
    MOVEMENT( 3, TrainMovementFactory.INSTANCE ),
    /**
     * Apparently this is not used in "production" at Network Rail
     */
    UNIDENTIFIED_TRAIN( 4, null ),
    REINSTATEMENT( 5, TrainReinstatementFactory.INSTANCE ),
    CHANGE_OF_ORIGIN( 6, ChangeOfOriginFactory.INSTANCE ),
    CHANGE_OF_IDENTITY( 7, ChangeOfIdentityFactory.INSTANCE ),
    /**
     * Apparently this is not used in "production" at Network Rail
     */
    CHANGE_OF_LOCATION( 8, null );

    private static final Map<String, TrustMovementType> NR_TYPES = new ConcurrentHashMap<>();
    private static final Map<Integer, TrustMovementType> TYPES = new ConcurrentHashMap<>();

    static
    {
        for( TrustMovementType t : values() )
        {
            TYPES.put( t.type, t );
            NR_TYPES.put( String.format( "%04d", t.type ), t );
            NR_TYPES.put( String.valueOf( t.type ), t );
        }
    }

    /**
     * Returns the {@link TrustMovementType} for the specified code. If not present it returns null.
     * <p>
     * This expects either the NR type like "0003" but also "3"
     * <p>
     * @param type <p>
     * @return
     */
    public static TrustMovementType getType( String type )
    {
        return NR_TYPES.get( type );
    }

    /**
     * Returns the {@link TrustMovementType} for the specified code. If not present it returns null.
     * <p>
     * @param type <p>
     * @return
     */
    public static TrustMovementType getType( int type )
    {
        return TYPES.get( type );
    }

    private final int type;
    private final Function<JsonObject, ? extends TrustMovement> factory;

    private TrustMovementType( int type, Function<JsonObject, ? extends TrustMovement> factory )
    {
        this.type = type;
        this.factory = factory;
    }

    public Function<JsonObject, ? extends TrustMovement> getFactory()
    {
        return factory;
    }

    public int getType()
    {
        return type;
    }

}
