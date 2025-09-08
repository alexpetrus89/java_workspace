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
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.JsonProcessingException;
import com.alex.universitymanagementsystem.service.impl.DegreeCourseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;




@RestController
@RequestMapping(path = "api/v1/degree-course")
public class DegreeCourseController {

    // instance variables
    private final DegreeCourseServiceImpl degreeCourseService;

    // autowired - dependency injection - constructor
    public DegreeCourseController(DegreeCourseServiceImpl degreeCourseService) {
        this.degreeCourseService = degreeCourseService;
    }


    // methods
    /**
     * retrieves all degree courses
     * @return ModelAndView
     */
    @GetMapping(path = "/read/degree-courses")
    public ModelAndView getAllDegreeCourses() {
        Set<DegreeCourseDto> degreeCourses = degreeCourseService.getDegreeCourses();
        return new ModelAndView("user_admin/degree_course/read/degree-courses", "degreeCourses", degreeCourses);
    }


    /**
     * retrieves all courses of a given degree course
     * @param name the name of the degree course
     * @return ModelAndView
     */
    @GetMapping(path = "/read/courses")
    public ModelAndView getCourses(@RequestParam String name) {
        List<CourseDto> courses = degreeCourseService.getCourses(name.toUpperCase());
        return new ModelAndView("user_admin/degree_course/courses", "courses",courses);
    }


    /**
     * retrieves all professors of a given degree course
     * @param name the name of the degree course
     * @return ModelAndView
     */
    @GetMapping(path = "/read/professors")
    public ModelAndView getProfessors(@RequestParam String name) {
        List<ProfessorDto> professors = degreeCourseService.getProfessors(name.toUpperCase());
        return new ModelAndView("user_admin/degree_course/professor-with-course-list","professors", professors);
    }


    /**
     * retrieves all students of a given degree course
     * @param name the name of the degree course
     * @return ModelAndView
     */
    @GetMapping(path = "read/students")
    public ModelAndView getStudents(@RequestParam String name) {
        List<StudentDto> students = degreeCourseService.getStudents(name.toUpperCase());
        return new ModelAndView("user_admin/degree_course/students","students", students);
    }


    /**
     * retrieves all degree courses for ajax request
     * @return http response entity
     * @throws JsonProcessingException if the object cannot be serialized to JSON
     */
    @GetMapping(path = "read/degree-courses/ajax")
    public ResponseEntity<Set<DegreeCourseDto>> getJsonOfDegreeCourses() {
        try {
            return ResponseEntity.ok(degreeCourseService.getDegreeCourses());
        } catch (DataAccessServiceException e) {
            throw new JsonProcessingException("Parsing not working a cause of data access error", e);
        }
    }


    /**
     * retrieves all courses of a given degree course for ajax request
     * @param name of degree course
     * @return String - a JSON string
     * @throws JsonProcessingException if the object cannot be serialized to JSON
     */
    @GetMapping(path = "read/courses/ajax")
    public String getJsonOfCourses(@RequestParam String name) throws JsonProcessingException {
        try {
            // retrieve the courses
            List<CourseDto> courses = degreeCourseService.getCourses(name.toUpperCase());
            return "{\"degreeCourseName\": [" + courses
                .stream()
                .map(this::serializeCourseDto)
                .collect(Collectors.joining(",")) + "]}";
        } catch (DataAccessServiceException e) {
            throw new JsonProcessingException("Parsing not working a cause of data access error", e);
        }
    }



    // helper methods
    /**
     * Serializes a CourseDto object to a JSON string.
     * @param courseDto the CourseDto object to serialize
     * @return a JSON string representation of the CourseDto object
     */
    private String serializeCourseDto(CourseDto courseDto) throws JsonProcessingException {
        try {
            return new ObjectMapper().writeValueAsString(courseDto);
        } catch (IOException e) {
            throw new JsonProcessingException("Error serializing CourseDto", e);
        }
    }

}