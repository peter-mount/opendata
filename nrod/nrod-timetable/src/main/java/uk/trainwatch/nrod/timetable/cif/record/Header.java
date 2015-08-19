/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Function;

/**
 * CIF file header
 * <p>
 * P16
 * <p>
 * @author Peter T Mount
 */
public class Header
        extends Record
        implements Serializable
{

    private static final long serialVersionUID = 1L;

    static final Function<CIFParser, Record> factory = p -> new Header(
            p.getString( 20 ),
            p.getDate_ddmmyy(),
            p.getTime_hhmm(),
            p.getString( 7 ),
            p.getString( 7 ),
            // F full, U update
            p.getBoolean( "F" ),
            p.getString( 1 ),
            p.getDate_ddmmyy(),
            p.getDate_ddmmyy()
    );

    private final String mainframeId;
    private final LocalDate dateOfExtract;
    private final LocalTime timeOfExtract;
    private final String currentFileRef;
    private final String lastFileRef;
    private final boolean fillExtract;
    private final String cifVersion;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Header( String mainframeId,
                   LocalDate dateOfExtract, LocalTime timeOfExtract,
                   String currentFileRef, String lastFileRef,
                   boolean fullExtract,
                   String cifVersion,
                   LocalDate startDate, LocalDate endDate )
    {
        super( RecordType.HD );
        this.mainframeId = mainframeId;
        this.dateOfExtract = dateOfExtract;
        this.timeOfExtract = timeOfExtract;
        this.currentFileRef = currentFileRef;
        this.lastFileRef = lastFileRef;
        this.fillExtract = fullExtract;
        this.cifVersion = cifVersion;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public void accept( RecordVisitor v )
    {
        v.visit( this );
    }

    public String getMainframeId()
    {
        return mainframeId;
    }

    public LocalDate getDateOfExtract()
    {
        return dateOfExtract;
    }

    public LocalTime getTimeOfExtract()
    {
        return timeOfExtract;
    }

    public LocalDateTime getLocalDateTimeOfExtract()
    {
        return dateOfExtract.atTime( timeOfExtract );
    }

    public String getCurrentFileRef()
    {
        return currentFileRef;
    }

    public String getLastFileRef()
    {
        return lastFileRef;
    }

    public boolean isFillExtract()
    {
        return fillExtract;
    }

    public String getCifVersion()
    {
        return cifVersion;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public LocalDate getEndDate()
    {
        return endDate;
    }

}
