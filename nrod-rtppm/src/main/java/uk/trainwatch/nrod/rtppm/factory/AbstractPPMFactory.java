/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.factory;

import java.util.function.Function;
import javax.json.JsonObject;
import uk.trainwatch.nrod.rtppm.model.AbstractPPM;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 */
public interface AbstractPPMFactory<T extends AbstractPPM>
        extends Function<JsonObject, T>
{

    default T apply( JsonObject o, T t )
    {
        t.setTotal( JsonUtils.getInt( o, "Total", 0 ) );

        t.setPpm( JsonUtils.computeIfPresent( o, "PPM", PPMFactory.INSTANCE ) );

        t.setRollingPPM( JsonUtils.computeIfPresent( o, "RollingPPM", RollingPPMFactory.INSTANCE ) );

        return t;
    }
}
