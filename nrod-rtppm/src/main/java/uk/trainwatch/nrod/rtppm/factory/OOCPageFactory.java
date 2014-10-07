/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.factory;

import java.util.stream.Collectors;
import javax.json.JsonObject;
import uk.trainwatch.nrod.rtppm.model.OOCPage;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 */
public enum OOCPageFactory
        implements AbstractPageFactory<OOCPage>
{

    INSTANCE;

    @Override
    public OOCPage apply( JsonObject t )
    {
        final OOCPage p = apply( t, new OOCPage() );

        p.setOperators( JsonUtils.<JsonObject>stream( t, "Operator" ).
                map( OperatorPPMFactory.OPERATOR_PPM ).
                collect( Collectors.toList() ) );

        return p;
    }

}
