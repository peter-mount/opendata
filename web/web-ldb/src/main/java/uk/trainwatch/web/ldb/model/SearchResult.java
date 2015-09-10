/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

import java.io.Serializable;
import java.sql.ResultSet;
import java.time.LocalTime;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
public class SearchResult
        implements Comparable<SearchResult>,
                   TimetableEntry,
                   Serializable
{

    private static final long serialVersionUID = 1L;

    public static final SQLFunction<ResultSet, SearchResult> fromSQL = rs -> new SearchResult(
            rs.getString( "rid" ),
            rs.getString( "dest" ),
            rs.getInt( "via" ),
            TimeUtils.getLocalTime( rs, "pta" ),
            TimeUtils.getLocalTime( rs, "ptd" ),
            TimeUtils.getLocalTime( rs, "wta" ),
            TimeUtils.getLocalTime( rs, "wtd" ),
            TimeUtils.getLocalTime( rs, "wtp" ),
            rs.getString( "plat" ),
            rs.getBoolean( "platsup" ),
            rs.getBoolean( "cisplatsup" ),
            TimeUtils.getLocalTime( rs, "wtm" ),
            rs.getBoolean( "can" ),
            rs.getBoolean( "term" ),
            rs.getString( "trainid" ),
            rs.getString( "assoc" ),
            rs.getString( "assoctpl" )
    );

    private final String rid;
    private final String tpl;
    private final int via;
    private final LocalTime pta;
    private final LocalTime ptd;
    private final LocalTime wta;
    private final LocalTime wtd;
    private final LocalTime wtp;
    private final String plat;
    private final boolean platsup;
    private final boolean cisplatsup;
    private final LocalTime wtm;
    private final boolean canc;
    private final boolean term;
    private final String trainid;
    private final String assoc;
    private final String assoctpl;

    public SearchResult( String rid, String tpl, int via, LocalTime pta, LocalTime ptd, LocalTime wta, LocalTime wtd, LocalTime wtp, String plat,
                         boolean platsup, boolean cisplatsup, LocalTime wtm, boolean canc, boolean term, String trainid, String assoc, String assoctpl )
    {
        this.rid = rid;
        this.tpl = tpl;
        this.via = via;
        this.pta = pta;
        this.ptd = ptd;
        this.wta = wta;
        this.wtd = wtd;
        this.wtp = wtp;
        this.plat = plat;
        this.platsup = platsup;
        this.cisplatsup = cisplatsup;
        this.wtm = wtm;
        this.canc = canc;
        this.term = term;
        this.trainid = trainid;
        this.assoc = assoc;
        this.assoctpl = assoctpl;
    }

    public String getRid()
    {
        return rid;
    }

    @Override
    public String getTpl()
    {
        return tpl;
    }

    public int getVia()
    {
        return via;
    }

    @Override
    public LocalTime getPta()
    {
        return pta;
    }

    @Override
    public LocalTime getPtd()
    {
        return ptd;
    }

    @Override
    public LocalTime getWta()
    {
        return wta;
    }

    @Override
    public LocalTime getWtd()
    {
        return wtd;
    }

    @Override
    public LocalTime getWtp()
    {
        return wtp;
    }

    public LocalTime getWtm()
    {
        return wtm;
    }

    @Override
    public String getPlat()
    {
        return plat;
    }

    @Override
    public boolean isPlatsup()
    {
        return platsup;
    }

    @Override
    public boolean isCisplatsup()
    {
        return cisplatsup;
    }

    @Override
    public int compareTo( SearchResult o )
    {
        return TimetableEntry.SORT.compare( this, o );
    }

    @Override
    public int getTplid()
    {
        return 0;
    }

    public boolean isCanc()
    {
        return canc;
    }

    public boolean isTerm()
    {
        return term;
    }

    public String getTrainid()
    {
        return trainid;
    }

    public String getAssoc()
    {
        return assoc;
    }

    public String getAssoctpl()
    {
        return assoctpl;
    }

}
