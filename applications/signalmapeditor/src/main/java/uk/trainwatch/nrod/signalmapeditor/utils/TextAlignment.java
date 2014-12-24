package uk.trainwatch.nrod.signalmapeditor.utils;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * An enumeration representing how text is aligned horizontally.
 *
 * @author peter
 */
public enum TextAlignment
{

    LEFT,
    RIGHT,
    CENTER,
    CENTRE;

    /**
     * A variant of valueOf() which, if the requested name is not a valid value
     * it will default to TextAlignment.LEFT.
     * <p>
     * @param name String version of enum values, case insensitive.
     * <p>
     * @return TextAlignment for name or TextAlignment.LEFT
     */
    public static TextAlignment valueOfWithDefault( String name )
    {
        return valueOfWithDefault( name, TextAlignment.LEFT );
    }

    /**
     * A variant of valueOf() which takes a default value that is returned if
     * the requested name is not a valid value.
     * <p>
     * @param name             String version of enum values, case insensitive.
     * @param defaultAlignment the default to return
     * <p>
     * @return TextAlignment for name or defaultAlignment
     */
    public static TextAlignment valueOfWithDefault( String name,
                                                    TextAlignment defaultAlignment )
    {
        try {
            return name == null ? defaultAlignment : TextAlignment.valueOf(
                    name.toUpperCase() );
        }
        catch( Exception e ) {
            return defaultAlignment;
        }
    }

    public double drawEmbossedString( final Graphics g, final String src, final Rectangle2D rect )
    {
        return drawEmbossedString(
                g,
                g.getFontMetrics(),
                src,
                rect.getX(),
                rect.getY(),
                rect.getWidth() );
    }

    /**
     * Utility method to actually draw a string using this alignment, but in
     * an embossed manner - i.e. a disabled item of text
     *
     * @param g     Graphics
     * @param fm    FontMetrics of current font
     * @param src   String to draw
     * @param left  coordinate to fit the string
     * @param width maximum line width of the string
     * @param y     coordinate of top of line
     * <p>
     * @return y coordinate of next line.
     */
    public double drawEmbossedString(
            final Graphics g,
            final FontMetrics fm,
            final String src,
            final double left,
            final double y,
            final double width )
    {
        Color c = g.getColor();
        g.setColor( c.brighter() );
        double ret = drawString( g, fm, src, left + 1, y + 1, width );
        g.setColor( c.darker() );
        drawString( g, fm, src, left, y, width );
        g.setColor( c );
        return ret;
    }

    public double drawString( final Graphics g, final String src,
                              final Rectangle2D rect )
    {
        return drawString(
                g,
                g.getFontMetrics(),
                src,
                rect.getX(),
                rect.getY(),
                rect.getWidth() );
    }

    /**
     * Utility method to actually draw a string using this alignment.
     *
     * @param g     Graphics
     * @param fm    FontMetrics of current font
     * @param src   String to draw
     * @param left  coordinate to fit the string
     * @param width maximum line width of the string
     * @param y     coordinate of top of line
     * <p>
     * @return y coordinate of next line.
     */
    public double drawString(
            final Graphics g,
            final FontMetrics fm,
            final String src,
            final double left,
            final double y,
            final double width )
    {
        double w = fm.stringWidth( src );
        if( w > width || src.contains( "\n" ) ) {
            // Split the line
            String s = src;
            List<String> lines = new ArrayList<>();

            while( s.length() > 0 ) {
                String t = s;
                
                // Split on new line
                while( t.indexOf( '\n' ) > -1 ) {
                    t = t.substring( 0, t.indexOf( '\n' ) );
                    w = fm.stringWidth( t );
                }

                // Split on space if too wide
                while( w > width && t.lastIndexOf( ' ' ) > -1 ) {
                    t = t.substring( 0, t.lastIndexOf( ' ' ) );
                    w = fm.stringWidth( t );
                }

                if( w > width ) {
                    // Split by character
                    while( w > width ) {
                        t = t.substring( 0, t.length() - 1 );
                        w = fm.stringWidth( t );
                    }
                    s = s.substring( t.length() );
                }
                else if( t.length() < s.length() ) {
                    // Remove the space from the string
                    s = s.substring( t.length() + 1 );
                }
                else {
                    s = "";
                }
                
                lines.add( t );
                w = fm.stringWidth( s );
            }

            double y1 = y - ((double) lines.size() * fm.getHeight() / 2.0);
            for( String t: lines ) {
                y1 = drawStringImpl( g, fm, t, left, y1, width );
            }

            return y1;
        }

        // No wrapping required
        return drawStringImpl( g, fm, src, left, y, width );
    }

    private double drawStringImpl(
            final Graphics g,
            final FontMetrics fm,
            final String s,
            final double left,
            final double y,
            final double width )
    {
        switch( this ) {
            case CENTER:
            case CENTRE:
                g.drawString(
                        s,
                        (int) (left + ((width - (double) fm.stringWidth( s )) / 2.0)),
                        (int) (y + (double) fm.getMaxAscent()) );
                break;

            case LEFT:
                g.drawString(
                        s,
                        (int) left,
                        (int) (y + (double) fm.getMaxAscent()) );
                break;

            case RIGHT:
                g.drawString(
                        s,
                        (int) (left + width - (double) fm.stringWidth( s )),
                        (int) (y + (double) fm.getMaxAscent()) );
                break;
        }
        return y + (double) fm.getHeight();
    }
}
