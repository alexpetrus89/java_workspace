package com.alex.universitymanagementsystem.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.ExaminationOutcome;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationAppealRepository;
import com.alex.universitymanagementsystem.repository.ExaminationOutcomeRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.service.ExaminationOutcomeService;

import jakarta.transaction.Transactional;

@Service
public class ExaminationOutcomeServiceImpl implements ExaminationOutcomeService {

    // logger
	private static final Logger logger = LoggerFactory.getLogger(ExaminationOutcomeServiceImpl.class);

	// constants
	private static final String DATA_ACCESS_ERROR = "data access error";

    // instance variable
    private final ExaminationOutcomeRepository examinationOutcomeRepository;
    private final ExaminationAppealRepository examinationAppealRepository;
    private final ExaminationAppealServiceImpl examinationAppealServiceImpl;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public ExaminationOutcomeServiceImpl(
        ExaminationOutcomeRepository examinationOutcomeRepository,
        ExaminationAppealRepository examinationAppealRepository,
        ExaminationAppealServiceImpl examinationAppealServiceImpl,
        StudentRepository studentRepository,
        CourseRepository courseRepository
    ) {
        this.examinationOutcomeRepository = examinationOutcomeRepository;
        this.examinationAppealRepository = examinationAppealRepository;
        this.examinationAppealServiceImpl = examinationAppealServiceImpl;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }


    /**
     * Get an examination outcome by student register and course
     * @param course name of the course
     * @param register of the student
     * @param LocalDate date of the examination
     * @return ExaminationOutcome outcome
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws UnsupportedOperationException if the register is not unique
     */
    @Override
    public ExaminationOutcome getOutcomeByCourseAndStudent(@NonNull String name, @NonNull String register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {
        try {
            Student student = studentRepository.findByRegister(new Register(register));
            Course course = courseRepository.findByNameAndDegreeCourse(name, student.getDegreeCourse());
            return examinationOutcomeRepository.findByCourseAndRegister(course, register);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }



    /**
     * Save an examination outcome
     * @param ExaminationOutcome outcome
     * @return ExaminationOutcome outcome
     * @throws NullPointerException
     * @throws IllegalArgumentException
     * @throws ObjectAlreadyExistsException
     */
    @Override
    @Transactional(rollbackOn = {NullPointerException.class, IllegalArgumentException.class, ObjectAlreadyExistsException.class})
    public ExaminationOutcome addNewExaminationOutcome(@NonNull ExaminationOutcome outcome)
        throws NullPointerException, IllegalArgumentException, ObjectAlreadyExistsException
    {
        try {

            // sanity checks
            if(!examinationAppealRepository.existsById(outcome.getExaminationAppeal().getId()))
                throw new IllegalArgumentException("examination appeal does not exist");

            if(!studentRepository.existsByRegister(new Register(outcome.getRegister())))
                throw new IllegalArgumentException("student does not exist");

            if(outcome.getId() != null && examinationOutcomeRepository.existsById(outcome.getId()))
                throw new ObjectAlreadyExistsException(DomainType.EXAMINATION_OUTCOME);

            if(outcome.isPresent())
                examinationOutcomeRepository.saveAndFlush(outcome);

            examinationAppealServiceImpl.deleteBookedExaminationAppeal(outcome.getExaminationAppeal().getId(), new Register(outcome.getRegister()));
            return outcome;
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }


    /**
     * Delete an examination outcome
     * @param outcome examination outcome of the student
     * @return ExaminationOutcome outcome
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws ObjectNotFoundException if the outcome does not exist
     * @throws UnsupportedOperationException if the outcome is not unique
     */
    @Override
    public ExaminationOutcome deleteExaminationOutcome(@NonNull ExaminationOutcome outcome)
        throws NullPointerException, IllegalArgumentException, ObjectNotFoundException, UnsupportedOperationException
    {
        try {
            examinationOutcomeRepository.delete(outcome);
            return outcome;
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }


}
