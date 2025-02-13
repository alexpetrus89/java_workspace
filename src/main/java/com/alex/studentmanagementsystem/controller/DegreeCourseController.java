package com.alex.studentmanagementsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.ProfessorMapper;
import com.alex.studentmanagementsystem.service.implementation.CourseServiceImpl;
import com.alex.studentmanagementsystem.service.implementation.DegreeCourseServiceImpl;
import com.alex.studentmanagementsystem.service.implementation.StudentServiceImpl;
import com.alex.studentmanagementsystem.utility.CreateView;


@RestController
@RequestMapping(path = "api/v1/degree-course")
public class DegreeCourseController {

    // constants
    private static final String ERROR = "error";
    private static final String NOT_FOUND_PATH = "exception/object-not-found";

    // instance variables
    private final DegreeCourseServiceImpl degreeCourseServiceImpl;
    private final CourseServiceImpl courseServiceImpl;
    private final StudentServiceImpl studentServiceImpl;

    // autowired - dependency injection - constructor
    public DegreeCourseController(
        DegreeCourseServiceImpl degreeCourseServiceImpl,
        CourseServiceImpl courseServiceImpl,
        StudentServiceImpl studentServiceImpl
    ) {
        this.degreeCourseServiceImpl = degreeCourseServiceImpl;
        this.courseServiceImpl = courseServiceImpl;
        this.studentServiceImpl = studentServiceImpl;
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
     * @param String
     * @return ModelAndView
     */
    @GetMapping("/courses/view")
    public ModelAndView getCourses(@RequestParam String degreeCourseName) {

        return new ModelAndView(
            "degree_course/course-list",
            "courses",
            courseServiceImpl
                .getCourses()
                .stream()
                .filter(degreeCourse -> degreeCourse
                    .getDegreeCourse()
                    .getName()
                    .equals(degreeCourseName)
                )
                .toList()
        );
    }

    /**
     * retrieves all professors of a given degree course
     * @return ModelAndView
     */
    @GetMapping("/professors/view")
    public ModelAndView getProfessors(@RequestParam String degreeCourseName) {

        try {

            return new ModelAndView(
                "degree_course/professor-with-course-list",
                "professors",
                courseServiceImpl
                    .getCourses()
                    .stream()
                    .filter(degreeCourse -> degreeCourse
                        .getDegreeCourse()
                        .getName()
                        .equals(degreeCourseName)
                    )
                    .map(course -> ProfessorMapper.mapToProfessorDto(course.getProfessor()))
                    .distinct()
                    .toList()
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
     * @param String
     * @return ModelAndView
     */
    @GetMapping("/students/view")
    public ModelAndView getStudents(@RequestParam String degreeCourseName) {

        return new ModelAndView(
            "degree_course/student-list",
            "students",
            studentServiceImpl
                .getStudents()
                .stream()
                .filter(degreeCourse -> degreeCourse
                    .getDegreeCourse()
                    .getName()
                    .equals(degreeCourseName)
                )
                .toList()
        );
    }

}
