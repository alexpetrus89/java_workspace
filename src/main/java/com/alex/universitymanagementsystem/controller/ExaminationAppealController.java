package com.alex.universitymanagementsystem.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.ExaminationAppealServiceImpl;

@RestController
@RequestMapping(path = "api/v1/examination-appeal")
public class ExaminationAppealController {

    // constants
    private static final String TITLE = "title";
    private static final String ERROR = "Errore";
    private static final String ERROR_PATH = "/error";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String STACK_TRACE = "stackTrace";

    // instance variables
    private final ExaminationAppealServiceImpl  examinationAppealServiceImpl;

    // constructor
    public ExaminationAppealController(ExaminationAppealServiceImpl examinationAppealService) {
        this.examinationAppealServiceImpl = examinationAppealService;
    }

    /**
     * Retrieves all examination appeals for a student
     * @return ModelAndView
     * @throws ObjectNotFoundException
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws NullPointerException
     * @throws ClassCastException
     */
    @GetMapping(path = "/view")
    public ModelAndView getExaminationAppealsByStudent(@AuthenticationPrincipal Student student) {
        try {
            List<ExaminationAppeal> examinationAppeals = examinationAppealServiceImpl.getExaminationAppealByStudent(student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/calendar", "examinationAppeals", examinationAppeals);
        } catch (ObjectNotFoundException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }

    @PostMapping(path = "/create")
    public ModelAndView createNewExaminationAppeal(
        @AuthenticationPrincipal Professor professor,
        @RequestParam String course,
        @RequestParam String degreeCourse,
        @RequestParam String description,
        @RequestParam LocalDate date
    ) {

        try {
            ExaminationAppeal examAppeal = examinationAppealServiceImpl.addNewExaminationAppeal(course, degreeCourse, professor, description, date);
            return new ModelAndView("user_professor/examinations/examination_appeal/create-result", "examinationAppeal", examAppeal);
        } catch (ObjectNotFoundException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }


    @PostMapping(path = "/booked/{id}")
    public ModelAndView bookExaminationAppeal(@AuthenticationPrincipal Student student, @PathVariable Long id) {
        try {
            ExaminationAppeal examAppeal = examinationAppealServiceImpl.bookExaminationAppeal(id, student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/booked-result", "examinationAppeal", examAppeal);
        } catch (ObjectNotFoundException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }

}
