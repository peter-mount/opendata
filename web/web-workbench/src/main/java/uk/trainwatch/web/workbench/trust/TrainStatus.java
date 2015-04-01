/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.workbench.trust;

/**
 *
 * @author peter
 */
public enum TrainStatus
{

    INITIAL( "Initialising", false ),
    /**
     * Train has been activated but not yet started
     */
    ACTIVATED( "Activated", false ),
    /**
     * Train is running early
     */
    EARLY( "Early", true ),
    /**
     * Train is on time
     */
    ON_TIME( "On Time", true ),
    /**
     * Train is late
     */
    LATE( "Late", true ),
    /**
     * Train is delayed 30+ minutes
     */
    VERY_LATE( "Very Late", true ),
    /**
     * Train has been cancelled
     */
    CANCELLED( "Cancelled", false );

    private final String label;
    private final boolean delayed;

    private TrainStatus( String label, boolean delayed )
    {
        this.label = label;
        this.delayed = delayed;
    }

    public String getLabel()
    {
        return label;
    }

    public boolean isDelayed()
    {
        return delayed;
    }

    public static TrainStatus getByDelay( long delay )
    {
        if( delay < 0 )
        {
            return EARLY;
        }
        if( delay < 300 )
        {
            return ON_TIME;
        }
        if( delay < 1800 )
        {
            return LATE;
        }
        return VERY_LATE;
    }
}
