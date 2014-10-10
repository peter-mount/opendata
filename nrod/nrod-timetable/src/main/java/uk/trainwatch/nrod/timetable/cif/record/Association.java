/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.timetable.cif.record;

import java.time.LocalDate;
import java.util.function.Function;
import uk.trainwatch.nrod.timetable.cif.TransactionType;

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
            p.getString( 7 ),
            p.getString( 2 ),
            p.getString( 1 ),
            p.getString( 7 ),
            p.getString( 1 ),
            p.getString( 1 ),
            p.skip( 1 ),
            p.getString( 1 ),
            p.skip( 31 ),
            p.getString( 1 )
    );

    private final TransactionType transactionType;
    private final String mainTrainUID;
    private final String assocTrainUID;
    private final LocalDate startDate;
    private final LocalDate endDate;
    // This is 1 char per day, 0 or 1
    private final String assocDays;
    private final String assocCat;
    private final String assocDateInd;
    private final String assocLocation;
    private final String baseLocSuffix;
    private final String assocLocSuffix;
    private final String assocType;
    private final String stpIndicator;

    public Association( TransactionType transactionType, String mainTrainUID, String assocTrainUID, LocalDate startDate,
                        LocalDate endDate, String assocDays, String assocCat, String assocDateInd, String assocLocation,
                        String baseLocSuffix, String assocLocSuffix,
                        Void diagramType,
                        String assocType,
                        Void spare,
                        String stpIndicator )
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

    public static Function<CIFParser, Record> getFactory()
    {
        return factory;
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

    public String getAssocDays()
    {
        return assocDays;
    }

    public String getAssocCat()
    {
        return assocCat;
    }

    public String getAssocDateInd()
    {
        return assocDateInd;
    }

    public String getAssocLocation()
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

    public String getAssocType()
    {
        return assocType;
    }

    public String getStpIndicator()
    {
        return stpIndicator;
    }

    @Override
    public String toString()
    {
        return "Association{" + "transactionType=" + transactionType + ", mainTrainUID=" + mainTrainUID + ", assocTrainUID=" + assocTrainUID + ", startDate=" + startDate + ", endDate=" + endDate + ", assocDays=" + assocDays + ", assocCat=" + assocCat + ", assocDateInd=" + assocDateInd + ", assocLocation=" + assocLocation + ", baseLocSuffix=" + baseLocSuffix + ", assocLocSuffix=" + assocLocSuffix + ", assocType=" + assocType + ", stpIndicator=" + stpIndicator + '}';
    }

}
