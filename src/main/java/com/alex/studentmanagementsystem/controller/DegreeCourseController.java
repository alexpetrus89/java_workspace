package com.alex.studentmanagementsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.service.implementation.DegreeCourseServiceImpl;
import com.alex.studentmanagementsystem.utility.CreateView;


@RestController
@RequestMapping(path = "api/v1/degree-course")
public class DegreeCourseController {

    // constants
    private static final String ERROR = "error";
    private static final String NOT_FOUND_PATH = "exception/object-not-found";

    // instance variables
    private final DegreeCourseServiceImpl degreeCourseServiceImpl;

    // autowired - dependency injection - constructor
    public DegreeCourseController(DegreeCourseServiceImpl degreeCourseServiceImpl) {
        this.degreeCourseServiceImpl = degreeCourseServiceImpl;
    }


    // methods
    /**
     * retrieves all degree courses
     * @return ModelAndView
     */
    @GetMapping("/view")
    public ModelAndView getDegreeCourses() {

        return new ModelAndView(
            "degree_course/degree-course-list",
            "degreeCourses",
            degreeCourseServiceImpl.getDegreeCourses()
        );
    }


    /**
     * retrieves all courses of a given degree course
     * @param String name the name of the degree course
     * @return ModelAndView
     */
    @GetMapping("/courses/view")
    public ModelAndView getCourses(@RequestParam String name) {

        return new ModelAndView(
            "degree_course/course-list",
            "courses",
            degreeCourseServiceImpl.getCourses(name.toLowerCase())
        );
    }


    /**
     * retrieves all professors of a given degree course
     * @param String name the name of the degree course
     * @return ModelAndView
     */
    @GetMapping("/professors/view")
    public ModelAndView getProfessors(@RequestParam String name) {

        try {
            return new ModelAndView(
                "degree_course/professor-with-course-list",
                "professors",
                degreeCourseServiceImpl.getProfessors(name.toLowerCase())
            );

        } catch (ObjectNotFoundException e) {

            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }

    }


    /**
     * retrieves all students of a given degree course
     * @param String name the name of the degree course
     * @return ModelAndView
     */
    @GetMapping("/students/view")
    public ModelAndView getStudents(@RequestParam String name) {

        return new ModelAndView(
            "degree_course/student-list",
            "students",
            degreeCourseServiceImpl.getStudents(name.toUpperCase())
        );
    }

}
