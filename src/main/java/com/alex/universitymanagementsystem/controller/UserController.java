package com.alex.universitymanagementsystem.controller;

import java.util.HashSet;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.StudyPlan;
import com.alex.universitymanagementsystem.domain.User;
import com.alex.universitymanagementsystem.domain.immutable.FiscalCode;
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
    private static final String ERROR_PATH = "/exception/error";

    // instance variables
    private final UserServiceImpl userServiceImpl;
    private final StudentServiceImpl studentServiceImpl;
    private final ProfessorServiceImpl professorServiceImpl;
    private final PasswordEncoder passwordEncoder;

    /** Autowired - dependency injection - constructor */
    public UserController(
        UserServiceImpl userServiceImpl,
        StudentServiceImpl studentServiceImpl,
        PasswordEncoder passwordEncoder,
        ProfessorServiceImpl professorServiceImpl
    ) {
        this.userServiceImpl = userServiceImpl;
        this.studentServiceImpl = studentServiceImpl;
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
            return userServiceImpl.addNewUser(new RegistrationForm(builder).toUser(passwordEncoder));
        } catch (NullPointerException e) {
            return "forward:" + ERROR_PATH;
        }
    }


    /**
     * Creates a new user with role STUDENT
     * @param request HTTP request
     * @param DegreeCourse degree course object
     * @return ModelAndView
     */
    @PostMapping(path = "/create-student")
    public ModelAndView createNewUserWithRoleStudent(HttpServletRequest request, @ModelAttribute DegreeCourse degreeCourse) {
        try{
            // recupera l'oggetto UserDto dalla sessione
            Builder builder = (Builder) request.getSession().getAttribute(BUILDER);
            // create Student
            Student student = new Student(builder, passwordEncoder);
            // set the degree course
            student.setDegreeCourse(degreeCourse);
            // set the study plan
            student.setStudyPlan(new StudyPlan(student, "ORD270", new HashSet<>(degreeCourse.getCourses())));
            // save
            studentServiceImpl.addNewStudent(student);
            return new ModelAndView("user_student/create/student-result", "student", student);
        } catch (ObjectAlreadyExistsException e) {
            return new ModelAndView("exception/creation/student-already-exists", "message", e.getMessage());
        } catch (ObjectNotFoundException e) {
            return new ModelAndView("exception/creation/degree-course-not-found", "message", e.getMessage());
        }
    }


    /**
     * Creates a new user with role PROFESSOR
     * @param request HTTP request
     * @param String fiscal code
     * @return ModelAndView
     */
    @PostMapping(path = "/create-professor")
    public ModelAndView createNewUserWithRoleProfessor(HttpServletRequest request, @RequestParam String fiscalCode) {
        try {
            // recupera l'oggetto UserDto dalla sessione
            Builder builder = (Builder) request.getSession().getAttribute(BUILDER);
            // create Student
            Professor professor = new Professor(builder, passwordEncoder);
            // set the degree course
            professor.setFiscalCode(new FiscalCode(fiscalCode));
            // saves
            professorServiceImpl.addNewProfessor(professor);
            return new ModelAndView("user_professor/create/professor-result", "professor", professor);
        } catch (ObjectAlreadyExistsException e) {
            return new ModelAndView("exception/creation/professor-already-exists", "message", e.getMessage());
        } catch (IllegalArgumentException | UnsupportedOperationException | ObjectNotFoundException e) {
            return new ModelAndView("exception/creation/fiscal-code-not-found", "message", e.getMessage());
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
                "user/update/update-result",
                "result",
                userServiceImpl.updateUser(new RegistrationForm(builder)) != null?
                    "User updated successfully" : "User not updated"
            );
        } catch (IllegalArgumentException e) {
            return new ModelAndView("exception/update/invalid-parameters", "message", e.getMessage());
        }
    }


    /**
     * Deletes the user
     * @param String userId
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
        } catch (UsernameNotFoundException e) {
            return new ModelAndView("exception/object-not-found", "message", e.getMessage());
        } catch (IllegalArgumentException e) {
            return new ModelAndView("exception/update/invalid-parameters", "message", e.getMessage());
        }
    }

}
