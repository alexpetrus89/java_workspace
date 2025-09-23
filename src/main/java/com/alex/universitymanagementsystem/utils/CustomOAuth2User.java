package com.alex.universitymanagementsystem.utils;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuth2User implements OAuth2User {

    // instance variables
    private final UserDetails userDetails; // utente reale dal DB
    private final OAuth2User oauth2User; // dati del provider

    // constructor
    public CustomOAuth2User(UserDetails userDetails, OAuth2User oauth2User) {
        this.userDetails = userDetails;
        this.oauth2User = oauth2User;
    }

    // getters
    public UserDetails getUserDetails() {
        return userDetails;
    }

    public String getRole() {
        return userDetails.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .filter(r -> r.startsWith("ROLE_"))
            .findFirst()
            .orElse("ROLE_GUEST");
    }


    // --- OAuth2User delegate ---
    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public String getName() {
        return userDetails.getUsername(); // email reale dal DB
    }


}

