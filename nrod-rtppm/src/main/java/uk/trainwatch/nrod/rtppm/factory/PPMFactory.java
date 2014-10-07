/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.factory;

import java.util.function.Function;
import javax.json.JsonObject;
import uk.trainwatch.nrod.rtppm.model.PPM;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 */
public enum PPMFactory
        implements Function<JsonObject, PPM>
{

    INSTANCE;

    @Override
    public PPM apply( JsonObject t )
    {
        PPM p = new PPM();
        p.setRag( t.getString( "rag", "" ) );
        p.setValue( JsonUtils.getInt( t, "text", 0 ) );
        return p;
    }

}
