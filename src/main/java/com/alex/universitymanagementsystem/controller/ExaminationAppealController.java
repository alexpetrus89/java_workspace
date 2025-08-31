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

import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.ExaminationAppealDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.mapper.ProfessorMapper;
import com.alex.universitymanagementsystem.service.CourseService;
import com.alex.universitymanagementsystem.service.ExaminationAppealService;

@RestController
@RequestMapping(path = "api/v1/examination-appeal")
public class ExaminationAppealController {

    // constants
    private static final String EXAMINATION_APPEAL = "appeal";
    private static final String EXAMINATION_APPEALS = "appeals";

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
     * @param student
     * @return ModelAndView
     */
    @GetMapping(path = "/available/student")
    public ModelAndView getExaminationAppealsAvailableForStudent(@AuthenticationPrincipal Student student) {
        List<ExaminationAppealDto> appeals = examinationAppealService.getExaminationAppealsAvailable(student.getRegister());
        return new ModelAndView("user_student/examinations/examination_appeal/available-calendar", EXAMINATION_APPEALS, appeals);
    }


    /**
     * Retrieves all examination appeals for a student
     * @param student
     * @return ModelAndView
     */
    @GetMapping(path = "/booked/student")
    public ModelAndView getExaminationAppealsBookedByStudent(@AuthenticationPrincipal Student student) {
        List<ExaminationAppealDto> appeals = examinationAppealService.getExaminationAppealsBookedByStudent(student.getRegister());
        return new ModelAndView("user_student/examinations/examination_appeal/booked-calendar", EXAMINATION_APPEALS, appeals);
    }


    /**
     * Retrieves all examination appeals for a professor
     * @param professor
     * @return ModelAndView
     */
    @GetMapping(path = "/view/professor")
    public ModelAndView getExaminationAppealsMadeByProfessor(@AuthenticationPrincipal Professor professor) {
        List<ExaminationAppealDto> appeals = examinationAppealService.getExaminationAppealsMadeByProfessor(professor.getUniqueCode());
        return new ModelAndView("user_professor/examinations/examination_appeal/calendar", EXAMINATION_APPEALS, appeals);
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
        ExaminationAppealDto appeal = examinationAppealService.getExaminationAppealById(id);
        return new ModelAndView("user_professor/examinations/examination_appeal/students-booked", EXAMINATION_APPEAL, appeal);
    }


    /**
     * methods for courses functionality
     * @param Professor professor
     * @return ModelAndView
     */
    @GetMapping(path = "/make")
    public ModelAndView getProfessorCourses(@AuthenticationPrincipal Professor professor) {
        List<CourseDto> courses = courseService.getCoursesByProfessor(ProfessorMapper.toDto(professor));
        return new ModelAndView("user_professor/examinations/examination_appeal/create/create-examination-appeal", "courses", courses);
    }


    /**
     * Deletes an examination appeal
     * @param Professor professor
     * @return a ModelAndView object for the "user_student/study_plan/study_plan_modify"
     * view with the ChangeCoursesDto object as the model
     */
    @GetMapping(path = "/delete")
    public ModelAndView deleteExaminationAppeal(@AuthenticationPrincipal Professor professor) {
        List<ExaminationAppealDto> appeals = examinationAppealService.getExaminationAppealsMadeByProfessor(professor.getUniqueCode());
        return new ModelAndView("user_professor/examinations/examination_appeal/delete/delete-examination-appeal", EXAMINATION_APPEALS, appeals);
    }


    /**
     * Create new examination appeal
     * @param professor owners of the examination appeal
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
        @RequestParam String courseCfu,
        @RequestParam String description,
        @RequestParam LocalDate date
    ) {

        ExaminationAppealDto dto = new ExaminationAppealDto();
        dto.setCourse(courseName);
        dto.setDegreeCourse(degreeCourseName);
        dto.setCourseCfu(courseCfu);
        dto.setProfessorCode(professor.getUniqueCode().toString());
        dto.setDescription(description);
        dto.setDate(date);
        ExaminationAppealDto appeal = examinationAppealService.addNewExaminationAppeal(dto);
        return new ModelAndView("user_professor/examinations/examination_appeal/create/create-result", EXAMINATION_APPEAL, appeal);
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
        ProfessorDto professorDto = ProfessorMapper.toDto(professor);
        return new ModelAndView(
            "user_professor/examinations/examination_appeal/delete/delete-result",
            "result",
            examinationAppealService.deleteExaminationAppeal(professorDto, id) ?
                "appeal delete successfully" : "appeal not deleted"
        );
    }


    /**
     * Book an examination appeal
     * @param Student student
     * @param Long id
     * @return ModelAndView
     */
    @PostMapping(path = "/booked/{id}")
    public ModelAndView bookExaminationAppeal(@AuthenticationPrincipal Student student, @PathVariable Long id) {
        ExaminationAppealDto appeal = examinationAppealService.addStudentToAppeal(id, student.getRegister());
        return new ModelAndView("user_student/examinations/examination_appeal/booked-result", EXAMINATION_APPEAL, appeal);
    }


    /**
     * Delete an examination appeal reservation
     * @param Student student
     * @param Long id
     * @return ModelAndView
     */
    @DeleteMapping(path = "delete-booked/{id}")
    public ModelAndView deleteBookedExaminationAppeal(@AuthenticationPrincipal Student student, @PathVariable Long id) {
        examinationAppealService.removeStudentFromAppeal(id, student.getRegister());
        return new ModelAndView("user_student/examinations/examination_appeal/delete-booked-result");
    }


}
