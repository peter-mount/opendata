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
package uk.trainwatch.nrod.rtppm.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.function.BiConsumer;
import uk.trainwatch.nrod.rtppm.model.RTPPMDataMsg;
import uk.trainwatch.util.Streams;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author Peter T Mount
 */
public class OperatorPPMSQL
{

    /**
     * Imports all OperatorPagePPM's within an RTPPMDataMsg
     * <p>
     */
    public static final BiConsumer<Connection, RTPPMDataMsg> INSERT_OPERATORPAGEPPM
                                                             = (con, m) -> Streams.stream( m.getOperatorPages() ).
            forEach( SQLConsumer.guardIgnoreDuplicates(
                            o ->
                            {
                                try( PreparedStatement s = con.prepareStatement(
                                        "SELECT rtppm.operatorppm(?,?,?,?,?,?,?,?)" ) )
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
                                    s.executeQuery().
                                    close();
                                        }
                            }
                    )
            );
}
