/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.gis;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import uk.trainwatch.util.sql.SQL;

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

    void setDataSource( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    public List<StationPosition> find( String name )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() )
        {
            try( PreparedStatement ps = con.prepareStatement( "SELECT * FROM gis.stations WHERE name=?" ) )
            {
                ps.setString( 1, name );
                return SQL.stream( ps, StationPosition.fromSQL ).
                        collect( Collectors.toList() );
            }
        }
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
        // Estimate the radius for the DB query - we will filter for accuracy later
        double radius = GIS.MILES_TO_DEGREES * (double) range;

        // We want to set the distance from pos
        UnaryOperator<StationPosition> distanceCalculator = GIS.setDistance( pos );

        // Filter results so only those 0 < d <= range are included
        Predicate<StationPosition> rangeFilter = GIS.getRange( range );

        try( Connection con = dataSource.getConnection() )
        {
            try( PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM gis.stations WHERE latitude BETWEEN ? AND ? AND longitude BETWEEN ? AND ? ORDER BY name" ) )
            {
                ps.setDouble( 1, pos.getLatitude() - radius );
                ps.setDouble( 2, pos.getLatitude() + radius );
                ps.setDouble( 3, pos.getLongitude() - radius );
                ps.setDouble( 4, pos.getLongitude() + radius );
                return SQL.stream( ps, StationPosition.fromSQL ).
                        map( distanceCalculator ).
                        filter( rangeFilter ).
                        sorted( Distance.COMPARATOR ).
                        limit( 10 ).
                        collect( Collectors.toList() );
            }
        }
    }

}
