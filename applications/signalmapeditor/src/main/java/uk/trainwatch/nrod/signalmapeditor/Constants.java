/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor;

import java.awt.Point;
import uk.trainwatch.nrod.signalmapeditor.map.Node;

/**
 *
 * @author peter
 */
public interface Constants
{

    static final int COLUMN_WIDTH = 50;
    static final int ROW_HEIGHT = 25;
    static final int TOP_LEFT_OFFSET = 14;
    static final int FONT_SIZE = 10;

    static final int COLUMN_CENTER = COLUMN_WIDTH >>> 1;
    static final int ROW_CENTER = ROW_HEIGHT >>> 1;

    static int getX( int x )
    {
        return TOP_LEFT_OFFSET + (x * COLUMN_WIDTH);
    }

    static int getY( int y )
    {
        return TOP_LEFT_OFFSET + (y * ROW_HEIGHT);
    }

    static int getX( Node b )
    {
        return getX( b.getX() );
    }

    static int getY( Node b )
    {
        return getY( b.getY() );
    }

    static int getGridX( Point p )
    {
        return getGridX( (int) p.getX() );
    }

    static int getGridY( Point p )
    {
        return getGridY( (int) p.getY() );
    }

    static int getGridX( int x )
    {
        return (x - TOP_LEFT_OFFSET) / COLUMN_WIDTH;
    }

    static int getGridY( int y )
    {
        return (y - TOP_LEFT_OFFSET) / ROW_HEIGHT;
    }

}
