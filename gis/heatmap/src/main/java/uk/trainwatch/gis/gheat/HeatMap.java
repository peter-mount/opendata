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
package uk.trainwatch.gis.gheat;

import uk.trainwatch.gis.gheat.datasources.DataManager;
import uk.trainwatch.gis.heatmap.ThemeManager;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class HeatMap
{

    public static final int SIZE = 256;
    public static final int MAX_ZOOM = 31;

    private final Tile tile;
    private final DataManager dataManager;
    public boolean isInitialised = false;

    public HeatMap( Tile tile, DataManager dataManager )
    {
        this.tile = tile;
        this.dataManager = Objects.requireNonNull( dataManager, "No 'Data manager' has been specified" );
    }

    public BufferedImage getTile( String colorScheme,
                                  int zoom,
                                  int x,
                                  int y,
                                  boolean changeOpacityWithZoom,
                                  int defaultOpacity )
            throws Exception
    {
        if( colorScheme.isEmpty() ) {
            throw new Exception( "A color scheme is required" );
        }

        BufferedImage dot = ThemeManager.INSTANCE.getDot( zoom );
        return tile.generate( ThemeManager.INSTANCE.getScheme( colorScheme ),
                              dot,
                              zoom,
                              x,
                              y,
                              dataManager.getPointsForTile( x, y, dot, zoom ), changeOpacityWithZoom, defaultOpacity );
    }

//    public static BufferedImage getTile( DataManager dataManager, String colorScheme, int zoom, int x, int y )
//            throws Exception
//    {
//        return HeatMap.getTile( dataManager, colorScheme, zoom, x, y, false, 100 );
//    }
}
