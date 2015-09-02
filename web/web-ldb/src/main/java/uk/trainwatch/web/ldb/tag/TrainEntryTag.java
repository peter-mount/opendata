/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.ldb.tag;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import uk.trainwatch.util.CDIUtils;
import uk.trainwatch.util.Streams;
import uk.trainwatch.web.ldb.cache.TrainCache;
import uk.trainwatch.web.ldb.model.Association;
import uk.trainwatch.web.ldb.model.Train;
import uk.trainwatch.web.ldb.model.TrainEntry;

/**
 * Forms a TrainEntry table from a train and it's associations
 * <p>
 * @author peter
 */
public class TrainEntryTag
        extends BodyTagSupport
{

    private String var;
    private String maxIndex;
    private Train train;

    @Inject
    private TrainCache trainCache;

    @SuppressWarnings("LeakingThisInConstructor")
    public TrainEntryTag()
    {
        CDIUtils.inject( this );
    }

    private class T
    {

        private final Association assoc;
        private final Train train;
        private final int index;
        private TrainEntry entry;
        private List<TrainEntry> entries;

        public T( Association assoc, Train train, int index )
        {
            this.assoc = assoc;
            this.train = train;
            this.index = index;
        }

        public Association getAssoc()
        {
            return assoc;
        }

        public Train getTrain()
        {
            return train;
        }

        public int getIndex()
        {
            return index;
        }

        public TrainEntry getEntry()
        {
            return entry;
        }

        public void setEntry( TrainEntry entry )
        {
            this.entry = entry;
        }

        public List<TrainEntry> getEntries()
        {
            return entries;
        }

        public void setEntries( List<TrainEntry> entries )
        {
            this.entries = entries;
        }

    }

    @Override
    public int doStartTag()
            throws JspException
    {
        AtomicInteger index = new AtomicInteger();

        List<T> trains = Streams.concat(
                Stream.of( new T( null, train, index.getAndIncrement() ) ),
                train.isAssociationsPresent()
                ? train.getAssociations().
                        stream().
                        filter( a -> "VV".equals( a.getCat() ) || "JJ".equals( a.getCat() ) ).
                        map( a -> {
                            try {
                                Train t = trainCache.get( a.getAssoc() );
                                return new T( a, t, index.getAndIncrement() );
                            }
                            catch( SQLException ex ) {
                                Logger.getLogger( TrainEntryTag.class.getName() ).log( Level.SEVERE, null, ex );
                                return null;
                            }
                        } )
                : Stream.empty()
        ).
                filter( Objects::nonNull ).
                filter( t -> t.getTrain().isForecastPresent() ).
                collect( Collectors.toList() );

        List<TrainEntry> entries = trains.stream().
                map( t -> {
                    // Form list of entries, sorted but also linked to each other
                    List<TrainEntry> l = t.getTrain().
                    forecastEntries().
                    map( e -> new TrainEntry( t.getAssoc(), t.getTrain(), e, t.getIndex() ) ).
                    sorted().
                    collect( Collectors.toList() );

                    TrainEntry last = null;
                    for( TrainEntry e: l ) {
                        e.setPrevious( last );
                        if( last == null ) {
                            t.setEntry( e );
                        }
                        last = e;
                    }

                    t.setEntries( l );
                    return l;
                } ).
                flatMap( Collection::stream ).
                sorted().
                collect( Collectors.toList() );

        // Now link the lines by each association so they get placed in the correct position
        trains.stream().
                filter( t -> t.assoc != null ).
                forEach( t -> {
                    String main = t.assoc.getMain();
                    String tpl = t.assoc.getTpl();

                    t.entries.stream().
                    filter( e -> e.getTpl().equals( tpl ) ).
                    findAny().
                    ifPresent( ae -> {
                        trains.stream().
                        filter( t2 -> t2.train.getRid().equals( main ) ).
                        findAny().
                        ifPresent( t2 -> {
                            t2.entries.stream().
                            filter( e -> e.getTpl().equals( t.assoc.getTpl() ) ).
                            findAny().
                            ifPresent( e -> {
                                ae.setPrevious( e );
                            } );
                        } );
                    } );
                } );
        //Collections.sort( entries );

        // Finally override the row positions to the indices
        for( int i = 0; i < entries.size(); i++ ) {
            entries.get( i ).setRow( i );
        }

        pageContext.setAttribute( var, entries );

        if( maxIndex != null ) {
            pageContext.setAttribute( maxIndex, index.incrementAndGet() );
        }

        return SKIP_BODY;
    }

    public void setVar( String var )
    {
        this.var = var;
    }

    public void setTrain( Train train )
    {
        this.train = train;
    }

    public void setMaxIndex( String maxIndex )
    {
        this.maxIndex = maxIndex;
    }

}
