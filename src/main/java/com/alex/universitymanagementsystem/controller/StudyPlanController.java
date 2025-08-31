package com.alex.universitymanagementsystem.controller;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;
import com.alex.universitymanagementsystem.dto.ExaminationDto;
import com.alex.universitymanagementsystem.dto.StudyPlanDto;
import com.alex.universitymanagementsystem.dto.SwapCoursesDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.service.DegreeCourseService;
import com.alex.universitymanagementsystem.service.ExaminationService;
import com.alex.universitymanagementsystem.service.StudyPlanService;

import jakarta.validation.Valid;



@RestController
@RequestMapping(path ="api/v1/study_plan")
public class StudyPlanController {

    // logger
    private final Logger logger =
        org.slf4j.LoggerFactory.getLogger(StudyPlanController.class);

    // instance variables
    private final StudyPlanService studyPlanService;
    private final DegreeCourseService degreeCourseService;
    private final ExaminationService examinationService;

    public StudyPlanController(
        StudyPlanService studyPlanService,
        DegreeCourseService degreeCourseService,
        ExaminationService examinationService
    ) {
        this.studyPlanService = studyPlanService;
        this.degreeCourseService = degreeCourseService;
        this.examinationService = examinationService;
    }


    /**
     * This method is used to get the study plan of a student.
     * It takes the student as a parameter and returns a ModelAndView object.
     * @param student
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
    public ModelAndView getStudyPlanView(@AuthenticationPrincipal Student student) {
        StudyPlanDto studyPlan = studyPlanService.getStudyPlanByRegister(student.getRegister());
        return new ModelAndView("user_student/study_plan/study_plan_view", "studyPlan", studyPlan);
    }


    /**
     * Retrieves the courses related to a student's study plan and
     * degree courses.
     * This includes all available courses, all degree courses, and
     * the courses currently in the student's study plan.
     * The method constructs a SwapCoursesDto object that contains
     * this information along with the student's degree course name
     * and a security token, then returns a ModelAndView for modifying
     * the study plan.
     *
     * @param student the authenticated student whose study plan is to be retrieved
     * @return a ModelAndView object for the "user_student/study_plan/study_plan_modify"
     * view with the SwapCoursesDto object as the model
     */
    @GetMapping(path = "/modify")
    public ModelAndView modifyStudyPlan(@AuthenticationPrincipal Student student) {
        // Retrieve all degree courses, student's degree course, student's study plan and security token
        Set<DegreeCourseDto> degreeCourses = degreeCourseService.getDegreeCourses();
        String degreeCourse = student.getDegreeCourse().getName();
        Set<CourseDto> studyPlan = getFilteredStudyPlan(student.getRegister());
        String token = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Token not found"));

        SwapCoursesDto courses = new SwapCoursesDto(
            degreeCourses,
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
    @PutMapping(path = "/swap")
    public ModelAndView swapCourses(
        @AuthenticationPrincipal Student student,
        @Valid @ModelAttribute SwapCoursesDto dto
    ) {
        dto.setRegister(student.getRegister().toString());
        studyPlanService.swapCourses(dto);
        Set<CourseDto> courses = studyPlanService.getCoursesByRegister(student.getRegister());
        return new ModelAndView("user_student/study_plan/study_plan_courses", "courses", courses);
    }



    // helper methods
    /**
     * Retrieves the filtered study plan for a student.
     * @param register the student's register
     * @return a set of CourseDto objects representing the filtered study plan
     * @throws DataAccessServiceException if there is an error accessing the data
     */
    private Set<CourseDto> getFilteredStudyPlan(Register register) throws DataAccessServiceException
    {
        return studyPlanService
            .getCoursesByRegister(register)
            .stream()
            .filter(course -> {
                try {
                    return examinationService
                        .getExaminationsByStudentRegister(register)
                        .stream()
                        .map(ExaminationDto::getCourseName)
                        .noneMatch(courseName -> courseName.equals(course.getName()));
                } catch (DataAccessServiceException e) {
                    logger.error("Failed to get examinations by student register", e);
                    return false;
                }
            })
            .collect(Collectors.toSet());
    }

}
