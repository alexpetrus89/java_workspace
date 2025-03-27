package com.alex.universitymanagementsystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.CourseServiceImpl;
import com.alex.universitymanagementsystem.utils.CourseType;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping(path = "api/v1/course")
public class CourseController {

    // constants
    private static final String TITLE = "title";
    private static final String ERROR = "Errore";
    private static final String ERROR_PATH = "/error/error";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String STACK_TRACE = "stackTrace";

    private static final String COURSE = "course";
    private final CourseServiceImpl courseServiceImpl;

    public CourseController(CourseServiceImpl courseServiceImpl) {
        this.courseServiceImpl = courseServiceImpl;
    }


    /**
     * retrieves all courses
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
    public ModelAndView getCourses() {

        List<CourseDto> courses = courseServiceImpl.getCourses();
        return new ModelAndView("course/course-list", "courses", courses);
    }


    /**
     * retrieves a course by its name and degree course name
     * @param courseName
     * @param degreeCourseName
     * @return ModelAndView
     * @throws ObjectNotFoundException if no course with the given name and degree course name exists
     * @throws NullPointerException if the course name or degree course name is null
     * @throws IllegalArgumentException if the course name or degree course name is empty
     * @throws UnsupportedOperationException if the course name or degree course name is not unique
     */
    @GetMapping("/read/name")
    public ModelAndView getCourse(@RequestParam String courseName, @RequestParam String degreeCourseName) {
        CourseDto course = courseServiceImpl
            .getCourseByNameAndDegreeCourseName(courseName, degreeCourseName);
        return new ModelAndView("course/read/read", COURSE, course);
    }

    @GetMapping(path = "/view/professor")
    public ModelAndView getCoursesByProfessor(@AuthenticationPrincipal Professor professor) {
        try {
            List<CourseDto> courses = courseServiceImpl.getCoursesByProfessor(professor);
            return new ModelAndView("user_professor/courses/course-list", "courses", courses);
        } catch (ObjectNotFoundException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }


    /**
     * creates a new course
     * @return ModelAndView
     */
    @GetMapping("/create")
    public ModelAndView createNewCourseAndReturnView() {
        return new ModelAndView("course/create/create", COURSE, new Course());
    }

    /**
     * Update Course
     * @return ModelAndView
     */
    @GetMapping("/update")
    public ModelAndView updateCourseAndReturnView() {
        return new ModelAndView("course/update/update", COURSE, new Course());
    }


    /**
     * create a new course
     * @param name name of the course
     * @param type type of the course
     * @param cfu cfu of the course
     * @param uniqueCode unique code of the professor
     * @param degreeCourseName degree course name
     * @return ModelAndView
     * @throws ObjectAlreadyExistsException if a course with the given name already exists
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws UnsupportedOperationException if any of the parameters is not unique
     * @throws NullPointerException if any of the parameters is null
     */
    @PostMapping("/create")
    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public ModelAndView createNewCourse(
        @RequestParam String name,
        @RequestParam CourseType type,
        @RequestParam Integer cfu,
        @RequestParam String uniqueCode,
        @RequestParam String degreeCourseName
    ) {

        try{
            Course course = courseServiceImpl.addNewCourse(name, type, cfu, uniqueCode, degreeCourseName);
            return new ModelAndView("course/create/create-result", COURSE, course);
        } catch (RuntimeException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }

    }


    /**
     * update a course
     * @param oldCourseName old name of the course
     * @param oldDegreeCourseName old degree course name
     * @param newCourseName new name of the course
     * @param newDegreeCourseName new degree course name
     * @param newType new type of the course
     * @param newCfu new cfu of the course
     * @param newUniqueCode new unique code of the course
     * @return ModelAndView
     * @throws ObjectNotFoundException if no course with the given name exists.
     * @throws ObjectAlreadyExistsException if a course with the new name already exists
     * @throws IllegalArgumentException - if any of the parameters is invalid.
     * @throws UnsupportedOperationException - if any of the parameters is not unique.
     * @throws NullPointerException - if any of the parameters is null.
     */
    @PutMapping(path = "/update")
    public ModelAndView updateCourse(
        @RequestParam("old_name") String oldCourseName,
        @RequestParam("old_degree_course_name") String oldDegreeCourseName,
        @RequestParam("new_name") String newCourseName,
        @RequestParam("new_degree_course_name") String newDegreeCourseName,
        @RequestParam("new_type") CourseType newType,
        @RequestParam("new_cfu") Integer newCfu,
        @RequestParam("new_unique_code") String newUniqueCode
    ) {

        try {

            Course course = courseServiceImpl.updateCourse(
                    oldCourseName.toLowerCase(),
                    oldDegreeCourseName.toUpperCase(),
                    newCourseName.toLowerCase(),
                    newDegreeCourseName.toUpperCase(),
                    newType,
                    newCfu,
                    newUniqueCode.toLowerCase()
            );
            return new ModelAndView("course/update/update-result", COURSE,course);
        } catch (RuntimeException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }


    /**
     * deletes a course by name
     * @param courseName name of the course
     * @param degreeCourseName degree course name
     * @return ModelAndView
     * @throws ObjectNotFoundException if no course with the given name exists
     * @throws IllegalArgumentException if the name is null or empty
     * @throws UnsupportedOperationException if the name is not unique
     * @throws NullPointerException if the name is null
     */
    @DeleteMapping(path = "/delete/name")
    public ModelAndView deleteCourseByName(@RequestParam String courseName, @RequestParam String degreeCourseName) {

        try{
            // retrieve course
            CourseDto courseDto = courseServiceImpl
                .getCourseByNameAndDegreeCourseName(courseName, degreeCourseName);
            // delete
            courseServiceImpl.deleteCourse(courseDto.getCourseId());
            return new ModelAndView("course/delete/delete-result", COURSE, courseDto);
        } catch (RuntimeException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }



}

