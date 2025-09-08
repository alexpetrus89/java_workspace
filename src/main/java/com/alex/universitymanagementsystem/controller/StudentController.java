package com.alex.universitymanagementsystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.entity.immutable.Register;
import com.alex.universitymanagementsystem.service.StudentService;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    // constants
    private static final String STUDENT = "student";
    private static final String STUDENTS = "students";

    // instance variable
    private final StudentService studentService;

    /** Autowired - dependency injection  - constructor */
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }


    // model and view methods
    /** GET request */

    /**
     * Retrieves all students
     * @return ModelAndView
     */
    @GetMapping(path = "/read/students")
	public ModelAndView getAllStudents() {
        List<StudentDto> students = studentService.getStudents();
        return new ModelAndView("user_admin/student/read/students", STUDENTS, students);
    }


    /**
     * Retrieves a student by name
     * @param name the name of the student
     * @return ModelAndView
     */
    @GetMapping(path = "/read/name")
	public ModelAndView getStudentsByName(@RequestParam String name) {
        List<StudentDto> students = studentService.getStudentsByFullname(name.toLowerCase());
        return new ModelAndView("user_admin/student/read/read-results", STUDENTS, students);
    }


    /**
     * Retrieves a student by register
     * @param register the register of the student
     * @return ModelAndView
     */
    @GetMapping(path = "/read/register")
	public ModelAndView getStudentByRegister(@RequestParam String register) {
        StudentDto student = studentService.getStudentByRegister(new Register(register));
        return new ModelAndView("user_admin/student/read/read-result", STUDENT, student);
    }



}
