package com.alex.universitymanagementsystem.component;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UmsCustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // constant
    public static final String LOGIN = "/login";
    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {

        // ===== Fallback =====
        if (authentication == null) {
            response.sendRedirect(LOGIN);
            return;
        }

        String role = null;

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            role = (String) oauthToken
                .getPrincipal()
                .getAttributes()
                .get("role");
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            role = authentication
                .getAuthorities()
                .stream()
                .filter(authority -> authority.getAuthority().startsWith("ROLE_"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found"))
                .getAuthority();
        }

        // ===== Fallback =====
        if (role == null) {
            response.sendRedirect(LOGIN);
            return;
        }

        String redirectUrl = switch (role) {
            case "ROLE_STUDENT" -> "/user_student/student-home";
            case "ROLE_PROFESSOR" -> "/user_professor/professor-home";
            case "ROLE_ADMIN" -> "/user_admin/admin-home";
            default -> LOGIN;
        };

        response.sendRedirect(redirectUrl);
    }

}
