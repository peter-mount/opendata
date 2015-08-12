/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.mediawiki;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;

/**
 * Extracts the page content
 * <p>
 * @author peter
 */
public class ContentExtractor
        implements UnaryOperator<Page>
{

    @Override
    public Page apply( Page page )
    {
        if( page != null ) {
            AtomicBoolean extract = new AtomicBoolean( true );

            page.getContent().removeIf( l -> {
                if( l.contains( "<!-- bodycontent -->" ) ) {
                    return extract.getAndSet( false );
                }

                if( l.contains( "<!-- /bodycontent -->" ) ) {
                    extract.set( true );
                }
                return extract.get();
            } );
        }
        return page;
    }

}
