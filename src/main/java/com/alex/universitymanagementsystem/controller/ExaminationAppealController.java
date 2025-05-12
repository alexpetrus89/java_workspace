package com.alex.universitymanagementsystem.controller;

import java.time.LocalDate;
import java.util.List;

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
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.MakeAppealDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.service.impl.CourseServiceImpl;
import com.alex.universitymanagementsystem.service.impl.ExaminationAppealServiceImpl;
import com.alex.universitymanagementsystem.service.impl.StudentServiceImpl;

@RestController
@RequestMapping(path = "api/v1/examination-appeal")
public class ExaminationAppealController {

    // constants
    private static final String EXAMINATION_APPEAL = "examinationAppeal";
    private static final String EXAMINATION_APPEALS = "examinationAppeals";
    private static final String EXCEPTION_VIEW_NAME = "exception/read/error";
    private static final String EXCEPTION_MESSAGE = "message";

    // instance variables
    private final ExaminationAppealServiceImpl  examinationAppealServiceImpl;
    private final StudentServiceImpl studentServiceImpl;
    private final CourseServiceImpl courseServiceImpl;

    // constructor
    public ExaminationAppealController(
        ExaminationAppealServiceImpl examinationAppealService,
        StudentServiceImpl studentServiceImpl,
        CourseServiceImpl courseServiceImpl
    ) {
        this.examinationAppealServiceImpl = examinationAppealService;
        this.studentServiceImpl = studentServiceImpl;
        this.courseServiceImpl = courseServiceImpl;
    }

    /**
     * Retrieves all examination appeals for a student
     * @return ModelAndView
     */
    @GetMapping(path = "/available/student")
    public ModelAndView getExaminationAppealsAvailableForStudent(@AuthenticationPrincipal Student student) {
        try {
            List<ExaminationAppeal> examAppeals = examinationAppealServiceImpl.getExaminationAppealsAvailable(student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/available-calendar",EXAMINATION_APPEALS, examAppeals);
        } catch (NullPointerException | IllegalArgumentException | UnsupportedOperationException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Retrieves all examination appeals for a student
     * @return ModelAndView
     */
    @GetMapping(path = "/booked/student")
    public ModelAndView getExaminationAppealsBookedByStudent(@AuthenticationPrincipal Student student) {
        try {
            List<ExaminationAppeal> examAppeals = examinationAppealServiceImpl.getExaminationAppealsBooked(student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/booked-calendar", EXAMINATION_APPEALS, examAppeals);
        } catch (NullPointerException | IllegalArgumentException | UnsupportedOperationException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Retrieves all examination appeals for a professor
     * @return ModelAndView
     */
    @GetMapping(path = "/view/professor")
    public ModelAndView getExaminationAppealsMakeByProfessor(@AuthenticationPrincipal Professor professor) {
        try {
            List<ExaminationAppeal> examAppeals = examinationAppealServiceImpl.getExaminationAppealsByProfessor(professor.getUniqueCode());
            return new ModelAndView("user_professor/examinations/examination_appeal/calendar", EXAMINATION_APPEALS, examAppeals);
        } catch (NullPointerException | IllegalArgumentException | ObjectNotFoundException | UnsupportedOperationException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
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
            List<StudentDto> students = examinationAppealServiceImpl
                .getExaminationAppealById(id)
                .getStudents()
                .stream()
                .map(studentServiceImpl::getStudentByRegister)
                .toList();

            MakeAppealDto appeal = new MakeAppealDto(id, date, students);
            return new ModelAndView("user_professor/examinations/examination_appeal/students-booked", "appeal", appeal);
        } catch (NullPointerException | IllegalArgumentException | ObjectNotFoundException | UnsupportedOperationException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * methods for courses functionality
     * @param Professor professor
     * @return ModelAndView
     */
    @GetMapping(path = "/make")
    public ModelAndView getProfessorCourses(@AuthenticationPrincipal Professor professor) {
        List<CourseDto> courses = courseServiceImpl.getCoursesByProfessor(professor);
        return new ModelAndView("user_professor/examinations/examination_appeal/create/create-examination-appeal", "courses", courses);
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
            List<ExaminationAppeal> appeals = examinationAppealServiceImpl.getExaminationAppealsByProfessor(professor.getUniqueCode());
            return new ModelAndView("user_professor/examinations/examination_appeal/delete/delete-examination-appeal", "appeals", appeals);
        } catch (NullPointerException | IllegalArgumentException | UnsupportedOperationException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


    /**
     * Create new examination appeal
     * @param Professor professor owners of the examination appeal
     * @param String course id
     * @param String description of the examination appeal
     * @param LocalDate date of the examination appeal
     */
    @PostMapping(path = "/create")
    public ModelAndView createNewExaminationAppeal(
        @AuthenticationPrincipal Professor professor,
        @RequestParam String courseId,
        @RequestParam String description,
        @RequestParam LocalDate date
    ) {

        try {
            ExaminationAppeal examAppeal = examinationAppealServiceImpl.addNewExaminationAppeal(new CourseId(courseId), professor, description, date);
            return new ModelAndView("user_professor/examinations/examination_appeal/create/create-result", EXAMINATION_APPEAL, examAppeal);
        } catch (IllegalArgumentException e) {
            return new ModelAndView("exception/examination_appeal/invalid-parameter", "error", e.getMessage());
        } catch (NullPointerException | ObjectNotFoundException | UnsupportedOperationException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
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
            return new ModelAndView(
                "user_professor/examinations/examination_appeal/delete/delete-result",
                "result",
                examinationAppealServiceImpl.deleteExaminationAppeal(professor, id) ?
                    "appeal delete successfully" : "appeal not deleted"
            );
        } catch (NullPointerException | IllegalArgumentException | ObjectNotFoundException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
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
            ExaminationAppeal examAppeal = examinationAppealServiceImpl.addStudentToAppeal(id, student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/booked-result", EXAMINATION_APPEAL, examAppeal);
        } catch (NullPointerException | IllegalArgumentException | UnsupportedOperationException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
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
            examinationAppealServiceImpl.removeStudentFromAppeal(id, student.getRegister());
            return new ModelAndView("user_student/examinations/examination_appeal/delete-booked-result");
        } catch (NullPointerException | IllegalArgumentException | IllegalStateException | UnsupportedOperationException e) {
            return new ModelAndView(EXCEPTION_VIEW_NAME, EXCEPTION_MESSAGE, e.getMessage());
        }
    }


}
