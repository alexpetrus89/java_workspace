package com.alex.universitymanagementsystem.component.login;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.utils.CustomOAuth2User;
import com.alex.universitymanagementsystem.utils.PrincipalExtractor;

@Component
public class CustomOAuth2UserExtractor implements PrincipalExtractor {

    @Override
    public boolean supports(Object principal) {
        return principal instanceof CustomOAuth2User;
    }

    @Override
    public UserDetails extractUserDetails(Object principal) {
        return ((CustomOAuth2User) principal).getUserDetails();
    }

    @Override
    public Collection<? extends GrantedAuthority> extractAuthorities(Object principal) {
        return ((CustomOAuth2User) principal).getAuthorities();
    }
}

