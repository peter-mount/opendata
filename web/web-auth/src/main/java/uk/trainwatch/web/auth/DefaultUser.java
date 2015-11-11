/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.auth;

import uk.trainwatch.web.servlet.User;
import java.io.Serializable;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import uk.trainwatch.util.sql.SQL;

/**
 *
 * @author peter
 */
public class DefaultUser
        implements User,
                   Principal,
                   Serializable
{

    private static final long serialVersionUID = 1L;
    private final int userid;
    private final String name;
    private final boolean enabled;
    private final boolean locked;
    private final String email;
    private final String homepage;
    private final String firstName;
    private final String lastName;
    private final String fullName;
    private final Set<String> roles;
    private final String pass;
    private final Map<String, Object> socialNetworks;

    public DefaultUser( ResultSet rs, Connection con )
            throws SQLException
    {
        userid = rs.getInt( "id" );
        name = rs.getString( "username" );
        pass = rs.getString( "pass" );
        enabled = rs.getBoolean( "enabled" );
        locked = rs.getBoolean( "locked" );
        email = rs.getString( "email" );
        homepage = rs.getString( "homepage" );
        firstName = rs.getString( "first" );
        lastName = rs.getString( "last" );
        fullName = rs.getString( "name" );

        try( PreparedStatement ps = SQL.prepare( con, "SELECT name FROM roles r INNER JOIN user_roles u ON r.id=u.roleid WHERE u.userid=?", userid ) ) {
            roles = SQL.stream( ps, SQL.STRING_LOOKUP ).collect( Collectors.toSet() );
        }

        try( PreparedStatement ps = SQL.prepare( con, "SELECT netId,data FROM user_social WHERE userid=?", userid ) ) {
            socialNetworks = SQL.stream( ps, rs2 -> new Object()
            {
                SocialNetwork net = SocialNetwork.getSocialNetwork( rs2.getInt( 1 ) );
                Object obj = SQL.getSerializable( rs2, 2 );
            } )
                    .collect( Collectors.toMap( o -> o.net.getLabel(), o -> o.obj ) );
        }
    }

    String getPass()
    {
        return pass;
    }

    @Override
    public Set<String> getRoles()
    {
        return roles;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean isUserInRole( String role )
    {
        return roles.contains( role );
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    @Override
    public boolean isLocked()
    {
        return locked;
    }

    @Override
    public String getEmail()
    {
        return email;
    }

    @Override
    public String getHomepage()
    {
        return homepage;
    }

    @Override
    public String getFirstName()
    {
        return firstName;
    }

    @Override
    public String getLastName()
    {
        return lastName;
    }

    @Override
    public String getFullName()
    {
        return fullName;
    }

    @Override
    public Collection<String> getSocialNetworks()
    {
        return new ArrayList<>( socialNetworks.keySet() );
    }

    @Override
    public <T> T getSocialNetwork( String network )
    {
        return (T) socialNetworks.get( network );
    }

}
