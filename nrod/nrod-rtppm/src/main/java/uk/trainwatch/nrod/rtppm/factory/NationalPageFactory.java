/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.factory;

import java.util.stream.Collectors;
import javax.json.JsonObject;
import uk.trainwatch.nrod.rtppm.model.NationalPage;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 */
public enum NationalPageFactory
        implements AbstractPageFactory<NationalPage>
{

    INSTANCE;

    @Override
    public NationalPage apply( JsonObject t )
    {
        final NationalPage np = apply( t, new NationalPage() );

        np.setWebMessageOfMoment( t.getString( "WebMessageOfMoment", "" ) );
        np.setStaleFlag( JsonUtils.getBoolean( t, "StaleFlag", false ) );

        np.setNationalPPM( JsonUtils.computeIfPresent( t, "NationalPPM", NationalPPMFactory.INSTANCE ) );

        np.setSectors( JsonUtils.<JsonObject>stream( t, "Sector" ).
                map( new SectorPPMFactory() ).
                collect( Collectors.toList() ) );

        np.setOperators( JsonUtils.<JsonObject>stream( t, "Operator" ).
                map( OperatorPPMFactory.OPERATOR_PAGE ).
                collect( Collectors.toList() ) );

        return np;
    }

}
