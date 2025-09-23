package com.alex.universitymanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.enum_type.RoleType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;





@Controller
@RequestMapping(path = "/registration")
public class RegistrationController {

    // GET request
    /**
     * @return String
     */
    @GetMapping
    public ModelAndView registration() {
        return new ModelAndView("registration", "form", new RegistrationForm());
    }


    // POST request
    /**
     * Process registration
     * @param form - RegistrationForm
     * @param request - HttpServletRequest
     * @param sessionStatus - SessionStatus
     * @return String - redirect
     */
    @PostMapping
    public String processRegistration (
        @Valid @ModelAttribute RegistrationForm form,
        HttpServletRequest request,
        SessionStatus sessionStatus
    ) {

        // memorize form object into session
        request.getSession().setAttribute("form", form);

        return switch (form.getRole()) {
            // Reindirizza l'utente al metodo createNewStudent
            case RoleType.STUDENT -> "redirect:user_student/student/create/select-degree-course";
            // Reindirizza l'utente professor al login
            case RoleType.PROFESSOR -> "forward:api/v1/user/create-professor";
            // Reindirizza l'utente admin al login
            case RoleType.ADMIN -> "forward:api/v1/user/create-admin";
            // Reindirizza all'utente alla pagina di login
            default -> "redirect:/login";
        };
    }

}
