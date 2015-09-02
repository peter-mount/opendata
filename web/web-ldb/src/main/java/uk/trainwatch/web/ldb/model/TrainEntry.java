/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Contains a ForecastEntry but allows us to merge multiple trains together in the detail page
 * on uktra.in
 * <p>
 * @author peter
 */
public class TrainEntry
        implements Comparable<TrainEntry>
{

    private final Association association;
    private final Train train;
    private final ForecastEntry entry;
    private final int index;
    private int row = -1;
    private TrainEntry previous;

    public TrainEntry( Association association, Train train, ForecastEntry entry, int index )
    {
        this.association = association;
        this.train = train;
        this.entry = entry;
        this.index = index;
    }

    public TrainEntry getPrevious()
    {
        return previous;
    }

    public void setPrevious( TrainEntry previous )
    {
        this.previous = previous;
    }

    public void setRow( int row )
    {
        this.row = row;
    }

    public int getRow()
    {
        return row > -1 ? row : (previous == null ? 0 : previous.getRow() + 1);
    }

    public int getPreviousRow()
    {
        return previous == null ? getRow() : previous.getRow();
    }

    public Train getTrain()
    {
        return train;
    }

    public Association getAssociation()
    {
        return association;
    }

    public int getIndex()
    {
        return index;
    }

    public int getPreviousIndex()
    {
        return previous == null ? index : previous.getIndex();
    }

    public long getId()
    {
        return entry.getId();
    }

    public String getTpl()
    {
        return entry.getTpl();
    }

    public int getTplid()
    {
        return entry.getTplid();
    }

    public boolean isSup()
    {
        return entry.isSup();
    }

    public LocalTime getPta()
    {
        return entry.getPta();
    }

    public LocalTime getPtd()
    {
        return entry.getPtd();
    }

    public LocalTime getWta()
    {
        return entry.getWta();
    }

    public LocalTime getWtd()
    {
        return entry.getWtd();
    }

    public LocalTime getWtp()
    {
        return entry.getWtp();
    }

    public Duration getDelay()
    {
        return entry.getDelay();
    }

    public LocalTime getArr()
    {
        return entry.getArr();
    }

    public LocalTime getDep()
    {
        return entry.getDep();
    }

    public LocalTime getEtarr()
    {
        return entry.getEtarr();
    }

    public LocalTime getEtdep()
    {
        return entry.getEtdep();
    }

    public LocalTime getPass()
    {
        return entry.getPass();
    }

    public LocalTime getEtpass()
    {
        return entry.getEtpass();
    }

    public String getPlat()
    {
        return entry.getPlat();
    }

    public boolean isPlatsup()
    {
        return entry.isPlatsup();
    }

    public boolean isCisplatsup()
    {
        return entry.isCisplatsup();
    }

    public String getPlatsrc()
    {
        return entry.getPlatsrc();
    }

    public int getLength()
    {
        return entry.getLength();
    }

    public boolean isDetatchfront()
    {
        return entry.isDetatchfront();
    }

    public LocalTime getTm()
    {
        return entry.getTm();
    }

    public ScheduleEntry getScheduleEntry()
    {
        return entry.getScheduleEntry();
    }

    public void setScheduleEntry( ScheduleEntry scheduleEntry )
    {
        entry.setScheduleEntry( scheduleEntry );
    }

    public boolean isScheduleEntryPresent()
    {
        return entry.isScheduleEntryPresent();
    }

    private ForecastEntry getEntry()
    {
        return entry;
    }

    public int compareTo( TrainEntry o )
    {
        int r = entry.compareTo( o.getEntry() );
        if( r == 0 ) {
            r = Integer.compare( index, o.getIndex() );
        }
        return r;
    }

    public boolean isReport()
    {
        return entry.isReport();
    }

    public boolean isCallingPoint()
    {
        return entry.isCallingPoint();
    }

    public boolean isCancelled()
    {
        return entry.isScheduleEntryPresent() ? entry.getScheduleEntry().isCan() : false;
    }

    public boolean isPast()
    {
        return entry.getArr() != null || entry.getDep() != null || entry.getPass() != null;
    }
}
