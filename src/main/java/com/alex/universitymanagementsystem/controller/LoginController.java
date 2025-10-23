package com.alex.universitymanagementsystem.controller;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alex.universitymanagementsystem.service.RedirectLoginService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private final RedirectLoginService redirectLoginService;

    public LoginController(RedirectLoginService redirectLoginService) {
        this.redirectLoginService = redirectLoginService;
    }

    @GetMapping("/login")
    public String loginPage(
        @RequestParam(required = false) String error,
        @RequestParam(required = false) String logout,
        Model model
    ) {

        if (error != null)
            model.addAttribute("errorMessage", "Invalid username or password.");

        if (logout != null)
            model.addAttribute("logoutMessage", "You have been logged out.");

        return "login";
    }


    @GetMapping("/profile")
    public void redirectProfile(HttpServletResponse response) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Case: user not logged or anonymous
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            response.sendRedirect("/login");
            return;
        }

        // Case: user authenticated → get first role and redirect
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        redirectLoginService.redirectBasedOnRole(role, response);
    }


}

