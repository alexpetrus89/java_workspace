package com.alex.universitymanagementsystem.controller;


import java.util.List;
import java.util.function.Supplier;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.annotation.ValidUniqueCode;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.ProfessorService;



@Validated
@RestController
@RequestMapping(path = "api/v1/professor")
public class ProfessorController {

    // constants
    private static final String PROFESSOR = "professor";
    private static final String PROFESSORS = "professors";


    // instance variable
    private final ProfessorService professorService;

    /** Autowired - dependency injection - constructor */
    public ProfessorController(ProfessorService professorService) {
        this.professorService = professorService;
    }


    // model and view methods
    /** GET request */
    /**
     * Retrieves all professors
     * @return ModelAndView
     */
    @GetMapping(path = "/read/professors")
    public ModelAndView getAllProfessors() {
        List<ProfessorDto> professors = professorService.getProfessors();
        return new ModelAndView("user_admin/professor/read/professors", PROFESSORS, professors);
    }


    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor
     * @return ModelAndView
     */
    @GetMapping(path = "/read/uniquecode")
    public ModelAndView getProfessorByUniqueCode(@RequestParam @ValidUniqueCode String uniqueCode) {
        return handleProfessorSearch(() -> professorService.getProfessorByUniqueCode(uniqueCode),
            "No professors found with unique code: " + uniqueCode);
    }


    /**
     * Retrieves a professor by name
     * @param name the name of the professor
     * @return ModelAndView
     */
    @GetMapping(path = "/read/name")
    public ModelAndView getProfessorsByFullname(@RequestParam String fullName) {
        List<ProfessorDto> professors = professorService.getProfessorsByFullname(fullName);
        return new ModelAndView("user_admin/professor/read/read-results", PROFESSORS, professors);
    }



    // helpers
    /**
     * Metodo di supporto per riutilizzare la logica di ricerca.
     */
    private ModelAndView handleProfessorSearch(Supplier<ProfessorDto> supplier, String notFoundMessage) {
        try {
            ProfessorDto professor = supplier.get();
            return new ModelAndView("user_admin/professor/read/read-result")
                .addObject(PROFESSOR, professor);
        } catch (ObjectNotFoundException _) {
            return new ModelAndView("user_admin/professor/read/read-result")
                .addObject("message", notFoundMessage);
        }
    }


}
