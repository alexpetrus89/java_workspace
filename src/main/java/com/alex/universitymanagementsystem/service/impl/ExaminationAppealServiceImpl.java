package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.domain.immutable.DegreeCourseId;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationAppealRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;

@Service
public class ExaminationAppealServiceImpl {

    // instance variables
    private final ExaminationAppealRepository examinationAppealRepository;
    private final CourseRepository courseRepository;
    private final DegreeCourseRepository degreeCourseRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;

    // constructor
    public ExaminationAppealServiceImpl(
        ExaminationAppealRepository examinationAppealRepository,
        CourseRepository courseRepository,
        DegreeCourseRepository degreeCourseRepository,
        ProfessorRepository professorRepository,
        StudentRepository studentRepository
    ) {
        this.examinationAppealRepository = examinationAppealRepository;
        this.courseRepository = courseRepository;
        this.degreeCourseRepository = degreeCourseRepository;
        this.professorRepository = professorRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Retrieves all examination appeals
     * @return a list of examination appeals
     */
    public List<ExaminationAppeal> getExaminationAppeals() {
        return examinationAppealRepository.findAll();
    }


    /**
     * Retrieves all examination appeals for a student
     * @param register
     * @return a list of examination appeals
     */
    public List<ExaminationAppeal> getExaminationAppealByStudent(Register register) {

        List<UUID> courseIds = studentRepository
            .findByRegister(register)
            .getStudyPlan()
            .getCourses()
            .stream()
            .map(Course::getCourseId)
            .map(CourseId::id)
            .toList();

        return examinationAppealRepository.findByIdIn(courseIds);
    }



    /**
     * Adds a new examination appeal
     * @param courseName
     * @param degreeCourseName
     * @param professor
     * @param description
     * @param date
     * @return examinationAppeal
     * @throws IllegalArgumentException
     */
    public ExaminationAppeal addNewExaminationAppeal(String courseName, String degreeCourseName, Professor professor, String description, LocalDate date) {

        if(!professorRepository.existsByUniqueCode(professor.getUniqueCode()))
            throw new IllegalArgumentException("Professor does not exist");

        DegreeCourseId degreeCourseId = degreeCourseRepository
            .findByName(degreeCourseName.toUpperCase())
            .getId();

        Course course = courseRepository.findByNameAndDegreeCourse(courseName, degreeCourseId);
        ExaminationAppeal exam = new ExaminationAppeal(course, description, date);

        return examinationAppealRepository.saveAndFlush(exam);
    }

}
