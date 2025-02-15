package com.alex.studentmanagementsystem.service.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.alex.studentmanagementsystem.domain.Course;
import com.alex.studentmanagementsystem.domain.Examination;
import com.alex.studentmanagementsystem.domain.Student;
import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.dto.ExaminationDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.ExaminationMapper;
import com.alex.studentmanagementsystem.repository.CourseRepository;
import com.alex.studentmanagementsystem.repository.ExaminationRepository;
import com.alex.studentmanagementsystem.repository.ProfessorRepository;
import com.alex.studentmanagementsystem.repository.StudentRepository;
import com.alex.studentmanagementsystem.service.ExaminationService;

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
    private final ExaminationRepository examinationRepository;


    // autowired - dependency injection - constructor
    public ExaminationServiceImpl(
        StudentRepository studentRepository,
        ProfessorRepository professorRepository,
        CourseRepository courseRepository,
        ExaminationRepository examinationRepository
    ) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
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
     * @param UniqueCode uniqueCode
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the professor does not exist
     * @throws NullPointerException if the unique code is null
     * @throws UnsupportedOperationException if the unique code is not unique
     * @throws ClassCastException if the unique code is not a string
     * @throws IllegalArgumentException if the unique code is empty
     */
    @Override
    public List<ExaminationDto> getExaminationsByProfessorUniqueCode(@NonNull UniqueCode uniqueCode)
        throws ObjectNotFoundException
    {

        if (!professorRepository.existsByUniqueCode(uniqueCode))
            throw new ObjectNotFoundException(uniqueCode);

        List<Course> courses = courseRepository
            .findByProfessor(uniqueCode)
            .orElseGet(ArrayList::new);

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
     * @param CourseId courseId
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException if the course does not exist
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
     * @param String courseName
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException
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
     * @param Register register
     * @param String courseName
     * @param int grade
     * @param boolean withHonors
     * @param LocalDate date
     * @return ExaminationDto
     * @throws ObjectAlreadyExistsException if the examination already exists.
     * @throws ObjectNotFoundException if the student or course does not exist.
     * @throws IllegalArgumentException if the date is in the past or the grade
     *                                  is not between 0 and 30 or Degree course
     *                                  does not match
     */
    @Override
	@Transactional
    public Examination addNewExamination(
        Register register,
        String courseName,
        int grade,
        boolean withHonors,
        LocalDate date
    ) throws ObjectAlreadyExistsException {

        Course course = courseRepository
            .findByName(courseName)
            .orElseThrow(() -> new ObjectNotFoundException(courseName, EXCEPTION_COURSE_IDENTIFIER));

        Student student = studentRepository
            .findByRegister(register)
            .orElseThrow(() -> new ObjectNotFoundException(register));

        // sanity check
        List<Examination> examinations = examinationRepository.findExaminationsByCourseName(courseName);
        examinations.forEach(examination -> {
            if (examination.getStudent().getRegister().equals(register))
                throw new ObjectAlreadyExistsException(courseName + " for student with register " + register, EXCEPTION_EXAMINATION_IDENTIFIER);
        });

        // sanity checks
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
     * @param Register oldRegister
     * @param String oldCourseName
     * @param Register newRegister
     * @param String newCourseName
     * @param int grade
     * @param boolean withHonors
     * @param LocalDate date
     * @return Examination
     * @throws ObjectNotFoundException if the student or course does not exist.
     * @throws IllegalArgumentException if the date is in the past or the grade
     *                                  is not between 0 and 30 or Degree course
     *                                  does not match
     */
    @Override
	@Transactional
    public Examination updateExamination(
        Register oldRegister,
        String oldCourseName,
        Register newRegister,
        String newCourseName,
        int grade,
        boolean withHonors,
        LocalDate date
    ) throws ObjectAlreadyExistsException {

		Student newStudent = studentRepository
			.findByRegister(newRegister)
			.orElseThrow(() -> new ObjectNotFoundException(newRegister));

		Course newCourse = courseRepository
			.findByName(newCourseName)
            .orElseThrow(() -> new ObjectNotFoundException(newCourseName, EXCEPTION_COURSE_IDENTIFIER));

        Examination updatableExamination = examinationRepository
            .findExaminationsByCourseName(oldCourseName)
            .stream()
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
     * @param Register register
     * @param String name
     * @throws ObjectNotFoundException if the examination does not exist
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

