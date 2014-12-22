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
import uk.trainwatch.ogl.naptan.AirReference;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
class AirReferences
        extends BaseImporter<AirReference>
{

    private static final String INSERT_SQL
            = "INSERT INTO " + SCHEMA + ".naptan_air ("
            + "atcoCode, iataCode,"
            + "Name, NameLang,"
            + "creationdt,modificationdt,"
            + "revisionNumber,modification"
            + ") VALUES ("
            + "?,?,"
            + "?,?,"
            + "?,?,"
            + "?,?)";

    public AirReferences( Path path )
    {
        super( path );
    }

    @Override
    public void initDB( Connection con )
            throws SQLException
    {
        SQL.deleteIdTable( con, SCHEMA, "naptan_air" );
    }

    @Override
    public Function<CSVRecord, AirReference> getMapper()
    {
        return r -> new AirReference(
                // ID is always null here as it's not from the DB
                0L,
                r.get( 0 ),
                r.get( 1 ),
                r.get( 2 ),
                r.get( 3 ),
                TimeUtils.parseXMLLocalDateTime( r.get( 4 ) ),
                TimeUtils.parseXMLLocalDateTime( r.get( 5 ) ),
                Integer.parseInt( r.get( 6 ) ),
                r.get( 7 )
        );
    }

    @Override
    public PreparedStatement prepare( Connection con )
            throws SQLException
    {
        return con.prepareStatement( INSERT_SQL );
    }

    @Override
    public Consumer<AirReference> insert( PreparedStatement ps )
    {
        return SQLConsumer.guard( r -> {
            int i = 1;
            ps.setString( i++, r.getAtcoCode() );
            ps.setString( i++, r.getIataCode() );
            ps.setString( i++, r.getName() );
            ps.setString( i++, r.getNameLang() );
            SQL.setLocalDateTime( ps, i++, r.getCreated() );
            SQL.setLocalDateTime( ps, i++, r.getModified() );
            ps.setInt( i++, r.getRevision() );
            ps.setString( i++, r.getModification() );
            ps.executeUpdate();
        }
        );
    }

}
