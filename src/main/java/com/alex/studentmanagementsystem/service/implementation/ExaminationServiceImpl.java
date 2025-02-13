package com.alex.studentmanagementsystem.service.implementation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public List<ExaminationDto> getExaminationsByStudentRegister(Register register)
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
    public List<ExaminationDto> getExaminationsByProfessorUniqueCode(UniqueCode uniqueCode)
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
    public List<ExaminationDto> getExaminationsByCourseId(CourseId courseId)
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
    public List<ExaminationDto> getExaminationsByCourseName(String courseName)
        throws ObjectNotFoundException
    {

        if (!courseRepository.existsByName(courseName))
            throw new ObjectNotFoundException(courseName, EXCEPTION_COURSE_IDENTIFIER);

        return examinationRepository
            .findExaminationsByCourseName(courseName)
            .stream()
            .map(ExaminationMapper::mapToExaminationDto)
            .toList();
    }

    /**
     * Add new examination
     * @param Register registration
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
    public ExaminationDto addNewExamination(
        Register registration,
        String courseName,
        int grade,
        boolean withHonors,
        LocalDate date
    ) throws ObjectAlreadyExistsException {

        Course course = courseRepository
            .findByName(courseName)
            .orElseThrow(() -> new ObjectNotFoundException(courseName, EXCEPTION_COURSE_IDENTIFIER));

        Student student = studentRepository
            .findByRegister(registration)
            .orElseThrow(() -> new ObjectNotFoundException(registration));

        // sanity check
        List<Examination> examinations = examinationRepository.findExaminationsByCourseName(courseName);
        examinations.forEach(examination -> {
            if (examination.getStudent().getRegister().equals(registration))
                throw new ObjectAlreadyExistsException(courseName + " for student with register " + registration, EXCEPTION_EXAMINATION_IDENTIFIER);
        });

        // sanity check
        if(!student.getDegreeCourse().getName().equals(course.getDegreeCourse().getName()))
            throw new IllegalArgumentException("Degree course does not match");

        // sanity check
        if (date.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Examination Date must be in the future");

        Examination examination = new Examination(
            course,
            student,
            grade,
            withHonors,
            date
        );

		examinationRepository.saveAndFlush(examination);

        return ExaminationMapper.mapToExaminationDto(examination);
    }

}

