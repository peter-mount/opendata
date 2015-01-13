/*
 * This is a fork of GHEAT_JAVA https://github.com/varunpant/GHEAT-JAVA
 * <p>
 * The MIT License
 * <p>
 * Copyright (c) 2014 Varun Pant
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * <p>
 */
package uk.trainwatch.gis.gheat.datasources;

import uk.trainwatch.gis.gheat.DataPoint;
import uk.trainwatch.gis.gheat.PointLatLng;
import uk.trainwatch.gis.gheat.Projections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

public class PostGisDataSource
        implements HeatMapDataSource
{

    private final DataSource dataSource;
    static String query = null;

    public PostGisDataSource( DataSource dataSource, String query )
    {
        this.dataSource = dataSource;
        this.query = query;
    }

    @Override
    public PointLatLng[] getList( DataPoint tlb, DataPoint lrb, int zoom, Projections _projection )
    {

        List<PointLatLng> llList = null;

        PointLatLng ptlb;
        PointLatLng plrb;

        ptlb = _projection.fromPixelToLatLng( tlb, zoom );
        plrb = _projection.fromPixelToLatLng( lrb, zoom );

        llList = getData( plrb.getLongitude(), plrb.getLatitude(), ptlb.getLongitude(), ptlb.getLatitude() );
        PointLatLng[] result = new PointLatLng[llList.size()];
        for( int i = 0; i < llList.size(); i++ ) {
            result[i] = llList.get( i );
        }

        return result;
    }

    private List<PointLatLng> getData( double llx, double lly, double ulx, double uly )
    {
        List<PointLatLng> llList = new ArrayList<PointLatLng>();

        try( Connection con = dataSource.getConnection() ) {
            String stm = query;

            try( PreparedStatement pst = con.prepareStatement( stm ) ) {
                pst.setDouble( 1, llx );
                pst.setDouble( 2, lly );
                pst.setDouble( 3, ulx );
                pst.setDouble( 4, uly );

                System.out.println( pst );
                ResultSet rs = pst.executeQuery();
                while( rs.next() ) {
                    double weight = rs.getDouble( "weight" );
                    double longitude = rs.getDouble( "longitude" );//x
                    double latitude = rs.getDouble( "latitude" ); //y
                    PointLatLng pt = new PointLatLng( longitude, latitude, weight );
                    llList.add( pt );
                }
            }
        }
        catch( Exception ex ) {
            Logger lgr = Logger.getLogger( PostGisDataSource.class.getName() );
            lgr.log( Level.SEVERE, ex.getMessage(), ex );

        }
        finally {
            return llList;
        }
    }
}
