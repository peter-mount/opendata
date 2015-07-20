/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.ogl.naptan;

/**
 *
 * @author peter
 */
public class StopInArea
{

    private final long id;
    private final String stop;
    private final String atco;

    public StopInArea( long id, String stop, String atco )
    {
        this.id = id;
        this.stop = stop;
        this.atco = atco;
    }

    public long getId()
    {
        return id;
    }

    public String getStop()
    {
        return stop;
    }

    public String getAtco()
    {
        return atco;
    }

}
