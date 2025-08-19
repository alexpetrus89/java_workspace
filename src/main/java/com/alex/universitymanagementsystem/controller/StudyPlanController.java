package com.alex.universitymanagementsystem.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
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
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;
import com.alex.universitymanagementsystem.dto.ModifyStudyPlanDto;
import com.alex.universitymanagementsystem.dto.StudyPlanDto;
import com.alex.universitymanagementsystem.dto.SwapCoursesDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.DegreeCourseServiceImpl;
import com.alex.universitymanagementsystem.service.impl.StudyPlanServiceImpl;


@RestController
@RequestMapping(path ="api/v1/study_plan")
public class StudyPlanController {

    // constants
    private static final String EXCEPTION_MESSAGE = "message";

    @Value("#{dataAccessExceptionUri}")
    private String dataAccessExceptionUri;
    @Value("#{notFoundExceptionUri}")
    private String notFoundExceptionUri;
    @Value("#{alreadyExistsExceptionUri}")
    private String alreadyExistsExceptionUri;

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
        try {
            StudyPlanDto studyPlan = studyPlanServiceImpl.getStudyPlanByRegister(student.getRegister());
            return new ModelAndView("user_student/study_plan/study_plan_info", "studyPlan", studyPlan);
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Retrieves the courses related to a student's study plan and
     * degree courses.
     * This includes all available courses, all degree courses, and
     * the courses currently in the student's study plan.
     * The method constructs a ChangeCoursesDto object that contains
     * this information along with the student's degree course name
     * and a security token, then returns a ModelAndView for modifying
     * the study plan.
     *
     * @param student the authenticated student whose study plan is to be retrieved
     * @return a ModelAndView object for the "user_student/study_plan/study_plan_modify"
     * view with the ChangeCoursesDto object as the model
     */
    @GetMapping(path = "/modify")
    public ModelAndView modifyStudyPlan(@AuthenticationPrincipal Student student) {
        try {
            // Retrieve all degree courses, student's degree course, student's study plan and security token
            Set<DegreeCourseDto> degreeCourses = degreeCourseServiceImpl.getDegreeCourses();
            String degreeCourse = student.getDegreeCourse().getName();
            Set<CourseDto> studyPlan = studyPlanServiceImpl.getCoursesByRegister(student.getRegister());
            String token = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Token not found"));

            ModifyStudyPlanDto courses = new ModifyStudyPlanDto(
                degreeCourses,
                degreeCourse,
                studyPlan,
                token
            );

            return new ModelAndView("user_student/study_plan/study_plan_modify", "courses", courses);
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
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
    public ModelAndView changeCourses(
        @AuthenticationPrincipal Student student,
        @RequestParam String degreeCourseOfNewCourse,
        @RequestParam String degreeCourseOfOldCourse,
        @RequestParam String courseToAdd,
        @RequestParam String courseToRemove
    ) {
        Register register = student.getRegister();

        try {
            SwapCoursesDto dto = new SwapCoursesDto();
            dto.setRegister(student.getRegister());
            dto.setNewDegreeCourseName(degreeCourseOfNewCourse);
            dto.setOldDegreeCourseName(degreeCourseOfOldCourse);
            dto.setCourseToAddName(courseToAdd);
            dto.setCourseToRemoveName(courseToRemove);

            studyPlanServiceImpl.swapCourses(dto);
            Set<CourseDto> courses = studyPlanServiceImpl.getCoursesByRegister(register);
            return new ModelAndView("user_student/study_plan/study_plan_courses", "courses", courses);
        } catch (ObjectAlreadyExistsException e) {
            return new ModelAndView(alreadyExistsExceptionUri + "object-already-exists", EXCEPTION_MESSAGE, e.getMessage());
        } catch (ObjectNotFoundException e) {
            return new ModelAndView(notFoundExceptionUri + "object-not-found", EXCEPTION_MESSAGE, e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ModelAndView("validation/study_plan/invalid-choice", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }

}
