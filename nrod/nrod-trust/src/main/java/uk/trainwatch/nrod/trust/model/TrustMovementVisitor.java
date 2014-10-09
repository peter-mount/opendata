/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

/**
 *
 * @author Peter T Mount
 */
public interface TrustMovementVisitor
{

    default void visit( TrainActivation a )
    {
    }

    default void visit( TrainCancellation c )
    {
    }

    default void visit( TrainMovement m )
    {
    }

    default void visit( TrainReinstatement r )
    {
    }

    default void visit( ChangeOfOrigin r )
    {
    }

    default void visit( ChangeOfIdentity r )
    {
    }
}
