/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.tpnm.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.function.Function;
import javax.xml.datatype.XMLGregorianCalendar;
import uk.trainwatch.util.sql.SQL;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 * @param <T>
 */
public class TextImporter<T>
        extends AbstractImporter<Object, T>
        implements SQLConsumer<Object>
{

    private final String schema;
    private final String table;
    private final Function<T, Integer> id;
    private final Function<T, String> text;
    private final Function<T, XMLGregorianCalendar> ts;
    private final PreparedStatement ps;

    public TextImporter( Connection con, String schema, String table, Function<T, Integer> id, Function<T, String> text, Function<T, XMLGregorianCalendar> ts )
            throws SQLException
    {
        super( con, 100, schema + "." + table );
        this.id = id;
        this.schema = schema;
        this.text = text;
        this.ts = ts;
        this.table = table;

        ps = SQL.prepare( con, "INSERT INTO " + schema + "." + table + " (id,text,ts) VALUES (?,?,?)" );
    }

    @Override
    protected void process( T v )
            throws SQLException
    {
        if( isFirst() ) {
            SQL.deleteTable( con, schema, table );
        }

        SQL.setParameters( ps,
                           id.apply( v ),
                           text.apply( v ),
                           new Timestamp( ts.apply( v ).toGregorianCalendar().toInstant().getEpochSecond() ) )
                .executeUpdate();
    }

}
