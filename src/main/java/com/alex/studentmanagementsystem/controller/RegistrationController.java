package com.alex.studentmanagementsystem.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alex.studentmanagementsystem.repository.UserRepository;
import com.alex.studentmanagementsystem.utility.Builder;
import com.alex.studentmanagementsystem.utility.RegistrationForm;





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
     * processRegistration
     * @param RegistrationForm
     * @return String
     * @throws IllegalArgumentException
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
        @RequestParam("phone") String phone
    ) {
        // create a new form builder
        Builder form = new Builder();
        // set the values
        form.withUsername(username);
        form.withPassword(password);
        form.withFullname(fullname);
        form.withStreet(street);
        form.withCity(city);
        form.withState(state);
        form.withZip(zip);
        form.withPhone(phone);
        // create the form
        RegistrationForm registrationForm = new RegistrationForm(form);
        userRepository.save(registrationForm.toUser(passwordEncoder));
        return "redirect:/login";
    }

}
