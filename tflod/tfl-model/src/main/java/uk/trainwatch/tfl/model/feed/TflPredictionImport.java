/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.model.feed;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
public class TflPredictionImport
        implements SQLConsumer<String>
{

    private static final String IMPORT_SQL = "SELECT tfl.prediction(?::xml)";

    private final DataSource dataSource;

    public TflPredictionImport( DataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    @Override
    public void accept( String xml )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             PreparedStatement ps = SQL.prepare( con, IMPORT_SQL ) ) {
            {
                ps.setString( 1, xml );
                try( ResultSet rs = ps.executeQuery() ) {

                }
            }
        }
    }
}
