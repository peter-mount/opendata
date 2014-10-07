/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.util;

import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 *
 * @author Peter T Mount
 */
public class JMS
{

    private static final Logger LOG = Logger.getLogger( JMS.class.getName() );
    /**
     * Function that converts a {@link Message} to {@link TextMessage} or null if it is not a TextMessage.
     */
    public static final Function<Message, TextMessage> toTextMessage = m -> m instanceof TextMessage ? (TextMessage) m : null;

    /**
     * Function to return the text of a {@link TextMessage} or null if the message is null.
     */
    public static final Function<TextMessage, String> getText = m ->
    {
        try
        {
            return m == null ? null : m.getText();
        }
        catch( JMSException ex )
        {
            LOG.log( Level.SEVERE, "Failed to getText()", ex );
            return null;
        }
    };

    /**
     * Composed function that returns the text from a {@link Message} or null if the message is not actually an
     * {@link TextMessage} instance.
     */
    public static final Function<Message, String> toText = toTextMessage.andThen( getText );
}
