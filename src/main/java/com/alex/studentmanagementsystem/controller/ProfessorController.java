package com.alex.studentmanagementsystem.controller;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.studentmanagementsystem.domain.Professor;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.dto.ProfessorDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.ProfessorMapper;
import com.alex.studentmanagementsystem.service.implementation.ProfessorServiceImpl;
import com.alex.studentmanagementsystem.utility.CreateView;

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
     * @param uniqueCode
     * @return ModelAndView
     * @throws NullPointerException
     */
    @GetMapping(path = "/read/uniquecode")
    public ModelAndView getProfessorsByUniqueCodeAndReturnView(
        @RequestParam UniqueCode uniqueCode
    ) {

        try {

            return new CreateView(
                ProfessorMapper.mapToProfessor(
                    professorServiceImpl.getProfessorByUniqueCode(uniqueCode)
                ),
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
     * @param professorName
     * @return ModelAndView
     * @throws NullPointerException
     */
    @GetMapping(path = "/read/name")
    public ModelAndView getProfessorsByNameAndReturnView(
        @RequestParam String professorName
    ) {
        try {

            return new CreateView(
                ProfessorMapper.mapToProfessor(
                    professorServiceImpl.getProfessorByName(professorName)
                ),
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
     * @param professorDto
     * @return ModelAndView
     * @throws ObjectAlreadyExistsException
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
     * @param professorDto
     * @return ModelAndView
     * @throws ObjectNotFoundException
     * @throws NullPointerException
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
     * @param uniqueCode
     * @return ModelAndView
     * @throws ObjectNotFoundException
     * @throws NullPointerException
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
