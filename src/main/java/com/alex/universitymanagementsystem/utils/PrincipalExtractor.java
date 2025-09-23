package com.alex.universitymanagementsystem.utils;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public interface PrincipalExtractor {

    boolean supports(Object principal);

    UserDetails extractUserDetails(Object principal);

    Collection<? extends GrantedAuthority> extractAuthorities(Object principal);
}

