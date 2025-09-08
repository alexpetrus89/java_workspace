package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alex.universitymanagementsystem.component.ServiceHelpers;
import com.alex.universitymanagementsystem.component.validator.ServiceValidators;
import com.alex.universitymanagementsystem.dto.ExaminationOutcomeDto;
import com.alex.universitymanagementsystem.entity.Course;
import com.alex.universitymanagementsystem.entity.ExaminationAppeal;
import com.alex.universitymanagementsystem.entity.ExaminationOutcome;
import com.alex.universitymanagementsystem.entity.Student;
import com.alex.universitymanagementsystem.entity.immutable.Register;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ExaminationOutcomeMapper;
import com.alex.universitymanagementsystem.repository.ExaminationOutcomeRepository;
import com.alex.universitymanagementsystem.service.ExaminationAppealService;
import com.alex.universitymanagementsystem.service.ExaminationOutcomeService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class ExaminationOutcomeServiceImpl implements ExaminationOutcomeService {

	// constants
    private static final String COURSE_ERROR = "Course name cannot be null or empty";
    private static final String REGISTER_ERROR = "Register cannot be null or empty";
    private static final String DATA_ACCESS_ERROR = "data access error";

    // instance variables
    private final ExaminationOutcomeRepository examinationOutcomeRepository;
    private final ExaminationAppealService examinationAppealService;
    private final ServiceHelpers helpers;
    private final ServiceValidators validators;

    public ExaminationOutcomeServiceImpl(
        ExaminationOutcomeRepository examinationOutcomeRepository,
        ExaminationAppealService examinationAppealService,
        ServiceHelpers helpers,
        ServiceValidators validators
    ) {
        this.examinationOutcomeRepository = examinationOutcomeRepository;
        this.examinationAppealService = examinationAppealService;
        this.helpers = helpers;
        this.validators = validators;
    }


    /**
     * retrive outcome by id
     * @param id of the examination outcome
     * @return ExaminationOutcomeDto
     * @throws ObjectNotFoundException if the outcome does not exist
     * @throws DataAccessServiceException if there is a data access error
     */
    @Override
    public ExaminationOutcomeDto getOutcomeById(Long id)
        throws ObjectNotFoundException, DataAccessServiceException {
        try {
            return helpers.mapOutcomeToDto(helpers.fetchExaminationOutcome(id));
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Get an examination outcome by student register and course
     * @param course name of the course
     * @param register of the student
     * @param LocalDate date of the examination
     * @return ExaminationOutcomeDto outcome
     * @throws IllegalArgumentException if the course name or register is invalid
     * @throws ObjectNotFoundException if the outcome does not exist
     * @throws DataAccessServiceException if there is a data access error
     */
    @Override
    public ExaminationOutcomeDto getOutcomeByCourseAndStudent(String courseName, String register)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
        // sanity checks
        validators.validateNotNullOrNotBlank(courseName, COURSE_ERROR);
        validators.validateNotNullOrNotBlank(register, REGISTER_ERROR);

        try {
            Student student = helpers.fetchStudent(register);
            Course course = helpers.fetchCourse(courseName, student.getDegreeCourse().getName());

            ExaminationOutcome outcome =  examinationOutcomeRepository
                .findByAppeal_CourseAndRegister(course, register)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.EXAMINATION_OUTCOME));

            return helpers.mapOutcomeToDto(outcome);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Get an examination outcomes by student register
     * @param register of the student
     * @return List of examination outcomes
     * @throws ObjectNotFoundException if the student does not exist
     * @throws DataAccessServiceException if there is a data access error
     */
    @Override
    public List<ExaminationOutcomeDto> getStudentOutcomes(String register)
        throws ObjectNotFoundException, DataAccessServiceException
    {
        validators.validateStudentExists(new Register(register));

        try {
            return examinationOutcomeRepository
                .findByRegister(register)
                .stream()
                .map(helpers::mapOutcomeToDto)
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Save an examination outcome
     * @param ExaminationOutcomeDto data transfer object
     * @return ExaminationOutcome outcome
     * @throws NoSuchElementException if the student does not exist
     * @throws ObjectNotFoundException if the appeal does not exist
     * @throws ObjectAlreadyExistsException if the outcome already exists
     * @throws DataAccessServiceException if there is a data access error
     */
    @Override
    @Transactional(rollbackOn = {
        NoSuchElementException.class,
        ObjectNotFoundException.class,
        ObjectAlreadyExistsException.class
    })
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Optional<ExaminationOutcomeDto> addNewExaminationOutcome(@Valid ExaminationOutcomeDto dto)
        throws NoSuchElementException, ObjectNotFoundException, ObjectAlreadyExistsException, DataAccessServiceException {

        Register studentRegister = new Register(dto.getRegister());

        // sanity checks
        validators.validateStudentExists(studentRegister);
        validators.validateExaminationOutcomeAlreadyExists(dto.getAppeal().getId(), dto.getRegister());

        try {
            ExaminationAppeal appeal = helpers.fetchExaminationAppeal(dto.getAppeal().getId());
            ExaminationOutcome outcome = ExaminationOutcomeMapper.toEntity(dto, appeal);

            // if the outcome is created successfully, save the outcome and return Optional.of(dto)
            if (outcome != null) {
                examinationOutcomeRepository.saveAndFlush(outcome);
                examinationAppealService.removeStudentFromAppeal(dto.getAppeal().getId(), studentRegister);
                return Optional.of(dto);
            }

            // se l'outcome non è stato creato con successo, ritorno Optional.empty()
            examinationAppealService.removeStudentFromAppeal(dto.getAppeal().getId(), studentRegister);
            return Optional.empty();

        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }



    /**
     * Delete an examination outcome
     * @param id of the examination outcome
     * @return ExaminationOutcomeDto outcome
     * @throws ObjectNotFoundException if the outcome does not exist
     * @throws DataAccessServiceException if there is a data access error
     */
    @Override
    @Transactional(rollbackOn = ObjectNotFoundException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ExaminationOutcomeDto deleteExaminationOutcome(Long id)
        throws ObjectNotFoundException, DataAccessServiceException {
        try {
            ExaminationOutcome outcome = helpers.fetchExaminationOutcome(id);
            examinationOutcomeRepository.delete(outcome);
            return helpers.mapOutcomeToDto(outcome);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }



    @Scheduled(fixedDelay = 1209600000) // ogni due settimane
    public void cleanExpiredOutcomes() throws DataAccessServiceException {
        LocalDate today = LocalDate.now();
        LocalDate expirationDateTwoWeeks = today.minusWeeks(2);

        try {
            List<ExaminationOutcome> expiredExaminationOutcomes = examinationOutcomeRepository
                .findByAppeal_DateBefore(expirationDateTwoWeeks);

            expiredExaminationOutcomes
                .stream()
                .forEach(outcome -> {
                    if (outcome.getGrade() > 18) {
                        // Chiamata all'URL per creare l'esame
                        String register = outcome.getRegister();
                        String courseName = outcome.getAppeal().getCourse().getName();
                        String degreeCourseName = outcome.getAppeal().getCourse().getDegreeCourse().getName();
                        String grade = String.valueOf(outcome.getGrade());
                        Boolean withHonors = outcome.isWithHonors();
                        LocalDate date = outcome.getAppeal().getDate();

                        RestTemplate restTemplate = new RestTemplate();
                        String url = "http://localhost:8081/api/v1/examination/create";
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                        map.add("register", register);
                        map.add("courseName", courseName);
                        map.add("degreeCourseName", degreeCourseName);
                        map.add("grade", grade);
                        map.add("withHonors", String.valueOf(withHonors));
                        map.add("date", date.toString());

                        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
                        restTemplate.postForObject(url, request, String.class);
                    }

                examinationOutcomeRepository.delete(outcome);
            });
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


}
