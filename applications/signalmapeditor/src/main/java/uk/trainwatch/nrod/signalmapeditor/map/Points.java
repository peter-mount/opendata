/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

/**
 * In the map editor, a set of points is defined as a Node.
 * <p>
 * The initial version takes up 1x2 spaces
 * @author peter
 */
public class Points
        extends LineNode
{

    @Override
    public boolean contains( int x, int y )
    {
        return super.contains( x, y ) || y==getY()+1;
    }

    @Override
    public void accept( MapVisitor v )
    {
        v.visit( this );
    }

}
