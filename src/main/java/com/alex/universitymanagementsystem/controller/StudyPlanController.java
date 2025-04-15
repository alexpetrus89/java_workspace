package com.alex.universitymanagementsystem.controller;

import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.ChangeCoursesDto;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;
import com.alex.universitymanagementsystem.dto.StudyPlanDto;
import com.alex.universitymanagementsystem.service.impl.DegreeCourseServiceImpl;
import com.alex.universitymanagementsystem.service.impl.StudyPlanServiceImpl;


@RestController
@RequestMapping(path ="api/v1/study_plan")
public class StudyPlanController {

    // instance variables
    private final StudyPlanServiceImpl studyPlanServiceImpl;
    private final DegreeCourseServiceImpl degreeCourseServiceImpl;

    public StudyPlanController(
        StudyPlanServiceImpl studyPlanServiceImpl,
        DegreeCourseServiceImpl degreeCourseServiceImpl
    ) {
        this.studyPlanServiceImpl = studyPlanServiceImpl;
        this.degreeCourseServiceImpl = degreeCourseServiceImpl;
    }


    /**
     * This method is used to get the study plan of a student.
     * It takes the student as a parameter and returns a ModelAndView object.
     * @param student
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
    public ModelAndView getStudyPlanInfo(@AuthenticationPrincipal Student student) {
        StudyPlanDto studyPlan = studyPlanServiceImpl.getStudyPlanByRegister(student.getRegister());
        return new ModelAndView("user_student/study_plan/study_plan_info", "studyPlan", studyPlan);
    }


    /**
     * Retrieves the courses related to a student's study plan and degree courses.
     * This includes all available courses, all degree courses, and the courses
     * currently in the student's study plan. The method constructs a
     * ChangeCoursesDto object that contains this information along with the
     * student's degree course name and a security token, then returns a
     * ModelAndView for modifying the study plan.
     *
     * @param student the authenticated student whose study plan is to be retrieved
     * @return a ModelAndView object for the "user_student/study_plan/study_plan_modify" view 
     *         with the ChangeCoursesDto object as the model
     */
    @GetMapping(path = "/courses")
    public ModelAndView getCourses(@AuthenticationPrincipal Student student) {

        // Retrieve all courses and degree courses and the courses of the student
        Set<DegreeCourseDto> allDegreeCourses = degreeCourseServiceImpl.getDegreeCourses();
        String degreeCourse = student.getDegreeCourse().getName();
        Set<CourseDto> studyPlan= studyPlanServiceImpl.getCoursesByRegister(student.getRegister());
        String token = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Token non trovato"));

        ChangeCoursesDto courses = new ChangeCoursesDto(
            allDegreeCourses,
            degreeCourse,
            studyPlan,
            token
        );

        return new ModelAndView("user_student/study_plan/study_plan_modify", "courses", courses);
    }

    /**
     * This method is used to add a course to the study plan of a student.
     * It takes the student, degree course, and course as parameters and returns a ModelAndView object.
     * @param student
     * @param degreeCourse
     * @param course
     * @return ModelAndView
     */
    @PutMapping(path = "/change")
    public ModelAndView changeCourse(
        @AuthenticationPrincipal Student student,
        @RequestParam String degreeCourseOfNewCourse,
        @RequestParam String degreeCourseOfOldCourse,
        @RequestParam String courseToAdd,
        @RequestParam String courseToRemove
    ) {
        Register register = student.getRegister();
        studyPlanServiceImpl.changeCourse(register, degreeCourseOfNewCourse, degreeCourseOfOldCourse, courseToAdd, courseToRemove);
        Set<CourseDto> courses = studyPlanServiceImpl.getCoursesByRegister(register);
        return new ModelAndView("user_student/study_plan/study_plan_courses", "courses", courses);
    }

}
