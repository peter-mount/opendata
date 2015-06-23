/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.tfl.trackernet;

import java.io.InputStream;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.trainwatch.tfl.trackernet.model.Root;
import uk.trainwatch.tfl.trackernet.model.Station;

/**
 *
 * @author peter
 */
public class TrackerNetJaxbContextTest
{

    private static final String PREDICTIONS[] =
    {
        "Prediction-C.xml",
        "Prediction-H.xml"
    };

    @Test
    public void testUnmarshal()
            throws Exception
    {
        for( String srcFile : PREDICTIONS )
        {
            try( InputStream is = getClass().getResourceAsStream( srcFile ) )
            {
                Root root = TrackerNetJaxbContext.INSTANCE.unmarshall( is );
                assertNotNull( "no root from unmarshall", root );

                assertNotNull( root.getTime() );
                assertNotNull( root.getTime().getTimeStamp() );
                System.out.printf( "%s %s\n", srcFile, root.getTime() );

                List<Station> stations = root.getStations();
                assertFalse( "No stations", stations.isEmpty() );
            }
        }
    }

}
