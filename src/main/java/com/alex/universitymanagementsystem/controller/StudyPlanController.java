package com.alex.universitymanagementsystem.controller;

import java.util.Set;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.service.impl.StudyPlanServiceImpl;


@RestController
@RequestMapping(path ="api/v1/study_plan")
public class StudyPlanController {

    // instance variables
    private final StudyPlanServiceImpl studyPlanServiceImpl;

    public StudyPlanController(StudyPlanServiceImpl studyPlanServiceImpl) {
        this.studyPlanServiceImpl = studyPlanServiceImpl;
    }

    @GetMapping("/view")
    public ModelAndView getStudyPlan(@AuthenticationPrincipal Student student) {
        Set<CourseDto> courses = studyPlanServiceImpl.getCoursesByRegister(student.getRegister());
        return new ModelAndView("study_plan/study_plan_courses", "courses", courses);
    }

    @PutMapping("/change")
    public ModelAndView changeCourse(
        @AuthenticationPrincipal Student student,
        @RequestParam String degreeCourseOfNewCourse,
        @RequestParam String courseToAdd,
        @RequestParam String degreeCourseOfOldCourse,
        @RequestParam String courseToRemove
    ) {
        studyPlanServiceImpl.changeCourse(student.getRegister(), degreeCourseOfNewCourse, degreeCourseOfOldCourse, courseToAdd, courseToRemove);
        Set<CourseDto> courses = studyPlanServiceImpl.getCoursesByRegister(student.getRegister());
        return new ModelAndView("study_plan/study_plan_courses", "courses", courses);
    }

}
