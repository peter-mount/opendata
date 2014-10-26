/*
 * Copyright 2014 Peter T Mount.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.trainwatch.nrod.rtppm.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.kohsuke.MetaInfServices;
import uk.trainwatch.nrod.rtppm.factory.RTPPMDataMsgFactory;
import uk.trainwatch.util.JsonUtils;
import uk.trainwatch.util.Streams;
import uk.trainwatch.util.app.DBUtility;
import uk.trainwatch.util.app.Utility;
import uk.trainwatch.util.sql.SQLConsumer;
import uk.trainwatch.util.sql.UncheckedSQLException;

/**
 *
 * @author Peter T Mount
 */
@MetaInfServices( Utility.class )
public class ImportRTPPM
        extends DBUtility
{

    private static final Logger LOG = Logger.getLogger( ImportRTPPM.class.getName() );
    private List<String> fileNames;

    @Override
    public boolean parseArgs( CommandLine cmd )
    {
        if( super.parseArgs( cmd ) )
        {
            fileNames = cmd.getArgList();
            return true;
        }
        return false;
    }

    @Override
    public void runUtility()
            throws Exception
    {
        for( String fileName : fileNames )
        {
            LOG.log( Level.INFO, () -> "Importing " + fileName );
            try( BufferedReader r = new BufferedReader( new FileReader( fileName ) );
                 Connection con = getConnection() )
            {
                try
                {
                    con.setAutoCommit( false );
                    r.lines().
                            map( JsonUtils.parseJsonObject ).
                            map( RTPPMDataMsgFactory.INSTANCE ).
                            forEach( m -> Streams.stream( m.getOperatorPages() ).
                                    forEach( SQLConsumer.guard(
                                                    o ->
                                                    {
                                                        try( PreparedStatement s = con.prepareStatement(
                                                                "INSERT INTO rtppm.realtime (operator,ts,run,ontime,late,canc,ppm,rolling)"
                                                                + " VALUES (rtppm.operator(?),?,?,?,?,?,?,?)" ) )
                                                        {
                                                            int i = 1;
                                                            s.setString( i++, o.getName() );
                                                            s.setTimestamp( i++, new Timestamp( m.getTimestamp() ) );
                                                            s.setInt( i++, o.getTotal() );
                                                            s.setInt( i++, o.getOnTime() );
                                                            s.setInt( i++, o.getLate() );
                                                            s.setInt( i++, o.getCancelVeryLate() );
                                                            s.setInt( i++, o.getPpm().
                                                                      getValue() );
                                                            s.setInt( i++, o.getRollingPPM().
                                                                      getValue() );
                                                            s.executeUpdate();
                                                                }
                                                    }
                                            )
                                    )
                            );
                    con.commit();
                }
                catch( UncheckedSQLException |
                       SQLException ex )
                {
                    LOG.log( Level.SEVERE, ex.getMessage(), ex );
                    con.rollback();
                }
            }

        }
    }

}
