/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.rtppm.factory;

import javax.json.JsonObject;
import uk.trainwatch.nrod.rtppm.model.OperatorPPM;
import uk.trainwatch.nrod.rtppm.model.OperatorPagePPM;
import uk.trainwatch.util.JsonUtils;

/**
 *
 * @author Peter T Mount
 * @param <T>
 */
public abstract class OperatorPPMFactory<T extends OperatorPPM>
        implements AbstractPPMFactory<T>
{

    public static OperatorPPMFactory<OperatorPPM> OPERATOR_PPM = new OperatorPPMFactory<OperatorPPM>()
    {
        @Override
        public OperatorPPM apply( JsonObject t )
        {
            return applyOuter( t, new OperatorPPM() );
        }
    };

    public static OperatorPPMFactory<OperatorPagePPM> OPERATOR_PAGE = new OperatorPPMFactory<OperatorPagePPM>()
    {
        @Override
        public OperatorPagePPM apply( JsonObject t )
        {
            final OperatorPagePPM o = applyOuter( t, new OperatorPagePPM() );

            o.setOnTime( JsonUtils.getInt( t, "OnTime", 0 ) );
            o.setLate( JsonUtils.getInt( t, "Late", 0 ) );
            o.setCancelVeryLate( JsonUtils.getInt( t, "CancelVeryLate", 0 ) );

            return o;
        }
    };

    protected T applyOuter( JsonObject t, T o )
    {
        apply( t, o );

        o.setCode( JsonUtils.getInt( t, "code", 0 ) );
        o.setKeySymbol( t.getString( "keySymbol", "" ) );
        o.setName( JsonUtils.getString( t, "name", "" ) );

        return o;
    }

}
