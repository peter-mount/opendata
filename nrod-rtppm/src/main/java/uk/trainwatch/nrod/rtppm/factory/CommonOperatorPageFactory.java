/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.factory;

import java.util.stream.Collectors;
import javax.json.JsonObject;
import uk.trainwatch.nrod.rtppm.model.CommonOperatorPage;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 */
public enum CommonOperatorPageFactory
        implements AbstractPageFactory<CommonOperatorPage>
{

    INSTANCE;

    @Override
    public CommonOperatorPage apply( JsonObject t )
    {
        final CommonOperatorPage p = apply( t, new CommonOperatorPage() );

        p.setOperators( JsonUtils.<JsonObject>stream( t, "OperatorPage" ).
                map( OperatorPPMFactory.OPERATOR_PAGE ).
                collect( Collectors.toList() ) );

        return p;
    }

}
