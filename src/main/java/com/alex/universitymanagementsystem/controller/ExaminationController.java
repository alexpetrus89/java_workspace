package com.alex.universitymanagementsystem.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Examination;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.ExaminationServiceImpl;
import com.alex.universitymanagementsystem.utils.CreateView;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping(path = "api/v1/examination")
public class ExaminationController {

    // constants
    private static final String ERROR = "error";
    private static final String NOT_FOUND_PATH = "exception/object-not-found";
    private static final String ATTRIBUTE_NAME = "examinations";
    private static final String VIEW_PATH = "examination/examination-list";

    // instance variable
    private final ExaminationServiceImpl examinationServiceImpl;

    // autowired - dependency injection - constructor
    public ExaminationController(ExaminationServiceImpl examinationServiceImpl) {
        this.examinationServiceImpl = examinationServiceImpl;
    }

    // methods
    /**
     * Returns a list of examinations
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
    public ModelAndView getExaminations() {

        return new CreateView(
            ATTRIBUTE_NAME,
            examinationServiceImpl.getExaminations(),
            VIEW_PATH
        ).getModelAndView();
    }


    /**
     * Returns a list of examinations by course name
     * @param name name of the course
     * @return ModelAndView
     * @throws ObjectNotFoundException if the course does not exist
     * @throws IllegalArgumentException if the name is null or empty
     * @throws ClassCastException if the name is not a string
     * @throws UnsupportedOperationException if the name is not unique
     * @throws NullPointerException if the name is null
     */
    @GetMapping(path = "/course-name")
    public ModelAndView getExaminationsByCourseName(@RequestParam String name) {
        try {

            return new CreateView(
                ATTRIBUTE_NAME,
                examinationServiceImpl.getExaminationsByCourseName(name.toLowerCase()),
                VIEW_PATH
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
     * Returns a list of examinations by student register
     * @param register register of the student
     * @return ModelAndView
     * @throws ObjectNotFoundException if the student does not exist
     * @throws IllegalArgumentException if the register is null or empty
     * @throws ClassCastException if the register is not a string
     * @throws UnsupportedOperationException if the register is not unique
     * @throws NullPointerException if the register is null or empty
     */
    @GetMapping(path = "/student-register")
    public ModelAndView getExaminationsByStudentRegister(@RequestParam String register) {
        try {

            return new CreateView(
                ATTRIBUTE_NAME,
                examinationServiceImpl.getExaminationsByStudentRegister(new Register(register.toLowerCase())),
                VIEW_PATH
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
     * Returns a list of examinations by professor unique code
     * @param uniqueCode unique code of the professor
     * @return ModelAndView
     * @throws ObjectNotFoundException if the professor does not exist
     * @throws NullPointerException if the unique code is null
     * @throws UnsupportedOperationException if the unique code is not unique
     * @throws ClassCastException if the unique code is not a string
     * @throws IllegalArgumentException if the unique code is empty
     */
    @GetMapping(path = "/professor-unique-code")
    public ModelAndView getExaminationsByProfessorUniqueCode(@RequestParam String uniqueCode) {
        try {

            return new CreateView(
                ATTRIBUTE_NAME,
                examinationServiceImpl.getExaminationsByProfessorUniqueCode(new UniqueCode(uniqueCode.toLowerCase())),
                VIEW_PATH
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
     * Creates a new Examination
     * @return ModelAndView
     */
    @GetMapping("/create")
    public ModelAndView createNewExaminationAndReturnView() {
        return new CreateView(
            new Examination(),
            "examination/create/create"
        ).getModelAndView();
    }


    /**
     * Update Examination
     * @return ModelAndView
     */
    @GetMapping("/update")
    public ModelAndView updateExaminationAndReturnView() {
        return new CreateView(
            new Examination(),
            "examination/update/update"
        ).getModelAndView();
    }


    /**
     * Creates a new Examination
     * @param register the student's registration
     * @param courseName the course name
     * @param degreeCourseName the degree course name
     * @param grade the grade obtained in the examination
     * @param withHonors whether the examination was passed with honors
     * @param date the date of the examination
     * @return a ModelAndView containing the details of the newly added examination
     * @throws ObjectAlreadyExistsException if the examination already exists
     * @throws ObjectNotFoundException if the student or course does not exist
     * @throws IllegalArgumentException if the date is in the past or the grade
     *                                  is not between 0 and 30 or Degree course
     *                                  does not match
     */
    @PostMapping(path = "/create")
    @Transactional
    public ModelAndView createNewExamination(
        @RequestParam String register,
        @RequestParam String courseName,
        @RequestParam String degreeCourseName,
        @RequestParam String grade,
        @RequestParam Boolean withHonors,
        @RequestParam LocalDate date
    ) {

        try {
            return new CreateView(
                examinationServiceImpl.addNewExamination(
                    new Register(register),
                    courseName,
                    degreeCourseName.toUpperCase(),
                    Integer.parseInt(grade),
                    withHonors,
                    date
                ),
                "examination/create/create-result"
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
     * Updates an existing Examination
     * @param oldRegister the old student's registration number
     * @param oldCourseName the old course name
     * @param oldDegreeCourseName the old degree course name
     * @param newRegister the new student's registration number
     * @param newCourseName the new course name
     * @param newDegreeCourseName the new degree course name
     * @param grade the new grade obtained in the examination
     * @param withHonors whether the examination was passed with honors
     * @param date the new date of the examination
     * @return a ModelAndView containing the details of the updated examination
     * @throws ObjectNotFoundException if the student or course does not exist
     * @throws IllegalArgumentException if the date is in the past or the grade
     *                                  is not between 0 and 30 or Degree course
     *                                  does not match
     */
    @PutMapping(path = "/update")
    @Transactional
    public ModelAndView updateExamination(
        @RequestParam("old_register") String oldRegister,
        @RequestParam("old_course") String oldCourseName,
        @RequestParam("old_degree_course_name") String oldDegreeCourseName,
        @RequestParam("new_register") String newRegister,
        @RequestParam("new_course") String newCourseName,
        @RequestParam("new_degree_course") String newDegreeCourseName,
        @RequestParam("grade") String grade,
        @RequestParam("withHonors") Boolean withHonors,
        @RequestParam("date") LocalDate date
    ) {

        try {
            return new CreateView(
                examinationServiceImpl.updateExamination(
                    new Register(oldRegister.toLowerCase()),
                    oldCourseName.toLowerCase(),
                    oldDegreeCourseName.toUpperCase(),
                    new Register(newRegister.toLowerCase()),
                    newCourseName.toLowerCase(),
                    newDegreeCourseName.toUpperCase(),
                    Integer.parseInt(grade),
                    withHonors,
                    date
                ),
                "examination/create/create-result"
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
     * Deletes an existing Examination
     * @param register the student's registration
     * @param courseName the course name
     * @return ModelAndView
     * @throws ObjectNotFoundException if the student or course does not exist
     * @throws IllegalArgumentException if the course name is null or empty
     *                                  or the register is null or empty
     * @throws UnsupportedOperationException if the course name is not unique
     *                                   or if the register is not unique
     * @throws NullPointerException if the course name is null or the register
     *                              is null
     */
    @DeleteMapping(path = "/delete")
    @Transactional
    public ModelAndView deleteExamination(
        @RequestParam String register,
        @RequestParam String courseName
    ) {

        try {
            examinationServiceImpl.deleteExamination(
                new Register(register.toLowerCase()),
                courseName.toLowerCase()
            );

            return new CreateView("examination/delete/delete-result")
                .getModelAndView();

        } catch (RuntimeException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }


}
