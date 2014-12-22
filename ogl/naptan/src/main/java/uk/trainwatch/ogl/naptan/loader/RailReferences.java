/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.ogl.naptan.loader;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.commons.csv.CSVRecord;
import uk.trainwatch.ogl.naptan.RailReference;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
class RailReferences
        extends BaseImporter<RailReference>
{

    private static final String INSERT_SQL
            = "INSERT INTO " + SCHEMA + ".naptan_rail ("
            + "atcoCode, tiplocCode, crsCode,"
            + "stationName, stationNameLang,"
            + "gridType,easting,northing,"
            + "creationdt,modificationdt,"
            + "revisionNumber,modification"
            + ") VALUES ("
            + "?,?,?,?,?,?,?,?,"
            + "?,?,"
            + "?,?)";

    public RailReferences( Path path )
    {
        super( path );
    }

    @Override
    public void initDB( Connection con )
            throws SQLException
    {
        SQL.deleteIdTable( con, SCHEMA, "naptan_rail" );
    }

    @Override
    public Function<CSVRecord, RailReference> getMapper()
    {
        return r -> new RailReference(
                // ID is always null here as it's not from the DB
                0L,
                r.get( 0 ),
                r.get( 1 ),
                r.get( 2 ),
                r.get( 3 ),
                r.get( 4 ),
                r.get( 5 ),
                Integer.parseInt( r.get( 6 ) ),
                Integer.parseInt( r.get( 7 ) ),
                TimeUtils.parseXMLLocalDateTime( r.get( 8 ) ),
                TimeUtils.parseXMLLocalDateTime( r.get( 9 ) ),
                Integer.parseInt( r.get( 10 ) ),
                r.get( 11 )
        );
    }

    @Override
    public PreparedStatement prepare( Connection con )
            throws SQLException
    {
        return con.prepareStatement( INSERT_SQL );
    }

    @Override
    public Consumer<RailReference> insert( PreparedStatement ps )
    {
        return SQLConsumer.guard( r -> {
            int i = 1;
            ps.setString( i++, r.getAtcoCode() );
            ps.setString( i++, r.getTiplocCode() );
            ps.setString( i++, r.getCrsCode() );
            ps.setString( i++, r.getStationName() );
            ps.setString( i++, r.getStationLang() );
            ps.setString( i++, r.getGridType() );
            ps.setInt( i++, r.getEasting() );
            ps.setInt( i++, r.getNorthing() );
            SQL.setLocalDateTime( ps, i++, r.getCreationDateTime() );
            SQL.setLocalDateTime( ps, i++, r.getModificationDateTime() );
            ps.setInt( i++, r.getRevisionNumber() );
            ps.setString( i++, r.getModification() );
            ps.executeUpdate();
        }
        );
    }

}
