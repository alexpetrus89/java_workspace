package com.alex.studentmanagementsystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.studentmanagementsystem.dto.CourseDto;
import com.alex.studentmanagementsystem.dto.DegreeCourseDto;
import com.alex.studentmanagementsystem.dto.ProfessorDto;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.service.implementation.CourseServiceImplementation;
import com.alex.studentmanagementsystem.service.implementation.DegreeCourseServiceImplementation;
import com.alex.studentmanagementsystem.utility.CreateView;


@RestController
@RequestMapping(path = "api/v1/degree-course")
public class DegreeCourseController {

    private static final String ERROR = "error";
    private static final String NOT_FOUND_PATH = "exception/object-not-found";

    private final DegreeCourseServiceImplementation degreeCourseServiceImplementation;
    private final CourseServiceImplementation courseServiceImplementation;

    public DegreeCourseController(
        DegreeCourseServiceImplementation degreeCourseServiceImplementation,
        CourseServiceImplementation courseServiceImplementation
    ) {
        this.degreeCourseServiceImplementation = degreeCourseServiceImplementation;
        this.courseServiceImplementation = courseServiceImplementation;
    }


    @GetMapping("/view")
    public ModelAndView getDegreeCourses() {

        try {
            List<DegreeCourseDto> degreeCourses =
                degreeCourseServiceImplementation.getDegreeCourses();

            return new ModelAndView(
                "degree_course/degree-course-list",
                "degreeCourses",
                degreeCourses
            );

        } catch (ObjectNotFoundException e) {

            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }

    @GetMapping("/courses/view")
    public ModelAndView getCourses(@RequestParam String degreeCourseName) {

        try {
            List<CourseDto> filteredCourses = courseServiceImplementation
                .getCourses()
                .stream()
                .filter(degreeCourse -> degreeCourse
                    .getDegreeCourse()
                    .getName()
                    .equals(degreeCourseName)
                )
                .toList();

            return new ModelAndView(
                "degree_course/course-list",
                "courses",
                filteredCourses
            );

        } catch (ObjectNotFoundException e) {

            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }

    @GetMapping("/professors/view")
    public ModelAndView getProfessors() {

        try {
            List<ProfessorDto> professors =
                degreeCourseServiceImplementation.getProfessorsOfDegreeCourse("INGEGNERIA GESTIONALE");

            return new ModelAndView(
                "degree_course/professor-with-course-list",
                "professors",
                professors
            );

        } catch (ObjectNotFoundException e) {

            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }

    }

}
