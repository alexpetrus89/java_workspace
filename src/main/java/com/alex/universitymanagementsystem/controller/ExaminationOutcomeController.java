package com.alex.universitymanagementsystem.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.alex.universitymanagementsystem.dto.ExaminationAppealDto;
import com.alex.universitymanagementsystem.dto.ExaminationOutcomeDto;
import com.alex.universitymanagementsystem.entity.Student;
import com.alex.universitymanagementsystem.entity.immutable.Register;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.service.EmailService;
import com.alex.universitymanagementsystem.service.ExaminationAppealService;
import com.alex.universitymanagementsystem.service.ExaminationOutcomeService;
import com.alex.universitymanagementsystem.service.OutcomeNotificationService;
import com.alex.universitymanagementsystem.service.StudentService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;



@RestController
@RequestMapping(path = "api/v1/examination-outcome")
public class ExaminationOutcomeController {

    // constants
    private static final String EXCEPTION_MESSAGE = "message";

    // logger
    private final Logger logger =
        LoggerFactory.getLogger(ExaminationOutcomeController.class);

    @Value("#{dataAccessExceptionUri}")
    private String dataAccessExceptionUri;

    // instance variables
    private final ExaminationOutcomeService examinationOutcomeService;
    private final ExaminationAppealService examinationAppealService;
    private final StudentService studentService;
    private final OutcomeNotificationService outcomeNotificationService;
    private final EmailService emailService;

    // constructor
    public ExaminationOutcomeController(
        ExaminationOutcomeService examinationOutcomeService,
        ExaminationAppealService examinationAppealService,
        StudentService studentService,
        OutcomeNotificationService outcomeNotificationService,
        EmailService emailService
    ) {
        this.examinationOutcomeService = examinationOutcomeService;
        this.examinationAppealService = examinationAppealService;
        this.studentService = studentService;
        this.outcomeNotificationService = outcomeNotificationService;
        this.emailService = emailService;
    }


    /**
     * View an examination outcome
     * @param student of the student
     * @return ModelAndView
     */
    @GetMapping(path = "/read/outcomes")
    public ModelAndView getAllExaminationOutcomes(@AuthenticationPrincipal Student student) {
        List<ExaminationOutcomeDto> outcomes = examinationOutcomeService.getStudentOutcomes(student.getRegister().toString());
        return new ModelAndView("user_student/examinations/examination_outcome/outcome", "outcomes", outcomes);
    }


    /**
     * Get an examination outcome
     * @param outcome
     * @return ModelAndView
     */
    @GetMapping(path = "/read/outcome")
    public ModelAndView getOutcome(@RequestParam Long id) {
        ExaminationOutcomeDto outcome = examinationOutcomeService.getOutcomeById(id);
        return new ModelAndView("user_student/examinations/examination_outcome/outcome-result", "outcome", outcome);
    }


    /**
     * Make an examination outcome
     * @param register of the student
     * @param id of the examination appeal
     * @return ModelAndView
     */
    @GetMapping(path = "/make/{register}/{id}")
    public ModelAndView makeExaminationOutcome(@PathVariable String register, @PathVariable Long id) {
        ExaminationAppealDto appeal = examinationAppealService.getExaminationAppealById(id);
        ExaminationOutcomeDto outcome = new ExaminationOutcomeDto(appeal, register);
        return new ModelAndView("user_professor/examinations/examination_outcome/evaluation", "outcome", outcome);
    }


    /**
     * Create an examination outcome
     * @param outcome
     * @return ModelAndView
     */
    @PostMapping(path = "/create")
    public ModelAndView addNewExaminationOutcome(@Valid @ModelAttribute ExaminationOutcomeDto outcome) {
        return examinationOutcomeService
            .addNewExaminationOutcome(outcome)
            .map(this::handleSuccess)
            .orElseGet(this::failureView);
    }


    /**
     * Handle the confirmation of refusal
     * @param confirm of the refusal
     * @param model
     * @return String
     */
    @PostMapping(path = "confirm-refusal")
    public void handleRefusalConfirmation(@RequestParam String confirm, HttpServletResponse response)
        throws IOException
    {
        if (confirm.equals("yes"))
            // Reindirizza alla pagina di conferma del rifiuto
            response.sendRedirect("/user_student/examinations/examination_outcome/refusal-confirmed");
        // Reindirizza alla pagina di outcome-result
        response.sendRedirect("/api/v1/examination-outcome/view");
    }


    /**
     * Delete an examination outcome
     * @param outcome
     * @return ModelAndView
     */
    @DeleteMapping(path = "/delete/{id}")
    public ModelAndView deleteExaminationOutcome(@PathVariable Long id) {
        examinationOutcomeService.deleteExaminationOutcome(id);
        return new ModelAndView("/user_student/examinations/examination-menu");
    }




    // helper methods
    /** Gestisce il caso di outcome creato con successo */
    private ModelAndView handleSuccess(ExaminationOutcomeDto outcome) {
        try {
            String username = studentService
                .getStudentByRegister(new Register(outcome.getRegister()))
                .getUsername();

            String message = buildNotificationMessage(outcome);
            outcomeNotificationService.notifyExamOutcome(username, message);
            emailService.sendEmail(username, "Exam Outcome Notification", message);

            return successView();
        } catch (DataAccessServiceException e) {
            logger.error("Error notifying exam outcome for user: " + outcome.getRegister(), e);
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }

    /** Costruisce il messaggio di notifica */
    private String buildNotificationMessage(ExaminationOutcomeDto outcome) {
        return "The results of the \"" + outcome.getAppeal().getCourse() +
                "\" exam held on " + outcome.getAppeal().getDate() + " are available.";
    }

    /** View in caso di successo */
    private ModelAndView successView() {
        return new ModelAndView(
            "user_professor/examinations/examination_outcome/outcome-result",
            "result",
            "outcome created successfully"
        );
    }

    /** View in caso di fallimento */
    private ModelAndView failureView() {
        return new ModelAndView(
            "user_professor/examinations/examination_outcome/outcome-result",
            "result",
            "outcome not created"
        );
    }

}