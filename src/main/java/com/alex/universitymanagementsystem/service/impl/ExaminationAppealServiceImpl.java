package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.domain.immutable.DegreeCourseId;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationAppealRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.service.ExaminationAppealService;



@Service
public class ExaminationAppealServiceImpl implements ExaminationAppealService {

    // logger
	private static final Logger logger = LoggerFactory.getLogger(ExaminationAppealServiceImpl.class);

	// constants
    private static final String REGISTER_BLANK_ERROR = "Register cannot be empty";
	private static final String DATA_ACCESS_ERROR = "data access error";
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
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if the register is blank
     * @throws UnsupportedOperationException if the register is not unique
     */
    @Override
    public List<ExaminationAppeal> getExaminationAppealByStudent(@NonNull Register register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {

        if(register.toString().isBlank())
            throw new IllegalArgumentException(REGISTER_BLANK_ERROR);

        if(!studentRepository.existsByRegister(register))
            throw new IllegalArgumentException(STUDENT_NOT_EXIST);

        try {
            List<UUID> courseIds = studentRepository
                .findByRegister(register)
                .getStudyPlan()
                .getCourses()
                .stream()
                .map(Course::getCourseId)
                .map(CourseId::id)
                .toList();

            return examinationAppealRepository.findByIdIn(courseIds);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return Collections.emptyList();
        }
    }


    /**
     * Adds a new examination appeal
     * @param courseName
     * @param degreeCourseName
     * @param professor
     * @param description
     * @param date
     * @return examinationAppeal
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws ObjectNotFoundException if the degree course or professor
     *         does not exist
     * @throws UnsupportedOperationException if the register is not unique
     */
    @Override
    public ExaminationAppeal addNewExaminationAppeal(
        @NonNull String courseName,
        @NonNull String degreeCourseName,
        @NonNull Professor professor,
        @NonNull String description,
        @NonNull LocalDate date
    ) throws NullPointerException, IllegalArgumentException, ObjectNotFoundException
    {
        // sanity checks
        if(courseName.isBlank())
            throw new IllegalArgumentException("Course name cannot be empty");

        if(degreeCourseName.isBlank())
            throw new IllegalArgumentException("Degree course name cannot be empty");

        if(date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Date cannot be in the past");

        if(!degreeCourseRepository.existsByName(degreeCourseName))
            throw new ObjectNotFoundException(DomainType.DEGREE_COURSE);

        if(!professorRepository.existsByUniqueCode(professor.getUniqueCode()))
            throw new ObjectNotFoundException(DomainType.PROFESSOR);

        try {
            DegreeCourseId degreeCourseId = degreeCourseRepository
                .findByName(degreeCourseName.toUpperCase())
                .getId();

            Course course = courseRepository.findByNameAndDegreeCourse(courseName, degreeCourseId);
            ExaminationAppeal exam = new ExaminationAppeal(course, description, date);

            return examinationAppealRepository.saveAndFlush(exam);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }


    /**
     * Adds a student to an examination appeal
     * @param id examination appeal ids
     * @param register student register
     * @return examinationAppeal
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws UnsupportedOperationException if the register is not unique
     */
    @Override
    public ExaminationAppeal bookExaminationAppeal(@NonNull Long id, @NonNull Register register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {
        if(id.toString().isBlank())
            throw new IllegalArgumentException("Id cannot be empty");

        if(register.toString().isBlank())
            throw new IllegalArgumentException(REGISTER_BLANK_ERROR);

        if(!studentRepository.existsByRegister(register))
            throw new IllegalArgumentException(STUDENT_NOT_EXIST);

        try {
            ExaminationAppeal exam = examinationAppealRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Examination Appeal does not exist"));
            exam.addStudent(register);
            return examinationAppealRepository.saveAndFlush(exam);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }

    /**
     * Removes a student from an examination appeal
     * @param id examination appeal ids
     * @param register student register
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     * @throws IllegalStateException
     */
    @Override
    public void deleteBookedExaminationAppeal(@NonNull Long id, @NonNull Register register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException, IllegalStateException
    {
        if(id.toString().isBlank())
            throw new IllegalArgumentException("Id cannot be empty");

        if(register.toString().isBlank())
            throw new IllegalArgumentException(REGISTER_BLANK_ERROR);

        if(!studentRepository.existsByRegister(register))
            throw new IllegalArgumentException(STUDENT_NOT_EXIST);

        try {
            ExaminationAppeal exam = examinationAppealRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Examination Appeal does not exist"));
            exam.removeStudent(register);
            examinationAppealRepository.saveAndFlush(exam);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
        }
    }

}
