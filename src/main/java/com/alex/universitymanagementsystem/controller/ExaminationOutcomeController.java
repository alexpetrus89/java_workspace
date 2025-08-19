package com.alex.universitymanagementsystem.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.dto.ExaminationAppealDto;
import com.alex.universitymanagementsystem.dto.ExaminationOutcomeDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.ExaminationAppealServiceImpl;
import com.alex.universitymanagementsystem.service.impl.ExaminationOutcomeServiceImpl;

import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping(path = "api/v1/examination-outcome")
public class ExaminationOutcomeController {

    // constants
    private static final String EXCEPTION_MESSAGE = "message";
    private static final String OBJECT_NOT_FOUND = "/object-not-found";

    @Value("#{dataAccessExceptionUri}")
    private String dataAccessExceptionUri;
    @Value("#{notFoundExceptionUri}")
    private String notFoundExceptionUri;
    @Value("#{alreadyExistsExceptionUri}")
    private String alreadyExistsExceptionUri;

    // instance variables
    private final ExaminationOutcomeServiceImpl examinationOutcomeServiceImpl;
    private final ExaminationAppealServiceImpl  examinationAppealServiceImpl;

    // constructor
    public ExaminationOutcomeController(
        ExaminationOutcomeServiceImpl examinationOutcomeServiceImpl,
        ExaminationAppealServiceImpl examinationAppealServiceImpl
    ) {
        this.examinationOutcomeServiceImpl = examinationOutcomeServiceImpl;
        this.examinationAppealServiceImpl = examinationAppealServiceImpl;
    }


    /**
     * View an examination outcome
     * @param student of the student
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
    public ModelAndView getExaminationOutcomes(@AuthenticationPrincipal Student student) {
        try {
            List<ExaminationOutcomeDto> outcomes = examinationOutcomeServiceImpl.getStudentOutcomes(student.getRegister().toString());
            return new ModelAndView("user_student/examinations/examination_outcome/outcome", "outcomes", outcomes);
        } catch (NoSuchElementException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Get an examination outcome
     * @param outcome
     * @return ModelAndView
     */
    @GetMapping(path = "/outcome")
    public ModelAndView getOutcome(@RequestParam Long id) {
        try {
            ExaminationOutcomeDto outcome = examinationOutcomeServiceImpl.getOutcomeById(id);
            return new ModelAndView("user_student/examinations/examination_outcome/outcome-result", "outcome", outcome);
        } catch (ObjectNotFoundException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Make an examination outcome
     * @param register of the student
     * @param id of the examination appeal
     * @return ModelAndView
     */
    @GetMapping(path = "/make/{register}/{id}")
    public ModelAndView makeExaminationOutcome(@PathVariable String register, @PathVariable Long id) {
        try {
            ExaminationAppealDto appeal = examinationAppealServiceImpl.getExaminationAppealById(id);
            ExaminationOutcomeDto outcome = new ExaminationOutcomeDto(appeal, register);
            return new ModelAndView("user_professor/examinations/examination_outcome/evaluation", "outcome", outcome);
        } catch (IllegalArgumentException e) {
            return new ModelAndView("exception/illegal/illegal-parameter", EXCEPTION_MESSAGE, e.getMessage());
        } catch (NoSuchElementException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Create an examination outcome
     * @param outcome
     * @return ModelAndView
     */
    @PostMapping(path = "/create")
    public ModelAndView addNewExaminationOutcome(@ModelAttribute ExaminationOutcomeDto outcome) {
        try {
            return new ModelAndView(
                "user_professor/examinations/examination_outcome/outcome-result",
                "result",
                examinationOutcomeServiceImpl.addNewExaminationOutcome(outcome) != null?
                    "outcome created successfully" : "outcome not created"
                );
        } catch (ObjectNotFoundException | NoSuchElementException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (ObjectAlreadyExistsException e) {
            return new ModelAndView(alreadyExistsExceptionUri + "object-already-exists", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Handle the confirmation of refusal
     * @param confirm of the refusal
     * @param model
     * @return String
     */
    @PostMapping(path = "confirm-refusal")
    public void handleRefusalConfirmation(@RequestParam("confirm") String confirm, HttpServletResponse response)
        throws IOException
    {
        if (confirm.equals("yes"))
        // Reindirizza alla pagina di conferma del rifiuto
            response.sendRedirect("/user_student/examinations/examination_outcome/refusal-confirmed");
        else
        // Reindirizza alla pagina di outcome-result
            response.sendRedirect("/api/v1/examination-outcome/view");
    }


    /**
     * Delete an examination outcome
     * @param outcome
     * @return ModelAndView
     */
    @DeleteMapping(path = "/delete/{outcome}")
    public ModelAndView deleteExaminationOutcome(@PathVariable ExaminationOutcomeDto outcome) {
        try {
            examinationOutcomeServiceImpl.deleteExaminationOutcome(outcome);
            return new ModelAndView("user_professor/examinations/examination_appeal/delete/delete-result");
        } catch (ObjectNotFoundException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }

}
