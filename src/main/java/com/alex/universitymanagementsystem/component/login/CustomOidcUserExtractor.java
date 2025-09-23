package com.alex.universitymanagementsystem.component.login;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.utils.CustomOidcUser;
import com.alex.universitymanagementsystem.utils.PrincipalExtractor;

@Component
public class CustomOidcUserExtractor implements PrincipalExtractor {

    @Override
    public boolean supports(Object principal) {
        return principal instanceof CustomOidcUser;
    }

    @Override
    public UserDetails extractUserDetails(Object principal) {
        return ((CustomOidcUser) principal).getUserDetails();
    }

    @Override
    public Collection<? extends GrantedAuthority> extractAuthorities(Object principal) {
        return ((CustomOidcUser) principal).getAuthorities();
    }
}

