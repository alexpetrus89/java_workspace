package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.ExaminationOutcome;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.ExaminationAppealDto;
import com.alex.universitymanagementsystem.dto.ExaminationOutcomeDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ExaminationAppealMapper;
import com.alex.universitymanagementsystem.mapper.ExaminationOutcomeMapper;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationAppealRepository;
import com.alex.universitymanagementsystem.repository.ExaminationOutcomeRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.service.ExaminationAppealService;
import com.alex.universitymanagementsystem.service.ExaminationOutcomeService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class ExaminationOutcomeServiceImpl implements ExaminationOutcomeService {

	// constants
	private static final String DATA_ACCESS_ERROR = "data access error";

    // instance variables
    private final ExaminationOutcomeRepository examinationOutcomeRepository;
    private final ExaminationAppealRepository examinationAppealRepository;
    private final ExaminationAppealService examinationAppealService;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public ExaminationOutcomeServiceImpl(
        ExaminationOutcomeRepository examinationOutcomeRepository,
        ExaminationAppealRepository examinationAppealRepository,
        ExaminationAppealService examinationAppealService,
        StudentRepository studentRepository,
        CourseRepository courseRepository
    ) {
        this.examinationOutcomeRepository = examinationOutcomeRepository;
        this.examinationAppealRepository = examinationAppealRepository;
        this.examinationAppealService = examinationAppealService;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
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
            ExaminationOutcome outcome =  examinationOutcomeRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("ExaminationOutcome with id " + id + " not found"));
            return mapToDto(outcome);
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
        if (courseName == null || courseName.isBlank())
            throw new IllegalArgumentException("Course name must not be blank");

        if (register == null || register.isBlank())
            throw new IllegalArgumentException("Student register must not be blank");

        try {
            Student student = studentRepository
                .findByRegister(new Register(register))
                .orElseThrow(() -> new ObjectNotFoundException("Student not found with register: " + register));

            Course course = courseRepository
                .findByNameAndDegreeCourseName(courseName, student.getDegreeCourse().getName())
                .orElseThrow(() -> new ObjectNotFoundException("Course not found for student"));

            ExaminationOutcome outcome =  examinationOutcomeRepository
                .findByAppeal_CourseAndRegister(course, register)
                .orElseThrow(() -> new ObjectNotFoundException("ExaminationOutcome not found"));

            return mapToDto(outcome);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Get an examination outcomes by student register
     * @param register of the student
     * @return List of examination outcomes
     * @throws NoSuchElementException if the student does not exist
     * @throws DataAccessServiceException if there is a data access error
     */
    @Override
    public List<ExaminationOutcomeDto> getStudentOutcomes(String register)
        throws NoSuchElementException, DataAccessServiceException
    {
        try {
            // sanity checks
            if(!studentRepository.existsByRegister(new Register(register)))
                throw new NoSuchElementException("student does not exist");

            List<ExaminationOutcome> outcomes =  examinationOutcomeRepository.findByRegister(register);
            return outcomes
                .stream()
                .map(this::mapToDto)
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

        // verifica se lo studente esiste
        if (!studentRepository.existsByRegister(studentRegister))
            throw new NoSuchElementException("Student does not exist");

        // verifica se l'esito esiste già
        if (examinationOutcomeRepository.existsByIdAndRegister(dto.getAppeal().getId(), dto.getRegister()))
            throw new ObjectAlreadyExistsException(DomainType.EXAMINATION_OUTCOME);

        try {
            // recupera l'appello
            ExaminationAppeal appeal = examinationAppealRepository
                .findById(dto.getAppeal().getId())
                .orElseThrow(() -> new ObjectNotFoundException("Appeal not found"));

            ExaminationOutcome outcome = ExaminationOutcomeMapper.toEntity(dto, appeal);

            // se l'outcome è stato creato con successo, salva l'esito e ritorna Optional.of(dto)
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
            ExaminationOutcome outcome = examinationOutcomeRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.EXAMINATION_OUTCOME));

            examinationOutcomeRepository.delete(outcome);
            return mapToDto(outcome);
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


    private ExaminationOutcomeDto mapToDto(ExaminationOutcome outcome) {
        ExaminationAppeal appeal = outcome.getAppeal();
        Set<StudentDto> students = studentRepository
            .findByRegisterIn(appeal.getRegisters())
            .stream()
            .map(StudentMapper::toDto)
            .collect(Collectors.toSet());
        ExaminationAppealDto dto = ExaminationAppealMapper.toDto(appeal, students);
        return ExaminationOutcomeMapper.toDto(outcome, dto);
    }


}
