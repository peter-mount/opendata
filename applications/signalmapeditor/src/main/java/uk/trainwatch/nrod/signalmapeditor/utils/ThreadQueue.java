/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.utils;

import java.awt.EventQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * @author peter
 */
public class ThreadQueue
{
    
    private static final Executor executor = Executors.newWorkStealingPool();

    public static void executeLater( Runnable command )
    {
        executor.execute( command );
    }

    public static void executeSwingLater( Runnable command )
    {
        EventQueue.invokeLater( command );
    }

    /**
     * Execute off the swing thread. If not on swing thread then run it now.
     * @param command 
     */
    public static void execute( Runnable command )
    {
        if( EventQueue.isDispatchThread() )
        {
            executeLater( command );
        }
        else
        {
            command.run();
        }
    }

    public static void executeSwing( Runnable command )
    {
        if( EventQueue.isDispatchThread() )
        {
            command.run();
        }
        else
        {
            executeSwingLater( command );
        }
    }

}
