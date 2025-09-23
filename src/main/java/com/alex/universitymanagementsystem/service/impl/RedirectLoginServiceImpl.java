package com.alex.universitymanagementsystem.service.impl;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.service.RedirectLoginService;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class RedirectLoginServiceImpl implements RedirectLoginService {

    // constant
    public static final String LOGIN = "/login";


    /**
     * Redirects the user to the appropriate home page based on their role.
     * @param role the role of the user
     *              (e.g., "ROLE_STUDENT", "ROLE_PROFESSOR", "ROLE_ADMIN")
     * @param response the HttpServletResponse object
     * @throws java.io.IOException if an input or output exception occurs
     */
    @Override
    public void redirectBasedOnRole(String role, HttpServletResponse response) throws IOException {
        // Implementazione del metodo di reindirizzamento basato sul ruolo
        String redirectUrl = switch (role) {
            case "ROLE_STUDENT" -> "/user_student/student-home";
            case "ROLE_PROFESSOR" -> "/user_professor/professor-home";
            case "ROLE_ADMIN" -> "/user_admin/admin-home";
            default -> LOGIN;
        };

        response.sendRedirect(redirectUrl);
    }


}
