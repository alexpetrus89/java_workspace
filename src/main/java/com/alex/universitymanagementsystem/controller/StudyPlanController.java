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
import com.alex.universitymanagementsystem.dto.ChangeCoursesDto;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.StudyPlanDto;
import com.alex.universitymanagementsystem.service.impl.CourseServiceImpl;
import com.alex.universitymanagementsystem.service.impl.DegreeCourseServiceImpl;
import com.alex.universitymanagementsystem.service.impl.StudyPlanServiceImpl;


@RestController
@RequestMapping(path ="api/v1/study_plan")
public class StudyPlanController {

    // instance variables
    private final StudyPlanServiceImpl studyPlanServiceImpl;
    private final DegreeCourseServiceImpl degreeCourseServiceImpl;
    private final CourseServiceImpl courseServiceImpl;

    public StudyPlanController(
        StudyPlanServiceImpl studyPlanServiceImpl,
        DegreeCourseServiceImpl degreeCourseServiceImpl,
        CourseServiceImpl courseServiceImpl
    ) {
        this.studyPlanServiceImpl = studyPlanServiceImpl;
        this.degreeCourseServiceImpl = degreeCourseServiceImpl;
        this.courseServiceImpl = courseServiceImpl;
    }

    @GetMapping(path = "/view")
    public ModelAndView getStudyPlanInfo(@AuthenticationPrincipal Student student) {
        StudyPlanDto studyPlan = studyPlanServiceImpl.getStudyPlanByRegister(student.getRegister());
        return new ModelAndView("user_student/study_plan/study_plan_info", "studyPlan", studyPlan);
    }

    @GetMapping(path = "/courses")
    public ModelAndView getCourses(@AuthenticationPrincipal Student student) {

        String token = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Token non trovato"));

        ChangeCoursesDto courses = new ChangeCoursesDto(
            degreeCourseServiceImpl.getDegreeCourses(),
            studyPlanServiceImpl.getCoursesByRegister(student.getRegister()),
            courseServiceImpl.getCourses(),
            student.getDegreeCourse().getName(),
            token
        );

        return new ModelAndView("user_student/study_plan/study_plan_modify", "courses", courses);
    }

    @PutMapping(path = "/change")
    public ModelAndView changeCourse(
        @AuthenticationPrincipal Student student,
        @RequestParam String degreeCourseOfNewCourse,
        @RequestParam String courseToAdd,
        @RequestParam String degreeCourseOfOldCourse,
        @RequestParam String courseToRemove
    ) {
        studyPlanServiceImpl.changeCourse(student.getRegister(), degreeCourseOfNewCourse, degreeCourseOfOldCourse, courseToAdd, courseToRemove);
        Set<CourseDto> courses = studyPlanServiceImpl.getCoursesByRegister(student.getRegister());
        return new ModelAndView("user_student/study_plan/study_plan_courses", "courses", courses);
    }

}
