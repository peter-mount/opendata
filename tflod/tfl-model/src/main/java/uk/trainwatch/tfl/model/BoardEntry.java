/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * An entry within a board
 * <p>
 * @author peter
 */
public class BoardEntry
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    private int id;
    private long platId;
    private long destId;
    private int due;
    private LocalDateTime ts;
    private LocalDateTime expected;
    private int dir;
    private String curLoc;
    private String towards;
    private String operationType;
    private String vehicleId;
    private String mode;

    public BoardEntry()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public long getPlatId()
    {
        return platId;
    }

    public void setPlatId( long platId )
    {
        this.platId = platId;
    }

    public long getDestId()
    {
        return destId;
    }

    public void setDestId( long destId )
    {
        this.destId = destId;
    }

    public int getDue()
    {
        return due;
    }

    public void setDue( int due )
    {
        this.due = due;
    }

    public LocalDateTime getTs()
    {
        return ts;
    }

    public void setTs( LocalDateTime ts )
    {
        this.ts = ts;
    }

    public LocalDateTime getExpected()
    {
        return expected;
    }

    public void setExpected( LocalDateTime expected )
    {
        this.expected = expected;
    }

    public int getDir()
    {
        return dir;
    }

    public void setDir( int dir )
    {
        this.dir = dir;
    }

    public String getCurLoc()
    {
        return curLoc;
    }

    public void setCurLoc( String curLoc )
    {
        this.curLoc = curLoc;
    }

    public String getTowards()
    {
        return towards;
    }

    public void setTowards( String towards )
    {
        this.towards = towards;
    }

    public String getOperationType()
    {
        return operationType;
    }

    public void setOperationType( String operationType )
    {
        this.operationType = operationType;
    }

    public String getVehicleId()
    {
        return vehicleId;
    }

    public void setVehicleId( String vehicleId )
    {
        this.vehicleId = vehicleId;
    }

    public String getMode()
    {
        return mode;
    }

    public void setMode( String mode )
    {
        this.mode = mode;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 37 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null || getClass() != obj.getClass() ) {
            return false;
        }
        final BoardEntry other = (BoardEntry) obj;
        return this.id == other.id;
    }

}
