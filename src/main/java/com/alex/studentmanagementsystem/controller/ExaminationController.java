package com.alex.studentmanagementsystem.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.studentmanagementsystem.domain.Examination;
import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.service.implementation.CourseServiceImpl;
import com.alex.studentmanagementsystem.service.implementation.ExaminationServiceImpl;
import com.alex.studentmanagementsystem.utility.CreateView;

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
    private final CourseServiceImpl courseServiceImpl;

    // autowired - dependency injection - constructor
    public ExaminationController(
        ExaminationServiceImpl examinationServiceImpl,
        CourseServiceImpl courseServiceImpl
    ) {
        this.examinationServiceImpl = examinationServiceImpl;
        this.courseServiceImpl = courseServiceImpl;
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
     * @param String courseName
     * @return ModelAndView
     * @throws ObjectNotFoundException
     * @throws NullPointerException
     */
    @GetMapping(path = "/course-name")
    public ModelAndView getExaminationsByCourseName(
        @RequestParam String courseName
    ) {
        try {

            return new CreateView(
                ATTRIBUTE_NAME,
                examinationServiceImpl.getExaminationsByCourseId(
                    courseServiceImpl
                        .getCourseByName(courseName)
                        .getCourseId()
                ),
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
     * @param Register
     * @return ModelAndView
     */
    @GetMapping(path = "/student-register")
    public ModelAndView getExaminationsByStudentRegister(
        @RequestParam Register register
    ) {
        try {

            return new CreateView(
                ATTRIBUTE_NAME,
                examinationServiceImpl.getExaminationsByStudentRegister(register),
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
     * @param UniqueCode
     * @return ModelAndView
     * @throws NullPointerException
     * @throws UnsupportedOperationException
     * @throws ClassCastException
     * @throws IllegalArgumentException
     */
    @GetMapping(path = "/professor-unique-code")
    public ModelAndView getExaminationsByProfessorUniqueCode(
        @RequestParam UniqueCode uniqueCode
    ) {
        try {

            return new CreateView(
                ATTRIBUTE_NAME,
                examinationServiceImpl.getExaminationsByProfessorUniqueCode(uniqueCode),
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
     * @param registration the student's registration
     * @param course the course name
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
        @RequestParam String registration,
        @RequestParam String course,
        @RequestParam String grade,
        @RequestParam Boolean withHonors,
        @RequestParam LocalDate date
    ) {

        try {
            return new CreateView(
                examinationServiceImpl.addNewExamination(
                    new Register(registration),
                    course,
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
     * @param oldRegistration the old student's registration
     * @param oldCourseName the old course name
     * @param newRegistration the new student's registration
     * @param newCourseName the new course name
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
        @RequestParam("old_registration") String oldRegistration,
        @RequestParam("old_course") String oldCourseName,
        @RequestParam("new_registration") String newRegistration,
        @RequestParam("new_course") String newCourseName,
        @RequestParam("grade") String grade,
        @RequestParam("withHonors") Boolean withHonors,
        @RequestParam("date") LocalDate date
    ) {

        try {
            return new CreateView(
                examinationServiceImpl.updateExamination(
                    new Register(oldRegistration),
                    oldCourseName,
                    new Register(newRegistration),
                    newCourseName,
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


}
