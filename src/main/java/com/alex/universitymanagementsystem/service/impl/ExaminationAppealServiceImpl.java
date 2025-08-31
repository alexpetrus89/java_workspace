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

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.Examination;
import com.alex.universitymanagementsystem.domain.ExaminationAppeal;
import com.alex.universitymanagementsystem.domain.ExaminationOutcome;
import com.alex.universitymanagementsystem.domain.Professor;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ExaminationAppealDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ExaminationAppealMapper;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationAppealRepository;
import com.alex.universitymanagementsystem.repository.ExaminationOutcomeRepository;
import com.alex.universitymanagementsystem.repository.ExaminationRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.service.ExaminationAppealService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;



@Service
public class ExaminationAppealServiceImpl implements ExaminationAppealService {

	// constants
    private static final String REGISTER_BLANK_ERROR = "Register cannot be empty";
    private static final String STUDENT_NOT_EXISTS = "Student does not exist";
    private static final String PROFESSOR_NOT_EXISTS = "Professor does not exist";
    private static final String EXAMINATION_APPEAL_NOT_EXISTS = "Examination Appeal does not exist";
    private static final String DATA_ACCESS_ERROR = "data access error";

    // instance variables
    private final ExaminationAppealRepository examinationAppealRepository;
    private final ExaminationOutcomeRepository examinationOutcomeRepository;
    private final ExaminationRepository examinationRepository;
    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;

    // constructor
    public ExaminationAppealServiceImpl(
        ExaminationAppealRepository examinationAppealRepository,
        ExaminationOutcomeRepository examinationOutcomeRepository,
        ExaminationRepository examinationRepository,
        CourseRepository courseRepository,
        ProfessorRepository professorRepository,
        StudentRepository studentRepository
    ) {
        this.examinationAppealRepository = examinationAppealRepository;
        this.examinationOutcomeRepository = examinationOutcomeRepository;
        this.examinationRepository = examinationRepository;
        this.courseRepository = courseRepository;
        this.professorRepository = professorRepository;
        this.studentRepository = studentRepository;
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
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException
    {

        if (id == null) throw new IllegalArgumentException("Id cannot be null");

        try {
            // Retrieve appeal
            ExaminationAppeal appeal = examinationAppealRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(EXAMINATION_APPEAL_NOT_EXISTS));

            return mapToDto(appeal);
        } catch (PersistenceException e) {
            throw new DataAccessServiceException(DATA_ACCESS_ERROR, e);
        }

    }


    /**
     * Retrieves all examination appeals for a student
     * @param register student register
     * @return a list of dto's of examination appeals available
     * @throws IllegalArgumentException if the register is blank
     * @throws NoSuchElementException if the student does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public List<ExaminationAppealDto> getExaminationAppealsAvailable(Register register)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException
    {

        if (register.toString().isBlank())
            throw new IllegalArgumentException(REGISTER_BLANK_ERROR);

        try {
            List<UUID> courseIds = studentRepository
                .findByRegister(register)
                .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_EXISTS))
                .getStudyPlan()
                .getCourses()
                .stream()
                .map(Course::getId)
                .map(CourseId::id)
                .filter(courseId -> !examinationRepository
                    .findByRegister(register.toString())
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
                    ExaminationAppealDto dto = mapToDto(appeal);
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
     * @throws NoSuchElementException if the student does not exists
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public List<ExaminationAppealDto> getExaminationAppealsBookedByStudent(Register register)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException
    {

        if (register.toString().isBlank())
            throw new IllegalArgumentException(REGISTER_BLANK_ERROR);

        try {
            List<UUID> courseIds = studentRepository
                .findByRegister(register)
                .orElseThrow(() -> new NoSuchElementException(STUDENT_NOT_EXISTS))
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
                    .map(this::mapToDto)
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
     * @throws NoSuchElementException if the professor does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    public List<ExaminationAppealDto> getExaminationAppealsMadeByProfessor(UniqueCode uniqueCode)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException
    {

        if (uniqueCode.toString().isBlank())
            throw new IllegalArgumentException(REGISTER_BLANK_ERROR);

        if (!professorRepository.existsByUniqueCode(uniqueCode))
            throw new NoSuchElementException(PROFESSOR_NOT_EXISTS);

        try {
            List<UUID> courseIds = courseRepository
                .findByProfessor(uniqueCode)
                .stream()
                .map(Course::getId)
                .map(CourseId::id)
                .toList();

            return examinationAppealRepository
                .findByCourse_Id_IdIn(courseIds)
                .stream()
                .map(this::mapToDto)
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
     * @throws NoSuchElementException if the degree course or professor does not exist
     * @throws IllegalStateException if the professor does not teach the course
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {IllegalArgumentException.class, NoSuchElementException.class, IllegalStateException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ExaminationAppealDto addNewExaminationAppeal(@Valid ExaminationAppealDto dto)
        throws IllegalArgumentException, NoSuchElementException, IllegalStateException, DataAccessServiceException
    {
        try {
            // retrieve degreeCourse and course
            Course course = courseRepository
                .findByNameAndDegreeCourseName(dto.getCourse(), dto.getDegreeCourse())
                .orElseThrow(() -> new IllegalArgumentException("Course name cannot be empty"));

            Professor professor = professorRepository
                .findByUniqueCode(new UniqueCode(dto.getProfessorCode()))
                .orElseThrow(() -> new NoSuchElementException(PROFESSOR_NOT_EXISTS));

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
     * @throws ObjectNotFoundException if the professor does not exists
     * @throws NoSuchElementException if the examination appeal does not exists
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {ObjectNotFoundException.class, NoSuchElementException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public boolean deleteExaminationAppeal(ProfessorDto professorDto, Long id)
        throws ObjectNotFoundException, NoSuchElementException, DataAccessServiceException
    {
        if (!professorRepository.existsByUniqueCode(new UniqueCode(professorDto.getUniqueCode())))
            throw new ObjectNotFoundException(DomainType.PROFESSOR);

        try {
            ExaminationAppeal examAppeal = examinationAppealRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(EXAMINATION_APPEAL_NOT_EXISTS));

            if(!examinationAppealRepository.existsById(examAppeal.getId()))
                return false;

            examinationAppealRepository.delete(examAppeal);
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
     * @throws NoSuchElementException if the student or examination appeal does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {IllegalArgumentException.class, NoSuchElementException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ExaminationAppealDto addStudentToAppeal(Long id, Register register)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException
    {
        // sanity checks
        if (id == null) throw new IllegalArgumentException("Id cannot be null");

        if (register == null) throw new IllegalArgumentException(REGISTER_BLANK_ERROR);

        if (!studentRepository.existsByRegister(register))
            throw new NoSuchElementException(STUDENT_NOT_EXISTS);

        try {
            ExaminationAppeal appeal = examinationAppealRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(EXAMINATION_APPEAL_NOT_EXISTS));
            appeal.addRegister(register);
            ExaminationAppeal updateAppeal = examinationAppealRepository.saveAndFlush(appeal);
            return mapToDto(updateAppeal);
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
     * @throws NoSuchElementException if the student or examination appeal does not exist
     * @throws DataAccessServiceException if there is an error accessing the database
     */
    @Override
    @Transactional(rollbackOn = {IllegalArgumentException.class, NoSuchElementException.class})
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ExaminationAppealDto removeStudentFromAppeal(Long id, Register register)
        throws IllegalArgumentException, NoSuchElementException, DataAccessServiceException
    {
        // sanity checks
        if (id == null) throw new IllegalArgumentException("Id cannot be null");

        if (register == null) throw new IllegalArgumentException(REGISTER_BLANK_ERROR);

        if (!studentRepository.existsByRegister(register))
            throw new NoSuchElementException(STUDENT_NOT_EXISTS);

        try {
            ExaminationAppeal appeal = examinationAppealRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(EXAMINATION_APPEAL_NOT_EXISTS));
            appeal.removeRegister(register);
            ExaminationAppeal updateAppeal = examinationAppealRepository.save(appeal);
            return mapToDto(updateAppeal);
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


    /**
     * Maps an examination appeal entity to a data transfer object.
     */
    private ExaminationAppealDto mapToDto(ExaminationAppeal appeal) {
        Set<StudentDto> students = studentRepository
            .findByRegisterIn(appeal.getRegisters())
            .stream()
            .map(StudentMapper::toDto)
            .collect(Collectors.toSet());
        return ExaminationAppealMapper.toDto(appeal, students);
    }



}
