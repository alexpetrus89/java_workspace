package com.alex.studentmanagementsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.studentmanagementsystem.service.implementation.CourseServiceImpl;
import com.alex.studentmanagementsystem.utility.CreateView;

@RestController
@RequestMapping(path = "api/v1/course")
public class CourseController {

    private static final String ATTRIBUTE_NAME = "courses";
    private static final String VIEW_NAME = "course/course-list";

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



}

