/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.factory;

import javax.json.JsonObject;
import uk.trainwatch.nrod.rtppm.model.NationalPPM;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 */
public enum NationalPPMFactory
        implements AbstractPPMFactory<NationalPPM>
{

    INSTANCE;

    @Override
    public NationalPPM apply( JsonObject t )
    {
        final NationalPPM p = apply( t, new NationalPPM() );

        p.setOnTime( JsonUtils.getInt( t, "OnTime", 0 ) );
        p.setLate( JsonUtils.getInt( t, "Late", 0 ) );
        p.setCancelVeryLate( JsonUtils.getInt( t, "CancelVeryLate", 0 ) );

        return p;
    }

}
