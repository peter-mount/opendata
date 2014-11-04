/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.function.Function;
import uk.trainwatch.nrod.location.Tiploc;
import uk.trainwatch.nrod.timetable.cif.TransactionType;
import uk.trainwatch.nrod.timetable.util.AssociationCategory;
import uk.trainwatch.nrod.timetable.util.AssociationDateIndicator;
import uk.trainwatch.nrod.timetable.util.AssociationType;
import uk.trainwatch.nrod.timetable.util.DaysRun;
import uk.trainwatch.nrod.timetable.util.STPIndicator;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author Peter T Mount
 */
public class Association
        extends Record
{

    static final Function<CIFParser, Record> factory = p -> new Association(
            p.getTransactionType(),
            p.getString( 6 ),
            p.getString( 6 ),
            p.getDate_yymmdd(),
            p.getDate_yymmdd(),
            p.getDaysRun(),
            p.getAssociationCategory(),
            p.getAssociationDateIndicator(),
            p.getTiploc(),
            p.getString( 1 ),
            p.getString( 1 ),
            p.skip( 1 ),
            p.getAssociationType(),
            p.skip( 31 ),
            p.getSTPIndicator()
    );

    public static final SQLFunction<ResultSet, Association> fromSql = r -> new Association(
            // Dummy entry
            TransactionType.NEW,
            r.getString( 1 ),
            r.getString( 2 ),
            TimeUtils.getLocalDate( r, 3 ),
            TimeUtils.getLocalDate( r, 4 ),
            new DaysRun( r.getInt( 5 ) ),
            AssociationCategory.lookup( r.getInt( 6 ) ),
            AssociationDateIndicator.lookup( r.getInt( 7 ) ),
            new Tiploc( r.getString( 8 ) ),
            r.getString( 9 ),
            r.getString( 10 ),
            null,
            AssociationType.lookup( r.getInt( 11 ) ),
            null,
            STPIndicator.lookup( r.getInt( 12 ) )
    );

    private final TransactionType transactionType;
    private final String mainTrainUID;
    private final String assocTrainUID;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final DaysRun assocDays;
    private final AssociationCategory assocCat;
    private final AssociationDateIndicator assocDateInd;
    private final Tiploc assocLocation;
    private final String baseLocSuffix;
    private final String assocLocSuffix;
    private final AssociationType assocType;
    private final STPIndicator stpIndicator;

    public Association( TransactionType transactionType,
                        String mainTrainUID,
                        String assocTrainUID,
                        LocalDate startDate,
                        LocalDate endDate,
                        DaysRun assocDays,
                        AssociationCategory assocCat,
                        AssociationDateIndicator assocDateInd,
                        Tiploc assocLocation,
                        String baseLocSuffix, String assocLocSuffix,
                        Void diagramType,
                        AssociationType assocType,
                        Void spare,
                        STPIndicator stpIndicator )
    {
        super( RecordType.AA );
        this.transactionType = transactionType;
        this.mainTrainUID = mainTrainUID;
        this.assocTrainUID = assocTrainUID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.assocDays = assocDays;
        this.assocCat = assocCat;
        this.assocDateInd = assocDateInd;
        this.assocLocation = assocLocation;
        this.baseLocSuffix = baseLocSuffix;
        this.assocLocSuffix = assocLocSuffix;
        this.assocType = assocType;
        this.stpIndicator = stpIndicator;
    }

    @Override
    public void accept( RecordVisitor v )
    {
        v.visit( this );
    }

    public TransactionType getTransactionType()
    {
        return transactionType;
    }

    public String getMainTrainUID()
    {
        return mainTrainUID;
    }

    public String getAssocTrainUID()
    {
        return assocTrainUID;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public LocalDate getEndDate()
    {
        return endDate;
    }

    public DaysRun getAssocDays()
    {
        return assocDays;
    }

    public AssociationCategory getAssociationCategory()
    {
        return assocCat;
    }

    public AssociationDateIndicator getAssocDateInd()
    {
        return assocDateInd;
    }

    public Tiploc getAssocLocation()
    {
        return assocLocation;
    }

    public String getBaseLocSuffix()
    {
        return baseLocSuffix;
    }

    public String getAssocLocSuffix()
    {
        return assocLocSuffix;
    }

    public AssociationType getAssocType()
    {
        return assocType;
    }

    public STPIndicator getStpIndicator()
    {
        return stpIndicator;
    }

}
