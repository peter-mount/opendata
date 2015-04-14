/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.mediawiki;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts the page title from the page
 * <p>
 * @author peter
 */
public class TitleExtractor
        implements UnaryOperator<Page>
{

    private static final Pattern TITLE = Pattern.compile( "<title>(.+?)</title>" );

    @Override
    public Page apply( Page page )
    {
        // Get the page title
        page.lines().
                filter( l -> l.contains( "<title>" ) ).
                forEach( l -> {

                    Matcher m = TITLE.matcher( l );
                    if( m.matches() ) {
                        page.setTitle( m.group( 1 ) );
                    }
                } );

        return page;
    }

}
