package com.alex.universitymanagementsystem.component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.dto.ExaminationAppealDto;
import com.alex.universitymanagementsystem.dto.ExaminationDto;
import com.alex.universitymanagementsystem.dto.ExaminationOutcomeDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.entity.Course;
import com.alex.universitymanagementsystem.entity.DegreeCourse;
import com.alex.universitymanagementsystem.entity.Examination;
import com.alex.universitymanagementsystem.entity.ExaminationAppeal;
import com.alex.universitymanagementsystem.entity.ExaminationOutcome;
import com.alex.universitymanagementsystem.entity.Professor;
import com.alex.universitymanagementsystem.entity.Student;
import com.alex.universitymanagementsystem.entity.immutable.Register;
import com.alex.universitymanagementsystem.entity.immutable.UniqueCode;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.ExaminationAppealMapper;
import com.alex.universitymanagementsystem.mapper.ExaminationMapper;
import com.alex.universitymanagementsystem.mapper.ExaminationOutcomeMapper;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationAppealRepository;
import com.alex.universitymanagementsystem.repository.ExaminationOutcomeRepository;
import com.alex.universitymanagementsystem.repository.ExaminationRepository;
import com.alex.universitymanagementsystem.repository.ProfessorRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;

@Component
public class ServiceHelpers {

    // instance variables
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final DegreeCourseRepository degreeCourseRepository;
    private final ExaminationRepository examinationRepository;
    private final ExaminationAppealRepository examinationAppealRepository;
    private final ExaminationOutcomeRepository examinationOutcomeRepository;

    public ServiceHelpers(
        StudentRepository studentRepository,
        ProfessorRepository professorRepository,
        CourseRepository courseRepository,
        DegreeCourseRepository degreeCourseRepository,
        ExaminationRepository examinationRepository,
        ExaminationAppealRepository examinationAppealRepository,
        ExaminationOutcomeRepository examinationOutcomeRepository
    ) {
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
        this.degreeCourseRepository = degreeCourseRepository;
        this.examinationRepository = examinationRepository;
        this.examinationAppealRepository = examinationAppealRepository;
        this.examinationOutcomeRepository = examinationOutcomeRepository;
    }



    /**
     * Fetches a student by their register.
     * @param register the register of the student
     * @return the student entity
     */
    public Student fetchStudent(String register) {
        return studentRepository
            .findByRegister(new Register(register))
            .orElseThrow(() -> new ObjectNotFoundException(DomainType.STUDENT));
    }


    /**
     * Fetches a Professor entity by its unique code.
     * @param uniqueCode the unique code of the professor
     * @return the Professor entity
     * @throws ObjectNotFoundException if the professor does not exist
     */
    public Professor fetchProfessor(String uniqueCode) {
        return professorRepository
            .findByUniqueCode(new UniqueCode(uniqueCode))
            .orElseThrow(() -> new ObjectNotFoundException(DomainType.PROFESSOR));
    }


    /**
     * Fetches a Course entity by its name and degree course name.
     * @param courseName the name of the course
     * @param degreeCourseName the name of the degree course
     * @return the Course entity
     * @throws ObjectNotFoundException if the course does not exist
     */
    public Course fetchCourse(String courseName, String degreeCourseName) {
        return courseRepository
            .findByNameAndDegreeCourseName(courseName, degreeCourseName.toUpperCase())
            .orElseThrow(() -> new ObjectNotFoundException(DomainType.COURSE));
    }


    /**
     * Fetches courses by their unique code.
     * @param uniqueCode the unique code of the course
     * @return a set of course entities
     */
    public Set<Course> fetchCourses(String uniqueCode) {
        return courseRepository.findByProfessor(new UniqueCode(uniqueCode));
    }


    /**
     * Fetches a DegreeCourse entity by its name.
     * @param name the name of the degree course
     * @return the DegreeCourse entity
     * @throws ObjectNotFoundException if the degree course does not exist
     */
    public DegreeCourse fetchDegreeCourse(String name) {
        return degreeCourseRepository
            .findByName(name.toUpperCase())
            .orElseThrow(() -> new ObjectNotFoundException(DomainType.DEGREE_COURSE));
    }


    /**
     * Fetches all examinations for a student by their register.
     * @param register the register of the student
     * @return a list of Examination entities
     */
    public List<Examination> fetchExaminations(String register) {
        return examinationRepository.findByRegister(register);
    }


    /**
     * Finds an existing examination for a student in a specific course and degree program.
     * @param course the course entity
     * @param register the student's register
     * @param degreeCourseName the name of the degree course
     * @return the existing Examination entity
     * @throws ObjectNotFoundException if the examination does not exist
     */
    public Examination findExistingExamination(Course course, String register, String degreeCourseName) {
        return examinationRepository
            .findByCourse_Id_Id(course.getId().getId())
            .stream()
            .filter(e -> e.getRegister().equals(register))
            .filter(e -> e.getCourse().getDegreeCourse().getName().equals(degreeCourseName))
            .findFirst()
            .orElseThrow(() -> new ObjectNotFoundException(DomainType.EXAMINATION));
    }


    /**
     * Fetches an ExaminationAppeal entity by its ID.
     * @param id the ID of the examination appeal
     * @return the ExaminationAppeal entity
     * @throws ObjectNotFoundException if the examination appeal does not exist
     */
    public ExaminationAppeal fetchExaminationAppeal(Long id) throws ObjectNotFoundException {
        return examinationAppealRepository
            .findById(id)
            .orElseThrow(() -> new ObjectNotFoundException(DomainType.EXAMINATION_APPEAL));
    }


    /**
     * Fetches an ExaminationOutcome entity by its ID.
     * @param id
     * @return
     * @throws ObjectNotFoundException
     */
    public ExaminationOutcome fetchExaminationOutcome(Long id) throws ObjectNotFoundException {
        return examinationOutcomeRepository
            .findById(id)
            .orElseThrow(() -> new ObjectNotFoundException(DomainType.EXAMINATION_OUTCOME));
    }


    /**
     * Maps a list of Examination entities to a list of ExaminationDto.
     * @param exams the list of Examination entities
     * @return the list of ExaminationDto
     */
    public List<ExaminationDto> mapExaminations(List<Examination> exams) {
        return exams.stream().map(ExaminationMapper::toDto).toList();
    }


    /**
     * Maps an examination appeal entity to a data transfer object.
     * @param appeal the examination appeal entity
     * @return the examination appeal data transfer object
     */
    public ExaminationAppealDto mapAppealToDto(ExaminationAppeal appeal) {
        Set<StudentDto> students = studentRepository
            .findByRegisterIn(appeal.getRegisters())
            .stream()
            .map(StudentMapper::toDto)
            .collect(Collectors.toSet());
        return ExaminationAppealMapper.toDto(appeal, students);
    }


    /**
     * Maps an examination appeal entity to a data transfer object.
     * @param appeal the examination appeal entity
     * @return the examination appeal data transfer object
     */
    public ExaminationOutcomeDto mapOutcomeToDto(ExaminationOutcome outcome) {
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

