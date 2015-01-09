/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import uk.trainwatch.gis.kml.Placemark;
import uk.trainwatch.util.cache.Cache;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLFunction;

/**
 *
 * @author peter
 */
public enum StationPositionManager
{

    INSTANCE;

    /**
     * 0.014492 is ~ 1 mile in latitude, so it'll do
     */
    private DataSource dataSource;

    private final Cache<String, List<StationPosition>> nameCache = new Cache<>();
    private final Cache<StationPosition, List<StationPosition>> nearByCache = new Cache<>();
    /**
     * Function to get the 10 nearby stations to another.
     * <p>
     * For this to work the StationPosition in the input must have latitude,longitude and distance set in degrees. All other fields are ignored.
     */
    private final SQLFunction<StationPosition, List<StationPosition>> nearBy = center -> {
        double r = center.getDistance();
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "SELECT * FROM gis.stations WHERE latitude BETWEEN ? AND ? AND longitude BETWEEN ? AND ?",
                                                     center.getLatitude() - r,
                                                     center.getLatitude() + r,
                                                     center.getLongitude() - r,
                                                     center.getLongitude() + r
            ) ) {
                Logger.getGlobal().
                        log( Level.INFO, () -> "ps " + ps );
                List<StationPosition> l = SQL.stream( ps, StationPosition.fromSQL ).
                        // We want to set the distance from pos
                        map( GIS.setDistance( center ) ).
                        // Filter results so only those 0 < d <= range are included
                        filter( GIS.getRange( r / GIS.MILES_TO_DEGREES ) ).
                        // Sort by distance then limit the result size
                        sorted( Distance.COMPARATOR ).
                        limit( 10 ).
                        collect( Collectors.toList() );

                Logger.getLogger( "XXX" ).
                        log( Level.WARNING, () -> "Center " + center + " Range " + r + " returning " + l );
                return l;
            }
        }
    };

    void setDataSource( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    public List<StationPosition> find( String name )
            throws SQLException
    {
        return nameCache.computeSQLIfAbsent( name, () -> {
            try( Connection con = dataSource.getConnection() ) {
                try( PreparedStatement ps = SQL.prepare( con, "SELECT * FROM gis.stations WHERE name=?", name ) ) {
                    return SQL.stream( ps, StationPosition.fromSQL ).
                            collect( Collectors.toList() );
                }
            }
        } );
    }

    public List<StationPosition> findTiploc( String tiploc )
            throws SQLException
    {
        return nameCache.computeSQLIfAbsent( tiploc, () -> {
            try( Connection con = dataSource.getConnection() ) {
                try( PreparedStatement ps = SQL.prepare( con, "SELECT * FROM gis.stations WHERE tiploc=?", tiploc ) ) {
                    return SQL.stream( ps, StationPosition.fromSQL ).
                            collect( Collectors.toList() );
                }
            }
        } );
    }

    /**
     * Return the stations within range miles (approx)
     * <p>
     * @param pos
     * @param range
     *              <p>
     * @return
     *         <p>
     * @throws java.sql.SQLException
     */
    public List<StationPosition> nearby( StationPosition pos, int range )
            throws SQLException
    {
        if( range == 0 ) {
            return Collections.emptyList();
        }

        // Estimate the radius for the DB query - we will filter for accuracy later
        return nearByCache.computeSQLIfAbsent( new StationPosition( pos, GIS.MILES_TO_DEGREES * (double) range ), nearBy );
    }

    public void forEach( Consumer<Placemark> c )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( Statement s = con.createStatement() ) {
                SQL.stream( s.executeQuery( "SELECT *, name as \"Name\" FROM gis.stations" ), Placemark.fromSQL ).
                        forEach( c );
            }
        }
    }

    public List<StationPosition> getAll()
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( Statement s = con.createStatement() ) {
                return SQL.stream( s.executeQuery( "SELECT *, name as \"Name\" FROM gis.stations" ), StationPosition.fromSQL ).
                        sorted( StationPosition.NAME_COMPARATOR ).
                        collect( Collectors.toList() );
            }
        }
    }
}
