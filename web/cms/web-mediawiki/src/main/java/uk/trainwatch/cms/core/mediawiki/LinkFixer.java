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
                            replaceAll( "href=\"/cms/index.php\\?title=", "href=\"/" ).
                            replaceAll( "href=\"/cms/index.php&amp;title=", "href=\"/" ).
                            replaceAll( "href=\"/cms/index.php/", "href=\"/" ).
                            // Remove nofollow tag
                            replaceAll( " rel=\"nofollow\"", "" ).
                            // Remove unavailable page links leaving just the text present
                            replaceAll( "<a href=\"/.+?&amp;.*?action=edit.*?>(.+?)</a>", "$1" ).
                            // Fix images links
                            replaceAll( "src=\"/cms/images", "src=\"/images" ).
                            // Strip image links
                            replaceAll( "<a href=\"/File:.+?>(.+?)</a>", "$1" ).
                            // Fix links that appear to use index.php
                            replaceAll( "<a href=\"/index.php[/\\?](.+?)\".*?>(.+?)</a>", "<a href=\"$1\">$2</a>" ).
                            replaceAll( "<a href=\"/cms/index.php[/\\?](.+?)\".*?>(.+?)</a>", "<a href=\"$1\">$2</a>" )
                    ).
                    collect( Collectors.toList() )
            );
        }
        return page;
    }

}
