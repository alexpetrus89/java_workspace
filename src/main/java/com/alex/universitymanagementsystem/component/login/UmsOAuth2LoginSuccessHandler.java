package com.alex.universitymanagementsystem.component.login;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.service.RedirectLoginService;
import com.alex.universitymanagementsystem.utils.PrincipalExtractor;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class UmsOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RedirectLoginService redirectLoginService;
    private final List<PrincipalExtractor> principalExtractors;

    public UmsOAuth2LoginSuccessHandler(
        RedirectLoginService redirectLoginService,
        List<PrincipalExtractor> principalExtractors
    ) {
        this.redirectLoginService = redirectLoginService;
        this.principalExtractors = principalExtractors;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        Object principal = authentication.getPrincipal();

        // Trova l’extractor giusto
        PrincipalExtractor principalExtractor = principalExtractors
            .stream()
            .filter(e -> e.supports(principal))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException(
                "Unsupported principal type: " + principal.getClass()));

        UserDetails userDetails = principalExtractor.extractUserDetails(principal);
        Collection<? extends GrantedAuthority> authorities = principalExtractor.extractAuthorities(principal);

        // Ricrea il token con l’utente DB
        UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Redirect in base al ruolo
        String role = authorities
            .stream()
            .map(GrantedAuthority::getAuthority)
            .filter(r -> r.startsWith("ROLE_"))
            .findFirst()
            .orElse("ROLE_GUEST");

        redirectLoginService.redirectBasedOnRole(role, response);
    }


}

