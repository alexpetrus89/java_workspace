package com.alex.universitymanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alex.universitymanagementsystem.service.PasswordResetTokenService;

@Controller
public class PasswordResetController {

    // constants
    private static final String ERROR = "error";
    private static final String RESET_PASSWORD = "reset-password";

    // instance variables
    private final PasswordResetTokenService resetService;

    // constructor
    public PasswordResetController(PasswordResetTokenService resetService) {
        this.resetService = resetService;
    }



    /**
     * Visualizza il modulo per il recupero della password
     * @return
     */
    @GetMapping("/forgot-password")
    public String forgotPasswordForm() { return "forgot-password"; }


    /**
     * Invia una richiesta di reset della password
     * @param email
     * @param model
     * @return
     */
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        try {
            resetService.sendPasswordResetLink(email);
            model.addAttribute("message", "Password reset link sent to your email.");
        } catch (Exception _) {
            model.addAttribute(ERROR, "Email not found.");
        }
        return "forgot-password";
    }


    /**
     * Visualizza il modulo per resettare la password
     * @param token
     * @param model
     * @return
     */
    @GetMapping("/reset-password")
    public String resetPasswordForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return RESET_PASSWORD; // pagina con campi password/confirm
    }


    /**
     * Processa la richiesta di reset della password
     * @param token
     * @param password
     * @param confirm
     * @param model
     * @return
     */
    @PostMapping("/reset-password")
    public String processReset(
        @RequestParam String token,
        @RequestParam String password,
        @RequestParam String confirm,
        Model model
    ) {
        if (!password.equals(confirm)) {
            model.addAttribute(ERROR, "Passwords do not match");
            return RESET_PASSWORD;
        }

        try {
            resetService.resetPassword(token, password);
            return "redirect:/login?resetSuccess";
        } catch (Exception e) {
            model.addAttribute(ERROR, e.getMessage());
            return RESET_PASSWORD;
        }
    }



}

