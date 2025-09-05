package com.alex.universitymanagementsystem.component.validator;

import java.time.LocalDate;
import java.util.function.BooleanSupplier;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alex.universitymanagementsystem.domain.immutable.ExaminationId;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationAppealRepository;
import com.alex.universitymanagementsystem.repository.ExaminationOutcomeRepository;
import com.alex.universitymanagementsystem.repository.ExaminationRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;

@Component
public class ServiceValidators {

    // instance variables
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final DegreeCourseRepository degreeCourseRepository;
    private final ExaminationRepository examinationRepository;
    private final ExaminationAppealRepository examinationAppealRepository;
    private final ExaminationOutcomeRepository examinationOutcomeRepository;

    public ServiceValidators(
        ProfessorRepository professorRepository,
        StudentRepository studentRepository,
        CourseRepository courseRepository,
        DegreeCourseRepository degreeCourseRepository,
        ExaminationRepository examinationRepository,
        ExaminationAppealRepository examinationAppealRepository,
        ExaminationOutcomeRepository examinationOutcomeRepository
    ) {
        this.professorRepository = professorRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.degreeCourseRepository = degreeCourseRepository;
        this.examinationRepository = examinationRepository;
        this.examinationAppealRepository = examinationAppealRepository;
        this.examinationOutcomeRepository = examinationOutcomeRepository;
    }


    // --- generic helpers ---

    /**
     * Validates that an object exists.
     * @param exists whether the object exists
     * @param type the type of the object
     */
    private void validateEntityExists(BooleanSupplier existsCheck, DomainType type) {
        if (!existsCheck.getAsBoolean()) throw new ObjectNotFoundException(type);
    }


    /**
     * Validates that an object exists.
     * @param exists whether the object exists
     * @param type the type of the object
     */
    private void validateEntityNotExists(BooleanSupplier existsCheck, DomainType type) {
        if (existsCheck.getAsBoolean()) throw new ObjectAlreadyExistsException(type);
    }



    // --- domain specific ---

    /**
     * Validates that a professor exists.
     * @param uniqueCode the unique code of the professor
     * @throws ObjectNotFoundException if the professor does not exist
     */
    public void validateProfessorExists(UniqueCode uniqueCode) {
        validateEntityExists(() -> professorRepository.existsByUniqueCode(uniqueCode), DomainType.PROFESSOR);
    }


    /**
     * Validates that a professor already exists.
     * @param uniqueCode the unique code of the professor
     * @throws ObjectAlreadyExistsException if the professor already exists
     */
    public void validateProfessorAlreadyExists(UniqueCode uniqueCode) {
        validateEntityNotExists(() -> professorRepository.existsByUniqueCode(uniqueCode), DomainType.PROFESSOR);
    }


    /**
     * Validates that a student exists.
     * @param register the register of the student
     * @throws ObjectNotFoundException if the student does not exist
     */
    public void validateStudentExists(Register register) {
        validateEntityExists(() -> studentRepository.existsByRegister(register), DomainType.STUDENT);
    }


    public void validateStudentAlreadyExists(Register register) {
        validateEntityNotExists(() -> studentRepository.existsByRegister(register), DomainType.STUDENT);
    }

    /**
     * Validates that a course exists.
     * @param courseName the name of the course
     * @param degreeCourseName the name of the degree course
     * @throws ObjectNotFoundException if the course does not exist
     */
    public void validateCourseExists(String courseName, String degreeCourseName) {
        validateEntityExists(() -> courseRepository.existsByNameAndDegreeCourseName(
            courseName, degreeCourseName.toUpperCase()), DomainType.COURSE);
    }


    /**
     * Validates that a degree course exists.
     * @param degreeCourseName the name of the degree course
     * @throws ObjectNotFoundException if the degree course does not exist
     */
    public void validateDegreeCourseExists(String degreeCourseName) {
        validateEntityExists(() -> degreeCourseRepository.existsByName(degreeCourseName.toUpperCase()), DomainType.DEGREE_COURSE);
    }


    /**
     * Validates that an examination exists.
     * @param id the ID of the examination
     * @throws ObjectNotFoundException if the examination does not exist
     */
    public void validateExaminationExists(ExaminationId id) {
        validateEntityExists(() -> examinationRepository.existsById(id), DomainType.EXAMINATION);
    }


    /**
     * Checks if an examination appeal exists.
     * @param id the ID of the examination appeal
     * @return whether the examination appeal exists
     */
    public boolean examinationAppealExists(Long id) {
        return examinationAppealRepository.existsById(id);
    }


    /**
     * Validates that an examination appeal exists.
     * @param id the ID of the examination appeal
     * @throws ObjectNotFoundException if the examination appeal does not exist
     */
    public void validateExaminationAppealExists(Long id) {
        validateEntityExists(() -> examinationAppealRepository.existsById(id), DomainType.EXAMINATION_APPEAL);
    }

    public void validateExaminationOutcomeAlreadyExists(Long id) {
        validateEntityNotExists(() -> examinationOutcomeRepository.existsById(id), DomainType.EXAMINATION_OUTCOME);
    }


    /**
     * Validates that an examination outcome exists.
     * @param id the ID of the examination outcome
     * @param register the register of the student
     * @throws ObjectNotFoundException if the examination outcome does not exist
     */
    public void validateExaminationOutcomeExists(Long id, String register) {
        validateEntityExists(() -> examinationOutcomeRepository.existsByIdAndRegister(id, register), DomainType.EXAMINATION_OUTCOME);
    }


    /**
     * Validates that an examination outcome already exists.
     * @param id the ID of the examination outcome
     * @param register the register of the student
     */
    public void validateExaminationOutcomeAlreadyExists(Long id, String register) {
        validateEntityNotExists(() -> examinationOutcomeRepository.existsByIdAndRegister(id, register), DomainType.EXAMINATION_OUTCOME);
    }


    /**
     * Validates that a string is not blank.
     * @param value the string to validate
     * @param message the error message to include if validation fails
     * @throws IllegalArgumentException if the string is null or blank
     */
    public void validateNotNullOrNotBlank(String value, String message) {
        if (!StringUtils.hasText(value)) throw new IllegalArgumentException(message);
    }


    /**
     * Validates that the grade is valid for honors.
     * @param grade the grade to validate
     * @param withHonors whether the examination is with honors
     */
    public void validateGradeWithHonors(int grade, boolean withHonors) {
        if (withHonors && grade != 30)
            throw new IllegalArgumentException(String.format("Cannot assign honors to a grade of %d. Only 30 is allowed.", grade));
    }


    /**
     * Parses and validates the grade from a string.
     * @param gradeStr the grade as a string
     * @param withHonors whether the examination is with honors
     * @param date the date of the examination
     * @return the validated grade
     */
    public int parseAndValidateGrade(String gradeStr, boolean withHonors, LocalDate date) {
        int grade;

        try {
            grade = Integer.parseInt(gradeStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Grade must be an integer.", e);
        }

        if (grade < 0 || grade > 30) throw new IllegalArgumentException("Grade must be between 0 and 30.");

        validateGradeWithHonors(grade, withHonors);

        if (date.isAfter(LocalDate.now())) throw new IllegalArgumentException("Examination date cannot be in the future.");

        return grade;
    }


}

