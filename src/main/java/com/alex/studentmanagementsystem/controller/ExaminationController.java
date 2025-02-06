package com.alex.studentmanagementsystem.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.dto.CourseDto;
import com.alex.studentmanagementsystem.dto.ExaminationDto;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.service.implementation.CourseServiceImplementation;
import com.alex.studentmanagementsystem.service.implementation.ExaminationServiceImplementation;
import com.alex.studentmanagementsystem.utility.CreateView;

@RestController
@RequestMapping(path = "api/v1/examination")
public class ExaminationController {

    // constants
    private static final String ERROR = "error";
    private static final String NOT_FOUND_PATH = "exception/object-not-found";
    private static final String ATTRIBUTE_NAME = "examinations";
    private static final String VIEW_NAME = "examination/examination-list";

    // instance variable
    private final ExaminationServiceImplementation examinationServiceImplementation;
    private final CourseServiceImplementation courseServiceImplementation;

    // autowired - dependency injection - constructor
    public ExaminationController(
        ExaminationServiceImplementation examinationServiceImplementation,
        CourseServiceImplementation courseServiceImplementation
    ) {
        this.examinationServiceImplementation = examinationServiceImplementation;
        this.courseServiceImplementation = courseServiceImplementation;
    }

    /** GET request */
    @GetMapping(path = "/view")
    public ModelAndView getExaminations() {
        List<ExaminationDto> examinations =
            examinationServiceImplementation.getExaminations();

        return new CreateView(
            ATTRIBUTE_NAME,
            examinations,
            VIEW_NAME
        ).getModelAndView();
    }


    @GetMapping(path = "/course-name")
    public ModelAndView getExaminationsByCourseName(
        @RequestParam String courseName
    ) {
        try {
            CourseDto courseDto =
                courseServiceImplementation.getCourseByName(courseName);

            List<ExaminationDto> examinations =
                examinationServiceImplementation
                    .getExaminationsByCourseId(courseDto.getCourseId());

            return new CreateView(
                ATTRIBUTE_NAME,
                examinations,
                VIEW_NAME
            ).getModelAndView();

        } catch (ObjectNotFoundException e) {

            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }


    @GetMapping(path = "/student-register")
    public ModelAndView getExaminationsByStudentRegister(
        @RequestParam Register register
    ) {
        try {
            List<ExaminationDto> examinations =
                examinationServiceImplementation
                    .getExaminationsByStudentRegister(register);

            return new CreateView(
                ATTRIBUTE_NAME,
                examinations,
                VIEW_NAME
            ).getModelAndView();

        } catch (ObjectNotFoundException e) {

            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }


    @GetMapping(path = "/professor-unique-code")
    public ModelAndView getExaminationsByProfessorUniqueCode(
        @RequestParam UniqueCode uniqueCode
    ) {
        try {
            List<ExaminationDto> examinations =
                examinationServiceImplementation
                    .getExaminationsByProfessorUniqueCode(uniqueCode);

            return new CreateView(
                ATTRIBUTE_NAME,
                examinations,
                VIEW_NAME
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
