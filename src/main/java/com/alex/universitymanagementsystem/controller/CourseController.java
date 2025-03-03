package com.alex.universitymanagementsystem.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.CourseServiceImpl;
import com.alex.universitymanagementsystem.utils.CourseType;
import com.alex.universitymanagementsystem.utils.CreateView;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping(path = "api/v1/course")
public class CourseController {

    // constants
    private static final String ATTRIBUTE_NAME = "courses";
    private static final String VIEW_NAME = "course/course-list";
    private static final String ERROR = "error";
    private static final String NOT_FOUND_PATH = "exception/object-not-found";
    private static final String ALREADY_EXISTS_PATH = "exception/object-already-exists";

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

        return new CreateView(
            ATTRIBUTE_NAME,
            courseServiceImpl.getCourses(),
            VIEW_NAME
        ).getModelAndView();
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

        return new CreateView(
            "course",
            courseServiceImpl.getCourseByNameAndDegreeCourseName(courseName, degreeCourseName),
            "course/read/read-result"
        ).getModelAndView();
    }


    /**
     * creates a new course
     * @return ModelAndView
     */
    @GetMapping("/create")
    public ModelAndView createNewCourseAndReturnView() {
        return new CreateView(
            new Course(),
            "course/create/create"
        ).getModelAndView();
    }


    /**
     * Update Course
     * @return ModelAndView
     */
    @GetMapping("/update")
    public ModelAndView updateCourseAndReturnView() {
        return new CreateView(
            new Course(),
            "course/update/update"
        ).getModelAndView();
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
            return new CreateView(
                courseServiceImpl.addNewCourse(
                    name.toLowerCase(),
                    type,
                    cfu,
                    uniqueCode.toLowerCase(),
                    degreeCourseName.toUpperCase()
                ),
                "course/create/create-result"
            ).getModelAndView();

        } catch (RuntimeException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                ALREADY_EXISTS_PATH
            ).getModelAndView();
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
    @Transactional
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
            return new CreateView(
                courseServiceImpl.updateCourse(
                    oldCourseName.toLowerCase(),
                    oldDegreeCourseName.toUpperCase(),
                    newCourseName.toLowerCase(),
                    newDegreeCourseName.toUpperCase(),
                    newType,
                    newCfu,
                    newUniqueCode.toLowerCase()
                ),
                "course/update/update-result"
            ).getModelAndView();
        } catch (RuntimeException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
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
    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public ModelAndView deleteCourseByName(@RequestParam String courseName, @RequestParam String degreeCourseName) {

        try{
            // retrieve course
            CourseDto courseDto =
                courseServiceImpl.getCourseByNameAndDegreeCourseName(courseName, degreeCourseName);

            // delete
            courseServiceImpl.deleteCourse(courseDto.getCourseId());

            return new CreateView("student/delete/delete-result")
                .getModelAndView();

        } catch (RuntimeException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }



}

