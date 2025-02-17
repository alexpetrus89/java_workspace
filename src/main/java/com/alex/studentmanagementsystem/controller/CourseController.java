package com.alex.studentmanagementsystem.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.studentmanagementsystem.domain.Course;
import com.alex.studentmanagementsystem.dto.CourseDto;
import com.alex.studentmanagementsystem.dto.StudentDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.service.impl.CourseServiceImpl;
import com.alex.studentmanagementsystem.utils.CourseType;
import com.alex.studentmanagementsystem.utils.CreateView;

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
     * @param oldName old name of the course
     * @param newName new name of the course
     * @param newType new type of the course
     * @param newCfu new cfu of the course
     * @param newUniqueCode new unique code of the course
     * @param newDegreeCourseName new degree course name
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
                    newName.toLowerCase(),
                    oldName.toLowerCase(),
                    newType,
                    newCfu,
                    newUniqueCode.toLowerCase(),
                    newDegreeCourseName.toUpperCase()
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
     * @param name name of the course
     * @return ModelAndView
     * @throws ObjectNotFoundException if no course with the given name exists
     * @throws IllegalArgumentException if the name is null or empty
     * @throws UnsupportedOperationException if the name is not unique
     * @throws NullPointerException if the name is null
     */
    @DeleteMapping(path = "/delete/name")
    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public ModelAndView deleteStudentByName(@RequestParam String name) {

        try{
            CourseDto courseDto = courseServiceImpl.getCourseByName(name.toLowerCase());
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

