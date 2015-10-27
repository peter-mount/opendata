/*
 * Copyright 2014 peter.
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
package uk.trainwatch.nrod.feed.rtppm;

import java.sql.Connection;
import java.sql.SQLException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.nrod.rtppm.model.RTPPMDataMsg;
import uk.trainwatch.nrod.rtppm.sql.OperatorPPMSQL;
import uk.trainwatch.util.sql.Database;
import uk.trainwatch.util.sql.SQLConsumer;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class RtppmReporter
        implements SQLConsumer<RTPPMDataMsg>
{

    @Database("rail")
    @Inject
    private DataSource dataSource;

    @Override
    public void accept( RTPPMDataMsg t )
            throws SQLException
    {
        if( t != null ) {
            try( Connection con = dataSource.getConnection() ) {
                OperatorPPMSQL.INSERT_OPERATORPAGEPPM.accept( con, t );
            }
        }
    }

}
