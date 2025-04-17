package com.alex.universitymanagementsystem.component;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UmsCustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
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
            .orElseThrow(() -> new RuntimeException("Ruolo non trovato"))
            .getAuthority();

        String redirectUrl;
        switch (role) {
            case "ROLE_STUDENT" -> redirectUrl = "/user_student/student-home";
            case "ROLE_PROFESSOR" -> redirectUrl = "/user_professor/professor-home";
            case "ROLE_ADMIN" -> redirectUrl = "/user_admin/admin-home";
            default -> redirectUrl = "/login";
        }

        response.sendRedirect(redirectUrl);
    }

}
