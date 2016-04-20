/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.auth.twitter;

import java.io.IOException;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;
import uk.trainwatch.web.servlet.ApplicationRequest;
import uk.trainwatch.web.servlet.SecureServlet;

/**
 * Log into twitter
 * <p>
 * @author peter
 */
@WebServlet(name = "TwitterLoginServlet", urlPatterns = "/login/twitter")
public class TwitterLoginServlet
        extends SecureServlet
{

    @Inject
    private TwitterManager twitterManager;

    @Override
    protected void doSecureGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
//        try {
//            HttpSession session = request.getRequest().getSession( true );
//
//            Twitter twitter = twitterManager.newTwitter( "trainwatch" );
//            session.setAttribute( "twitter", twitter );
//
//            RequestToken requestToken = twitter.getOAuthRequestToken( "https://" + request.getServerName() + "/login/twitterCallback" );
//            session.setAttribute( "requestToken", requestToken );
//            request.getResponse().sendRedirect( requestToken.getAuthenticationURL() );
//        }
//        catch( TwitterException ex ) {
//            throw new ServletException( ex );
//        }
    }

}
