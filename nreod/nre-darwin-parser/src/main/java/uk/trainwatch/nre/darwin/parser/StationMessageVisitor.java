/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.parser;

import java.util.List;
import java.util.Objects;
import uk.trainwatch.nre.darwin.model.ppt.stationmessages.A;
import uk.trainwatch.nre.darwin.model.ppt.stationmessages.P;
import uk.trainwatch.nre.darwin.model.ppt.stationmessages.StationMessage;

/**
 * A simple visitor that allows us to traverse the Station Messages (OW) to get either text or html output.
 * <p>
 * @author peter
 */
public interface StationMessageVisitor
{

    default void visit( List<Object> l )
    {
        l.forEach( o -> visit( o ) );//this::visit );
    }

    default void visit( Object o )
    {
        if( o == null ) {
            return;
        }
        if( o instanceof A ) {
            visit( (A) o );
        }
        else if( o instanceof P ) {
            visit( (P) o );
        }
        else if( o instanceof StationMessage.Msg ) {
            visit( (StationMessage.Msg) o );
        }
        else if( o instanceof StationMessage.Station ) {
            visit( (StationMessage.Station) o );
        }
        else {
            visit( Objects.toString( o ) );
        }
    }

    void visit( String s );

    default void visit( A a )
    {
        visit( a.getValue() );
    }

    default void visit( P p )
    {
        visit( "\n" );
        visit( p.getContent() );
    }

    default void visit( StationMessage.Msg m )
    {
        visit( m.getContent() );
    }

    default void visit( StationMessage.Station s )
    {
        visit( s.getCrs() );
    }

}
