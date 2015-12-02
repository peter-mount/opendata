/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.cms.core.mediawiki;

import java.util.List;
import java.util.function.UnaryOperator;
import uk.trainwatch.cms.core.Page;

/**
 * Ensures our page is defined as an article
 * <p>
 * @author peter
 */
public class ArticleFormat
        implements UnaryOperator<Page>
{

    @Override
    public Page apply( Page page )
    {
        if( page != null ) {
            List<String> list = page.getContent();
            list.add( 0, "<article id=\"er-article-body\">" );
            list.add( "</article>" );
        }
        return page;
    }

}
