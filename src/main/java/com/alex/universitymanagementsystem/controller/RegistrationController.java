package com.alex.universitymanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.dto.Builder;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.enum_type.RoleType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;





@Controller
@RequestMapping(path = "/register")
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
     * @param request - HttpServletRequest
     * @param sessionStatus - SessionStatus
     * @param bindingResult - BindingResult
     * @param form - RegistrationForm
     * @return String - redirect
     */
    @PostMapping
    public String processRegistration (
        @Valid @ModelAttribute("form") RegistrationForm form,
        HttpServletRequest request,
        SessionStatus sessionStatus
    ) {

        // create a new form builder
        Builder builder = new Builder();
        // set the values
        builder.withUsername(form.getUsername());
        builder.withPassword(form.getPassword());
        builder.withFirstName(form.getFirstName().toLowerCase());
        builder.withLastName(form.getLastName().toLowerCase());
        builder.withDob(form.getDob());
        builder.withFiscalCode(form.getFiscalCode());
        builder.withStreet(form.getStreet());
        builder.withCity(form.getCity());
        builder.withState(form.getState());
        builder.withZip(form.getZip());
        builder.withPhone(form.getPhone());
        builder.withRole(form.getRole());

        // memorizza l'oggetto builder nella sessione
        request.getSession().setAttribute("builder", builder);

        return switch (form.getRole()) {
            // Reindirizza l'utente al metodo createNewStudent
            case RoleType.STUDENT -> "redirect:user_student/create/create-student-from-user";
            // Reindirizza l'utente professor al login
            case RoleType.PROFESSOR -> "forward:api/v1/user/create-professor";
            // Reindirizza l'utente admin al login
            case RoleType.ADMIN -> "forward:api/v1/user/create-admin";
            // Reindirizza all'utente alla pagina di login
            default -> "redirect:/login";
        };
    }

}
