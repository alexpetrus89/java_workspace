package com.alex.universitymanagementsystem.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.JsonProcessingException;
import com.alex.universitymanagementsystem.service.impl.DegreeCourseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;




@RestController
@RequestMapping(path = "api/v1/degree-course")
public class DegreeCourseController {

    // constants
    private static final String EXCEPTION_VIEW_NAME = "exception/read/error";
    private static final String EXCEPTION_MESSAGE = "message";

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
    @GetMapping(path = "/view")
    public ModelAndView getDegreeCourses() {
        Set<DegreeCourseDto> degreeCourses = degreeCourseServiceImpl.getDegreeCourses();
        return new ModelAndView("degree_course/degree-course-list", "degreeCourses", degreeCourses);
    }


    /**
     * retrieves all courses of a given degree course
     * @param name the name of the degree course
     * @return ModelAndView
     */
    @GetMapping(path = "/courses/view")
    public ModelAndView getCourses(@RequestParam String name) {
        try {
            List<CourseDto> courses = degreeCourseServiceImpl.getCourses(name.toUpperCase());
            return new ModelAndView("degree_course/course-list", "courses",courses);
        } catch (NullPointerException | IllegalArgumentException | UnsupportedOperationException | ClassCastException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * retrieves all professors of a given degree course
     * @param name the name of the degree course
     * @return ModelAndView
     */
    @GetMapping(path = "/professors/view")
    public ModelAndView getProfessors(@RequestParam String name) {
        try {
            List<ProfessorDto> professors = degreeCourseServiceImpl.getProfessors(name.toUpperCase());
            return new ModelAndView("degree_course/professor-with-course-list","professors", professors);
        } catch (NullPointerException | IllegalArgumentException | UnsupportedOperationException  e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }

    }


    /**
     * retrieves all students of a given degree course
     * @param name the name of the degree course
     * @return ModelAndView
     */
    @GetMapping(path = "/students/view")
    public ModelAndView getStudents(@RequestParam String name) {
        try {
            List<StudentDto> students = degreeCourseServiceImpl.getStudents(name.toUpperCase());
            return new ModelAndView("degree_course/student-list","students", students);
        } catch (NullPointerException | IllegalArgumentException | UnsupportedOperationException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * retrieves all degree courses for ajax request
     * @return http response entity
     */
    @GetMapping(path = "/ajax")
    public ResponseEntity<Set<DegreeCourseDto>> getDegreeCoursesForAjaxRequest() {
        return ResponseEntity.ok(degreeCourseServiceImpl.getDegreeCourses());
    }


    /**
     * retrieves all courses of a given degree course for ajax request
     * @param name
     * @return String - a JSON string
     * @throws JsonProcessingException if the object cannot be serialized to JSON
     */
    @GetMapping(path = "/courses/ajax")
    public String getJsonOfCourses(@RequestParam String name) throws JsonProcessingException {
        try {
        // retrieve the courses
            List<CourseDto> courses = degreeCourseServiceImpl.getCourses(name.toUpperCase());
            return "{\"degreeCourseName\": [" + courses
                .stream()
                .map(this::serializeCourseDto)
                .collect(Collectors.joining(",")) + "]}";
        } catch (NullPointerException | IllegalArgumentException | UnsupportedOperationException | ClassCastException e) {
            return "{\"message\": \"" + e.getMessage() + "\"}";
        }
    }


    private String serializeCourseDto(CourseDto courseDto) throws JsonProcessingException {
        try {
            return new ObjectMapper().writeValueAsString(courseDto);
        } catch (IOException e) {
            throw new JsonProcessingException("Error serializing CourseDto", e);
        }
    }

}