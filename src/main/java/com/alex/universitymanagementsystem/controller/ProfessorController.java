package com.alex.universitymanagementsystem.controller;


import java.util.List;
import java.util.regex.PatternSyntaxException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ProfessorMapper;
import com.alex.universitymanagementsystem.service.impl.ProfessorServiceImpl;

@RestController
@RequestMapping(path = "api/v1/professor")
public class ProfessorController {

    // constants
    private static final String PROFESSOR = "professor";
    private static final String PROFESSORS = "professors";
    private static final String ERROR_URL = "/exception/error";
    private static final String NOT_FOUND_URL = "exception/object-not-found";

    // instance variable
    private final ProfessorServiceImpl professorServiceImpl;

    /** Autowired - dependency injection - constructor */
    public ProfessorController(ProfessorServiceImpl professorServiceImpl) {
        this.professorServiceImpl = professorServiceImpl;
    }


    // methods
    /** GET request */
    /**
     * Retrieves all professors
     * @return ResponseEntity<List<ProfessorDto>>
     */
    @GetMapping(path = "/view")
    public ModelAndView getProfessors() {
        List<ProfessorDto> professors = professorServiceImpl.getProfessors();
        return new ModelAndView("professor/professor-list", PROFESSORS, professors);
    }


    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor
     * @return ModelAndView
     * @throws IllegalArgumentException if the unique code is empty
     * @throws UnsupportedOperationException if the unique code is not unique
     * @throws NullPointerException if the unique code is null
     */
    @GetMapping(path = "/read/uniquecode")
    public ModelAndView getProfessorsByUniqueCode(@RequestParam UniqueCode uniqueCode) {

        try {
            Professor professor = ProfessorMapper.mapToProfessor(professorServiceImpl.getProfessorByUniqueCode(uniqueCode));
            return new ModelAndView("professor/read/read-result", PROFESSOR, professor);
        } catch (UnsupportedOperationException | IllegalArgumentException | NullPointerException e) {
            return new ModelAndView(ERROR_URL, e.getMessage(), NOT_FOUND_URL);
        }
    }


    /**
     * Retrieves a professor by name
     * @param name the name of the professor
     * @return ModelAndView
     * @throws IllegalArgumentException if the name is empty or null
     * @throws UnsupportedOperationException if the name is not unique
     * @throws NullPointerException if the name is null
     */
    @GetMapping(path = "/read/name")
    public ModelAndView getProfessorsByName(@RequestParam String name) {

        try {
            Professor professor = ProfessorMapper.mapToProfessor(professorServiceImpl.getProfessorByName(name));
            return new ModelAndView("professor/read/read-result", PROFESSOR, professor);
        } catch (UnsupportedOperationException | IllegalArgumentException | NullPointerException e) {
            return new ModelAndView(ERROR_URL, e.getMessage(), NOT_FOUND_URL);
        }
    }


    /**
     * Updates a professor
     * @return ModelAndView
     */
    @GetMapping(path = "/update")
    public ModelAndView updateProfessorAndReturnView() {
        return new ModelAndView("professor/update/update", PROFESSOR, new Professor());
    }



    /** PUT request */
    /**
     * Updates a professor
     * @param professorDto thr data transfer object containing the new details
     *                     of the professor to be updated
     * @return ModelAndView
     * @throws ObjectNotFoundException if the professor does not exist
     * @throws IllegalArgumentException if the unique code is empty or null
     * @throws UnsupportedOperationException if the unique code is not unique
     * @throws NullPointerException if the unique code is null
     * @throws PatternSyntaxException if the regular expression's syntax is invalid
     */
    @PutMapping(path = "/update")
    public ModelAndView updateProfessor(@ModelAttribute ProfessorDto professorDto) {

        try {
            professorServiceImpl.updateProfessor(professorDto);
            Professor professor = ProfessorMapper.mapToProfessor(professorDto);
            return new ModelAndView("professor/update/update-result", PROFESSOR, professor);
        } catch (ObjectNotFoundException | IllegalArgumentException | NullPointerException | UnsupportedOperationException e) {
            return new ModelAndView(ERROR_URL, e.getMessage(), NOT_FOUND_URL);
        }
    }

}
