package com.alex.universitymanagementsystem.service;

import jakarta.servlet.http.HttpServletResponse;

public interface RedirectLoginService {

    /**
     * Redirects the user to the appropriate home page based on their role.
     * @param role the role of the user
     *              (e.g., "ROLE_STUDENT", "ROLE_PROFESSOR", "ROLE_ADMIN")
     * @param response the HttpServletResponse object
     * @throws java.io.IOException if an input or output exception occurs
     */
    void redirectBasedOnRole(String role, HttpServletResponse response) throws java.io.IOException;
}
