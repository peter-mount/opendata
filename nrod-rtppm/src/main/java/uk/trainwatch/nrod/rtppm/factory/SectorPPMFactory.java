/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.factory;

import java.util.function.Function;
import javax.json.JsonObject;
import uk.trainwatch.nrod.rtppm.model.SectorPPM;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 */
public class SectorPPMFactory
        implements Function<JsonObject, SectorPPM>
{

    @Override
    public SectorPPM apply( JsonObject t )
    {
        final SectorPPM s = new SectorPPM();

        s.setSectorDesc( t.getString( "sectorDesc", "" ) );
        s.setSectorCode( t.getString( "sectorCode", "" ) );
        s.setSectorPPM( JsonUtils.computeIfPresent( t, "SectorPPM", NationalPPMFactory.INSTANCE ) );
        return s;
    }

}
