/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.factory;

import java.util.function.Function;
import javax.json.JsonObject;
import uk.trainwatch.nrod.rtppm.model.RollingPPM;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 */
public enum RollingPPMFactory
        implements Function<JsonObject, RollingPPM>
{

    INSTANCE;

    @Override
    public RollingPPM apply( JsonObject t )
    {
        RollingPPM p = new RollingPPM();
        p.setRag( t.getString( "rag", "" ) );
        p.setValue( JsonUtils.getInt( t,  "text", 0 ) );

        p.setTrend( t.getString( "trendInd", "" ));
        p.setDisplay( JsonUtils.getBoolean( t, "displayFlag", false ) );
        return p;
    }
}
