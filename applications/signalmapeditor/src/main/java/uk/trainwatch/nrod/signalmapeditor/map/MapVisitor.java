/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 *
 * @author peter
 */
public interface MapVisitor
        extends BiConsumer<String, MapObject>,
                Consumer<Node>
{

    @Override
    default void accept( Node u )
    {
        u.accept( this );
    }

    @Override
    default void accept( String id, MapObject u )
    {
        u.accept( this );
    }

    default void visit( SignalMap map )
    {

    }

    default void visit( Berth b )
    {
    }

    default void visit( Line l )
    {
    }

    default void visit( Points p )
    {
    }
}
