package com.alex.universitymanagementsystem.controller;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.annotation.ValidRegister;
import com.alex.universitymanagementsystem.dto.ChangeDegreeCourseForm;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.StudentService;

import jakarta.validation.Valid;


@Validated
@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    // constants
    private static final String STUDENT = "student";
    private static final String STUDENTS = "students";
    private static final String MESSAGE = "message";

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
     * Retrieves a student by register
     * @param register the register of the student
     * @return ModelAndView
     */
    @GetMapping(path = "/read/register")
	public ModelAndView getStudentByRegister(@RequestParam @ValidRegister String register) {
        return handleStudentSearch(() -> studentService.getStudentByRegister(register),
            "No student found with register: " + register);
    }


    /**
     * Retrieves a student by name
     * @param name the name of the student
     * @return ModelAndView
     */
    @GetMapping(path = "/read/name")
	public ModelAndView getStudentsByFullName(@RequestParam String name) {
        List<StudentDto> students = studentService.getStudentsByFullname(name.toLowerCase());
        return new ModelAndView("user_admin/student/read/read-results", STUDENTS, students);
    }


    /**
     * displays the change degree course form
     * @return ModelAndView
     */
    @GetMapping("/change-degree-course")
    public ModelAndView getChangeDegreeCourseForm() {
        ChangeDegreeCourseForm form = new ChangeDegreeCourseForm();
        return new ModelAndView("user_admin/student/move/change-degree-course", "form", form);
    }


    /**
     * Updates the degree course of a student
     *@Valid @ModelAttribute("form") ChangeDegreeCourseForm form
     */
    @PostMapping("/change-degree-course")
    public ModelAndView changeDegreeCourse(
        @Valid @ModelAttribute ChangeDegreeCourseForm form,
        BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors())
            return new ModelAndView("user_admin/student/move/change-degree-course", "form", form);

        ModelAndView mav = new ModelAndView("user_admin/student/move/change-degree-course-result");
        String message = studentService.changeDegreeCourse(form.getRegister(), form.getDegreeCourse())
            ? String.format("Student %s has been successfully moved to degree course %s.",
                form.getRegister(), form.getDegreeCourse())
            : String.format("Error: Unable to change degree course for student %s.",
                form.getRegister());

        mav.addObject(MESSAGE, message);

        return mav;
    }



    // helpers
    /**
     * Metodo di supporto per riutilizzare la logica di ricerca.
     */
    private ModelAndView handleStudentSearch(Supplier<StudentDto> supplier, String notFoundMessage) {
        try {
            StudentDto student = supplier.get();
            return new ModelAndView("user_admin/student/read/read-result")
                .addObject(STUDENT, student);
        } catch (ObjectNotFoundException _) {
            return new ModelAndView("user_admin/student/read/read-result")
                .addObject(MESSAGE, notFoundMessage);
        }
    }


}
