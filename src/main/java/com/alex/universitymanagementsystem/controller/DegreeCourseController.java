package com.alex.universitymanagementsystem.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.DegreeCourseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;




@RestController
@RequestMapping(path = "api/v1/degree-course")
public class DegreeCourseController {

    // constants
    private static final String ERROR_URL = "/exception/error";
    private static final String NOT_FOUND_URL = "exception/object-not-found";

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
     * @throws ObjectNotFoundException if no degree course with the
     *                                 given name exists
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is empty
     * @throws UnsupportedOperationException if the name is not unique
     * @throws ClassCastException if the name is not a string
     */
    @GetMapping(path = "/courses/view")
    public ModelAndView getCourses(@RequestParam String name) {

        List<CourseDto> courses = degreeCourseServiceImpl.getCourses(name.toUpperCase());
        return new ModelAndView("degree_course/course-list", "courses",courses);
    }


    /**
     * retrieves all professors of a given degree course
     * @param name the name of the degree course
     * @return ModelAndView
     * @throws ObjectNotFoundException if no degree course with the given name exists
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is empty
     * @throws UnsupportedOperationException if the name is not unique
     */
    @GetMapping(path = "/professors/view")
    public ModelAndView getProfessors(@RequestParam String name) {

        try {
            List<ProfessorDto> professors = degreeCourseServiceImpl.getProfessors(name.toUpperCase());
            return new ModelAndView("degree_course/professor-with-course-list","professors", professors);

        } catch (ObjectNotFoundException e) {
            return new ModelAndView(ERROR_URL, e.getMessage(), NOT_FOUND_URL);
        }

    }


    /**
     * retrieves all students of a given degree course
     * @param name the name of the degree course
     * @return ModelAndView
     * @throws ObjectNotFoundException if no degree course with the given name exists
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is empty
     * @throws UnsupportedOperationException if the name is not unique
     */
    @GetMapping(path = "/students/view")
    public ModelAndView getStudents(@RequestParam String name) {

        List<StudentDto> students = degreeCourseServiceImpl.getStudents(name.toUpperCase());

        return new ModelAndView(
            "degree_course/student-list",
            "students",
            students
        );
    }


    /**
     * retrieves all courses of a given degree course for particular ajax request
     * @param name
     * @return a list of data transfer objects of courses
     * @throws JsonProcessingException if the object cannot be serialized to JSON
     */
    @GetMapping(path = "/courses/ajax")
    public String getStringOfSerializedCourses(@RequestParam String name) throws JsonProcessingException {
        // get the courses from the service
        List<CourseDto> courses = degreeCourseServiceImpl.getCourses(name.toUpperCase());
        return "{\"degreeCourseName\": [" + courses
            .stream()
            .map(this::serializeCourseDto)
            .collect(Collectors.joining(",")) + "]}";
    }


    private String serializeCourseDto(CourseDto courseDto) throws JsonProcessingException {
        try {
            return new ObjectMapper().writeValueAsString(courseDto);
        } catch (IOException e) {
            // Handle the exception, for example, by throwing a JsonProcessingException
            throw new JsonProcessingException("Error serializing CourseDto", e);
        }
    }

}