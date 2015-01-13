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

import java.util.ArrayList;
import java.util.List;

public class Opacity
{

    /*
     Alpha value that indicates an image is not transparent
     */
    public static final int OPAQUE = 255;

    /*
     Alpha value that indicates that an image is transparent
     */
    public static final int TRANSPARENT = 0;

    /* 
     Max zoom that google supports
     */
    public final int MAX_ZOOM = 31;
    public final int ZOOM_OPAQUE = -15;
    public final int ZOOM_TRANSPARENT = 15;
    public final int DEFAULT_OPACITY = 50;

    private final int zoomOpaque;
    private final int zoomTransparent;

    public Opacity( int zoomOpaque, int zoomTransparent )
    {
        this.zoomOpaque = zoomOpaque;
        this.zoomTransparent = zoomTransparent;
    }

    /*
     Uses default values if not specified
     */
    public Opacity()
    {
        zoomOpaque = ZOOM_OPAQUE;
        zoomTransparent = ZOOM_TRANSPARENT;
    }

    /*
     Build and return the zoom_to_opacity mapping
     returns index=zoom and value of the element is the opacity
     */
    public int[] buildZoomMapping()
    {
        List<Integer> zoomMapping = new ArrayList<>();
        int numberOfOpacitySteps;
        float opacityStep;

        numberOfOpacitySteps = zoomTransparent - zoomOpaque;

        if( numberOfOpacitySteps < 1 ) //don't want general fade
        {
            for( int i = 0; i <= MAX_ZOOM; i++ ) {
                zoomMapping.add( 0 );
            }
        }
        else //want general fade
        {
            opacityStep = ((float) OPAQUE / (float) numberOfOpacitySteps); //chunk of opacity
            for( int zoom = 0; zoom <= MAX_ZOOM; zoom++ ) {
                if( zoom <= zoomOpaque ) {
                    zoomMapping.add( OPAQUE );
                }
                else if( zoom >= zoomTransparent ) {
                    zoomMapping.add( TRANSPARENT );
                }
                else {
                    zoomMapping.add( (int) ((float) OPAQUE - (((float) zoom - (float) zoomOpaque) * opacityStep)) );
                }
            }
        }
        return toIntArray( zoomMapping );
    }

    int[] toIntArray( List<Integer> list )
    {
        int[] ret = new int[list.size()];
        int i = 0;
        for( Integer e: list ) {
            ret[i++] = e;
        }
        return ret;
    }
}
