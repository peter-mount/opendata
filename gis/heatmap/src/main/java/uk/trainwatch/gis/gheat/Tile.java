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

import uk.trainwatch.gis.gheat.graphics.BlendComposite;
import uk.trainwatch.gis.gheat.graphics.GammaCorrection;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile
{

    private final Cache cache;
    private final int[] zoomOpacity = new Opacity().buildZoomMapping();

    public Tile( Cache cache )
    {
        this.cache = cache;
    }

    public BufferedImage generate( BufferedImage colorScheme,
                                   BufferedImage dot,
                                   int zoom,
                                   int tileX,
                                   int tileY,
                                   DataPoint[] points,
                                   boolean changeOpacityWithZoom,
                                   int defaultOpacity )
            throws Exception
    {
        int expandedWidth;
        int expandedHeight;

        int x1;
        int x2;
        int y1;
        int y2;

        if( defaultOpacity < Opacity.TRANSPARENT || defaultOpacity > Opacity.OPAQUE ) {
            throw new Exception(
                    "The default opacity of '" + defaultOpacity + "' doesn't fall between '" + Opacity.TRANSPARENT + "' and '" + Opacity.OPAQUE + "'" );
        }

        //Translate tile to pixel coords.
        x1 = tileX * HeatMap.SIZE;
        x2 = x1 + 255;
        y1 = tileY * HeatMap.SIZE;
        y2 = y1 + 255;

        int extraPad = dot.getWidth() * 2;

        //Expand bounds by one dot width.
        x1 = x1 - extraPad;
        x2 = x2 + extraPad;
        y1 = y1 - extraPad;
        y2 = y2 + extraPad;
        expandedWidth = x2 - x1;
        expandedHeight = y2 - y1;

        BufferedImage tile;
        if( points.length == 0 ) {
            if( changeOpacityWithZoom ) {
                tile = getEmptyTile( colorScheme, zoomOpacity[zoom] );
            }
            else {
                tile = getEmptyTile( colorScheme, defaultOpacity );
            }

        }
        else {
            tile = getBlankImage( expandedHeight, expandedWidth );
            tile = addPoints( tile, dot, points );
            tile = trim( tile, dot );
            if( changeOpacityWithZoom ) {
                tile = colorize( tile, colorScheme, zoomOpacity[zoom] );
            }
            else {
                tile = colorize( tile, colorScheme, defaultOpacity );
            }
        }

        return tile;
    }

    /// Takes the gray scale and applies the color scheme to it.
    public BufferedImage colorize( BufferedImage tile, BufferedImage colorScheme, int zoomOpacity )
    {
        Color tilePixelColor;
        Color colorSchemePixel;

        for( int x = 0; x < tile.getWidth(); x++ ) {
            for( int y = 0; y < tile.getHeight(); y++ ) {
                //Get color for this intensity
                tilePixelColor = new Color( tile.getRGB( x, y ) );

                //Get the color of the scheme from the intensity on the tile
                //Only need to get one color in the tile, because it is grayscale, so each color will have the same intensity
                colorSchemePixel = new Color( colorScheme.getRGB( 0, tilePixelColor.getRed() ) );

                zoomOpacity = (int) ((((double) zoomOpacity / 255.0f)
                                      * ((double) colorSchemePixel.getAlpha() / 255.0f)) * 255f);

                tile.setRGB( x, y, new Color( colorSchemePixel.getRed(), colorSchemePixel.getGreen(), colorSchemePixel.getBlue(), zoomOpacity ).getRGB() );
            }
        }
        return tile;
    }

    /// <summary>
    /// Trim the larger tile to the correct size
    /// </summary>
    /// <param name="tile"></param>
    /// <returns></returns>
    public BufferedImage trim( BufferedImage tile, BufferedImage dot )
    {
        BufferedImage croppedTile = new BufferedImage( HeatMap.SIZE, HeatMap.SIZE, BufferedImage.TYPE_INT_ARGB );
        Graphics g = croppedTile.createGraphics();
        int adjPad = dot.getWidth() + (dot.getWidth() / 2);

        g.drawImage(
                tile, // Source Image
                0, 0, HeatMap.SIZE, HeatMap.SIZE,
                adjPad, // source x, adjusted for padded amount
                adjPad, // source y, adjusted for padded amount
                HeatMap.SIZE + adjPad, //source width
                HeatMap.SIZE + adjPad, // source height
                null
        );
        g.dispose();
        return croppedTile;
    }

    /// <summary>
    /// Add all of the points to the tile
    /// </summary>
    /// <param name="tile"></param>
    /// <param name="points"></param>
    /// <returns></returns>
    public BufferedImage addPoints( BufferedImage tile, BufferedImage dot, DataPoint[] points )
    {

        Graphics2D g = tile.createGraphics();
        g.setComposite( BlendComposite.Multiply );

        for( DataPoint point: points ) {
            BufferedImage src = point.getWeight() != 0 ? applyWeightToImage( dot, point.getWeight() ) : dot;
            g.drawImage( convert( src, BufferedImage.TYPE_INT_RGB ), (int) (point.getX() + dot.getWidth()), (int) (point.getY() + dot.getWidth()), null );
        }

        g.dispose();

        return tile;
    }

    public BufferedImage convert( BufferedImage src, int bufImgType )
    {
        BufferedImage img = new BufferedImage( src.getWidth(), src.getHeight(), bufImgType );
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage( src, 0, 0, null );
        g2d.dispose();
        return img;
    }

    /// <summary>
    /// Change the intensity of the image
    /// </summary>
    /// <param name="image">Dot image</param>
    /// <param name="weight">Weight to apply</param>
    /// <returns></returns>
    private BufferedImage applyWeightToImage( BufferedImage image, double weight )
    {
        Graphics2D graphic;
        double tempWeight;
        BufferedImage tempImage = new BufferedImage( image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB );

        graphic = tempImage.createGraphics();

        //I want to make the color more intense (White/bright)
        tempWeight = weight;

        AlphaComposite composite = AlphaComposite.getInstance( AlphaComposite.CLEAR, 0.0f );
        graphic.setComposite( composite );

        graphic.drawImage(
                image, // Source Image
                0, 0, image.getWidth(), image.getHeight(),
                0,
                0,
                image.getWidth(), image.getHeight(), // source height
                null
        );

        //New dot with a different intensity
        return GammaCorrection.gammaCorrection( tempImage, (tempWeight == 0 ? .1f : (tempWeight * 5)) );

    }

    /// <summary>
    /// Gets a blank image / canvas
    /// </summary>
    /// <returns></returns>
    public BufferedImage getBlankImage( int height, int width )
    {
        BufferedImage newImage;
        Graphics2D g;
        //Create a blank tile that is 32 bit and has an alpha
        newImage = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
        g = newImage.createGraphics();
        //Background must be white so the dots can blend
        g.setBackground( Color.WHITE );
        g.fillRect( 0, 0, height, width );
        g.dispose();
        return newImage;
    }

    /*
     Empty tile with no points on it.
     */
    public BufferedImage getEmptyTile( BufferedImage colorScheme, int zoomOpacity )
    {
        Color colorSchemePixelColor;
        BufferedImage tile;
        Graphics2D graphic;

        //If we have already created the empty tile then return it
        if( cache.hasEmptyTile( colorScheme.hashCode(), zoomOpacity ) ) {
            return cache.getEmptyTile( colorScheme.hashCode(), zoomOpacity );
        }

        //Create a blank tile that is 32 bit and has an alpha
        tile = new BufferedImage( HeatMap.SIZE, HeatMap.SIZE, BufferedImage.TYPE_INT_ARGB );

        graphic = tile.createGraphics();

        //Get the first pixel of the color scheme, on the dark side
        colorSchemePixelColor = new Color( colorScheme.getRGB( 0, colorScheme.getHeight() - 1 ) );

        zoomOpacity = (int) (((zoomOpacity / 255.0f) //# From Configuration
                              * (colorSchemePixelColor.getAlpha() / 255.0f) //#From per-pixel alpha
                              ) * 255.0f);

        graphic.setColor( new Color( colorSchemePixelColor.getRed(), colorSchemePixelColor.getGreen(), colorSchemePixelColor.getBlue(), zoomOpacity ) );

        graphic.fillRect( 0, 0, HeatMap.SIZE, HeatMap.SIZE );
        graphic.dispose();

        //Save the newly created empty tile
        //There is a empty tile for each scheme and zoom level
        //Double check it does not already exists
        if( !cache.hasEmptyTile( colorScheme.hashCode(), zoomOpacity ) ) {
            cache.putEmptyTile( tile, colorScheme.hashCode(), zoomOpacity );
        }

        return tile;
    }

}
