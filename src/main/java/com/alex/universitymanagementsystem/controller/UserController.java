package com.alex.universitymanagementsystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.DegreeCourseServiceImpl;
import com.alex.universitymanagementsystem.service.impl.ProfessorServiceImpl;
import com.alex.universitymanagementsystem.service.impl.StudentServiceImpl;
import com.alex.universitymanagementsystem.service.impl.UserServiceImpl;
import com.alex.universitymanagementsystem.utils.Builder;
import com.alex.universitymanagementsystem.utils.RegistrationForm;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    // constants
    private static final String BUILDER = "builder";
    private static final String TITLE = "title";
    private static final String ERROR = "Controller layer error";
    private static final String ERROR_PATH = "/exception/error";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String STACK_TRACE = "stackTrace";

    // instance variables
    private final UserServiceImpl userServiceImpl;
    private final StudentServiceImpl studentServiceImpl;
    private final ProfessorServiceImpl professorServiceImpl;
    private final DegreeCourseServiceImpl degreeCourseServiceImpl;
    private final PasswordEncoder passwordEncoder;

    /** Autowired - dependency injection - constructor */
    public UserController(
        UserServiceImpl userServiceImpl,
        StudentServiceImpl studentServiceImpl,
        DegreeCourseServiceImpl degreeCourseServiceImpl,
        PasswordEncoder passwordEncoder,
        ProfessorServiceImpl professorServiceImpl
    ) {
        this.userServiceImpl = userServiceImpl;
        this.studentServiceImpl = studentServiceImpl;
        this.degreeCourseServiceImpl = degreeCourseServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.professorServiceImpl = professorServiceImpl;
    }

    /**
     * Retrieves all users
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
    public ModelAndView getAllUsers() {
        List<User> users = userServiceImpl.getUsers();
        return new ModelAndView("user/read/user-list", "users", users);
    }


    /**
     * Updates the user
     * @return ModelAndView
     */
    @GetMapping(path = "/update")
    public ModelAndView updateUserAndReturnView() {
        return new ModelAndView("user/update/update", BUILDER, new Builder());
    }


    /**
     * Updates the user
     * @param request HTTP request
     * @return String
     * @throws RuntimeException if the updated user details are invalid
     * @throws IllegalArgumentException if the updated user details are invalid
     * @throws ObjectNotFoundException if the authenticated user is not found
     * @throws UserNotFoundException if the user is not found
     * @throws NullPointerException if the updated user details are null
     * @throws UnsupportedOperationException if the updated user details are not unique
     */
    @PostMapping(path = "/create-admin")
    public String createUser(HttpServletRequest request) {
        try {
            Builder builder = (Builder) request.getSession().getAttribute(BUILDER);
            return userServiceImpl.addNewUser(new RegistrationForm(builder).toUser(passwordEncoder));
        } catch (RuntimeException e) {
        // gestisci l'eccezione e restituisci un messaggio di errore significativo all'utente
            return "forward:" + ERROR_PATH;
        }
    }


    /**
     * Creates a new user with role student
     * @param request HTTP request
     * @param degreeCourse degree course name
     * @return ModelAndView
     * @throws ObjectAlreadyExistsException if a user with the given username already exists
     * @throws ObjectNotFoundException if the degree course does not exist
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws UnsupportedOperationException if any of the parameters is not unique
     * @throws NullPointerException if any of the parameters is null
     */
    @PostMapping(path = "/create-student")
    public ModelAndView createNewUserWithRoleStudent(HttpServletRequest request, @RequestParam String degreeCourse) {

        // recupera l'oggetto UserDto dalla sessione
        Builder builder = (Builder) request.getSession().getAttribute(BUILDER);
        // create Student
        Student student = new Student(builder, passwordEncoder);
        // set the degree course
        student.setDegreeCourse(degreeCourseServiceImpl.getDegreeCourseByName(degreeCourse));
        // save
        studentServiceImpl.addNewStudent(student);

        try{
            return new ModelAndView("user_student/create/student-result", "student", student);
        } catch (RuntimeException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }


    /**
     * Creates a new user with role professor
     * @param request HTTP request
     * @param fiscalCode fiscal code
     * @return ModelAndView
     * @throws ObjectAlreadyExistsException if a user with the given username already exists
     */
    @PostMapping(path = "/create-professor")
    public ModelAndView createNewUserWithRoleProfessor(HttpServletRequest request, @RequestParam String fiscalCode) {

        // recupera l'oggetto UserDto dalla sessione
        Builder builder = (Builder) request.getSession().getAttribute(BUILDER);
        // create Student
        Professor professor = new Professor(builder, passwordEncoder);
        // set the degree course
        professor.setFiscalCode(fiscalCode);
        // saves
        professorServiceImpl.addNewProfessor(professor);

        try {
            return new ModelAndView("user_professor/create/professor-result", "professor", professor);
        } catch (RuntimeException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }


    /**
     * Updates the user
     * @param form
     * @return ModelAndView
     */
    @PutMapping(path = "/update/build")
    public ModelAndView updateUser(@ModelAttribute Builder builder) {
        try {
            return new ModelAndView(
                "user/update/update-result",
                "result",
                userServiceImpl.updateUser(new RegistrationForm(builder)) != null?
                    "User updated successfully" : "User not updated"
            );
        } catch (RuntimeException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }


    /**
     * Deletes the user
     * @param username
     * @return ModelAndView
     */
    @DeleteMapping(path = "/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteUser(@NonNull @RequestParam("id") String userId) {
        try {
            return new ModelAndView(
                "user/delete/delete-result",
                "result",
                userServiceImpl.deleteUser(userId)?
                    "User delete successfully" : "User not deleted"
            );
        } catch (RuntimeException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }

}
