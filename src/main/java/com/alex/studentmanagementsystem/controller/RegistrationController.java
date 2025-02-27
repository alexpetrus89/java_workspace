package com.alex.studentmanagementsystem.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alex.studentmanagementsystem.repository.UserRepository;
import com.alex.studentmanagementsystem.utils.Builder;
import com.alex.studentmanagementsystem.utils.RegistrationForm;
import com.alex.studentmanagementsystem.utils.Role;





@Controller
@RequestMapping("/register")
public class RegistrationController {

    // instance variables
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /** Autowired - dependency injection - constructor */
    public RegistrationController(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
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
    public String processRegistration(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        @RequestParam("fullname") String fullname,
        @RequestParam("street") String street,
        @RequestParam("city") String city,
        @RequestParam("state") String state,
        @RequestParam("zip") String zip,
        @RequestParam("phone") String phone,
        @RequestParam("role") Role role
    ) {
        // create a new form builder
        Builder form = new Builder();
        // set the values
        form.withUsername(username);
        form.withPassword(password);
        form.withFullname(fullname.toLowerCase());
        form.withStreet(street);
        form.withCity(city);
        form.withState(state);
        form.withZip(zip);
        form.withPhone(phone);
        form.withRole(role);
        // create the form
        RegistrationForm registrationForm = new RegistrationForm(form);
        userRepository.saveAndFlush(registrationForm.toUser(passwordEncoder));
        return "redirect:/login";
    }

}
