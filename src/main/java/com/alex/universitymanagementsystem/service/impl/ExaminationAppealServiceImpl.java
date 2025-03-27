package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.lang.NonNull;
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
import com.alex.universitymanagementsystem.service.ExaminationAppealService;



@Service
public class ExaminationAppealServiceImpl implements ExaminationAppealService {

    // constants
    private static final String STUDENT_NOT_EXIST = "Student does not exist";

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
    @Override
    public List<ExaminationAppeal> getExaminationAppeals() {
        return examinationAppealRepository.findAll();
    }


    /**
     * Retrieves all examination appeals for a student
     * @param register
     * @return a list of examination appeals
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws NullPointerExceptionS
     */
    @Override
    public List<ExaminationAppeal> getExaminationAppealByStudent(@NonNull Register register) {

        if(!studentRepository.existsByRegister(register))
            throw new IllegalArgumentException(STUDENT_NOT_EXIST);

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
     * @throws UnsupportedOperationException
     * @throws NullPointerException
     * @throws IllegalStateException
     */
    @Override
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


    /**
     * Adds a student to an examination appeal
     * @param id examination appeal ids
     * @param register student register
     * @return examinationAppeal
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws NullPointerException
     */
    @Override
    public ExaminationAppeal bookExaminationAppeal(@NonNull Long id, @NonNull Register register) {

        if(!studentRepository.existsByRegister(register))
            throw new IllegalArgumentException(STUDENT_NOT_EXIST);

        ExaminationAppeal exam = examinationAppealRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Examination Appeal does not exist"));

        exam.addStudent(register);

        return examinationAppealRepository.saveAndFlush(exam);
    }

    /**
     * Removes a student from an examination appeal
     * @param id examination appeal ids
     * @param register student register
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws NullPointerException
     * @throws IllegalStateException
     */
    @Override
    public void deleteBookedExaminationAppeal(@NonNull Long id, @NonNull Register register) {

        if(!studentRepository.existsByRegister(register))
            throw new IllegalArgumentException(STUDENT_NOT_EXIST);

        ExaminationAppeal exam = examinationAppealRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Examination Appeal does not exist"));

        exam.removeStudent(register);

        examinationAppealRepository.saveAndFlush(exam);
    }

}
