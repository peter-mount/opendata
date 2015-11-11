/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.auth.twitter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;
import uk.trainwatch.web.servlet.User;
import uk.trainwatch.web.auth.UserFactory;
import uk.trainwatch.web.servlet.ApplicationRequest;
import uk.trainwatch.web.servlet.SecureServlet;

/**
 *
 * @author peter
 */
@WebServlet(name = "TwitterCallbackServlet", urlPatterns = "/login/twitterCallback")
public class TwitterCallbackServlet
        extends SecureServlet
{

    @Inject
    private UserFactory userFactory;

    @Override
    protected void doSecureGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        HttpSession session = request.getRequest().getSession();

        Twitter twitter = (Twitter) session.getAttribute( "twitter" );
        RequestToken requestToken = (RequestToken) session.getAttribute( "requestToken" );
        String verifier = request.getParam().get( "oauth_verifier" );

        try {
            if( twitter == null || requestToken == null || verifier == null ) {
                request.redirect( "/", true );
            }
            else {
                twitter.getOAuthAccessToken( requestToken, verifier );

                User user = null;
                if( request.isAuthenticated() ) {
                    // Link to existing user
                    user = userFactory.linkTwitter( request.getUser(), twitter );
                }
                else {
                    // Login/Register new user
                    user = userFactory.loginOrRegisterTwitter( twitter );
                }

                if( user != null ) {
                    session.setAttribute( User.KEY, user );
                }

                request.redirect( "/home", true );
            }
        }
        catch( SQLException |
               TwitterException ex ) {
            throw new ServletException( ex );
        }
        finally {
            session.removeAttribute( "twitter" );
            session.removeAttribute( "requestToken" );
        }
    }

}
