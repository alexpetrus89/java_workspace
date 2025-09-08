package com.alex.universitymanagementsystem.controller;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.dto.UpdateForm;
import com.alex.universitymanagementsystem.dto.UserDto;
import com.alex.universitymanagementsystem.entity.DegreeCourse;
import com.alex.universitymanagementsystem.service.ProfessorService;
import com.alex.universitymanagementsystem.service.StudentService;
import com.alex.universitymanagementsystem.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    // constants
    private static final String REGISTRATION_FORM = "form";
    private static final String UPDATE_FORM = "form";
    private static final String MESSAGE = "message";

    // instance variables
    private final UserService userService;
    private final StudentService studentService;
    private final ProfessorService professorService;

    /** Autowired - dependency injection - constructor */
    public UserController(
        UserService userService,
        StudentService studentService,
        ProfessorService professorService
    ) {
        this.userService = userService;
        this.studentService = studentService;
        this.professorService = professorService;
    }


    /**
     * Retrieves all users
     * @return ModelAndView
     */
    @GetMapping(path = "/read/users")
    public ModelAndView getAllUsers() {
        List<UserDto> users = userService.getUsers();
        return new ModelAndView("user_admin/admin/read/users", "users", users);
    }


    /**
     * Retrieves all users
     * @return ModelAndView
     */
    @GetMapping(path = "/selection")
    public ModelAndView getAllUsersForAction() {
        List<UserDto> users = userService.getUsers();
        return new ModelAndView("user_admin/admin/update/user-selection", "users", users);
    }


    /**
     * Updates the user
     * @return ModelAndView
     */
    @GetMapping(path = "/update")
    public ModelAndView instantiateFormForAdminUpdate() {
        return new ModelAndView("user_admin/admin/update/update", UPDATE_FORM, new UpdateForm());
    }


    /**
     * Updates the student
     * @return ModelAndView
     */
    @GetMapping(path = "/update/student")
    public ModelAndView instantiateFormForStudentUpdate() {
        return new ModelAndView("user_student/student/update/update", UPDATE_FORM, new UpdateForm());
    }


    /**
     * Updates the professor
     * @return ModelAndView
     */
    @GetMapping(path = "/update/professor")
    public ModelAndView instantiateFormForProfessorUpdate() {
        return new ModelAndView("user_professor/professor/update/update", UPDATE_FORM, new UpdateForm());
    }


    /**
     * Creates a new user with role ADMIN
     * @param request HTTP request
     * @return String
     */
    @PostMapping(path = "/create-admin")
    public ModelAndView createNewUserWithRoleAdmin(HttpServletRequest request) {
        return handleCreation(
            request,
            userService::addNewUser,
            this::adminSuccessView,
            this::adminFailureView
        );
    }


    /**
     * Creates a new user with role STUDENT
     * @param request HTTP request
     * @param degreeCourse degree course object
     * @param ordering ordering of the study plan
     * @return ModelAndView
     */
    @PostMapping(path = "/create-student")
    public ModelAndView createNewUserWithRoleStudent(HttpServletRequest request, @ModelAttribute DegreeCourse degreeCourse, String ordering) {
        return handleCreation(
            request,
            form -> studentService.addNewStudent(form, degreeCourse, ordering),
            this::studentSuccessView,
            this::studentFailureView
        );
    }


    /**
     * Creates a new user with role PROFESSOR
     * @param request HTTP request
     * @return ModelAndView
     */
    @PostMapping(path = "/create-professor")
    public ModelAndView createNewUserWithRoleProfessor(HttpServletRequest request) {
        return handleCreation(
            request,
            professorService::addNewProfessor,
            this::professorSuccessView,
            this::professorFailureView
        );
    }


    /**
     * Updates the user
     * @param Builder an instance of Builder class
     * @return ModelAndView
     */
    @PutMapping(path = "/update")
    public ModelAndView updateUser(@Valid @ModelAttribute UpdateForm form) {
        return new ModelAndView(
            "user_admin/update/update-result",
            "result",
            userService
                .updateUser(form)
                .map(_ -> "User updated successfully")
                .orElse("User not updated")
        );
    }


    /**
     * Deletes the user
     * @param String userId
     * @return ModelAndView
     */
    @DeleteMapping(path = "/delete")
    public ModelAndView deleteUser(@RequestParam("id") String userId) {
        return new ModelAndView(
            "user_admin/delete/delete-result",
            "result",
            userService
                .deleteUser(userId)
                .map(_ -> "User delete successfully")
                .orElse("User not deleted")
        );
    }






    // helper methods

    /**
     * Handles the creation of a user (student or professor)
     * @param <T>
     * @param request
     * @param creationFunction
     * @param successView
     * @param failureView
     * @return ModelAndView
     */
    private <T> ModelAndView handleCreation(
        HttpServletRequest request,
        Function<RegistrationForm, Optional<T>> creationFunction,
        Function<T, ModelAndView> successView,
        Supplier<ModelAndView> failureView
    ) {
        RegistrationForm form = (RegistrationForm) request.getSession().getAttribute(REGISTRATION_FORM);
        return creationFunction
            .apply(form)
            .map(successView)
            .orElseGet(failureView);
    }


    // views

    /** admin success view */
    private ModelAndView adminSuccessView(UserDto admin) {
        return new ModelAndView("user_admin/admin/create/admin-success", "admin", admin);
    }

    /** admin failure view */
    private ModelAndView adminFailureView() {
        return new ModelAndView("user_admin/admin/create/admin-failure", MESSAGE, "admin not created");
    }


    /** student success view */
    private ModelAndView studentSuccessView(StudentDto student) {
        return new ModelAndView("user_student/student/create/student-success", "student", student);
    }

    /** student failure view */
    private ModelAndView studentFailureView() {
        return new ModelAndView("user_student/student/create/student-failure", MESSAGE, "student not created");
    }


    /** professor success view */
    private ModelAndView professorSuccessView(ProfessorDto professor) {
        return new ModelAndView("user_professor/professor/create/professor-success", "professor", professor);
    }

    /** professor failure view */
    private ModelAndView professorFailureView() {
        return new ModelAndView("user_professor/professor/create/professor-failure", MESSAGE, "professor not created");
    }


}
