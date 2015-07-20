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
import uk.trainwatch.ogl.naptan.AreaHierarchy;
import uk.trainwatch.ogl.naptan.StopInArea;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 * NaPTAN StopsInArea - maps StopAreas to both Rail and other stops.
 * <p>
 * We use this as a means to map TfL stops (like DLR/Tube station) to a CRS
 * code (if known).
 * <p>
 * @author peter
 */
@XmlRootElement
public class AreaHierarchyLoader
        extends BaseImporter<AreaHierarchy>
{

    private static final String INSERT_SQL = "INSERT INTO " + SCHEMA + ".naptan_areahierarchy (parent,child) VALUES (?,?)";

    public AreaHierarchyLoader( Path path )
    {
        super( path );
    }

    @Override
    public void initDB( Connection con )
            throws SQLException
    {
        SQL.deleteIdTable( con, SCHEMA, "naptan_areahierarchy" );
    }

    @Override
    public Function<CSVRecord, AreaHierarchy> getMapper()
    {
        return r -> new AreaHierarchy(
                // ID is always null here as it's not from the DB
                0L,
                r.get( 0 ),
                r.get( 1 )
        );
    }

    @Override
    public PreparedStatement prepare( Connection con )
            throws SQLException
    {
        return con.prepareStatement( INSERT_SQL );
    }

    @Override
    public Consumer<AreaHierarchy> insert( PreparedStatement ps )
    {
        return SQLConsumer.guard( r -> {
            int i = 1;
            ps.setString( i++, r.getParent() );
            ps.setString( i++, r.getChild() );
            ps.executeUpdate();
        } );
    }

}
