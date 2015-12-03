/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.cms.core.mediawiki;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import uk.trainwatch.cms.core.Page;

/**
 * Strip out any html comments
 * <p>
 * @author peter
 */
public class CommentStripper
        implements UnaryOperator<Page>
{

    /**
     * Pattern for a comment on a single line
     */
    private static final Pattern COMMENT_WHOLE = Pattern.compile( "(<!--.+?-->)" );
    /**
     * Pattern for the start of a multi line comment
     */
    private static final Pattern COMMENT_START = Pattern.compile( "(<!--.*?)$" );
    /**
     * Pattern for the end of a multi line comment
     */
    private static final Pattern COMMENT_END = Pattern.compile( "^(.*?-->)" );

    private final ThreadLocal<Boolean> remove = new ThreadLocal<>();

    @Override
    public Page apply( final Page page )
    {
        if( page != null ) {
            remove.set( false );

            // Note the order is specific here
            page.setContent(
                    page.getContent()
                    .stream()
                    .map( this::stripEndBlock )
                    .map( this::stripStartBlock )
                    .map( this::stripComment )
                    .filter( l -> l != null && !remove.get() )
                    .collect( Collectors.toCollection( ArrayList::new ) )
            );
        }
        return page;
    }

    /**
     * Strip Pattern P1 - a complete comment in a line
     * <p>
     * @param s
     *          <p>
     * @return
     */
    private String stripComment( String s )
    {
        return strip( s, COMMENT_WHOLE, e -> {
        } );
    }

    private String stripStartBlock( String s )
    {
        return strip( s, COMMENT_START, e -> {
            remove.set( true );
        } );
    }

    private String stripEndBlock( String s )
    {
        return strip( s, COMMENT_END, e -> {
            remove.set( false );
        } );
    }

    @SuppressWarnings("UseOfObsoleteCollectionType")
    private String strip( String s, Pattern p, Consumer<String> action )
    {
        if( s == null ) {
            return null;
        }

        Matcher m = p.matcher( s );
        if( !m.find() ) {
            return s;
        }

        action.accept( s );

        StringBuffer b = new StringBuffer();
        do {
            m.appendReplacement( b, "" );
        }
        while( m.find() );
        m.appendTail( b );
        return b.toString();
    }
}
