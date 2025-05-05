package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Examination;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.dto.ExaminationDto;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ExaminationMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.service.ExaminationService;

import jakarta.transaction.Transactional;


@Service
public class ExaminationServiceImpl implements ExaminationService {

    // logger
	private static final Logger logger = LoggerFactory.getLogger(ExaminationServiceImpl.class);

	// constants
	private static final String DATA_ACCESS_ERROR = "data access error";
    private static final String REGISTER_BLANK_ERROR = "Register cannot be null or empty";
    private static final String COURSE_NAME_BLANK_ERROR = "Course name cannot be null or empty";

    // instance variables
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final DegreeCourseRepository degreeCourseRepository;
    private final ExaminationRepository examinationRepository;


    // autowired - dependency injection - constructor
    public ExaminationServiceImpl(
        StudentRepository studentRepository,
        ProfessorRepository professorRepository,
        CourseRepository courseRepository,
        DegreeCourseRepository degreeCourseRepository,
        ExaminationRepository examinationRepository
    ) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
        this.degreeCourseRepository = degreeCourseRepository;
        this.examinationRepository = examinationRepository;
    }


    /**
     * Get all examinations
     * @return List<ExaminationDto> a list of all examination data transfer objects
     */
    @Override
    public List<ExaminationDto> getExaminations() {
        return examinationRepository
            .findAll()
            .stream()
            .map(ExaminationMapper::mapToExaminationDto)
            .toList();
    }


    /**
     * Get all examinations by course id
     * @param courseId course id of the course
     * @return List<ExaminationDto>
     * @throws NullPointerException if the courseId is null
     * @throws ObjectNotFoundException if the course does not exist
     * @throws IllegalArgumentException if the courseId is Blank
     * @throws UnsupportedOperationException if the courseId is not unique
     */
    @Override
    public List<ExaminationDto> getExaminationsByCourseId(@NonNull CourseId courseId)
        throws NullPointerException, ObjectNotFoundException, IllegalArgumentException, UnsupportedOperationException
    {
        if(courseId.toString().isBlank())
            throw new IllegalArgumentException("CourseId cannot be null or empty");

        if (!courseRepository.existsById(courseId))
            throw new ObjectNotFoundException(DomainType.COURSE);

        try {
            return examinationRepository
                .findExaminationsByCourseId(courseId)
                .stream()
                .map(ExaminationMapper::mapToExaminationDto)
                .toList();
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return Collections.emptyList();
        }
    }


    /**
     * Get all examinations by student register
     * @param register register of the student
     * @return List<ExaminationDto>
     * @throws NullPointerException if the register is null
     * @throws IllegalArgumentException if the register is blank
     * @throws ObjectNotFoundException if the student does not exist
     * @throws UnsupportedOperationException if the register is not unique
     */
    @Override
    public List<ExaminationDto> getExaminationsByStudentRegister(@NonNull Register register)
        throws NullPointerException, IllegalArgumentException, ObjectNotFoundException, UnsupportedOperationException
    {
        if(register.toString().isBlank())
            throw new IllegalArgumentException("Register cannot be empty");

        if (!studentRepository.existsByRegister(register))
            throw new ObjectNotFoundException(register);

        try {
            return examinationRepository
                .findExaminationsByStudent(register)
                .stream()
                .map(ExaminationMapper::mapToExaminationDto)
                .toList();
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return Collections.emptyList();
        }
    }


    /**
     * Get all examinations by professor unique code
     * @param uniqueCode unique code of the professor
     * @return List<ExaminationDto>
     * @throws NullPointerException if the unique code is null
     * @throws IllegalArgumentException if the unique code is blank
     * @throws ObjectNotFoundException if the professor does not exist
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    @Override
    public List<ExaminationDto> getExaminationsByProfessorUniqueCode(@NonNull UniqueCode uniqueCode)
    throws NullPointerException, IllegalArgumentException, ObjectNotFoundException, UnsupportedOperationException
    {
        if(uniqueCode.toString().isBlank())
            throw new IllegalArgumentException("Unique Code cannot be null or empty");

        if (!professorRepository.existsByUniqueCode(uniqueCode))
            throw new ObjectNotFoundException(uniqueCode);

        try {
            Set<Course> courses = courseRepository.findByProfessor(uniqueCode);
            List<ExaminationDto> examinationsDto = new ArrayList<>();

            courses.forEach(course -> examinationsDto.addAll(
                examinationRepository
                    .findExaminationsByCourseId(course.getCourseId())
                    .stream()
                    .map(ExaminationMapper::mapToExaminationDto)
                    .toList()
            ));

            return examinationsDto;
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return Collections.emptyList();
        }
    }


    /**
     * Get all examinations by course name
     * @param name name of the course
     * @return List<ExaminationDto>
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is blank
     * @throws ObjectNotFoundException if the course does not exist
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    @Override
    public List<ExaminationDto> getExaminationsByCourseName(@NonNull String name)
        throws NullPointerException, IllegalArgumentException, ObjectNotFoundException, UnsupportedOperationException
    {
        if(name.isBlank())
            throw new IllegalArgumentException("Name cannot be null or empty");

        if (!courseRepository.existsByName(name))
            throw new ObjectNotFoundException(DomainType.COURSE);

        try {
            return examinationRepository
                .findExaminationsByCourseName(name)
                .stream()
                .map(ExaminationMapper::mapToExaminationDto)
                .toList();
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return Collections.emptyList();
        }
    }


    /**
     * Add new examination
     * @param register of the student
     * @param name of the course
     * @param name of the degree course
     * @param grade of the examination
     * @param withHonors whether the examination was passed with honors
     * @param date of the examination
     * @return Examination
     * @throws NullPointerException if the unique code is null or the course name is null
     * @throws IllegalArgumentException if the unique code is blank or the course name is
     *         blank or the degree course name is blank or if grade is not between 0 and 30
     *         or if the date is in the past or if the student or course does not exist
     * @throws ObjectAlreadyExistsException if the examination already exists.
     * @throws ObjectNotFoundException if the student or course does not exist.
     * @throws UnsupportedOperationException if the unique code is not unique
     *         or if the course name is not unique
     */
    @Override
	@Transactional
    public Examination addNewExamination(
        @NonNull Register register,
        @NonNull String courseName,
        @NonNull String degreeCourseName,
        @NonNull String grade,
        boolean withHonors,
        @NonNull LocalDate date
    ) throws NullPointerException, IllegalArgumentException, ObjectAlreadyExistsException, ObjectNotFoundException
    {

        // sanity checks
        if(register.toString().isBlank())
            throw new IllegalArgumentException(REGISTER_BLANK_ERROR);

        if(courseName.isBlank())
            throw new IllegalArgumentException(COURSE_NAME_BLANK_ERROR);

        if(degreeCourseName.isBlank())
            throw new IllegalArgumentException("Degree course name cannot be null or empty");

        int intGrade = 0;
        try {
            intGrade = Integer.parseInt(grade);

            if(intGrade < 0 || intGrade > 30)
                throw new IllegalArgumentException("Grade must be between 0 and 30");

            if(withHonors && intGrade != 30)
                throw new IllegalArgumentException("With honors can only be true if the grade is 30");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Grade must be a valid number");
        }

        if(date.isAfter(java.time.LocalDate.now()))
            throw new IllegalArgumentException("The date must be at least less than today");

        if (!studentRepository.existsByRegister(register))
            throw new ObjectNotFoundException(DomainType.STUDENT);

        if(!degreeCourseRepository.existsByName(degreeCourseName))
            throw new ObjectNotFoundException(DomainType.DEGREE_COURSE);

        if (!courseRepository.existsByNameAndDegreeCourse(courseName, degreeCourseName))
            throw new ObjectNotFoundException(DomainType.COURSE);


        try {
            DegreeCourse degreeCourse = degreeCourseRepository.findByName(degreeCourseName.toUpperCase());
            Course course = courseRepository.findByNameAndDegreeCourse(courseName, degreeCourse);
            Student student = studentRepository.findByRegister(register);

            // the student must be part of the degree course
            if(!degreeCourse.getStudents().contains(student))
                throw new IllegalStateException("student's must be part of the degree course");

            // the course must be part of the student's study plan
            if(!student.getStudyPlan().getCourses().contains(course))
                throw new ObjectNotFoundException(DomainType.COURSE);

            // check if the examination already exists
            examinationRepository
                .findExaminationsByCourseName(courseName)
                .stream()
                .forEach(examination -> {
                    if (examination.getStudent().getRegister().equals(register))
                        throw new ObjectAlreadyExistsException(register);
                });

            // create
            Examination examination = new Examination(course, student, intGrade, withHonors, date);
            // save
            examinationRepository.saveAndFlush(examination);
            return examination;
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }


    /**
     * Update existing examination
     * @param oldRegister the old student's register
     * @param oldCourseName the old course name
     * @param oldDegreeCourseName the old degree course name
     * @param newRegister the new student's register
     * @param newCourseName the new course name
     * @param newDegreeCourseName the new degree course name
     * @param grade the new grade
     * @param withHonors whether the examination was passed with honors
     * @param date the new date
     * @return Examination
     * @throws NullPointerException if any of the parameters is null
     * @throws ObjectNotFoundException if the student or course or degree course
     *         or examination to update does not exist.
     * @throws IllegalArgumentException if any of the parameters is blank or
     *         if the grade is not between 0 and 30 or if the date is in the
     *         past
     * @throws IllegalStateException if the student is not part of the degree course
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    @Override
	@Transactional
    public Examination updateExamination(
        @NonNull Register oldRegister,
        @NonNull String oldCourseName,
        @NonNull String oldDegreeCourseName,
        @NonNull Register newRegister,
        @NonNull String newCourseName,
        @NonNull String newDegreeCourseName,
        @NonNull String grade,
        boolean withHonors,
        @NonNull LocalDate date
    ) throws NullPointerException, IllegalArgumentException, IllegalStateException, ObjectNotFoundException, UnsupportedOperationException
    {

        // sanity checks
        if(oldRegister.toString().isBlank() || newRegister.toString().isBlank())
            throw new IllegalArgumentException(REGISTER_BLANK_ERROR);

        if(oldCourseName.isBlank() || newCourseName.isBlank())
            throw new IllegalArgumentException(COURSE_NAME_BLANK_ERROR);

        if(oldDegreeCourseName.isBlank() || newDegreeCourseName.isBlank())
            throw new IllegalArgumentException("Degree course name cannot be null or empty");

        int intGrade = 0;
        try {
            intGrade = Integer.parseInt(grade);

            if(intGrade < 0 || intGrade > 30)
                throw new IllegalArgumentException("Grade must be between 0 and 30");

            if(withHonors && intGrade != 30)
                throw new IllegalArgumentException("With honors can only be true if the grade is 30");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Grade must be a valid number");
        }

        if(date.isAfter(java.time.LocalDate.now()))
            throw new IllegalArgumentException("The date must be at least less than today");

        if (!studentRepository.existsByRegister(newRegister))
            throw new ObjectNotFoundException(DomainType.STUDENT);

        if(!degreeCourseRepository.existsByName(newDegreeCourseName))
            throw new ObjectNotFoundException(DomainType.DEGREE_COURSE);

        if (!courseRepository.existsByNameAndDegreeCourse(newCourseName, newDegreeCourseName))
            throw new ObjectNotFoundException(DomainType.COURSE);

        try{
            // retrieve data
            Student newStudent = studentRepository.findByRegister(newRegister);
            DegreeCourse newDegreeCourse = degreeCourseRepository.findByName(newDegreeCourseName.toUpperCase());
            Course newCourse = courseRepository.findByNameAndDegreeCourse(newCourseName, newDegreeCourse);
            Examination updatableExamination = examinationRepository
                .findExaminationsByCourseName(oldCourseName)
                .stream()
                .filter(exam -> exam.getCourse().getDegreeCourse().getName().equals(oldDegreeCourseName))
                .filter(exam -> exam.getStudent().getRegister().equals(oldRegister))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(DomainType.EXAMINATION));

            // the student must be part of the degree course
            if(!newDegreeCourse.getStudents().contains(newStudent))
                throw new IllegalStateException("student's must be part of the degree course");

            // the course must be part of the student's study plan
            if(!newStudent.getStudyPlan().getCourses().contains(newCourse))
                throw new ObjectNotFoundException(DomainType.COURSE);

            // update
            updatableExamination.setCourse(newCourse);
            updatableExamination.setStudent(newStudent);
            updatableExamination.setGrade(intGrade);
            updatableExamination.setWithHonors(withHonors);
            updatableExamination.setDate(date);

		    // save
            examinationRepository.saveAndFlush(updatableExamination);
            return updatableExamination;
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
            return null;
        }
    }


    /**
     * Delete existing examination
     * @param register register of the student
     * @param name name of the course
     * @throws NullPointerException if any of the parameters is null
     * @throws IllegalArgumentException if the course name is null or empty
     *         or the register is null or empty
     * @throws ObjectNotFoundException if the examination does not exist
     * @throws UnsupportedOperationException if the register is not unique
     *         or if the course name is not unique
     */
    @Override
	@Transactional
	public void deleteExamination(@NonNull Register register, @NonNull String name)
		throws NullPointerException, IllegalArgumentException, ObjectNotFoundException, UnsupportedOperationException
	{
        if(register.toString().isBlank())
            throw new IllegalArgumentException(REGISTER_BLANK_ERROR);

        if(name.isBlank())
            throw new IllegalArgumentException(COURSE_NAME_BLANK_ERROR);

        try {
            Examination examination = examinationRepository
                .findExaminationsByCourseName(name)
                .stream()
                .filter(exam -> !exam.getStudent().getRegister().equals(register))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Examination of course " + name + " and student register " + register));

            examinationRepository.delete(examination);
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR + " while deleting examination of course " + name + " and student register " + register, e);
        }
    }

}

