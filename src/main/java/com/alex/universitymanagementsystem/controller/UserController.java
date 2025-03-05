package com.alex.universitymanagementsystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.repository.UserRepository;
import com.alex.universitymanagementsystem.utils.Builder;
import com.alex.universitymanagementsystem.utils.CreateView;
import com.alex.universitymanagementsystem.utils.RegistrationForm;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping(path = "api/v1/user")
public class UserController {

    // instance variables
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /** Autowired - dependency injection - constructor */
    public UserController(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // methods
    /**
     * Retrieves all users
     * @return ModelAndView
     */
    @GetMapping("/view")
    public ModelAndView getAllUsers() {

        return new CreateView(
            "users",
            userRepository.findAll(),
            "user/read/user-list"
        ).getModelAndView();
    }


    /**
     * Updates the user
     * @return ModelAndView
     */
    @GetMapping("/update")
    public ModelAndView updateUserAndReturnView() {

        return new CreateView(
            new Builder(),
            "user/update/update"
        ).getModelAndView();
    }


    /**
     * Updates the user
     * @param formBuilder
     * @return String
     * @throws RuntimeException if the updated user details are invalid
     * @throws IllegalArgumentException if the updated user details are invalid
     * @throws ObjectNotFoundException if the authenticated user is not found
     * @throws UserNotFoundException if the user is not found
     * @throws NullPointerException if the updated user details are null
     * @throws UnsupportedOperationException if the updated user details are not unique
     */
    @PostMapping("/update/form")
    public String updateUserForm(@ModelAttribute Builder formBuilder) {
        try {
            return updateUser(new RegistrationForm(formBuilder));
        } catch (RuntimeException e) {
        // gestisci l'eccezione e restituisci un messaggio di errore significativo all'utente
            return "redirect:/error-page";
        }
    }


    /**
     * Updates the current authenticated user's information and saves it to the
     * repository.
     * This method is transactional and mapped to the HTTP PUT request for "/update".
     * @param form RegistrationForm containing updated user details.
     * @return String representing the redirect URL after the update process.
     * @throws ObjectNotFoundException if the authenticated user is not found.
	 * @throws UserNotFoundException if the user is not found.
     * @throws IllegalArgumentException if the updated user details are invalid.
	 */
	@Transactional
    @PutMapping("/update")
    public String updateUser(RegistrationForm form) throws ObjectNotFoundException
	{
        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = (User) userDetails;

        if(user == null)
            throw new ObjectNotFoundException("User not found", "user");

        try {
            user.setUsername(form.getUsername());
            user.setPassword(passwordEncoder.encode(form.getPassword()));
            user.setFullname(form.getFullname());
            user.setDob(form.getDob());
            user.setStreet(form.getStreet());
            user.setCity(form.getCity());
            user.setState(form.getState());
            user.setZip(form.getZip());
            user.setPhoneNumber(form.getPhone());
            user.setRole(form.getRole());

            userRepository.save(user);
            return "redirect:/login";

        } catch (ObjectNotFoundException e) {
            return "redirect:/user/update";
        }
    }

}
