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
public abstract class TrustMovement
{

    
    public abstract int getToc_id();
    public abstract String getTrain_id();
    
    public abstract void accept( TrustMovementVisitor v );
}
