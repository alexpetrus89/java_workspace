package com.alex.universitymanagementsystem.utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;


public class CustomOidcUser implements OidcUser {

    // instance variables
    private final UserDetails userDetails;
    private final OidcUser oidcUser; // provider data

    // constructor
    public CustomOidcUser(UserDetails userDetails, OidcIdToken idToken, OidcUserInfo userInfo) {
        this.userDetails = userDetails;
        this.oidcUser = new DefaultOidcUser(userDetails.getAuthorities(), idToken, userInfo, "email");
    }

    // getters
    public UserDetails getUserDetails() {
        return userDetails;
    }

    public List<GrantedAuthority> getAuthoritiesList() {
        return List.copyOf(oidcUser.getAuthorities());
    }

    public String getRole() {
        return userDetails
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .filter(r -> r.startsWith("ROLE_"))
            .findFirst()
            .orElse("ROLE_GUEST");
    }


    // --- OidcUser delegate ---
    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oidcUser.getAuthorities();
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }


}

