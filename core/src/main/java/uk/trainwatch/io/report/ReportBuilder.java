/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.io.report;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 *
 * @author Peter T Mount
 */
public class ReportBuilder
{

    private final JsonObjectBuilder report = Json.createObjectBuilder();
    private final LocalDateTime created = LocalDateTime.now();
    private String reportId;
    private String title;
    private String subTitle;
    private String description;
    private String recordPath;

    public ReportBuilder setReportId( String reportId )
    {
        this.reportId = reportId;
        return this;
    }

    public ReportBuilder setTitle( String title )
    {
        this.title = title;
        return this;
    }

    public ReportBuilder setSubTitle( String subTitle )
    {
        this.subTitle = subTitle;
        return this;
    }

    public ReportBuilder setDescription( String description )
    {
        this.description = description;
        return this;
    }

    public ReportBuilder setRecordPath( String... recordPath )
    {
        this.recordPath = String.join( "/", recordPath );
        return this;
    }

    public ReportBuilder setRecordPath( String fmt, Object... args )
    {
        this.recordPath = String.format( fmt, args );
        return this;
    }

    public JsonObject build()
    {
        Objects.requireNonNull( reportId, "No reportId" );
        Objects.requireNonNull( title, "No title" );

        JsonObjectBuilder b = Json.createObjectBuilder().
                add( "report", Json.createObjectBuilder().
                     add( "reportId", reportId ).
                     add( "title", title ).
                     add( "subtitle", subTitle == null ? "" : subTitle ).
                     add( "created", created.toString() ).
                     add( "description", description == null ? "" : description ).
                     add( "data", report )
                );

        // Add optional recordPath to outer layer for the archiver
        if( recordPath != null )
        {
            b.add( "recordPath", recordPath );
        }

        return b.build();
    }

    //<editor-fold defaultstate="collapsed" desc="Delegate to report JsonObjectBuilder">
    public ReportBuilder add( String name, JsonValue value )
    {
        report.add( name, value );
        return this;
    }

    public ReportBuilder add( String name, String value )
    {
        report.add( name, value );
        return this;
    }

    public ReportBuilder add( String name, BigInteger value )
    {
        report.add( name, value );
        return this;
    }

    public ReportBuilder add( String name, BigDecimal value )
    {
        report.add( name, value );
        return this;
    }

    public ReportBuilder add( String name, int value )
    {
        report.add( name, value );
        return this;
    }

    public ReportBuilder add( String name, long value )
    {
        report.add( name, value );
        return this;
    }

    public ReportBuilder add( String name, double value )
    {
        report.add( name, value );
        return this;
    }

    public ReportBuilder add( String name, boolean value )
    {
        report.add( name, value );
        return this;
    }

    public ReportBuilder addNull( String name )
    {
        report.addNull( name );
        return this;
    }

    public ReportBuilder add( String name, JsonObjectBuilder builder )
    {
        report.add( name, builder );
        return this;
    }

    public ReportBuilder add( String name, JsonArrayBuilder builder )
    {
        report.add( name, builder );
        return this;
    }
    //</editor-fold>
}
