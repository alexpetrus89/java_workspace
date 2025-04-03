package com.alex.universitymanagementsystem.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import com.alex.universitymanagementsystem.enum_type.RoleType;
import com.alex.universitymanagementsystem.service.impl.RegistrationServiceImpl;
import com.alex.universitymanagementsystem.utils.Builder;

import jakarta.servlet.http.HttpServletRequest;







@Controller
@RequestMapping("/register")
public class RegistrationController {

    // instance variables
    private final RegistrationServiceImpl registrationServiceImpl;

    // constructor
    public RegistrationController(RegistrationServiceImpl registrationServiceImpl) {
        this.registrationServiceImpl = registrationServiceImpl;
    }

    // methods
    /** GET request */
    /**
     * @return String
     */
    @GetMapping
    public String registerForm() {
        return "registration";
    }

    /** POST request */
    /**
     * Process registration
     * @param username
     * @param password
     * @param fullname
     * @param dob
     * @param street
     * @param city
     * @param state
     * @param zip
     * @param phone
     * @param role
     * @return String - redirect
     * @throws IllegalArgumentException if the username is already taken
     */
    @PostMapping
    public String processRegistration (
        HttpServletRequest request,
        SessionStatus sessionStatus,
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        @RequestParam("fullname") String fullname,
        @RequestParam("dob") LocalDate dob,
        @RequestParam("street") String street,
        @RequestParam("city") String city,
        @RequestParam("state") String state,
        @RequestParam("zip") String zip,
        @RequestParam("phone") String phone,
        @RequestParam("role") RoleType role
    ) {
        // create a new form builder
        Builder builder = new Builder();
        // set the values
        builder.withUsername(username);
        builder.withPassword(password);
        builder.withFullname(fullname.toLowerCase());
        builder.withDob(dob);
        builder.withStreet(street);
        builder.withCity(city);
        builder.withState(state);
        builder.withZip(zip);
        builder.withPhone(phone);
        builder.withRole(role);

        // username already in use check
        if(registrationServiceImpl.isUsernameAlreadyTaken(username))
            return "redirect:/validation/registration/username-already-taken";

        // memorizza l'oggetto builder nella sessione
        request.getSession().setAttribute("builder", builder);

        return switch (role) {
            // Reindirizza l'utente al metodo createNewStudent
            case RoleType.STUDENT -> "redirect:user_student/create/create-student-from-user";
            // Reindirizza l'utente al metodo createProfessor (non mostrato nel codice)
            case RoleType.PROFESSOR -> "redirect:user_professor/create/create-professor-from-user";
            // Reindirizza l'utente admin al login
            case RoleType.ADMIN -> "forward:api/v1/user/create-admin";
            // Reindirizza all'utente alla pagina di login
            default -> "redirect:/login";
        };
    }

}
