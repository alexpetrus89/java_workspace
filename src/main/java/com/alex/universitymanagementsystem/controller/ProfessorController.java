package com.alex.universitymanagementsystem.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.ProfessorServiceImpl;
import com.alex.universitymanagementsystem.utils.RegistrationForm;

@RestController
@RequestMapping(path = "api/v1/professor")
public class ProfessorController {

    // constants
    private static final String PROFESSOR = "professor";
    private static final String PROFESSORS = "professors";
    private static final String EXCEPTION_MESSAGE = "message";

    @Value("#{dataAccessExceptionUri}")
    private String dataAccessExceptionUri;
    @Value("#{notFoundExceptionUri}")
    private String notFoundExceptionUri;
    @Value("#{illegalArgumentExceptionUri}")
    private String illegalArgumentExceptionUri;


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
    public ModelAndView getAllProfessors() {
        try {
            List<ProfessorDto> professors = professorServiceImpl.getProfessors();
            return new ModelAndView("professor/professor-list", PROFESSORS, professors);
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Retrieves a professor by unique code
     * @param uniqueCode the unique code of the professor
     * @return ModelAndView
     */
    @GetMapping(path = "/read/uniquecode")
    public ModelAndView getProfessorByUniqueCode(@RequestParam UniqueCode uniqueCode) {

        try {
            ProfessorDto professor = professorServiceImpl.getProfessorByUniqueCode(uniqueCode);
            return new ModelAndView("professor/read/read-result", PROFESSOR, professor);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri + "illegal-parameter", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Retrieves a professor by name
     * @param name the name of the professor
     * @return ModelAndView
     */
    @GetMapping(path = "/read/name")
    public ModelAndView getProfessorsByFullname(@RequestParam String fullname) {
        try {
            List<ProfessorDto> professors = professorServiceImpl.getProfessorsByFullname(fullname);
            return new ModelAndView("professor/read/read-results", PROFESSORS, professors);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri + "illegal-parameter", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Updates a professor
     * @return ModelAndView
     */
    @GetMapping(path = "/update")
    public ModelAndView updateProfessorAndReturnView() {
        return new ModelAndView("professor/update/update", PROFESSOR, new RegistrationForm());
    }



    /** PUT request */
    /**
     * Updates a professor
     * @param professorDto thr data transfer object containing the new details
     *                     of the professor to be updated
     * @return ModelAndView
     */
    @PutMapping(path = "/update")
    public ModelAndView updateProfessor(@ModelAttribute RegistrationForm form) {
        try {
            ProfessorDto professor = professorServiceImpl.updateProfessor(form);
            return new ModelAndView("professor/update/update-result", PROFESSOR, professor);
        } catch (ObjectNotFoundException e) {
            return new ModelAndView(notFoundExceptionUri + "object-not-found", EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }

}
