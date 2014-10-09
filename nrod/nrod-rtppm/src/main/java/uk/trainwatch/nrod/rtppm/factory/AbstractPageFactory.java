/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.factory;

import java.util.function.Function;
import javax.json.JsonObject;
import uk.trainwatch.nrod.rtppm.model.AbstractPage;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 * @param <T>
 */
public interface AbstractPageFactory<T extends AbstractPage>
        extends  Function<JsonObject, T>
{

    default T apply( JsonObject t, T o )
    {
        o.setWebDisplayPeriod( JsonUtils.getInt( t, "WebDisplayPeriod", 0 ) );
        o.setWebFixedMsg1( t.getString( "WebFixedMsg1", "" ) );
        o.setWebFixedMsg2( t.getString( "WebFixedMsg2", "" ) );
        return o;
    }
}
