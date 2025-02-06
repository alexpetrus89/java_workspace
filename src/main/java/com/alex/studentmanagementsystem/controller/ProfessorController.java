package com.alex.studentmanagementsystem.controller;

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

import com.alex.studentmanagementsystem.dto.ProfessorDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.ProfessorMapper;
import com.alex.studentmanagementsystem.domain.Professor;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.service.implementation.ProfessorServiceImplementation;
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
    private final ProfessorServiceImplementation professorServiceImplementation;

    /** Autowired - dependency injection */
    public ProfessorController(
        ProfessorServiceImplementation professorServiceImplementation
    ) {
        this.professorServiceImplementation = professorServiceImplementation;
    }

    /** GET request */
    @GetMapping(path = "/view")
    public ModelAndView getProfessors() {
        List<ProfessorDto> professors =
            professorServiceImplementation.getProfessors();

        return new CreateView(
            "professors",
            professors,
            "professor/professor-list"
        ).getModelAndView();
    }

    @GetMapping(path = "/read/uniquecode")
    public ModelAndView getProfessorsByUniqueCodeAndReturnView(
        @RequestParam UniqueCode uniqueCode
    ) {

        try {
            ProfessorDto professorDto =
                professorServiceImplementation.getProfessorByUniqueCode(uniqueCode);

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

    @GetMapping(path = "/read/name")
    public ModelAndView getProfessorsByNameAndReturnView(
        @RequestParam String professorName
    ) {
        try {
            ProfessorDto professorDto =
                professorServiceImplementation.getProfessorByName(professorName);

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

    @GetMapping("/create")
    public ModelAndView createNewProfessorAndReturnView() {

        return new CreateView(
            new Professor(),
            "professor/create/create"
        ).getModelAndView();
    }

    @GetMapping("/update")
    public ModelAndView updateProfessorAndReturnView() {

        return new CreateView(
            new Professor(),
            "professor/update/update"
        ).getModelAndView();
    }



    /** POST request */
    @PostMapping("/create")
    public ModelAndView createNewProfessor(@ModelAttribute ProfessorDto professorDto) {
        try {
            professorServiceImplementation.addNewProfessor(professorDto);

            return new CreateView(
                ProfessorMapper.mapToProfessor(professorDto),
                "professor/create/create-result"
            ).getModelAndView();

        } catch (ObjectAlreadyExistsException e) {

            return new CreateView(
                ERROR,
                e.getMessage(),
                ALREADY_EXISTS_PATH
            ).getModelAndView();
        }
    }



    /** PUT request */
    @PutMapping("/update")
    public ModelAndView updateProfessor(@ModelAttribute ProfessorDto professorDto) {

        try {
            professorServiceImplementation.updateProfessor(professorDto);

            return new CreateView(
                ProfessorMapper.mapToProfessor(professorDto),
                "professor/update/update-result"
            ).getModelAndView();

        } catch (ObjectNotFoundException e) {

            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }


    /** DELETE request */
    @DeleteMapping(path = "/delete/uniquecode")
    @Transactional
    public ModelAndView deleteProfessorByUniqueCode(@RequestParam UniqueCode uniqueCode) {

        try {
            professorServiceImplementation.deleteProfessor(uniqueCode);

            return new CreateView(
                "professor/delete/delete-result"
            ).getModelAndView();

        } catch (ObjectNotFoundException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }

}
