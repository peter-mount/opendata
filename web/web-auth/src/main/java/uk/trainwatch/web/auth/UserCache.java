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
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemove;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;
import uk.trainwatch.util.sql.Database;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
@ApplicationScoped
@CacheDefaults(cacheName = "UserCache")
class UserCache
{

    private static final String SELECT = "SELECT * FROM users WHERE username=?";
    private static final String ENCODE_PASSWORD = "encode(digest(?,'md5'),'hex')";

    @Database("auth")
    @Inject
    private DataSource dataSource;

    /**
     * Removes a user from the cache - not the database
     * <p>
     * @param name
     * @param realm
     *              <p>
     * @throws SQLException
     */
    @CacheRemove
    public void removeUser( @CacheKey String name )
    {
        // No nothing it's all in the annotations
    }

    @CacheResult(skipGet = true)
    public User getFreshUser( @CacheKey String name )
            throws SQLException,
                   IOException,
                   ClassNotFoundException
    {
        return getUser( name );
    }

    @CacheResult
    @SuppressWarnings("ThrowableInstanceNotThrown")
    public User getUser( @CacheKey String name )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con, SELECT, name ) ) {
                return SQL.stream( ps, rs -> new DefaultUser( rs, con ) )
                        .findAny()
                        .orElseThrow( () -> new SQLException( "No such user" ) );
            }
        }
    }

    @CacheResult(skipGet = true)
    @SuppressWarnings("ThrowableInstanceNotThrown")
    public User getUser( @CacheKey String name, String pass )
            throws SQLException,
                   IOException,
                   ClassNotFoundException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con, SELECT + " AND pass=" + ENCODE_PASSWORD, name, pass ) ) {
                return SQL.stream( ps, rs -> new DefaultUser( rs, con ) )
                        .findAny()
                        .orElseThrow( () -> new SQLException( "No such user" ) );
            }
        }
    }

    private static final String UPDATE = "UPDATE users SET email=?, homepage=?, first=?, last=?, name=? WHERE username=?";

    @CachePut
    public void updateUser( @CacheKey String name, @CacheValue User user )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con, UPDATE,
                                                     user.getEmail(),
                                                     user.getHomepage(),
                                                     user.getFirstName(),
                                                     user.getLastName(),
                                                     user.getFullName(),
                                                     name ) ) {
                ps.executeUpdate();
            }
        }
    }

    @CacheRemove
    public boolean changePassword( @CacheKey String name, String existPass, String newPass )
            throws SQLException
    {
        try( Connection con = dataSource.getConnection() ) {
            try( PreparedStatement ps = SQL.prepare( con,
                                                     "UPDATE users SET pass=" + ENCODE_PASSWORD + " WHERE name=? AND pass=" + ENCODE_PASSWORD,
                                                     newPass,
                                                     name,
                                                     existPass ) ) {
                return ps.executeUpdate() > 0;
            }
        }
    }

}
