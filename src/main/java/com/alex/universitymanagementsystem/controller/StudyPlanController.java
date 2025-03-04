package com.alex.universitymanagementsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.StudyPlanServiceImpl;
import com.alex.universitymanagementsystem.utils.CreateView;


@RestController
@RequestMapping(path = "api/v1/studyPlan")
public class StudyPlanController {

    // constants
    private static final String ERROR = "error";
    private static final String NOT_FOUND_PATH = "exception/object-not-found";
    private static final String ALREADY_EXISTS_PATH = "exception/object-already-exists";

    // instance variables
    private final StudyPlanServiceImpl studyPlanServiceImpl;

    public StudyPlanController(StudyPlanServiceImpl studyPlanServiceImpl) {
        this.studyPlanServiceImpl = studyPlanServiceImpl;
    }

    /** GET request */

    /**
     * Retrieves all students
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
	public ModelAndView getCoursesAndReturnView(@RequestParam String register) {

        try {
            return new CreateView(
                studyPlanServiceImpl.getCoursesByRegister(new Register(register.toLowerCase())),
                "student/user_student/read-result"
            ).getModelAndView();

        } catch (ObjectNotFoundException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }

}
