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

import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Peter T Mount
 */
//@MetaInfServices( Utility.class )
public class ImportRTPPM
//        extends DBUtility
{

    private static final Logger LOG = Logger.getLogger( ImportRTPPM.class.getName() );
    private List<String> fileNames;

//    @Override
//    public boolean parseArgs( CommandLine cmd )
//    {
//        if( super.parseArgs( cmd ) )
//        {
//            fileNames = cmd.getArgList();
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void runUtility()
//            throws Exception
//    {
//        for( String fileName : fileNames )
//        {
//            LOG.log( Level.INFO, () -> "Importing " + fileName );
//            try( BufferedReader r = new BufferedReader( new FileReader( fileName ) );
//                 Connection con = getConnection() )
//            {
//                try
//                {
//                    r.lines().
//                            map( JsonUtils.parseJsonObject ).
//                            map( RTPPMDataMsgFactory.INSTANCE ).
//                            forEach( m -> OperatorPPMSQL.INSERT_OPERATORPAGEPPM.accept( con, m ) );
//                }
//                catch( UncheckedSQLException ex )
//                {
//                    LOG.log( Level.SEVERE, ex.getMessage(), ex );
//                }
//            }
//
//        }
//    }

}
