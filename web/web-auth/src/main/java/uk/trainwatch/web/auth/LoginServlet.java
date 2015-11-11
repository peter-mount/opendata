/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.auth;

import uk.trainwatch.web.servlet.User;
import java.io.IOException;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import uk.trainwatch.web.servlet.ApplicationRequest;
import uk.trainwatch.web.servlet.SecureServlet;

/**
 * Login a user with a username/password.
 * <p>
 * @author peter
 */
@WebServlet(name = "LoginServlet", description = "Login with username/password", urlPatterns = "/login")
public class LoginServlet
        extends SecureServlet
{

    @Inject
    private UserFactory userFactory;

    @Override
    protected void doSecureGet( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        request.renderTile( request.isAuthenticated() ? "home.logged" : "home.login" );
    }

    @Override
    protected void doSecurePost( ApplicationRequest request )
            throws ServletException,
                   IOException
    {
        if( request.isAuthenticated() ) {
            request.renderTile( "home.logged" );
        }
        else {
            Map<String, String> params = request.getParam();
            String name = params.get( "username" );
            String pass = params.get( "password" );

            User user = userFactory.login( name, pass );
            if( user != null ) {
                request.getRequest().getSession().setAttribute( User.KEY, user );
                request.renderTile( "home.logged" );
            }
            else {
                Map<String, Object> req = request.getRequestScope();
                req.put( "error", "Failed to login" );
                request.renderTile( "home.login" );
            }
        }
    }

}
