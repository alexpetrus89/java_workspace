package com.alex.universitymanagementsystem.controller;

import java.util.Optional;

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

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.ExaminationOutcome;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.service.impl.ExaminationAppealServiceImpl;
import com.alex.universitymanagementsystem.service.impl.ExaminationOutcomeServiceImpl;



@RestController
@RequestMapping(path = "api/v1/examination-outcome")
public class ExaminationOutcomeController {

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
     * @param course of the student
     * @param student of the student
     * @return ModelAndView
     */
    @GetMapping(path = "/view/course")
    public ModelAndView getExaminationOutcome(@RequestParam String course, @AuthenticationPrincipal Student student) {
        try {
            ExaminationOutcome outcome = examinationOutcomeServiceImpl.getOutcomeByCourseAndStudent(course.toLowerCase(), student.getRegister().toString());
            return outcome != null ?
                new ModelAndView("user_student/examinations/examination_appeal/outcome-result", "outcome", outcome) :
                new ModelAndView("user_student/examinations/examination_appeal/student-absent");
        } catch (NullPointerException | IllegalArgumentException | UnsupportedOperationException e) {
            return new ModelAndView("exception/read/error", "message", e.getMessage());
        }
    }


    /**
     * Make an examination outcome
     * @param register of the student
     * @param id of the examination appeal
     * @return ModelAndView
     */
    @GetMapping(path = "/make-outcome/{register}/{id}")
    public ModelAndView makeExaminationOutcome(@PathVariable String register, @PathVariable Long id) {
        try {
            ExaminationAppeal examAppeal = examinationAppealServiceImpl.getExaminationAppealById(id);
            ExaminationOutcome outcome = new ExaminationOutcome(examAppeal, register);
            return new ModelAndView("user_professor/examinations/examination_appeal/evaluation", "outcome", outcome);
        } catch (NullPointerException e) {
            return new ModelAndView("exception/read/error", "message", e.getMessage());
        }
    }


    /**
     * Create an examination outcome
     * @param outcome
     * @return ModelAndView
     */
    @PostMapping(path = "/create")
    public ModelAndView addNewOutcome(@ModelAttribute ExaminationOutcome outcome) {
        try {
            return new ModelAndView(
                "user_professor/examinations/examination_appeal/outcome-result",
                "result",
                examinationOutcomeServiceImpl.addNewExaminationOutcome(outcome) != null?
                    "outcome created successfully" : "outcome not created"
            );
        } catch (NullPointerException | IllegalArgumentException | ObjectAlreadyExistsException e) {
            return new ModelAndView("exception/read/error", "message", e.getMessage());
        }
    }


    @DeleteMapping(path = "/delete/{outcome}")
    public ModelAndView deleteExaminationOutcome(@PathVariable ExaminationOutcome outcome) {
        try {
            examinationOutcomeServiceImpl.deleteExaminationOutcome(outcome);
            return new ModelAndView("user_professor/examinations/examination_appeal/delete-result");
        } catch (NullPointerException | IllegalArgumentException | UnsupportedOperationException e) {
            return new ModelAndView("exception/read/error", "message", e.getMessage());
        }
    }



    @SendTo("/topic/notify")
    @PostMapping(value = "/notify")
    public String notifyOutcome(@ModelAttribute ExaminationOutcome outcome) {
        // Inviare il messaggio tramite WebSocket
        outcome.setMessage(HtmlUtils.htmlEscape("New examination outcome for " +
            Optional.ofNullable(outcome.getExaminationAppeal())
                    .map(ExaminationAppeal::getCourse)
                    .map(Course::getName)
                    .orElse("Unknown course") + "!")
        );
        // Invia il messaggio tramite WebSocket
        simpMessagingTemplate.convertAndSend("/topic/notify", "La tua valutazione è stata pubblicata!");
        return "";
    }


    @GetMapping("/notify-websocket")
    public String notifyWebSocket() {
    // Invia il messaggio tramite WebSocket
    simpMessagingTemplate.convertAndSend("/topic/notify", "La tua valutazione è stata pubblicata!");
    return "";
    }

}
