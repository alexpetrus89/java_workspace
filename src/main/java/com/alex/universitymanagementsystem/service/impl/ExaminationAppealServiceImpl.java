package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.component.ServiceHelpers;
import com.alex.universitymanagementsystem.component.validator.ServiceValidators;
import com.alex.universitymanagementsystem.dto.ExaminationAppealDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.entity.Course;
import com.alex.universitymanagementsystem.entity.Examination;
import com.alex.universitymanagementsystem.entity.ExaminationAppeal;
import com.alex.universitymanagementsystem.entity.ExaminationOutcome;
import com.alex.universitymanagementsystem.entity.Professor;
import com.alex.universitymanagementsystem.entity.immutable.CourseId;
import com.alex.universitymanagementsystem.entity.immutable.Register;
import com.alex.universitymanagementsystem.entity.immutable.UniqueCode;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ExaminationAppealMapper;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.ExaminationAppealRepository;
import com.alex.universitymanagementsystem.repository.ExaminationOutcomeRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.service.ExaminationAppealService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;



@Service
public class ExaminationAppealServiceImpl implements ExaminationAppealService {

	// constants
    private static final String REGISTER_ERROR = "Register cannot be null or empty";
    private static final String UNIQUE_CODE_ERROR = "Unique code cannot be null or empty";
    private static final String ID_ERROR = "Id cannot be null or empty";
    private static final String DATA_ACCESS_ERROR = "data access error";

    // instance variables
    private final ExaminationAppealRepository examinationAppealRepository;
    private final ExaminationOutcomeRepository examinationOutcomeRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;
    private final ServiceHelpers helpers;
    private final ServiceValidators validators;


    // constructor
    public ExaminationAppealServiceImpl(
        ExaminationAppealRepository examinationAppealRepository,
        ExaminationOutcomeRepository examinationOutcomeRepository,
        ProfessorRepository professorRepository,
        StudentRepository studentRepository,
        ServiceHelpers helpers,
        ServiceValidators validators
    ) {
        this.examinationAppealRepository = examinationAppealRepository;
        this.examinationOutcomeRepository = examinationOutcomeRepository;
        this.professorRepository = professorRepository;
        this.studentRepository = studentRepository;
        this.helpers = helpers;
        this.validators = validators;
    }


    /**
     * Retrieves all examination appeals
     * @return a list of examination appeals
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public List<ExaminationAppealDto> getExaminationAppeals() throws DataAccessServiceException {
        try {
            return examinationAppealRepository
                .findAll()
                .stream()
                .map(appeal -> {
                    Set<StudentDto> students = studentRepository
                        .findByRegisterIn(appeal.getRegisters())
                        .stream()
                        .map(StudentMapper::toDto)
                        .collect(Collectors.toSet());
                    return ExaminationAppealMapper.toDto(appeal, students);
                })
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Retrieves an examination appeal
     * @param id the ID of the examination appeal
     * @return examination appeal dto
     * @throws IllegalArgumentException if there is an error with id
     * @throws NoSuchElementException if the examination appeal does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public ExaminationAppealDto getExaminationAppealById(Long id)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
        // sanity check
        validators.validateNotNullOrNotBlank(id.toString(), ID_ERROR);

        try {
            // Retrieve appeal
            ExaminationAppeal appeal = helpers.fetchExaminationAppeal(id);
            return helpers.mapAppealToDto(appeal);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }

    }


    /**
     * Retrieves all examination appeals for a student
     * @param register student register
     * @return a list of dto's of examination appeals available
     * @throws IllegalArgumentException if the register is blank
     * @throws ObjectNotFoundException if the student does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public List<ExaminationAppealDto> getExaminationAppealsAvailable(Register register)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
        // sanity check
        validators.validateNotNullOrNotBlank(register.toString(), REGISTER_ERROR);

        try {
            List<UUID> courseIds = helpers.fetchStudent(register.toString())
                .getStudyPlan()
                .getCourses()
                .stream()
                .map(Course::getId)
                .map(CourseId::id)
                .filter(courseId -> !helpers.fetchExaminations(register.toString())
                    .stream()
                    .map(Examination::getCourse)
                    .map(Course::getId)
                    .map(CourseId::id)
                    .collect(Collectors.toSet())
                    .contains(courseId))
                .toList();

            return examinationAppealRepository
                .findByCourse_Id_IdIn(courseIds)
                .stream()
                .filter(appeal -> appeal
                    .getRegisters()
                    .stream()
                    .noneMatch(studentRegister -> studentRegister.equals(register)))
                .filter(appeal -> appeal.getDate().isAfter(LocalDate.now()))
                .map(appeal -> {
                    ExaminationAppealDto dto = helpers.mapAppealToDto(appeal);
                    professorRepository
                        .findByUniqueCode(new UniqueCode(dto.getProfessorCode()))
                        .ifPresent(profDto -> {
                            String fullName = profDto.getFirstName() + " " + profDto.getLastName();
                            dto.setProfessorFullName(fullName);
                        });
                    return dto;
                })
                .toList();

        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Retrieves all examination appeals booked by a student
     * @param register student register
     * @return a list of dto's of examination appeals booked
     * @throws IllegalArgumentException if the register is blank
     * @throws ObjectNotFoundException if the student does not exists
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public List<ExaminationAppealDto> getExaminationAppealsBookedByStudent(Register register)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
        // sanity check
        validators.validateNotNullOrNotBlank(register.toString(), REGISTER_ERROR);

        try {
            List<UUID> courseIds = helpers.fetchStudent(register.toString())
                .getStudyPlan()
                .getCourses()
                .stream()
                .map(Course::getId)
                .map(CourseId::id)
                .toList();

            return examinationAppealRepository
                .findByCourse_Id_IdIn(courseIds)
                .stream()
                .filter(appeal -> appeal
                    .getRegisters()
                    .stream()
                    .anyMatch(studentRegister -> studentRegister.equals(register)))
                    .map(helpers::mapAppealToDto)
                    .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Retrieves all examination appeals made by professor
     * @param uniqueCode professor unique code
     * @return a list of examination appeals data transfer objects
     * @throws IllegalArgumentException if the unique code is blank
     * @throws ObjectNotFoundException if the professor does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public List<ExaminationAppealDto> getExaminationAppealsMadeByProfessor(UniqueCode uniqueCode)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
        // sanity check
        validators.validateNotNullOrNotBlank(uniqueCode.toString(), UNIQUE_CODE_ERROR);
        validators.validateProfessorExists(uniqueCode);

        try {
            List<UUID> courseIds = helpers.fetchCourses(uniqueCode.toString())
                .stream()
                .map(Course::getId)
                .map(CourseId::id)
                .toList();

            return examinationAppealRepository
                .findByCourse_Id_IdIn(courseIds)
                .stream()
                .map(helpers:: mapAppealToDto)
                .toList();
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Adds a new examination appeal
     * @param dto examination appeal data transfer object
     * @return examinationAppeal data transfer object
     * @throws IllegalArgumentException if the course name or register is invalid
     * @throws ObjectNotFoundException if the degree course or professor does not exist
     * @throws IllegalStateException if the professor does not teach the course
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class, IllegalStateException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ExaminationAppealDto addNewExaminationAppeal(@Valid ExaminationAppealDto dto)
        throws IllegalArgumentException, ObjectNotFoundException, IllegalStateException, DataAccessServiceException
    {
        try {
            // retrieve degreeCourse and course
            Course course = helpers.fetchCourse(dto.getCourse(), dto.getDegreeCourse());
            Professor professor = helpers.fetchProfessor(dto.getProfessorCode());

            if(!professor.getUniqueCode().toString().equals(course.getProfessor().getUniqueCode().toString()))
                throw new IllegalStateException("Professor does not teach this course");

            ExaminationAppeal appeal = ExaminationAppeal.of(course, dto.getDescription(), dto.getDate());
            ExaminationAppeal savedAppeal = examinationAppealRepository.saveAndFlush(appeal);

            if(savedAppeal.getId() != null)
                return dto;
            else return null;
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * deletes an examination appeal
     * @param dto professor data transfer object
     * @param id examination appeal id
     * @return boolean
     * @throws ObjectNotFoundException if the professor does not exists or
     * if the examination appeal does not exists
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = ObjectNotFoundException.class)
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public boolean deleteExaminationAppeal(ProfessorDto professorDto, Long id)
        throws ObjectNotFoundException, DataAccessServiceException
    {

        // sanity check
        validators.validateNotNullOrNotBlank(id.toString(), ID_ERROR);
        validators.validateProfessorExists(new UniqueCode(professorDto.getUniqueCode()));

        try {
            ExaminationAppeal appeal = helpers.fetchExaminationAppeal(id);

            if(!validators.examinationAppealExists(appeal.getId()))
                return false;

            examinationAppealRepository.delete(appeal);
            return true;
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Adds a student to an examination appeal
     * @param id examination appeal ids
     * @param register student register
     * @return examinationAppeal data transfer object
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws ObjectNotFoundException if the student or examination appeal does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ExaminationAppealDto addStudentToAppeal(Long id, Register register)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
        // sanity checks
        validators.validateNotNullOrNotBlank(id.toString(), ID_ERROR);
        validators.validateNotNullOrNotBlank(register.toString(), REGISTER_ERROR);
        validators.validateStudentExists(register);

        try {
            ExaminationAppeal appeal = helpers.fetchExaminationAppeal(id);
            appeal.addRegister(register);
            ExaminationAppeal updatedAppeal = examinationAppealRepository.saveAndFlush(appeal);
            return helpers.mapAppealToDto(updatedAppeal);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Removes a student from an examination appeal
     * @param id examination appeal ids
     * @param register student register
     * @return examinationAppeal data transfer object
     * @throws IllegalArgumentException if any of the parameters is invalid
     * @throws ObjectNotFoundException if the student or examination appeal does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {IllegalArgumentException.class, ObjectNotFoundException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ExaminationAppealDto removeStudentFromAppeal(Long id, Register register)
        throws IllegalArgumentException, ObjectNotFoundException, DataAccessServiceException
    {
        // sanity checks
        validators.validateNotNullOrNotBlank(id.toString(), ID_ERROR);
        validators.validateNotNullOrNotBlank(register.toString(), REGISTER_ERROR);
        validators.validateStudentExists(register);

        try {
            ExaminationAppeal appeal = helpers.fetchExaminationAppeal(id);
            appeal.removeRegister(register);
            ExaminationAppeal updatedAppeal = examinationAppealRepository.save(appeal);
            return helpers.mapAppealToDto(updatedAppeal);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }


    /**
     * Adds an examination outcome to an examination appeal
     * @param outcome examination outcome
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    public void addExaminationOutcome(ExaminationOutcome outcome) throws DataAccessServiceException {
        try {
            examinationOutcomeRepository.saveAndFlush(outcome);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }



    /**
     * Deletes expired examination appeals
     */
    @Scheduled(fixedDelay = 86400000) // ogni giorno
    protected void cleanExpiredAppeals() throws DataAccessServiceException {
        LocalDate today = LocalDate.now();
        // clean instance every month
        LocalDate expirationDateOneMonth = today.minusMonths(1);

        try {
            List<ExaminationAppeal> expiredExaminationAppealsOneMonth = examinationAppealRepository
                .findByDateBefore(expirationDateOneMonth);

            expiredExaminationAppealsOneMonth
                .stream()
                .filter(ExaminationAppeal::deleteIfExpiredAndNoRegisters)
                .forEach(examinationAppealRepository::delete);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }
    }



}
