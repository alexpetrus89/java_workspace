package com.alex.studentmanagementsystem.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alex.studentmanagementsystem.domain.User;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.repository.UserRepository;
import com.alex.studentmanagementsystem.utils.CreateView;
import com.alex.studentmanagementsystem.utils.RegistrationForm;

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
            new User(),
            "user/update/update"
        ).getModelAndView();
    }



    /**
     * Updates the current authenticated user's information and saves it to the repository.
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
            user.setStreet(form.getStreet());
            user.setCity(form.getCity());
            user.setState(form.getState());
            user.setZip(form.getZip());
            user.setPhoneNumber(form.getPhone());

            userRepository.save(user);
            return "redirect:/login";

        } catch (ObjectNotFoundException e) {
            return "redirect:/user/update";
        }
    }

}
