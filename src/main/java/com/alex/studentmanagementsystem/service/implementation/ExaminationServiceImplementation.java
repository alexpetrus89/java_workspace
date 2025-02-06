package com.alex.studentmanagementsystem.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.alex.studentmanagementsystem.domain.Course;
import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;
import com.alex.studentmanagementsystem.dto.ExaminationDto;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.ExaminationMapper;
import com.alex.studentmanagementsystem.repository.CourseRepository;
import com.alex.studentmanagementsystem.repository.ExaminationRepository;
import com.alex.studentmanagementsystem.repository.ProfessorRepository;
import com.alex.studentmanagementsystem.repository.StudentRepository;
import com.alex.studentmanagementsystem.service.ExaminationService;


@Service
public class ExaminationServiceImplementation
    implements ExaminationService {

    // constants
    private static final String EXCEPTION_COURSE_IDENTIFIER = "course";

    // instance variables
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final ExaminationRepository examinationRepository;


    // autowired - dependency injection - constructor
    public ExaminationServiceImplementation(
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
     * @return List<ExaminationDto>
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
     * @param Register register
     * @return List<ExaminationDto>
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
     * @param UniqueCode uniqueCode
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException
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
     * @param CourseId courseId
     * @return List<ExaminationDto>
     * @throws ObjectNotFoundException
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


}

