package com.alex.universitymanagementsystem.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    // constants
    private static final String EXCEPTION_COURSE_IDENTIFIER = "course";
    private static final String EXCEPTION_EXAMINATION_IDENTIFIER = "examination";

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
     * Get all examinations by student register
     * @param Register register
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the student does not exist
     * @throws IllegalArgumentException if the register is null
     * @throws UnsupportedOperationException if the register is not unique
     * @throws NullPointerException if the register is null
     */
    @Override
    public List<ExaminationDto> getExaminationsByStudentRegister(@NonNull Register register)
        throws ObjectNotFoundException
    {

        if (!studentRepository.existsByRegister(register))
            throw new ObjectNotFoundException(register);

        return examinationRepository
            .findExaminationsByStudent(register)
            .stream()
            .map(ExaminationMapper::mapToExaminationDto)
            .toList();
    }


    /**
     * Get all examinations by professor unique code
     * @param uniqueCode unique code of the professor
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the professor does not exist
     * @throws IllegalArgumentException if the unique code is empty
     * @throws UnsupportedOperationException if the unique code is not unique
     * @throws NullPointerException if the unique code is null
     */
    @Override
    public List<ExaminationDto> getExaminationsByProfessorUniqueCode(@NonNull UniqueCode uniqueCode)
        throws ObjectNotFoundException
    {

        if (!professorRepository.existsByUniqueCode(uniqueCode))
            throw new ObjectNotFoundException(uniqueCode);

        List<Course> courses = courseRepository.findByProfessor(uniqueCode);

        List<ExaminationDto> examinationsDto = new ArrayList<>();

        courses.forEach(course -> examinationsDto.addAll(
            examinationRepository
                .findExaminationsByCourseId(course.getCourseId())
                .stream()
                .map(ExaminationMapper::mapToExaminationDto)
                .toList()
        ));

        return examinationsDto;
    }


    /**
     * Get all examinations by course id
     * @param courseId course id of the course
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the course does not exist
     * @throws IllegalArgumentException if the courseId is null
     * @throws UnsupportedOperationException if the courseId is not unique
     */
    @Override
    public List<ExaminationDto> getExaminationsByCourseId(@NonNull CourseId courseId)
        throws ObjectNotFoundException
    {

        if (!courseRepository.existsById(courseId))
            throw new ObjectNotFoundException(courseId.toString(), EXCEPTION_COURSE_IDENTIFIER);

        return examinationRepository
            .findExaminationsByCourseId(courseId)
            .stream()
            .map(ExaminationMapper::mapToExaminationDto)
            .toList();
    }


    /**
     * Get all examinations by course name
     * @param name name of the course
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the course does not exist
     * @throws IllegalArgumentException if the name is null or empty
     * @throws UnsupportedOperationException if the unique code is not unique
     */
    @Override
    public List<ExaminationDto> getExaminationsByCourseName(@NonNull String name)
        throws ObjectNotFoundException
    {

        if (!courseRepository.existsByName(name))
            throw new ObjectNotFoundException(name, EXCEPTION_COURSE_IDENTIFIER);

        return examinationRepository
            .findExaminationsByCourseName(name)
            .stream()
            .map(ExaminationMapper::mapToExaminationDto)
            .toList();
    }


    /**
     * Add new examination
     * @param register register of the student
     * @param courseName name of the course
     * @param degreeCourseName name of the degree course
     * @param grade grade of the examination
     * @param withHonors whether the examination was passed with honors
     * @param date date of the examination
     * @return Examination
     * @throws ObjectAlreadyExistsException if the examination already exists.
     * @throws ObjectNotFoundException if the student or course does not exist.
     * @throws IllegalArgumentException if the date is in the past or the grade
     *                                  is not between 0 and 30 or Degree course
     *                                  does not match or the unique code is null
     *                                  or the unique code is empty or the course
     *                                  name is null or the course name is empty
     * @throws UnsupportedOperationException if the unique code is not unique
     *                                        or if the course name is not unique
     * @throws NullPointerException if the unique code is null or the course name is null
     */
    @Override
    @NonNull
	@Transactional
    public Examination addNewExamination(
        Register register,
        String courseName,
        String degreeCourseName,
        int grade,
        boolean withHonors,
        LocalDate date
    ) throws ObjectAlreadyExistsException, ObjectNotFoundException {

        DegreeCourse degreeCourse = degreeCourseRepository.findByName(degreeCourseName.toUpperCase());

        Course course = courseRepository.findByNameAndDegreeCourse(courseName, degreeCourse.getId());

        Student student = studentRepository.findByRegister(register);

        // sanity check
        List<Examination> examinations = examinationRepository.findExaminationsByCourseName(courseName);

        examinations.forEach(examination -> {
            if (examination.getStudent().getRegister().equals(register))
                throw new ObjectAlreadyExistsException(courseName + " for student with register " + register, EXCEPTION_EXAMINATION_IDENTIFIER);
        });

        // sanity checks
        // TODO the course must be part of the student's study plan
        if(!student.getDegreeCourse().getName().equals(course.getDegreeCourse().getName()))
            throw new IllegalArgumentException("Degree course does not match");

        if(grade < 0 || grade > 30)
            throw new IllegalArgumentException("Grade must be between 0 and 30");

        if(withHonors && grade != 30)
            throw new IllegalArgumentException("With honors can only be true if the grade is 30");

        if(date == null || date.isAfter(java.time.LocalDate.now()))
            throw new IllegalArgumentException("The date must be at least less than today");

        // create
        Examination examination = new Examination(course, student, grade, withHonors, date);
        // save
		examinationRepository.saveAndFlush(examination);

        return examination;
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
     * @throws ObjectNotFoundException if the student or course does not exist.
     * @throws IllegalArgumentException if the date is in the past or the grade
     *                                  is not between 0 and 30 or Degree course
     *                                  does not match or the unique code is null
     *                                  or the unique code is empty or the course
     *                                  name is null or the course name is empty
     * @throws UnsupportedOperationException if the unique code is not unique
     * @throws NullPointerException if the unique code is null or the course name is null
     */
    @Override
    @NonNull
	@Transactional
    public Examination updateExamination(
        Register oldRegister,
        String oldCourseName,
        String oldDegreeCourseName,
        Register newRegister,
        String newCourseName,
        String newDegreeCourseName,
        int grade,
        boolean withHonors,
        LocalDate date
    ) throws ObjectNotFoundException {

		Student newStudent = studentRepository.findByRegister(newRegister);

        DegreeCourse newDegreeCourse = degreeCourseRepository.findByName(newDegreeCourseName.toUpperCase());

		Course newCourse = courseRepository.findByNameAndDegreeCourse(newCourseName, newDegreeCourse.getId());

        Examination updatableExamination = examinationRepository
            .findExaminationsByCourseName(oldCourseName)
            .stream()
            .filter(exam -> exam.getCourse().getDegreeCourse().getName().equals(oldDegreeCourseName))
            .filter(exam -> exam.getStudent().getRegister().equals(oldRegister))
            .findFirst()
            .orElseThrow(() -> new ObjectNotFoundException("Examination of course " + oldCourseName + " and student register " + oldRegister, EXCEPTION_EXAMINATION_IDENTIFIER));

        // sanity checks
        if(!newStudent.getDegreeCourse().getName().equals(newCourse.getDegreeCourse().getName()))
            throw new IllegalArgumentException("Degree course does not match");

        if(grade < 0 || grade > 30)
            throw new IllegalArgumentException("Grade must be between 0 and 30");

        if(withHonors && grade != 30)
            throw new IllegalArgumentException("With honors can only be true if the grade is 30");

        if(date == null || date.isAfter(java.time.LocalDate.now()))
            throw new IllegalArgumentException("The date must be at least less than today");

        // update
        updatableExamination.setCourse(newCourse);
        updatableExamination.setStudent(newStudent);
        updatableExamination.setGrade(grade);
        updatableExamination.setWithHonors(withHonors);
        updatableExamination.setDate(date);

		// save
        examinationRepository.saveAndFlush(updatableExamination);

        return updatableExamination;
    }


    /**
     * Delete existing examination
     * @param register register of the student
     * @param name name of the course
     * @throws ObjectNotFoundException if the examination does not exist
     * @throws IllegalArgumentException if the course name is null or empty
     *                                  or the register is null or empty
     * @throws UnsupportedOperationException if the register is not unique
     *                                       or if the course name is not unique
     */
    @Override
	@Transactional
	public void deleteExamination(@NonNull Register register, @NonNull String name)
		throws ObjectNotFoundException
	{

        Examination examination = examinationRepository
            .findExaminationsByCourseName(name)
            .stream()
            .filter(exam -> exam.getStudent().getRegister().equals(register))
            .findFirst()
            .orElseThrow(() -> new ObjectNotFoundException("Examination of course " + name + " and student register " + register, EXCEPTION_EXAMINATION_IDENTIFIER));

        examinationRepository.delete(examination);
    }

}

