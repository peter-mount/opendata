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

import uk.trainwatch.gis.gheat.PointLatLng;
import uk.trainwatch.gis.gheat.Projections;
import uk.trainwatch.gis.gheat.Size;
import uk.trainwatch.gis.gheat.HeatMap;
import uk.trainwatch.gis.gheat.MercatorProjection;
import uk.trainwatch.gis.gheat.DataPoint;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DataManager
{

    private final Projections projection = new MercatorProjection();
    private final HeatMapDataSource dataSource;

    public DataManager( HeatMapDataSource dataSource )
    {
        this.dataSource = dataSource;
    }

    public DataPoint[] getPointsForTile( int x, int y, BufferedImage dot, int zoom )
            throws InterruptedException
    {
        List<DataPoint> points = new ArrayList<>();
        Size maxTileSize = new Size( HeatMap.SIZE, HeatMap.SIZE );
        DataPoint adjustedDataPoint;
        DataPoint pixelCoordinate;

        //Top Left Bounds
        DataPoint tlb = projection.fromTileXYToPixel( new DataPoint( x, y ) );
        //Lower right bounds
        DataPoint lrb = new DataPoint( (tlb.getX() + maxTileSize.getWidth()) + dot.getWidth(), (tlb.getY() + maxTileSize.getHeight()) + dot.getWidth() );

        //pad the Top left bounds
        tlb = new DataPoint( tlb.getX() - dot.getWidth(), tlb.getY() - dot.getHeight() );

        PointLatLng[] TilePoints = dataSource.getList( tlb, lrb, zoom, projection );
        //Go throught the list and convert the points to pixel cooridents
        for( PointLatLng llDataPoint: TilePoints ) {
            //Now go through the list and turn it into pixel points
            pixelCoordinate = projection.fromLatLngToPixel( llDataPoint.getLatitude(), llDataPoint.getLongitude(), zoom );

            //Make sure the weight is still pointing after the conversion
            pixelCoordinate.setWeight( llDataPoint.getWeight() );

            //Adjust the point to the specific tile
            adjustedDataPoint = adjustMapPixelsToTilePixels( new DataPoint( x, y ), pixelCoordinate );

            //Make sure the weight  is still pointing after the conversion
            adjustedDataPoint.setWeight( pixelCoordinate.getWeight() );

            //Add the point to the list
            points.add( adjustedDataPoint );
        }

        return points.toArray( new DataPoint[points.size()] );
    }

    private static DataPoint adjustMapPixelsToTilePixels( DataPoint tileXYPoint, DataPoint mapPixelPoint )
    {
        return new DataPoint( mapPixelPoint.getX() - (tileXYPoint.getX() * HeatMap.SIZE), mapPixelPoint.getY() - (tileXYPoint.getY() * HeatMap.SIZE) );
    }

}
