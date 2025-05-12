package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

import jakarta.persistence.PersistenceException;
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
     * retrive outcome by id
     * @param id
     * @return ExaminationOutcome
     * @throws NullPointerException if the id is null
     * @throws IllegalArgumentException if the id is invalid
     */
    @Override
    public ExaminationOutcome getOutcomeById(@NonNull Long id)
        throws NullPointerException, IllegalArgumentException
    {
        return examinationOutcomeRepository
            .findById(id)
            .orElseThrow(null);
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
     * Get an examination outcomes by student register
     * @param register of the student
     * @return List of examination outcomes
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if the register is invalid or the student does not exist
     * @throws UnsupportedOperationException if the register is not unique
     */
    @Override
    public List<ExaminationOutcome> getStudentOutcomes(@NonNull String register)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException
    {
        try {
            // sanity checks
            if(!studentRepository.existsByRegister(new Register(register)))
                throw new IllegalArgumentException("student does not exist");

            return examinationOutcomeRepository.findByRegister(register);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return List.of();
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

            if(outcome.getGrade() < 0 || outcome.getGrade() > 30)
                throw new IllegalArgumentException("grade is not valid");

            if(outcome.isPresent())
                examinationOutcomeRepository.saveAndFlush(outcome);

            examinationAppealServiceImpl.removeStudentFromAppeal(outcome.getExaminationAppeal().getId(), new Register(outcome.getRegister()));
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



    @Scheduled(fixedDelay = 1209600000) // ogni due settimane
    public void cleanExpiredOutcomes() {
        LocalDate today = LocalDate.now();
        LocalDate expirationDateTwoWeeks = today.minusWeeks(2);

        try {
            List<ExaminationOutcome> expiredExaminationOutcomes = examinationOutcomeRepository
                .findByDateLessThan(expirationDateTwoWeeks);

            expiredExaminationOutcomes
                .stream()
                .forEach(outcome -> {
                    if (outcome.getGrade() > 18) {
                        // Chiamata all'URL per creare l'esame
                        String register = outcome.getRegister();
                        String courseName = outcome.getExaminationAppeal().getCourse().getName();
                        String degreeCourseName = outcome.getExaminationAppeal().getDegreeCourse();
                        String grade = String.valueOf(outcome.getGrade());
                        Boolean withHonors = outcome.isWithHonors();
                        LocalDate date = outcome.getExaminationAppeal().getDate();

                        RestTemplate restTemplate = new RestTemplate();
                        String url = "http://localhost:8080/api/v1/examination/create";
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
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
        } catch (NullPointerException | PersistenceException e) {
            logger.error("null pointer or persistence error", e);
        }
    }


}
