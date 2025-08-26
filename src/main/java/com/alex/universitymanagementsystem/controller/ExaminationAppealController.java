package com.alex.universitymanagementsystem.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.ExaminationAppealDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ProfessorMapper;
import com.alex.universitymanagementsystem.service.CourseService;
import com.alex.universitymanagementsystem.service.ExaminationAppealService;

@RestController
@RequestMapping(path = "api/v1/examination-appeal")
public class ExaminationAppealController {

    // constants
    private static final String EXAMINATION_APPEAL = "examinationAppeal";
    private static final String EXAMINATION_APPEALS = "examinationAppeals";
    private static final String EXCEPTION_MESSAGE = "message";
    private static final String OBJECT_NOT_FOUND = "/object-not-found";
    private static final String ILLEGAL_PARAMETER = "/illegal-parameter";

    @Value("#{dataAccessExceptionUri}")
    private String dataAccessExceptionUri;
    @Value("#{notFoundExceptionUri}")
    private String notFoundExceptionUri;
    @Value("#{illegalArgumentExceptionUri}")
    private String illegalArgumentExceptionUri;

    // instance variables
    private final ExaminationAppealService examinationAppealService;
    private final CourseService courseService;

    // constructor
    public ExaminationAppealController(
        ExaminationAppealService examinationAppealService,
        CourseService courseService
    ) {
        this.examinationAppealService = examinationAppealService;
        this.courseService = courseService;
    }

    /**
     * Retrieves all examination appeals for a student
     * @return ModelAndView
     */
    @GetMapping(path = "/available/student")
    public ModelAndView getExaminationAppealsAvailableForStudent(@AuthenticationPrincipal Student student) {
        try {
            List<ExaminationAppealDto> appeals = examinationAppealService.getExaminationAppealsAvailable(student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/available-calendar",EXAMINATION_APPEALS, appeals);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri + ILLEGAL_PARAMETER, EXCEPTION_MESSAGE, e.getMessage());
        } catch (NoSuchElementException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Retrieves all examination appeals for a student
     * @return ModelAndView
     */
    @GetMapping(path = "/booked/student")
    public ModelAndView getExaminationAppealsBookedByStudent(@AuthenticationPrincipal Student student) {
        try {
            List<ExaminationAppealDto> appeals = examinationAppealService.getExaminationAppealsBookedByStudent(student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/booked-calendar", EXAMINATION_APPEALS, appeals);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri + ILLEGAL_PARAMETER, EXCEPTION_MESSAGE, e.getMessage());
        } catch (NoSuchElementException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Retrieves all examination appeals for a professor
     * @return ModelAndView
     */
    @GetMapping(path = "/view/professor")
    public ModelAndView getExaminationAppealsMadeByProfessor(@AuthenticationPrincipal Professor professor) {
        try {
            List<ExaminationAppealDto> appeals = examinationAppealService.getExaminationAppealsMadeByProfessor(professor.getUniqueCode());
            return new ModelAndView("user_professor/examinations/examination_appeal/calendar", EXAMINATION_APPEALS, appeals);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri + ILLEGAL_PARAMETER, EXCEPTION_MESSAGE, e.getMessage());
        } catch (NoSuchElementException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Retrieves all students for an examination appeal
     * @param Long id of the examination appel
     * @param LocalDate date of the examination appeal
     * @param Professor professor owner of the examination appeal
     * @return ModelAndView
     */
    @GetMapping(path = "/view/students-booked/{id}/{date}")
    public ModelAndView getStudentsBooked(@PathVariable Long id, @PathVariable LocalDate date, @AuthenticationPrincipal Professor professor) {
        try {
            ExaminationAppealDto appeal = examinationAppealService.getExaminationAppealById(id);
            return new ModelAndView("user_professor/examinations/examination_appeal/students-booked", "appeal", appeal);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri + ILLEGAL_PARAMETER, EXCEPTION_MESSAGE, e.getMessage());
        } catch (NoSuchElementException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * methods for courses functionality
     * @param Professor professor
     * @return ModelAndView
     */
    @GetMapping(path = "/make")
    public ModelAndView getProfessorCourses(@AuthenticationPrincipal Professor professor) {
        try {
            List<CourseDto> courses = courseService.getCoursesByProfessor(ProfessorMapper.toDto(professor));
            return new ModelAndView("user_professor/examinations/examination_appeal/create/create-examination-appeal", "courses", courses);
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Deletes an examination appeal
     * @param student the authenticated student whose study plan is to be retrieved
     * @return a ModelAndView object for the "user_student/study_plan/study_plan_modify"
     * view with the ChangeCoursesDto object as the model
     */
    @GetMapping(path = "/delete")
    public ModelAndView deleteExaminationAppeal(@AuthenticationPrincipal Professor professor) {
        try {
            List<ExaminationAppealDto> appeals = examinationAppealService.getExaminationAppealsMadeByProfessor(professor.getUniqueCode());
            return new ModelAndView("user_professor/examinations/examination_appeal/delete/delete-examination-appeal", "appeals", appeals);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri + ILLEGAL_PARAMETER, EXCEPTION_MESSAGE, e.getMessage());
        } catch (NoSuchElementException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Create new examination appeal
     * @param Professor professor owners of the examination appeal
     * @param String course name
     * @param String degree course name
     * @param String description of the examination appeal
     * @param LocalDate date of the examination appeal
     */
    @PostMapping(path = "/create")
    public ModelAndView createNewExaminationAppeal(
        @AuthenticationPrincipal Professor professor,
        @RequestParam String courseName,
        @RequestParam String degreeCourseName,
        @RequestParam String description,
        @RequestParam LocalDate date
    ) {

        try {
            ExaminationAppealDto dto = new ExaminationAppealDto();
            dto.setCourse(courseName);
            dto.setDegreeCourse(degreeCourseName);
            dto.setProfessorCode(professor.getUniqueCode().toString());
            dto.setDescription(description);
            dto.setDate(date);
            ExaminationAppealDto appeal = examinationAppealService.addNewExaminationAppeal(dto);
            return new ModelAndView("user_professor/examinations/examination_appeal/create/create-result", EXAMINATION_APPEAL, appeal);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return new ModelAndView(illegalArgumentExceptionUri + ILLEGAL_PARAMETER, EXCEPTION_MESSAGE, e.getMessage());
        } catch (ObjectNotFoundException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Delete an examination appeal
     * @param Professor professor owners of the examination appeal
     * @param String course id
     * @param String degree course id
     * @param LocalDate date of the examination appeal
     */
    @DeleteMapping(path = "/delete")
    public ModelAndView deleteExaminationAppeal(
        @AuthenticationPrincipal Professor professor,
        @RequestParam Long id
    ) {
        try {
            ProfessorDto professorDto = ProfessorMapper.toDto(professor);
            return new ModelAndView(
                "user_professor/examinations/examination_appeal/delete/delete-result",
                "result",
                examinationAppealService.deleteExaminationAppeal(professorDto, id) ?
                    "appeal delete successfully" : "appeal not deleted"
            );
        } catch (ObjectNotFoundException | NoSuchElementException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Book an examination appeal
     * @param Student student
     * @param Long id
     * @return ModelAndView
     */
    @PostMapping(path = "/booked/{id}")
    public ModelAndView bookExaminationAppeal(@AuthenticationPrincipal Student student, @PathVariable Long id) {
        try {
            ExaminationAppealDto appeal = examinationAppealService.addStudentToAppeal(id, student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/booked-result", EXAMINATION_APPEAL, appeal);
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri + ILLEGAL_PARAMETER, EXCEPTION_MESSAGE, e.getMessage());
        } catch (NoSuchElementException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }

    /**
     * Delete an examination appeal reservation
     * @param Student student
     * @param Long id
     * @return ModelAndView
     */
    @DeleteMapping(path = "delete-booked/{id}")
    public ModelAndView deleteBookedExaminationAppeal(@AuthenticationPrincipal Student student, @PathVariable Long id) {
        try {
            examinationAppealService.removeStudentFromAppeal(id, student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/delete-booked-result");
        } catch (IllegalArgumentException e) {
            return new ModelAndView(illegalArgumentExceptionUri + ILLEGAL_PARAMETER, EXCEPTION_MESSAGE, e.getMessage());
        } catch (NoSuchElementException e) {
            return new ModelAndView(notFoundExceptionUri + OBJECT_NOT_FOUND, EXCEPTION_MESSAGE, e.getMessage());
        } catch (DataAccessServiceException e) {
            return new ModelAndView(dataAccessExceptionUri, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


}
