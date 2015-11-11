/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author peter
 */
public enum SocialNetwork
{

    TWITTER( 1, "Twitter", "/login/twitter", "/images/twitter/sign-in-with-twitter-gray.png" ),

    FACEBOOK( 2, "Facebook", "/login/facebook", "" );

    private static final Map<Object, SocialNetwork> IDS = new ConcurrentHashMap<>();

    static {
        for( SocialNetwork sn: values() ) {
            IDS.put( sn.getId(), sn );
            IDS.put( sn.getLabel(), sn );
            IDS.put( sn.toString(), sn );
            IDS.put( sn.toString().toLowerCase(), sn );
        }
    }

    public static SocialNetwork getSocialNetwork( String n )
    {
        return IDS.get( n );
    }

    public static SocialNetwork getSocialNetwork( int id )
    {
        return IDS.get( id );
    }

    private final int id;
    private final String label;
    private final String login;
    private final String image;

    private SocialNetwork( int id, String label, String login, String image )
    {
        this.id = id;
        this.label = label;
        this.login = login;
        this.image = image;
    }

    public int getId()
    {
        return id;
    }

    public String getLabel()
    {
        return label;
    }

    public String getLogin()
    {
        return login;
    }

    public String getImage()
    {
        return image;
    }

}
