package com.alex.universitymanagementsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.dto.UserDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
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
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String FORWARD = "forward:";
    private static final String EXCEPTION_MESSAGE = "message";

    @Value("#{genericExceptionUri}")
    private String genericExceptionUri;
    @Value("#{dataAccessExceptionUri}")
    private String dataAccessExceptionUri;
    @Value("#{accessDeniedExceptionUri}")
    private String accessDeniedExceptionUri;
    @Value("#{notFoundExceptionUri}")
    private String notFoundExceptionUri;
    @Value("#{alreadyExistsExceptionUri}")
    private String alreadyExistsExceptionUri;

    // instance variables
    private final UserServiceImpl userServiceImpl;
    private final StudentServiceImpl studentServiceImpl;
    private final ProfessorServiceImpl professorServiceImpl;

    /** Autowired - dependency injection - constructor */
    public UserController(
        UserServiceImpl userServiceImpl,
        StudentServiceImpl studentServiceImpl,
        ProfessorServiceImpl professorServiceImpl
    ) {
        this.userServiceImpl = userServiceImpl;
        this.studentServiceImpl = studentServiceImpl;
        this.professorServiceImpl = professorServiceImpl;
    }


    /**
     * Retrieves all users
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
    public ModelAndView getAllUsers() {
        try {
            // retrieves all users
            List<UserDto> users = userServiceImpl.getUsers();
            return new ModelAndView("user_admin/read/user-list", "users", users);
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Updates the user
     * @return ModelAndView
     */
    @GetMapping(path = "/update")
    public ModelAndView updateUserAndReturnView() {
        return new ModelAndView("user_admin/update/update", BUILDER, new Builder());
    }


    /**
     * Updates the student
     * @return ModelAndView
     */
    @GetMapping(path = "/update/student")
    public ModelAndView updateStudentAndReturnView() {
        return new ModelAndView("user_student/update/update", BUILDER, new Builder());
    }


    /**
     * Updates the professor
     * @return ModelAndView
     */
    @GetMapping(path = "/update/professor")
    public ModelAndView updateProfessorAndReturnView() {
        return new ModelAndView("user_professor/update/update", BUILDER, new Builder());
    }


    /**
     * Creates a new user with role ADMIN
     * @param request HTTP request
     * @return String
     */
    @PostMapping(path = "/create-admin")
    public String createUser(HttpServletRequest request) {
        try {
            Builder builder = (Builder) request.getSession().getAttribute(BUILDER);
            if(userServiceImpl.addNewUser(new RegistrationForm(builder)) != null)
                return REDIRECT_LOGIN;
            else return FORWARD + genericExceptionUri;
        } catch (ObjectAlreadyExistsException _) {
            return FORWARD + alreadyExistsExceptionUri + "object-already-exists";
        } catch (DataAccessServiceException _) {
            return FORWARD + dataAccessExceptionUri;
        }
    }


    /**
     * Creates a new user with role STUDENT
     * @param request HTTP request
     * @param DegreeCourse degree course object
     * @return ModelAndView
     */
    @PostMapping(path = "/create-student")
    public ModelAndView createNewUserWithRoleStudent(HttpServletRequest request, @ModelAttribute DegreeCourse degreeCourse, String ordering) {
        try{
            // recupera l'oggetto UserDto dalla sessione
            Builder builder = (Builder) request.getSession().getAttribute(BUILDER);
            // save
            StudentDto student = studentServiceImpl.addNewStudent(new RegistrationForm(builder), degreeCourse, ordering);
            return new ModelAndView("user_student/create/student-result", "student", student);
        } catch (ObjectAlreadyExistsException e) {
            return new ModelAndView(alreadyExistsExceptionUri + "professor-already-exists", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Creates a new user with role PROFESSOR
     * @param request HTTP request
     * @return ModelAndView
     */
    @PostMapping(path = "/create-professor")
    public ModelAndView createNewUserWithRoleProfessor(HttpServletRequest request) {
        try {
            // recupera l'oggetto UserDto dalla sessione
            Builder builder = (Builder) request.getSession().getAttribute(BUILDER);
            // save
            ProfessorDto professor = professorServiceImpl.addNewProfessor(new RegistrationForm(builder));
            return new ModelAndView("user_professor/create/professor-result", "professor", professor);
        } catch (ObjectAlreadyExistsException e) {
            return new ModelAndView(alreadyExistsExceptionUri + "professor-already-exists", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Updates the user
     * @param Builder an instance of Builder class
     * @return ModelAndView
     */
    @PutMapping(path = "/update/build")
    public ModelAndView updateUser(@ModelAttribute Builder builder) {
        try {
            return new ModelAndView(
                "user_admin/update/update-result",
                "result",
                userServiceImpl.updateUser(new RegistrationForm(builder)) != null?
                    "User updated successfully" : "User not updated"
            );
        } catch (ObjectNotFoundException e) {
            return new ModelAndView(alreadyExistsExceptionUri + "object-not-found", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Deletes the user
     * @param String userId
     * @return ModelAndView
     */
    @DeleteMapping(path = "/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView deleteUser(@RequestParam("id") String userId) {
        try {
            return new ModelAndView(
                "user_admin/delete/delete-result",
                "result",
                userServiceImpl.deleteUser(userId)?
                    "User delete successfully" : "User not deleted"
            );
        } catch (UsernameNotFoundException e) {
            return new ModelAndView(notFoundExceptionUri + "username-not-found", EXCEPTION_MESSAGE, e.getMessage());
        } catch (AccessDeniedException e) {
            return new ModelAndView(accessDeniedExceptionUri + "access-denied", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }

}
