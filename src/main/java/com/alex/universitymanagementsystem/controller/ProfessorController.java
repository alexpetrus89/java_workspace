package com.alex.universitymanagementsystem.controller;


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
import com.alex.universitymanagementsystem.utils.CreateView;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping(path = "api/v1/professor")
public class ProfessorController {

    // constants
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

        return new CreateView(
            "professors",
            professorServiceImpl.getProfessors(),
            "professor/professor-list"
        ).getModelAndView();
    }


    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor
     * @return ModelAndView
     * @throws ObjectNotFoundException if the professor does not exist
     * @throws IllegalArgumentException if the unique code is empty
     * @throws UnsupportedOperationException if the unique code is not unique
     * @throws NullPointerException if the unique code is null
     */
    @GetMapping(path = "/read/uniquecode")
    public ModelAndView getProfessorsByUniqueCode(@RequestParam UniqueCode uniqueCode) {

        try {
            ProfessorDto professorDto = professorServiceImpl.getProfessorByUniqueCode(uniqueCode);

            return new CreateView(
                ProfessorMapper.mapToProfessor(professorDto),
                "professor/read/read-result"
            ).getModelAndView();

        } catch (ObjectNotFoundException e) {

            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }


    /**
     * Retrieves a professor by name
     * @param name the name of the professor
     * @return ModelAndView
     * @throws ObjectNotFoundException if the professor does not exist
     * @throws IllegalArgumentException if the name is empty or null
     * @throws UnsupportedOperationException if the name is not unique
     * @throws NullPointerException if the name is null
     */
    @GetMapping(path = "/read/name")
    public ModelAndView getProfessorsByName(@RequestParam String name) {

        try {
            ProfessorDto professorDto = professorServiceImpl.getProfessorByName(name);

            return new CreateView(
                ProfessorMapper.mapToProfessor(professorDto),
                "professor/read/read-result"
            ).getModelAndView();

        } catch (ObjectNotFoundException e) {

            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }


    /**
     * Creates a new professor
     * @return ModelAndView
     */
    @GetMapping("/create")
    public ModelAndView createNewProfessorAndReturnView() {

        return new CreateView(
            new Professor(),
            "professor/create/create"
        ).getModelAndView();
    }


    /**
     * Updates a professor
     * @return ModelAndView
     */
    @GetMapping("/update")
    public ModelAndView updateProfessorAndReturnView() {

        return new CreateView(
            new Professor(),
            "professor/update/update"
        ).getModelAndView();
    }


    /** POST request */
    /**
     * Creates a new professor
     * @param professorDto the data transfer object containing the details
     *                     of the professor to be added
     * @return ModelAndView
     * @throws ObjectAlreadyExistsException if a professor with the same unique
     *                                      code already exists
     * @throws IllegalArgumentException if the unique code is empty or null
     * @throws UnsupportedOperationException if the unique code is not unique
     * @throws NullPointerException if the unique code is null
     */
    @PostMapping("/create")
    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public ModelAndView createNewProfessor(@ModelAttribute ProfessorDto professorDto) {

        try {
            professorServiceImpl.addNewProfessor(professorDto);

            return new CreateView(
                ProfessorMapper.mapToProfessor(professorDto),
                "professor/create/create-result"
            ).getModelAndView();

        } catch (RuntimeException e) {

            return new CreateView(
                ERROR,
                e.getMessage(),
                ALREADY_EXISTS_PATH
            ).getModelAndView();
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
     */
    @PutMapping("/update")
    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public ModelAndView updateProfessor(@ModelAttribute ProfessorDto professorDto) {

        try {
            professorServiceImpl.updateProfessor(professorDto);

            return new CreateView(
                ProfessorMapper.mapToProfessor(professorDto),
                "professor/update/update-result"
            ).getModelAndView();

        } catch (RuntimeException e) {

            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
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
    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public ModelAndView deleteProfessorByUniqueCode(@RequestParam UniqueCode uniqueCode) {

        try {
            professorServiceImpl.deleteProfessor(uniqueCode);

            return new CreateView(
                "professor/delete/delete-result"
            ).getModelAndView();

        } catch (RuntimeException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }

}
