package com.alex.universitymanagementsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.RegistrationForm;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.StudentServiceImpl;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {


    // constants
    private static final String STUDENT = "student";
    private static final String STUDENTS = "students";
    private static final String EXCEPTION_MESSAGE = "message";

    @Value("#{dataAccessExceptionUri}")
    private String dataAccessExceptionUri;
    @Value("#{notFoundExceptionUri}")
    private String notFoundExceptionUri;
    @Value("#{illegalArgumentExceptionUri}")
    private String illegalArgumentExceptionUri;

    // instance variable
    private final StudentServiceImpl studentServiceImpl;

    /** Autowired - dependency injection  - constructor */
    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }


    // model and view methods
    /** GET request */

    /**
     * Retrieves all students
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
	public ModelAndView getAllStudents() {
        try {
            List<StudentDto> students = studentServiceImpl.getStudents();
            return new ModelAndView("student/student-list", STUDENTS, students);
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Retrieves a student by register
     * @param register the register of the student
     * @return ModelAndView
     */
    @GetMapping(path = "/read/register")
	public ModelAndView getStudentByRegister(@RequestParam String register) {
        try {
            StudentDto student = studentServiceImpl.getStudentByRegister(new Register(register));
            return new ModelAndView("student/read/read-result", STUDENT, student);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri + "illegal-parameter", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Retrieves a student by name
     * @param name the name of the student
     * @return ModelAndView
     */
    @GetMapping(path = "/read/name")
	public ModelAndView getStudentsByName(@RequestParam String name) {

        try{
            List<StudentDto> students = studentServiceImpl.getStudentsByFullname(name.toLowerCase());
            return new ModelAndView("student/read/read-results", STUDENTS, students);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri + "illegal-parameter", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Updates a student
     * @return ModelAndView
     */
    @GetMapping(path = "/update")
    public ModelAndView updateStudentAndReturnView() {
        return new ModelAndView("student/update/update", STUDENT, new RegistrationForm());
    }


    /**PUT request*/

    /**
     * Updates a student
     * @param studentDto the student data transfer object
     * @return ModelAndView
     */
    @PutMapping(path = "/update")
    public ModelAndView updateStudent(@ModelAttribute RegistrationForm form) {

        try {
            StudentDto student = studentServiceImpl.updateStudent(form);
            return new ModelAndView("student/update/update-result", STUDENT, student);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri + "illegal-parameters", EXCEPTION_MESSAGE, e.getMessage());
        } catch (ObjectNotFoundException e) {
            return new ModelAndView(notFoundExceptionUri + "object-not-found", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


}
