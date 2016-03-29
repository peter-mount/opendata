/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.signal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.nrod.td.berth.BerthAreaMap;
import uk.trainwatch.nrod.td.berth.BerthMap;
import uk.trainwatch.nrod.td.berth.RecentAreaMap;
import uk.trainwatch.nrod.td.model.TDMessage;
import uk.trainwatch.util.RecentList;
import uk.trainwatch.util.config.Database;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class SignalManager
{

    private static final String SQL_SELECT = "SELECT * FROM reference.smart_area";
    private static final String SQL_ALL_AREAS = SQL_SELECT + " ORDER BY area";
    private static final String SQL_AREA = SQL_SELECT + " WHERE area=?";

    @Database("rail") @Inject
    private DataSource dataSource;

    @Inject
    private BerthAreaMap areaMap;

    @Inject
    private RecentAreaMap recentMap;

    public BerthAreaMap getAreaMap()
    {
        return areaMap;
    }

    public BerthMap getArea( String area )
    {
        return areaMap.getArea( area );
    }

    /**
     * Is the area valid
     * <p>
     * @param area
     *             <p>
     * @return
     */
    public boolean containsArea( String area )
    {
        return areaMap.containsArea( area );
    }

    /**
     * Returns the list of signal areas available in Smart
     * <p>
     * @return
     *         <p>
     * @throws SQLException
     */
    public Collection<SignalArea> getSignalAreas()
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             Statement s = con.createStatement() ) {
            return SQL.stream( s.executeQuery( SQL_ALL_AREAS ), SignalArea.fromSQL ).
                    collect( Collectors.toList() );
        }
    }

    public Optional<SignalArea> getSignalArea( String area )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection();
             PreparedStatement ps = SQL.prepare( con, SQL_AREA, area ) ) {
            return SQL.stream( ps, SignalArea.fromSQL ).
                    findFirst();
        }
    }

    public RecentList<TDMessage> getRecent( String area )
    {
        return recentMap.getRecentList( area );
    }
}
