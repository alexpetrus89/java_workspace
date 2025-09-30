package com.alex.universitymanagementsystem.component;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UmsAccessDeniedHandler implements AccessDeniedHandler {

    // logger
    private static final Logger logger =
        LoggerFactory.getLogger(UmsAccessDeniedHandler.class);

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        // Log dell'evento
        logger.error("Access denied for user: {}, URL: {}, reason: {}", request.getUserPrincipal(), request.getRequestURI(), accessDeniedException.getMessage());

        // Reindirizza alla pagina 403 personalizzata
        response.sendRedirect(request.getContextPath() + "/access-denied");
    }
}

