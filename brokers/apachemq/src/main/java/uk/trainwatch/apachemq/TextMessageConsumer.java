/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.apachemq;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * This consumer can be used to filter out all but JMS {@link TextMessage}'s from a topic and pass just the text content
 * on to another consumer.
 * <p>
 * @author Peter T Mount
 */
public class TextMessageConsumer
        implements Consumer<Message>
{

    private final Consumer<String> consumer;

    public TextMessageConsumer( final Consumer<String> consumer )
    {
        this.consumer = Objects.requireNonNull( consumer );
    }

    @Override
    public void accept( Message m )
    {
        if( m instanceof TextMessage )
        {
            TextMessage t = (TextMessage) m;
            try
            {
                consumer.accept( t.getText() );
            }
            catch( JMSException ex )
            {
                Logger.getLogger( TextMessageConsumer.class.getName() ).
                        log( Level.SEVERE, null, ex );
            }
        }
    }

    /**
     *
     * @param after
     * <p>
     * @return
     */
    public static Consumer<Message> transform( Consumer<String> after )
    {
        Objects.requireNonNull( after );
        return new TextMessageConsumer( after );
    }
}
