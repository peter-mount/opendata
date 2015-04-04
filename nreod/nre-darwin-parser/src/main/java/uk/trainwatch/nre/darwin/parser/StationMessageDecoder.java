/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.nre.darwin.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import uk.trainwatch.nre.darwin.model.ppt.stationmessages.A;
import uk.trainwatch.nre.darwin.model.ppt.stationmessages.P;

/**
 * Decodes a station message (OW) into common formats
 * <p>
 * @author peter
 */
public enum StationMessageDecoder
{

    /**
     * Simply decodes a message into plain text
     */
    TEXT
            {

                @Override
                public String decode( Object o )
                {
                    List<String> l = new ArrayList<>();
                    new StationMessageVisitor()
                    {

                        @Override
                        public void visit( String s )
                        {
                            l.add( s );
                        }
                    }.visit( o );
                    return l.stream().collect( Collectors.joining( " " ) );
                }

            },
    /**
     * Decodes a message into HTML
     */
    HTML
            {

                @Override
                public String decode( Object o )
                {
                    List<String> l = new ArrayList<>();
                    new StationMessageVisitor()
                    {

                        @Override
                        public void visit( String s )
                        {
                            l.add( s );
                        }

                        @Override
                        public void visit( A a )
                        {
                            visit( "<a href=\"" + a.getHref() + "\" target=\"_blank\">" + a.getValue() + "</a>" );
                        }

                        @Override
                        public void visit( P p )
                        {
                            visit( "<p>" );
                            visit( p.getContent() );
                            visit( "</p>" );
                        }

                    }.visit( o );
                    return l.stream().collect( Collectors.joining( " " ) );
                }

            };

    public abstract String decode( Object o );

}
