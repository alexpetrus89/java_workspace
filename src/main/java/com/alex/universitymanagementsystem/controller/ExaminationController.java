package com.alex.universitymanagementsystem.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Examination;
import com.alex.universitymanagementsystem.domain.ExaminationOutcome;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ExaminationDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.ExaminationOutcomeServiceImpl;
import com.alex.universitymanagementsystem.service.impl.ExaminationServiceImpl;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping(path = "api/v1/examination")
public class ExaminationController {

    // constants
    private static final String ATTRIBUTE_EXAMINATION = "examination";
    private static final String ATTRIBUTE_EXAMINATIONS = "examinations";
    private static final String VIEW_PATH = "examination/examination-list";
    private static final String EXCEPTION_VIEW_NAME = "exception/read/error";
    private static final String EXCEPTION_MESSAGE = "message";

    // instance variable
    private final ExaminationServiceImpl examinationServiceImpl;
    private final ExaminationOutcomeServiceImpl examinationOutcomeServiceImpl;

    // autowired - dependency injection - constructor
    public ExaminationController(
        ExaminationServiceImpl examinationServiceImpl,
        ExaminationOutcomeServiceImpl examinationOutcomeServiceImpl
    ) {
        this.examinationServiceImpl = examinationServiceImpl;
        this.examinationOutcomeServiceImpl = examinationOutcomeServiceImpl;
    }


    // methods
    /**
     * Returns a list of examinations
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
    public ModelAndView getExaminations() {
        List<ExaminationDto> examinations = examinationServiceImpl.getExaminations();
        return new ModelAndView(VIEW_PATH, ATTRIBUTE_EXAMINATIONS, examinations);
    }


    /**
     * Returns a list of examinations by course name
     * @param name name of the course
     * @return ModelAndView
     */
    @GetMapping(path = "/course-name")
    public ModelAndView getExaminationsByCourseName(@RequestParam String name) {

        try {
            List<ExaminationDto> examinations = examinationServiceImpl.getExaminationsByCourseName(name);
            return new ModelAndView(VIEW_PATH, ATTRIBUTE_EXAMINATIONS, examinations);
        } catch (NullPointerException | IllegalArgumentException | ObjectNotFoundException | UnsupportedOperationException  e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Returns a list of examinations by student register
     * @param register register of the student
     * @return ModelAndView
     */
    @GetMapping(path = "/student-register")
    public ModelAndView getExaminationsByStudentRegister(
        @AuthenticationPrincipal Student student,
        @RequestParam(required = false) String register
    ) {

        try {
            Register studRegister = student != null ? student.getRegister() : new Register(register);

            List<ExaminationDto> examinations = examinationServiceImpl.getExaminationsByStudentRegister(studRegister);
            return new ModelAndView("user_student/examinations/examinations", ATTRIBUTE_EXAMINATIONS, examinations);
        } catch (NullPointerException | IllegalArgumentException | ObjectNotFoundException | UnsupportedOperationException  e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Returns a list of examinations by professor unique code
     * @param uniqueCode unique code of the professor
     * @return ModelAndView
     */
    @GetMapping(path = "/professor-unique-code")
    public ModelAndView getExaminationsByProfessorUniqueCode(@RequestParam String uniqueCode) {

        try {
            List<ExaminationDto> examinations = examinationServiceImpl.getExaminationsByProfessorUniqueCode(new UniqueCode(uniqueCode));
            return new ModelAndView(VIEW_PATH, ATTRIBUTE_EXAMINATIONS, examinations);
        } catch (NullPointerException | IllegalArgumentException | ObjectNotFoundException | UnsupportedOperationException  e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Creates a new Examination
     * @return ModelAndView
     */
    @GetMapping("/create")
    public ModelAndView createNewExaminationAndReturnView() {
        return new ModelAndView("examination/create/create", ATTRIBUTE_EXAMINATION, new Examination());
    }


    /**
     * Update Examination
     * @return ModelAndView
     */
    @GetMapping("/update")
    public ModelAndView updateExaminationAndReturnView() {
        return new ModelAndView("examination/update/update", ATTRIBUTE_EXAMINATION, new Examination());
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
     */
    @PostMapping(path = "/create")
    public ModelAndView createNewExamination(
        @RequestParam String register,
        @RequestParam String courseName,
        @RequestParam String degreeCourseName,
        @RequestParam String grade,
        @RequestParam Boolean withHonors,
        @RequestParam LocalDate date
    ) {

        try {
            Examination examination = examinationServiceImpl.addNewExamination(
                new Register(register),
                courseName,
                degreeCourseName.toUpperCase(),
                grade,
                withHonors,
                date
            );
            ExaminationOutcome outcome = examinationOutcomeServiceImpl.getOutcomeByCourseAndStudent(courseName, register);
            examinationOutcomeServiceImpl.deleteExaminationOutcome(outcome);
            return new ModelAndView( "examination/create/create-result", ATTRIBUTE_EXAMINATION, examination);
        } catch (NullPointerException | IllegalArgumentException | IllegalStateException | ObjectNotFoundException | ObjectAlreadyExistsException | UnsupportedOperationException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Update existing examination
     * @param Register oldRegister the old student's register
     * @param String oldCourseName the old course name
     * @param String oldDegreeCourseName the old degree course name
     * @param Register newRegister the new student's register
     * @param String newCourseName the new course name
     * @param String newDegreeCourseName the new degree course name
     * @param String grade the new grade
     * @param Boolean withHonors whether the examination was passed with honors
     * @param LocalDate date the new date
     * @return Examination
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
            Examination examination = examinationServiceImpl.updateExamination(
                new Register(oldRegister.toLowerCase()),
                oldCourseName.toLowerCase(),
                oldDegreeCourseName.toUpperCase(),
                new Register(newRegister.toLowerCase()),
                newCourseName.toLowerCase(),
                newDegreeCourseName.toUpperCase(),
                grade,
                withHonors,
                date
            );
            return new ModelAndView("examination/create/create-result", ATTRIBUTE_EXAMINATION, examination);
        } catch (NullPointerException | IllegalArgumentException | ObjectNotFoundException | UnsupportedOperationException  e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Deletes an existing Examination
     * @param register the student's registration
     * @param courseName the course name
     * @return ModelAndView
     */
    @DeleteMapping(path = "/delete")
    public ModelAndView deleteExamination(
        @RequestParam String register,
        @RequestParam String courseName
    ) {

        try {
            examinationServiceImpl.deleteExamination(
                new Register(register.toLowerCase()),
                courseName.toLowerCase()
            );
            return new ModelAndView("examination/delete/delete-result");
        } catch (NullPointerException | IllegalArgumentException | ObjectNotFoundException | UnsupportedOperationException  e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


}
