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
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ExaminationDto;
import com.alex.universitymanagementsystem.dto.ExaminationOutcomeDto;
import com.alex.universitymanagementsystem.dto.UpdateExaminationDto;
import com.alex.universitymanagementsystem.service.ExaminationOutcomeService;
import com.alex.universitymanagementsystem.service.ExaminationService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/examination")
public class ExaminationController {

    // constants
    private static final String EXAMINATION = "examination";
    private static final String EXAMINATIONS = "examinations";
    private static final String EXAMINATIONS_LIST = "user_admin/examination/examination-list";


    // instance variable
    private final ExaminationService examinationService;
    private final ExaminationOutcomeService examinationOutcomeService;

    // autowired - dependency injection - constructor
    public ExaminationController(
        ExaminationService examinationService,
        ExaminationOutcomeService examinationOutcomeService
    ) {
        this.examinationService = examinationService;
        this.examinationOutcomeService = examinationOutcomeService;
    }


    // methods
    /**
     * Returns a list of examinations
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
    public ModelAndView getExaminations() {
        List<ExaminationDto> examinations = examinationService.getExaminations();
        return new ModelAndView(EXAMINATIONS_LIST, EXAMINATIONS, examinations);
    }


    /**
     * Returns a list of examinations by course name
     * @param courseName name of the course
     * @param degreeCourseName name of the degree course
     * @return ModelAndView
     */
    @GetMapping(path = "/course-name")
    public ModelAndView getExaminationsByCourseAndDegreeCourse(@RequestParam String courseName, @RequestParam String degreeCourseName) {
        List<ExaminationDto> examinations = examinationService.getExaminationsByCourseNameAndDegreeCourseName(courseName, degreeCourseName);
        return new ModelAndView(EXAMINATIONS_LIST, EXAMINATIONS, examinations);
    }


    /**
     * Returns a list of examinations by student register
     * @param register register of the student
     * @return ModelAndView
     */
    @GetMapping(path = "/student-register")
    public ModelAndView getExaminationsByStudentRegister(
        @AuthenticationPrincipal Student student,
        @Valid @RequestParam(required = false) String register
    ) {
        Register studRegister = student != null ? student.getRegister() : new Register(register);

        List<ExaminationDto> examinations = examinationService.getExaminationsByStudentRegister(studRegister);
        return new ModelAndView("user_student/examinations/examinations", EXAMINATIONS, examinations);
    }


    /**
     * Returns a list of examinations by professor unique code
     * @param uniqueCode unique code of the professor
     * @return ModelAndView
     */
    @GetMapping(path = "/professor-unique-code")
    public ModelAndView getExaminationsByProfessorUniqueCode(@RequestParam String uniqueCode) {
        List<ExaminationDto> examinations = examinationService.getExaminationsByProfessorUniqueCode(new UniqueCode(uniqueCode));
        return new ModelAndView(EXAMINATIONS_LIST, EXAMINATIONS, examinations);
    }


    /**
     * Creates a new Examination
     * @return ModelAndView
     */
    @GetMapping("/create")
    public ModelAndView instantiateExaminationForCreate() {
        return new ModelAndView("user_admin/examination/create/create", EXAMINATION, new Examination());
    }


    /**
     * Update Examination
     * @return ModelAndView
     */
    @GetMapping("/update")
    public ModelAndView instantiateExaminationForUpdate() {
        return new ModelAndView("user_admin/examination/update/update", EXAMINATION, new Examination());
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
        @RequestParam String courseCfu,
        @RequestParam String grade,
        @RequestParam Boolean withHonors,
        @RequestParam LocalDate date
    ) {
        ExaminationDto examination = new ExaminationDto(register, courseName, degreeCourseName, Integer.valueOf(courseCfu), Integer.valueOf(grade), withHonors, date);
        examinationService.addNewExamination(examination);
        ExaminationOutcomeDto outcome = examinationOutcomeService.getOutcomeByCourseAndStudent(courseName, register);
        examinationOutcomeService.deleteExaminationOutcome(outcome.getId());
        return new ModelAndView("user_admin/examination/create/create-result", EXAMINATION, examination);
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
        @RequestParam String grade,
        @RequestParam Boolean withHonors,
        @RequestParam LocalDate date
    ) {
        UpdateExaminationDto dto = new UpdateExaminationDto();
        dto.setOldRegister(oldRegister);
        dto.setOldCourseName(oldCourseName);
        dto.setOldDegreeCourseName(oldDegreeCourseName);
        dto.setNewRegister(newRegister);
        dto.setNewCourseName(newCourseName);
        dto.setNewDegreeCourseName(newDegreeCourseName);
        dto.setGrade(grade);
        dto.setWithHonors(withHonors);
        dto.setDate(date);
        ExaminationDto examination = examinationService.updateExamination(dto);
        return new ModelAndView("user_admin/examination/create/create-result", EXAMINATION, examination);
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
        @RequestParam String courseName,
        @RequestParam String degreeCourseName
    ) {

        examinationService.deleteExamination(
            register.toLowerCase(),
            courseName.toLowerCase(),
            degreeCourseName.toUpperCase()
        );
        return new ModelAndView("user_admin/examination/delete/delete-result");
    }


}
