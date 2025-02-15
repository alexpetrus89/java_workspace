package com.alex.studentmanagementsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.studentmanagementsystem.domain.Course;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.service.implementation.CourseServiceImpl;
import com.alex.studentmanagementsystem.utility.CourseType;
import com.alex.studentmanagementsystem.utility.CreateView;

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
     * creates a new course
     * @param courseDto
     * @return ModelAndView
     * @throws ObjectAlreadyExistsException if a course with the same name already exists
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
                    name,
                    type,
                    cfu,
                    uniqueCode,
                    degreeCourseName
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


    @PutMapping(path = "/update")
    @Transactional
    public ModelAndView updateCourse(
        @RequestParam("old_name") String oldName,
        @RequestParam("new_name") String newName,
        @RequestParam("new_type") CourseType newType,
        @RequestParam("new_cfu") Integer newCfu,
        @RequestParam("new_unique_code") String newUniqueCode,
        @RequestParam("new_degree_course_name") String newDegreeCourseName
    ) {

        try {
            return new CreateView(
                courseServiceImpl.updateCourse(
                    newName,
                    oldName,
                    newType,
                    newCfu,
                    newUniqueCode,
                    newDegreeCourseName
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



}

