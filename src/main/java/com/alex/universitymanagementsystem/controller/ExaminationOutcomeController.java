package com.alex.universitymanagementsystem.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.ExaminationOutcome;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.ExaminationAppealServiceImpl;

@RestController
@RequestMapping(path = "api/v1/examination-outcome")
public class ExaminationOutcomeController {

    // constants
    private static final String TITLE = "title";
    private static final String ERROR = "Errore";
    private static final String ERROR_URL = "/exception/error";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String STACK_TRACE = "stackTrace";

    // instance variables
    private final ExaminationAppealServiceImpl  examinationAppealServiceImpl;
    private final SimpMessagingTemplate simpMessagingTemplate;
    // constructor
    public ExaminationOutcomeController(ExaminationAppealServiceImpl examinationAppealService, SimpMessagingTemplate simpMessagingTemplate) {
        this.examinationAppealServiceImpl = examinationAppealService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping(path = "/make-outcome/{register}/{id}")
    public ModelAndView makeExaminationOutcome(@PathVariable String register, @PathVariable Long id) {
        try {
            ExaminationAppeal examAppeal = examinationAppealServiceImpl.getExaminationAppealById(id);
            ExaminationOutcome outcome = new ExaminationOutcome(examAppeal, register);
            return new ModelAndView("user_professor/examinations/examination_appeal/evaluation", "outcome", outcome);
        } catch (ObjectNotFoundException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_URL, model);
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
