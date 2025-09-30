package com.alex.universitymanagementsystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class AccessDeniedController {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AccessDeniedController.class);

    @Value("#{accessDeniedExceptionUri}")
    private String accessDeniedExceptionUri;


    @GetMapping("/access-denied")
    public ModelAndView handleAccessDeniedException() {
        logger.error("Access denied");
        String message = "Access Denied: You do not have permission to access this resource.";
        return new ModelAndView(accessDeniedExceptionUri, "message", message);
    }

}
