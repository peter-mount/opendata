/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.model;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 *
 * @author peter
 */
public class SearchResult
        implements Comparable<SearchResult>,
                   Serializable
{

    private static final long serialVersionUID = 1L;

    private final Train train;
    private final TimetableEntry time;

    public SearchResult( Train train, Predicate<Integer> filter )
    {
        this.train = train;
        time = train.findTime( filter );
    }

    public Train getTrain()
    {
        return train;
    }

    public TimetableEntry getTime()
    {
        return time;
    }

    public boolean isValid()
    {
        return train.isValid() && time != null;
    }

    @Override
    public int compareTo( SearchResult o )
    {
        return TimetableEntry.SORT.compare( time, o.time );
    }

}
