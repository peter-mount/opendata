/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.factory;

import java.util.function.Function;
import java.util.stream.Collectors;
import javax.json.JsonObject;
import uk.trainwatch.nrod.rtppm.model.RTPPMDataMsg;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.Streams;

/**
 *
 * @author Peter T Mount
 */
public enum RTPPMDataMsgFactory
        implements Function<JsonObject, RTPPMDataMsg>
{

    INSTANCE;

    @Override
    public RTPPMDataMsg apply( JsonObject t )
    {
        final RTPPMDataMsg msg = new RTPPMDataMsg();

        JsonObject m = t.getJsonObject( "RTPPMDataMsgV1" );

        msg.setOwner( m.getString( "owner", "" ) );

        final long timestamp = JsonUtils.getLong( m, "timestamp" );
        msg.setTimestamp( timestamp );

        msg.setClassification( m.getString( "classification", "" ) );
        msg.setSchemaLocation( m.getString( "schemaLocation", "" ) );

        m = m.getJsonObject( "RTPPMData" );

        msg.setNationalPage( JsonUtils.computeIfPresent( m, "NationalPage", NationalPageFactory.INSTANCE ) );
        msg.getNationalPage().
                getNationalPPM().
                setTimestamp( timestamp );

        msg.setOocPage( JsonUtils.computeIfPresent( m, "OOCPage", OOCPageFactory.INSTANCE ) );

        msg.setCommonOperatorPage( JsonUtils.computeIfPresent( m, "CommonOperatorPage",
                                                               CommonOperatorPageFactory.INSTANCE ) );

        msg.setOperatorPages( JsonUtils.<JsonObject>stream( m, "OperatorPage" ).
                map( op -> op.getJsonObject( "Operator" ) ).
                map( OperatorPPMFactory.OPERATOR_PAGE ).
                collect( Collectors.toList() ) );

        // Set the timestamps on each PPM instance
        Streams.concat(
                msg.getOperatorPages(),
                msg.getOocPage().
                getOperators()
        ).
                forEach( ppm -> ppm.setTimestamp( timestamp ) );

        return msg;
    }

}
