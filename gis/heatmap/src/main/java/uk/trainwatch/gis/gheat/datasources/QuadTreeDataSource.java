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
import uk.trainwatch.gis.gheat.datasources.QuadTree.QuadTree;

import java.io.BufferedReader;
import java.io.FileReader;

public class QuadTreeDataSource
        implements HeatMapDataSource
{

    static QuadTree qt = new QuadTree( -180.000000, -90.000000, 180.000000, 90.000000 );

    /*
     *Constructor for QuadTreeDataSource takes indexes for longitude,latitude and weight colums from csv file. */
    public QuadTreeDataSource( String filePath, int longitudeIndex, int latitudeIndex, int weightIndex )
    {
        LoadPointsFromFile( filePath, longitudeIndex, latitudeIndex, weightIndex );
    }

    @Override
    public PointLatLng[] getList( DataPoint tlb, DataPoint lrb, int zoom, Projections _projection )
    {

        PointLatLng ptlb = _projection.fromPixelToLatLng( lrb, zoom );
        PointLatLng plrb = _projection.fromPixelToLatLng( tlb, zoom );
        PointLatLng[] list = qt.searchIntersect( plrb.getLongitude(),
                                                 ptlb.getLatitude(),
                                                 ptlb.getLongitude(),
                                                 plrb.getLatitude() );
        return list;
    }

    private void LoadPointsFromFile( String source, int longitudeIndex, int latitudeIndex, int weightIndex )
    {
        String[] item;
        String[] lines = readAllTextFileLines( source );
        for( String line: lines ) {
            item = line.split( "," );
            qt.set( Double.parseDouble( item[longitudeIndex] ), Double.parseDouble( item[latitudeIndex] ), Double.parseDouble( item[latitudeIndex] ) );
        }
    }

    private static String[] readAllTextFileLines( String fileName )
    {
        StringBuilder sb = new StringBuilder();

        try {
            String textLine;

            BufferedReader br = new BufferedReader( new FileReader( fileName ) );

            while( (textLine = br.readLine()) != null ) {
                sb.append( textLine );
                sb.append( '\n' );
            }
        }
        catch( Exception ex ) {
            System.out.println( ex.getMessage() );
        }
        finally {
            if( sb.length() == 0 ) {
                sb.append( "\n" );
            }
        }
        return sb.toString().
                split( "\n" );
    }

}
