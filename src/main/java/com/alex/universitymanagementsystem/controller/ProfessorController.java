package com.alex.universitymanagementsystem.controller;


import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ProfessorMapper;
import com.alex.universitymanagementsystem.service.impl.ProfessorServiceImpl;

@RestController
@RequestMapping(path = "api/v1/professor")
public class ProfessorController {

    // constants
    private static final String PROFESSOR = "professor";
    private static final String PROFESSORS = "professors";
    private static final String ERROR = "error";
    private static final String NOT_FOUND_PATH = "exception/object-not-found";
    private static final String ALREADY_EXISTS_PATH = "exception/object-already-exists";

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
            return new ModelAndView(ERROR, e.getMessage(), NOT_FOUND_PATH);
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
            return new ModelAndView(ERROR, e.getMessage(), NOT_FOUND_PATH);
        }
    }


    /**
     * Creates a new professor
     * @return ModelAndView
     */
    @GetMapping("/create")
    public ModelAndView createNewProfessorAndReturnView() {
        return new ModelAndView("professor/create/create", PROFESSOR, new Professor());
    }


    /**
     * Updates a professor
     * @return ModelAndView
     */
    @GetMapping("/update")
    public ModelAndView updateProfessorAndReturnView() {
        return new ModelAndView("professor/update/update", PROFESSOR, new Professor());
    }


    /** POST request */
    /**
     * Creates a new professor
     * @param professorDto the data transfer object containing the details
     *                     of the professor to be added
     * @return ModelAndView
     * @throws ObjectAlreadyExistsException if a professor with the same unique
     *         code already exists
     * @throws IllegalArgumentException if the unique code is empty or null
     * @throws UnsupportedOperationException if the unique code is not unique
     * @throws NullPointerException if the unique code is null
     */
    @PostMapping("/create")
    public ModelAndView createNewProfessor(@ModelAttribute ProfessorDto professorDto) {

        try {
            professorServiceImpl.addNewProfessor(ProfessorMapper.mapToProfessor(professorDto));
            return new ModelAndView("professor/create/create-result", PROFESSOR, ProfessorMapper.mapToProfessor(professorDto));
        } catch (ObjectAlreadyExistsException | IllegalArgumentException | NullPointerException | UnsupportedOperationException e) {
            return new ModelAndView(ERROR, e.getMessage(), ALREADY_EXISTS_PATH);
        }
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
    @PutMapping("/update")
    public ModelAndView updateProfessor(@ModelAttribute ProfessorDto professorDto) {

        try {
            professorServiceImpl.updateProfessor(professorDto);
            Professor professor = ProfessorMapper.mapToProfessor(professorDto);
            return new ModelAndView("professor/update/update-result", PROFESSOR, professor);
        } catch (ObjectNotFoundException | IllegalArgumentException | NullPointerException | UnsupportedOperationException e) {
            return new ModelAndView(ERROR, e.getMessage(), NOT_FOUND_PATH);
        }
    }


    /** DELETE request */
    /**
     * Deletes a professor
     * @param uniqueCode the unique code of the professor
     * @return ModelAndView
     * @throws ObjectNotFoundException if the professor does not exist
     * @throws IllegalArgumentException if the unique code is empty
     * @throws UnsupportedOperationException if the unique code is not unique
     * @throws NullPointerException if the unique code is null
     */
    @DeleteMapping(path = "/delete/uniquecode")
    public ModelAndView deleteProfessorByUniqueCode(@RequestParam UniqueCode uniqueCode) {

        try {
            professorServiceImpl.deleteProfessor(uniqueCode);
            return new ModelAndView("professor/delete/delete-result");
        } catch (ObjectNotFoundException | IllegalArgumentException | NullPointerException | UnsupportedOperationException e) {
            return new ModelAndView(ERROR, e.getMessage(), NOT_FOUND_PATH);
        }
    }

}
