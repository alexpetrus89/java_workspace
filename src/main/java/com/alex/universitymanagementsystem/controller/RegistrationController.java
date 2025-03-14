package com.alex.universitymanagementsystem.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.alex.universitymanagementsystem.dto.UserDto;
import com.alex.universitymanagementsystem.service.impl.UserFactoryService;
import com.alex.universitymanagementsystem.utils.Builder;
import com.alex.universitymanagementsystem.utils.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;







@Controller
@RequestMapping("/register")
@SessionAttributes("user")
public class RegistrationController {

    // instance variables
    private final UserFactoryService userFactoryService;
    private final PasswordEncoder passwordEncoder;

    /** Autowired - dependency injection - constructor */
    public RegistrationController(
        UserFactoryService userFactoryService,
        PasswordEncoder passwordEncoder
    ) {
        this.userFactoryService = userFactoryService;
        this.passwordEncoder = passwordEncoder;
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
        @RequestParam("role") Role role
    ) throws JsonProcessingException {
        // create a new form builder
        Builder form = new Builder();
        // set the values
        form.withUsername(username);
        form.withPassword(password);
        form.withFullname(fullname.toLowerCase());
        form.withDob(dob);
        form.withStreet(street);
        form.withCity(city);
        form.withState(state);
        form.withZip(zip);
        form.withPhone(phone);
        form.withRole(role);

        // create and save user
        UserDto userDto = new UserDto(userFactoryService.createUser(form, passwordEncoder));

        // get the session
        HttpSession session = request.getSession();
        session.setAttribute("user", userDto.getUser());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String userDtoJson = mapper.writeValueAsString(userDto);
        String encodedUserDto = URLEncoder.encode(userDtoJson, StandardCharsets.UTF_8);

        return switch (role) {
            // Reindirizza l'utente al metodo createNewStudent
            case Role.STUDENT -> "redirect:role/create-student-from-user?userDto=" + encodedUserDto;
            // Reindirizza l'utente al metodo createProfessor (non mostrato nel codice)
            case Role.PROFESSOR -> "redirect:role/create-professor-from-user?userDto=" + userDto.getUser();
            // Reindirizza l'utente admin al login
            case Role.ADMIN -> "redirect:/login";
            // Reindirizza all'utente alla pagina di login
            default -> "redirect:/login";
        };
    }

}
