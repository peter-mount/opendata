/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.trainwatch.web.servlet;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

/**
 *
 * @author peter
 */
public interface User
        extends Principal
{

    static final String KEY = "user";

    Set<String> getRoles();

    boolean isUserInRole( String role );

    boolean isEnabled();

    boolean isLocked();

    String getEmail();

    default boolean isEmailPresent()
    {
        String email = getEmail();
        return email != null && !email.isEmpty();
    }

    String getHomepage();

    default boolean isHomepagePresent()
    {
        String home = getHomepage();
        return home != null && !home.isEmpty();
    }

    String getFirstName();

    String getLastName();

    String getFullName();

    Collection<String> getSocialNetworks();

    <T> T getSocialNetwork( String network );
}
