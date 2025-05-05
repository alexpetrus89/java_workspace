package com.alex.universitymanagementsystem.service.impl;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.ExaminationOutcome;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
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
    private final StudentRepository studentRepository;

    public ExaminationOutcomeServiceImpl(
        ExaminationOutcomeRepository examinationOutcomeRepository,
        ExaminationAppealRepository examinationAppealRepository,
        StudentRepository studentRepository
    ) {
        this.examinationOutcomeRepository = examinationOutcomeRepository;
        this.examinationAppealRepository = examinationAppealRepository;
        this.studentRepository = studentRepository;
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
    public ExaminationOutcome getOutcomeByCourseAndStudent(@NonNull String course, @NonNull String register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {
        try {
            return examinationOutcomeRepository.findByCourseAndRegister(course, register);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }


    /**
     * Get a list of examination outcomes by professor unique code
     * @param course name of the course
     * @param uniqueCode of the professor owner of the examination appeal
     * @return List<ExaminationOutcome> outcomes
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    @Override
    public List<ExaminationOutcome> getOutcomesByCourseAndProfessor(@NonNull String course, @NonNull UniqueCode uniqueCode)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {
        try {
            return examinationOutcomeRepository.findByCourseAndUniqueCode(course, uniqueCode);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return Collections.emptyList();
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

            if(!examinationOutcomeRepository.existsById(outcome.getId()))
                throw new ObjectAlreadyExistsException(DomainType.EXAMINATION_OUTCOME);

            return examinationOutcomeRepository.saveAndFlush(outcome);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }


    @Override
    public ExaminationOutcome deleteExaminationOutcome(@NonNull ExaminationOutcome outcome) throws NullPointerException,
            IllegalArgumentException, ObjectNotFoundException, UnsupportedOperationException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteExaminationOutcome'");
    }

}
