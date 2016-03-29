/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.auth;

import uk.trainwatch.web.servlet.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import uk.trainwatch.util.config.Database;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
@ApplicationScoped
public class UserFactory
{

    @Inject
    private UserCache userCache;

    @Database("auth")
    @Inject
    private DataSource dataSource;

    public User getFreshUser( String name )
    {
        try {
            return userCache.getFreshUser( name );
        }
        catch( SQLException |
               IOException |
               ClassNotFoundException ex ) {
            Logger.getLogger( UserFactory.class.getName() ).log( Level.SEVERE, null, ex );
            return null;
        }
    }

    public User getUser( String name )
    {
        try {
            return userCache.getUser( name );
        }
        catch( SQLException ex ) {
            Logger.getLogger( UserFactory.class.getName() ).log( Level.SEVERE, null, ex );
            return null;
        }
    }

    public User login( String name, String password )
    {
        try {
            return userCache.getUser( name, password );
        }
        catch( SQLException |
               IOException |
               ClassNotFoundException ex ) {
            Logger.getLogger( UserFactory.class.getName() ).log( Level.SEVERE, null, ex );
            return null;
        }
    }

    public void updateUser( User user )
            throws SQLException
    {
        userCache.updateUser( user.getName(), user );
    }

    public void changePassword( String name, String existPass, String newPass )
            throws SQLException
    {
        userCache.changePassword( name, existPass, newPass );
    }

    public User linkTwitter( User user, Twitter twitter )
            throws SQLException,
                   TwitterException
    {
        try( Connection con = dataSource.getConnection() ) {
            String name = user.getName();

            try( PreparedStatement ps = SQL.prepare( con, "SELECT linkTwitterUser(?,?,?,?)", name, twitter.getId(), twitter.getScreenName() ) ) {
                SQL.setBytes( ps, 4, twitter );
                userCache.removeUser( name );
                return SQL.stream( ps, SQL.STRING_LOOKUP )
                        .map( this::getFreshUser )
                        .findFirst()
                        .orElse( null );
            }
        }
    }

    public User loginOrRegisterTwitter( Twitter twitter )
            throws SQLException,
                   TwitterException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con, "SELECT createTwitterUser(?,?,?)", twitter.getId(), twitter.getScreenName() ) ) {
                SQL.setBytes( ps, 3, twitter );
                return SQL.stream( ps, SQL.STRING_LOOKUP )
                        .map( this::getFreshUser )
                        .findFirst()
                        .orElse( null );
            }
        }
    }
}
