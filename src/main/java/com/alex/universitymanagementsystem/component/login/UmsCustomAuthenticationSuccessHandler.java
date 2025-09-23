package com.alex.universitymanagementsystem.component.login;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.service.RedirectLoginService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UmsCustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // instance variable
    private final RedirectLoginService redirectLoginService;

    // constructor
    public UmsCustomAuthenticationSuccessHandler(RedirectLoginService redirectLoginService) {
        this.redirectLoginService = redirectLoginService;
    }

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {

        String role = authentication
            .getAuthorities()
            .stream()
            .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Role not found"))
            .getAuthority();

        redirectLoginService.redirectBasedOnRole(role, response);
    }

}
