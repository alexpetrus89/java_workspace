package com.alex.universitymanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.DegreeCourseMapper;
import com.alex.universitymanagementsystem.service.impl.DegreeCourseServiceImpl;
import com.alex.universitymanagementsystem.service.impl.UserServiceImpl;
import com.alex.universitymanagementsystem.utils.Builder;
import com.alex.universitymanagementsystem.utils.CreateView;
import com.alex.universitymanagementsystem.utils.RegistrationForm;

import jakarta.transaction.Transactional;

@Controller
@RequestMapping(path = "api/v1/user")
public class UserController {

    // constants
    private static final String ERROR = "error";
    private static final String ALREADY_EXISTS_PATH = "exception/object-already-exists";

    // instance variables
    private final UserServiceImpl userServiceImpl;
    private final DegreeCourseServiceImpl degreeCourseServiceImpl;

    /** Autowired - dependency injection - constructor */
    public UserController(
        UserServiceImpl userServiceImpl,
        DegreeCourseServiceImpl degreeCourseServiceImpl
    ) {
        this.userServiceImpl = userServiceImpl;
        this.degreeCourseServiceImpl = degreeCourseServiceImpl;
    }

    /**
     * Retrieves all users
     * @return ModelAndView
     */
    @GetMapping("/view")
    public ModelAndView getAllUsers() {

        return new CreateView(
            "users",
            userServiceImpl.getUsers(),
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
    public String createUser(@ModelAttribute Builder formBuilder) {
        try {
            return userServiceImpl.updateUser(new RegistrationForm(formBuilder));
        } catch (RuntimeException e) {
        // gestisci l'eccezione e restituisci un messaggio di errore significativo all'utente
            return "redirect:/error";
        }
    }

    @PutMapping("/update")
    public String updateUser(RegistrationForm form) {
        try {
            return userServiceImpl.updateUser(form);
        } catch (RuntimeException e) {
        // gestisci l'eccezione e restituisci un messaggio di errore significativo all'utente
            return "redirect:/error";
        }
    }

    @PostMapping("/create/student")
    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public ModelAndView createNewUserWithRoleStudent(@ModelAttribute Student student, @RequestParam String name) {

        // set the degree course
        student.setDegreeCourse(DegreeCourseMapper.mapToDegreeCourse(degreeCourseServiceImpl.getDegreeCourseByName(name)));

        try{

            return new CreateView(
                student,
                "role/student-result"
            ).getModelAndView();

        } catch (RuntimeException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                ALREADY_EXISTS_PATH
            ).getModelAndView();
        }
    }

}
