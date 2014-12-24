/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nrod.signalmapeditor.map;

/**
 * A simple node with some text
 * <p>
 * @author peter
 */
public class Text
        extends LineNode
{

    public static final String PROP_TEXT = "text_text";
    private String text;

    public Text( int x, int y )
    {
        super( x, y );
    }

    public Text( int x, int y, String text )
    {
        super( x, y );
        this.text = text;
    }

    public String getText()
    {
        System.out.println( "text=\"" + text + "\"" );
        return text;
    }

    public void setText( String text )
    {
        String oldText = this.text;
        this.text = text;
        firePropertyChange( PROP_TEXT, oldText, text );
    }

    @Override
    public void accept( MapVisitor v )
    {
        v.visit( this );
    }

}
