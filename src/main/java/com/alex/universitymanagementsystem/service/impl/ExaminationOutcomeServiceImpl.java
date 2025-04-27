package com.alex.universitymanagementsystem.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.ExaminationOutcome;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.repository.ExaminationAppealRepository;
import com.alex.universitymanagementsystem.repository.ExaminationOutcomeRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class ExaminationOutcomeServiceImpl {

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

}
