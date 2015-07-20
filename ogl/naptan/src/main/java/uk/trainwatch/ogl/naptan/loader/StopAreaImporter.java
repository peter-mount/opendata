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
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.csv.CSVRecord;
import uk.trainwatch.ogl.naptan.StopAreaReference;
import uk.trainwatch.util.TimeUtils;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 * NaPTAN StopAreas.
 * <p>
 * We use this as this maps the TfL feeds to an actual stop.
 * <p>
 * @author peter
 */
@XmlRootElement
public class StopAreaImporter
        extends BaseImporter<StopAreaReference>
{

    private static final String INSERT_SQL
                                = "INSERT INTO " + SCHEMA + ".naptan_stopareas ("
                                  + "code,"
                                  + "name, nameLang,"
                                  + "adminareacode,stopareatype,"
                                  + "gridType,easting,northing,"
                                  + "creationdt,modificationdt,"
                                  + "revisionNumber,modification"
                                  + ") VALUES ("
                                  + "?,"
                                  + "?,?,"
                                  + "?,?,"
                                  + "?,?,?,"
                                  + "?,?,"
                                  + "?,?)";

    public StopAreaImporter( Path path )
    {
        super( path );
    }

    @Override
    public void initDB( Connection con )
            throws SQLException
    {
        SQL.deleteIdTable( con, SCHEMA, "naptan_stopareas" );
    }

    @Override
    public Function<CSVRecord, StopAreaReference> getMapper()
    {
        return r -> new StopAreaReference(
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
                null,//TimeUtils.parseXMLLocalDateTime( r.get( 9 ) ),
                "".equals( r.get( 10 ) ) ? 0 : Integer.parseInt( r.get( 10 ) ),
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
    public Consumer<StopAreaReference> insert( PreparedStatement ps )
    {
        return SQLConsumer.guard( r -> {
            int i = 1;
            ps.setString( i++, r.getCode() );
            ps.setString( i++, r.getName() );
            ps.setString( i++, r.getNameLang() );
            ps.setString( i++, r.getAdminAreaCode() );
            ps.setString( i++, r.getStopAreaType() );
            ps.setString( i++, r.getGridType() );
            ps.setInt( i++, r.getEasting() );
            ps.setInt( i++, r.getNorthing() );
            SQL.setLocalDateTime( ps, i++, r.getCreated() );
            SQL.setLocalDateTime( ps, i++, r.getModified() );
            ps.setInt( i++, r.getRevision() );
            ps.setString( i++, r.getModification() );
            ps.executeUpdate();
        }
        );
    }

}
