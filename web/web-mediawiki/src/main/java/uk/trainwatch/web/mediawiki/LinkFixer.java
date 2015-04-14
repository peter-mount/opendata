/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.mediawiki;

import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

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
        page.setContent( page.lines().
                map( l -> l.replaceAll( "href=\"/index.php/", "href=\"/" ).
                        replaceAll( " rel=\"nofollow\"", "" ) ).
                collect( Collectors.toList() )
        );

        return page;
    }

}
