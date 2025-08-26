package com.alex.universitymanagementsystem.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.CreateCourseDto;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.UpdateCourseDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ProfessorMapper;
import com.alex.universitymanagementsystem.service.CourseService;
import com.alex.universitymanagementsystem.service.DegreeCourseService;
import com.alex.universitymanagementsystem.service.ProfessorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/course")
public class CourseController {

    // constants
    private static final String COURSE = "course";
    private static final String EXCEPTION_MESSAGE = "message";

    @Value("#{dataAccessExceptionUri}")
    private String dataAccessExceptionUri;
    @Value("#{notFoundExceptionUri}")
    private String notFoundExceptionUri;
    @Value("#{alreadyExistsExceptionUri}")
    private String alreadyExistsExceptionUri;
    @Value("#{illegalArgumentExceptionUri}")
    private String illegalArgumentExceptionUri;

    // instance variables
    private final CourseService courseService;
    private final ProfessorService professorService;
    private final DegreeCourseService degreeCourseService;

    public CourseController(CourseService courseService, ProfessorService professorService, DegreeCourseService degreeCourseService) {
        this.courseService = courseService;
        this.professorService = professorService;
        this.degreeCourseService = degreeCourseService;
    }


    /**
     * retrieves all courses
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
    public ModelAndView getCourses() {
        try {
            Set<CourseDto> courses = courseService.getCourses();
            return new ModelAndView("course/course-list", "courses", courses);
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * retrieves a course by its name and degree course name
     * @param courseName
     * @param degreeCourseName
     * @return ModelAndView
     */
    @GetMapping(path = "/read/name")
    public ModelAndView getCourse(@RequestParam String courseName, @RequestParam String degreeCourseName) {
        try {
            CourseDto course = courseService.getCourseByNameAndDegreeCourseName(courseName, degreeCourseName);
            return new ModelAndView("course/read/read-result", COURSE, course);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri + "/illegal-parameter", EXCEPTION_MESSAGE, e.getMessage());
        } catch (ObjectNotFoundException e) {
            return new ModelAndView(notFoundExceptionUri + "/object-not-found", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * retrieves all courses by professor
     * @return ModelAndView
     */
    @GetMapping(path = "/view/professor")
    public ModelAndView getCoursesByProfessor(@AuthenticationPrincipal Professor professor) {
        try {
            ProfessorDto professorDto = ProfessorMapper.toDto(professor);
            List<CourseDto> courses = courseService.getCoursesByProfessor(professorDto);
            return new ModelAndView("user_professor/courses/courses", "courses", courses);
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * creates a new course
     * @return ModelAndView
     */
    @GetMapping(path = "/create")
    public ModelAndView createNewCourseAndReturnView() {
        return new ModelAndView("course/create/create", COURSE, new CreateCourseDto());
    }

    /**
     * Update Course
     * @return ModelAndView
     */
    @GetMapping(path = "/update")
    public ModelAndView updateCourseAndReturnView() {
        return new ModelAndView("course/update/update", COURSE, new UpdateCourseDto());
    }


    /**
     * create a new course
     * @param course CourseDto object containing course details
     * @return ModelAndView
     */
    @PostMapping(path = "/create")
    public ModelAndView createNewCourse(@Valid @ModelAttribute("course") CreateCourseDto formDto) {

        try {

            ProfessorDto professor = professorService.getProfessorByUniqueCode(new UniqueCode(formDto.getUniqueCode()));
            DegreeCourseDto degreeCourse = degreeCourseService.getDegreeCourseByName(formDto.getDegreeCourseName());

            CourseDto courseDto = new CourseDto(
                formDto.getName(),
                formDto.getType(),
                formDto.getCfu(),
                professor,
                degreeCourse
            );

            CourseDto saved = courseService.addNewCourse(courseDto);

            return new ModelAndView("course/create/create-result", COURSE, saved);
        } catch (ObjectNotFoundException e) {
            return new ModelAndView(notFoundExceptionUri + "/object-not-found", EXCEPTION_MESSAGE, e.getMessage());
        } catch (ObjectAlreadyExistsException e) {
            return new ModelAndView(alreadyExistsExceptionUri + "/object-already-exists", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * update a course
     * @param oldCourseName old name of the course
     * @param oldDegreeCourseName old degree course name
     * @param courseDto CourseDto object containing updated course details
     * @return ModelAndView
     */
    @PutMapping(path = "/update")
    public ModelAndView updateCourse(@Valid @ModelAttribute UpdateCourseDto dto){
        try {

            CourseDto course = courseService.updateCourse(dto);
            return new ModelAndView("course/update/update-result", COURSE, course);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        } catch (ObjectNotFoundException e) {
            return new ModelAndView(notFoundExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * deletes a course by name
     * @param courseName name of the course
     * @param degreeCourseName degree course name
     * @return ModelAndView
     */
    @DeleteMapping("/courses/delete/{degreeCourseName}/{courseName}")
    public ModelAndView deleteCourseByName(@PathVariable String courseName, @PathVariable String degreeCourseName) {

        try{
            // retrieve course
            CourseDto courseDto = courseService.getCourseByNameAndDegreeCourseName(courseName, degreeCourseName);
            courseService.deleteByNameAndDegreeCourse(courseName, degreeCourseName);
            return new ModelAndView("course/delete/delete-result", COURSE, courseDto);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        } catch (ObjectNotFoundException e) {
            return new ModelAndView(notFoundExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }



}

