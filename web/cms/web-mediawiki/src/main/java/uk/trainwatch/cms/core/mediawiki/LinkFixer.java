/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.cms.core.mediawiki;

import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import uk.trainwatch.cms.core.Page;

/**
 * Ensures all internal links are correct
 * <p>
 * @author peter
 */
public class LinkFixer
        implements UnaryOperator<Page>
{

    @Override
    public Page apply( Page page )
    {
        if( page != null ) {
            page.setContent( page.lines().
                    map( l -> l.
                            // Remove MediaWiki view prefix
                            replaceAll( "href=\"/index.php/", "href=\"/" ).
                            // Remove nofollow tag
                            replaceAll( " rel=\"nofollow\"", "" ).
                            // Remove unavailable page links leaving the text present
                            replaceAll( "<a href=\"/index.php?.+?>(.+?)</a>", "$1" )
                    ).
                    collect( Collectors.toList() )
            );
        }
        return page;
    }

}
