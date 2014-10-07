/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.trust.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Java representation of the mvt_movement table and 0003 movement messages
 * <p/>
 * @author peter
 */
@XmlRootElement( name = "movement" )
@XmlAccessorType( XmlAccessType.FIELD )
public class TrainMovements
        implements Serializable
{

    private static final long serialVersionUID = -2358185935042140248L;
    private final List<TrainMovement> movements = new ArrayList<>();
    private Calendar date;
    private String id;

    public List<TrainMovement> getMovements()
    {
        return movements;
    }

    public Calendar getDate()
    {
        return date;
    }

    public void setDate( Calendar date )
    {
        this.date = date;
    }

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }
}
