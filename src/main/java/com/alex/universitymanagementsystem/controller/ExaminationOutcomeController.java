package com.alex.universitymanagementsystem.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
import org.springframework.web.util.HtmlUtils;

import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.ExaminationOutcome;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.service.impl.ExaminationAppealServiceImpl;
import com.alex.universitymanagementsystem.service.impl.ExaminationOutcomeServiceImpl;
import com.alex.universitymanagementsystem.utils.Notify;
import com.alex.universitymanagementsystem.utils.NotifyMessage;

import jakarta.servlet.http.HttpServletResponse;



@RestController
@RequestMapping(path = "api/v1/examination-outcome")
public class ExaminationOutcomeController {

    // constants
    private static final String EXCEPTION_VIEW_NAME = "exception/read/error";
    private static final String EXCEPTION_MESSAGE = "message";

    // instance variables
    private final ExaminationOutcomeServiceImpl examinationOutcomeServiceImpl;
    private final ExaminationAppealServiceImpl  examinationAppealServiceImpl;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // constructor
    public ExaminationOutcomeController(
        ExaminationOutcomeServiceImpl examinationOutcomeServiceImpl,
        ExaminationAppealServiceImpl examinationAppealServiceImpl,
        SimpMessagingTemplate simpMessagingTemplate
    ) {
        this.examinationOutcomeServiceImpl = examinationOutcomeServiceImpl;
        this.examinationAppealServiceImpl = examinationAppealServiceImpl;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    /**
     * View an examination outcome
     * @param student of the student
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
    public ModelAndView getExaminationOutcomes(@AuthenticationPrincipal Student student) {
        try {
            List<ExaminationOutcome> outcomes = examinationOutcomeServiceImpl.getStudentOutcomes(student.getRegister().toString());
            return new ModelAndView("user_student/examinations/examination_outcome/outcome", "outcomes", outcomes);
        } catch (NullPointerException | IllegalArgumentException | UnsupportedOperationException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
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
            ExaminationOutcome outcome = examinationOutcomeServiceImpl.getOutcomeById(id);
            return new ModelAndView("user_student/examinations/examination_outcome/outcome-result", "outcome", outcome);
        } catch (NullPointerException | IllegalArgumentException | UnsupportedOperationException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
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
            ExaminationAppeal examAppeal = examinationAppealServiceImpl.getExaminationAppealById(id);
            ExaminationOutcome outcome = new ExaminationOutcome(examAppeal, register);
            return new ModelAndView("user_professor/examinations/examination_outcome/evaluation", "outcome", outcome);
        } catch (NullPointerException | IllegalArgumentException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Create an examination outcome
     * @param outcome
     * @return ModelAndView
     */
    @PostMapping(path = "/create")
    public ModelAndView addNewExaminationOutcome(@ModelAttribute ExaminationOutcome outcome) {
        try {
            return new ModelAndView(
                "user_professor/examinations/examination_outcome/outcome-result",
                "result",
                examinationOutcomeServiceImpl.addNewExaminationOutcome(outcome) != null?
                    "outcome created successfully" : "outcome not created"
            );
        } catch (NullPointerException | IllegalArgumentException | ObjectAlreadyExistsException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Delete an examination outcome
     * @param outcome
     * @return ModelAndView
     */
    @DeleteMapping(path = "/delete/{outcome}")
    public ModelAndView deleteExaminationOutcome(@PathVariable ExaminationOutcome outcome) {
        try {
            examinationOutcomeServiceImpl.deleteExaminationOutcome(outcome);
            return new ModelAndView("user_professor/examinations/examination_appeal/delete/delete-result");
        } catch (NullPointerException | IllegalArgumentException | UnsupportedOperationException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }



    @MessageMapping("/news")
    @SendTo("/topic/notify")
    public Notify notifyOutcome(NotifyMessage message) throws InterruptedException {
        // Invia il messaggio tramite WebSocket
        Thread.sleep(1000); // simulated delay
        return new Notify(HtmlUtils.htmlEscape(message.getName()));
    }


    @GetMapping(path = "/notify-websocket")
    public String notifyWebSocket() {
        // Invia il messaggio tramite WebSocket
        simpMessagingTemplate.convertAndSend("/topic/notify", "La tua valutazione è stata pubblicata!");
        return "";
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

}
