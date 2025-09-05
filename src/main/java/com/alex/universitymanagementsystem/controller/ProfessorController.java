package com.alex.universitymanagementsystem.controller;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.service.ProfessorService;



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
    @GetMapping(path = "/view")
    public ModelAndView getAllProfessors() {
        List<ProfessorDto> professors = professorService.getProfessors();
        return new ModelAndView("user_professor/professor-list", PROFESSORS, professors);
    }


    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor
     * @return ModelAndView
     */
    @GetMapping(path = "/read/uniquecode")
    public ModelAndView getProfessorByUniqueCode(@RequestParam UniqueCode uniqueCode) {
        ProfessorDto professor = professorService.getProfessorByUniqueCode(uniqueCode);
        return new ModelAndView("user_professor/read/read-result", PROFESSOR, professor);
    }


    /**
     * Retrieves a professor by name
     * @param name the name of the professor
     * @return ModelAndView
     */
    @GetMapping(path = "/read/name")
    public ModelAndView getProfessorsByFullname(@RequestParam String fullname) {
        List<ProfessorDto> professors = professorService.getProfessorsByFullname(fullname);
        return new ModelAndView("user_professor/read/read-results", PROFESSORS, professors);
    }


}
