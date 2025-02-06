package com.alex.studentmanagementsystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.studentmanagementsystem.dto.CourseDto;
import com.alex.studentmanagementsystem.service.implementation.CourseServiceImplementation;
import com.alex.studentmanagementsystem.utility.CreateView;

@RestController
@RequestMapping(path = "api/v1/course")
public class CourseController {

    private static final String ATTRIBUTE_NAME = "courses";
    private static final String VIEW_NAME = "course/course-list";

    private final CourseServiceImplementation courseServiceImplementation;

    public CourseController(CourseServiceImplementation courseServiceImplementation) {
        this.courseServiceImplementation = courseServiceImplementation;
    }


    @GetMapping(path = "/view")
    public ModelAndView getCourses() {
        List<CourseDto> courses = courseServiceImplementation.getCourses();
        return new CreateView(ATTRIBUTE_NAME, courses, VIEW_NAME).getModelAndView();
    }



}

