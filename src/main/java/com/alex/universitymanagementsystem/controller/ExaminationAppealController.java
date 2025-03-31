package com.alex.universitymanagementsystem.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.ExaminationAppealServiceImpl;
import com.alex.universitymanagementsystem.service.impl.StudentServiceImpl;

@RestController
@RequestMapping(path = "api/v1/examination-appeal")
public class ExaminationAppealController {

    // constants
    private static final String TITLE = "title";
    private static final String ERROR = "Errore";
    private static final String ERROR_PATH = "/error";
    private static final String ERROR_MESSAGE = "errorMessage";
    private static final String STACK_TRACE = "stackTrace";
    private static final String EXAMINATION_APPEALS = "examinationAppeals";

    // instance variables
    private final ExaminationAppealServiceImpl  examinationAppealServiceImpl;
    private final StudentServiceImpl studentServiceImpl;

    // constructor
    public ExaminationAppealController(
        ExaminationAppealServiceImpl examinationAppealService,
        StudentServiceImpl studentServiceImpl
    ) {
        this.examinationAppealServiceImpl = examinationAppealService;
        this.studentServiceImpl = studentServiceImpl;
    }

    /**
     * Retrieves all examination appeals for a student
     * @return ModelAndView
     * @throws ObjectNotFoundException
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws NullPointerException
     * @throws ClassCastException
     */
    @GetMapping(path = "/available/student")
    public ModelAndView getExamAppealsAvailableByStudent(@AuthenticationPrincipal Student student) {
        try {
            List<ExaminationAppeal> examinationAppeals = examinationAppealServiceImpl.getExaminationAppealsAvailable(student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/available-calendar",EXAMINATION_APPEALS, examinationAppeals);
        } catch (ObjectNotFoundException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }


    @GetMapping(path = "/booked/student")
    public ModelAndView getExamAppealsBookedByStudent(@AuthenticationPrincipal Student student) {
        try {
            List<ExaminationAppeal> examinationAppeals = examinationAppealServiceImpl.getExaminationAppealsBooked(student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/booked-calendar", EXAMINATION_APPEALS, examinationAppeals);
        } catch (ObjectNotFoundException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }


    /**
     * Retrieves all examination appeals for a professor
     * @return ModelAndView
     * @throws ObjectNotFoundException
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws NullPointerException
     * @throws ClassCastException
     */
    @GetMapping(path = "/view/professor")
    public ModelAndView getExaminationAppealsByProfessor(@AuthenticationPrincipal Professor professor) {
        try {
            List<ExaminationAppeal> examinationAppeals = examinationAppealServiceImpl.getExaminationAppealsByProfessor(professor.getUniqueCode());
            return new ModelAndView("user_professor/examinations/examination_appeal/calendar", EXAMINATION_APPEALS, examinationAppeals);
        } catch (ObjectNotFoundException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }

    /**
     * Retrieves all students for an examination appeal
     * @param id
     * @param professor
     * @return ModelAndView
     * @throws ObjectNotFoundException
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     */
    @GetMapping(path = "/view/students-booked/{id}")
    public ModelAndView getStudentsBooked(@PathVariable Long id, @AuthenticationPrincipal Professor professor) {
        try {
            List<StudentDto> students = examinationAppealServiceImpl
                .getExaminationAppealById(id)
                .getStudents()
                .stream()
                .map(studentServiceImpl::getStudentByRegister)
                .toList();

            return new ModelAndView("user_professor/examinations/examination_appeal/students-booked", "students", students);
        } catch (ObjectNotFoundException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }

    @PostMapping(path = "/create")
    public ModelAndView createNewExaminationAppeal(
        @AuthenticationPrincipal Professor professor,
        @RequestParam String course,
        @RequestParam String degreeCourse,
        @RequestParam String description,
        @RequestParam LocalDate date
    ) {

        try {
            ExaminationAppeal examAppeal = examinationAppealServiceImpl.addNewExaminationAppeal(course, degreeCourse, professor, description, date);
            return new ModelAndView("user_professor/examinations/examination_appeal/create-result", "examinationAppeal", examAppeal);
        } catch (ObjectNotFoundException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }


    @DeleteMapping(path = "/delete")
    public ModelAndView deleteExaminationAppeal(
        @AuthenticationPrincipal Professor professor,
        @RequestParam String course,
        @RequestParam String degreeCourse,
        @RequestParam LocalDate date
    ) {

        try {
        examinationAppealServiceImpl.deleteExaminationAppeal(course, degreeCourse, professor, date);
            return new ModelAndView("user_professor/examinations/examination_appeal/delete-result");
        } catch (ObjectNotFoundException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }


    /**
     * Book an examination appeal
     * @param student
     * @param id
     * @return ModelAndView
     */
    @PostMapping(path = "/booked/{id}")
    public ModelAndView bookExaminationAppeal(@AuthenticationPrincipal Student student, @PathVariable Long id) {
        try {
            ExaminationAppeal examAppeal = examinationAppealServiceImpl.bookExaminationAppeal(id, student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/booked-result", "examinationAppeal", examAppeal);
        } catch (ObjectNotFoundException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }

    @DeleteMapping(path = "delete-booked/{id}")
    public ModelAndView deleteBookedExaminationAppeal(@AuthenticationPrincipal Student student, @PathVariable Long id) {
        try {
            examinationAppealServiceImpl.deleteBookedExaminationAppeal(id, student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/delete-booked-result");
        } catch (ObjectNotFoundException e) {
            Map<String, Object> model = new HashMap<>();
            model.put(TITLE, ERROR);
            model.put(ERROR_MESSAGE, e.getMessage());
            model.put(STACK_TRACE, e.getStackTrace());
            return new ModelAndView(ERROR_PATH, model);
        }
    }

}
