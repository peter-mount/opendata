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
        implements SQLConsumer<Object>
{

    private final Connection con;
    private final String sql;
    private final Function<T, Integer> id;
    private final Function<T, String> text;
    private final Function<T, XMLGregorianCalendar> ts;

    public TextImporter( Connection con, String schema, String table, Function<T, Integer> id, Function<T, String> text, Function<T, XMLGregorianCalendar> ts )
    {
        this( con, schema + "." + table, id, text, ts );
    }

    public TextImporter( Connection con, String table, Function<T, Integer> id, Function<T, String> text, Function<T, XMLGregorianCalendar> ts )
    {
        this.con = con;
        this.id = id;
        this.text = text;
        this.ts = ts;

        sql = "INSERT INTO " + table + " (id,text,ts) VALUES (?,?,?)";
    }

    @Override
    public void accept( Object o )
            throws SQLException
    {
        T v = (T) o;
        try( PreparedStatement ps = SQL.prepare( con,
                                                 sql,
                                                 id.apply( v ),
                                                 text.apply( v ),
                                                 new Timestamp( ts.apply( v ).toGregorianCalendar().toInstant().getEpochSecond() ) ) ) {
            ps.executeUpdate();
        }
    }

}
