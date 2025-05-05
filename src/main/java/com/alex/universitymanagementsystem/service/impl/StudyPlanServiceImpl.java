package com.alex.universitymanagementsystem.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.StudyPlanDto;
import com.alex.universitymanagementsystem.enum_type.DomainType;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.CourseMapper;
import com.alex.universitymanagementsystem.mapper.ExaminationMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationRepository;
import com.alex.universitymanagementsystem.repository.StudentRepository;
import com.alex.universitymanagementsystem.repository.StudyPlanRepository;
import com.alex.universitymanagementsystem.service.StudyPlanService;


@Service
public class StudyPlanServiceImpl implements StudyPlanService {

    // logger
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(StudyPlanServiceImpl.class);

    // constants
    private static final String DATA_ACCESS_ERROR = "An error occurred while accessing the database.";

    // instance variables
    private final StudyPlanRepository studyPlanRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final DegreeCourseRepository degreeCourseRepository;
    private final ExaminationRepository examinationRepository;
    private final ExaminationAppealServiceImpl examinationAppealServiceImpl;

    public StudyPlanServiceImpl(
        StudyPlanRepository studyPlanRepository,
        StudentRepository studentRepository,
        DegreeCourseRepository degreeCourseRepository,
        CourseRepository courseRepository,
        ExaminationRepository examinationRepository,
        ExaminationAppealServiceImpl examinationAppealServiceImpl
    ) {
        this.studyPlanRepository = studyPlanRepository;
        this.studentRepository = studentRepository;
        this.degreeCourseRepository = degreeCourseRepository;
        this.courseRepository = courseRepository;
        this.examinationRepository = examinationRepository;
        this.examinationAppealServiceImpl = examinationAppealServiceImpl;
    }

    /**
     * Retrieves the ordering of the study plan
     * @param register the register of the student
     * @return the ordering of the study plan
     */
    @Override
    public String getOrderingByRegister(@NonNull Register register) {
        // retrieve student ordering
        return studentRepository
            .findByRegister(register)
            .getStudyPlan()
            .getOrdering();
    }


    /**
     * Retrieves the courses of the study plan
     * @param register the register of the student
     * @return the courses of the study plan
     */
    @Override
    public Set<CourseDto> getCoursesByRegister(@NonNull Register register) {
        // retrieve the courses of the study plan
        return studentRepository
            .findByRegister(register)
            .getStudyPlan()
            .getCourses()
            .stream()
            .map(CourseMapper::mapToCourseDto)
            .collect(Collectors.toSet());
    }


    /**
     * Retrieves the study plan of the student
     * @param register the register of the student
     * @return the study plan of the student
     * @throws NullPointerException if the register is null
     */
    @Override
    public StudyPlanDto getStudyPlanByRegister(@NonNull Register register) {
        // retrieve the ordering of the study plan
        String ordering = studentRepository
            .findByRegister(register)
            .getStudyPlan()
            .getOrdering();
        // retrieve the courses of the study plan
        Set<Course> courses =  studentRepository
            .findByRegister(register)
            .getStudyPlan()
            .getCourses()
            .stream()
            .collect(Collectors.toSet());

        return new StudyPlanDto(ordering, courses);
    }


    /**
     * Change courses to the study plan
     * @param register student register
     * @param degreeCourseOfNewCourse name of degree course of new course
     * @param degreeCourseOfOldCourse name of degree course of old course
     * @param courseToAddName name of the course to add
     * @param courseToRemoveName name of the course to remove
     * @throws NullPointerException if any of the parameters are null
     * @throws ObjectAlreadyExistsException if a course with the same name already exists
     * @throws ObjectNotFoundException if a degree course with the given name does not exist
     * @throws IllegalArgumentException if the cfu of the new course is not equal to the cfu
     *                                  of the old course
     * @throws IllegalStateException if the course has examinations
     */
    @Override
    @Transactional
    public void changeCourse(
        @NonNull Register register,
        @NonNull String degreeCourseOfNewCourse,
        @NonNull String degreeCourseOfOldCourse,
        @NonNull String courseToAddName,
        @NonNull String courseToRemoveName
    ) throws NullPointerException, ObjectAlreadyExistsException, ObjectNotFoundException, IllegalArgumentException, IllegalStateException
    {
        // sanity checks
        if(courseToAddName.equals(courseToRemoveName))
            throw new ObjectAlreadyExistsException(DomainType.COURSE);

        try {
            // retrieve all data
            Student student = studentRepository.findByRegister(register);
            DegreeCourse degreeCourseNew = degreeCourseRepository.findByName(degreeCourseOfNewCourse.toUpperCase());
            DegreeCourse degreeCourseOld = degreeCourseRepository.findByName(degreeCourseOfOldCourse.toUpperCase());
            Course courseToAdd = courseRepository.findByNameAndDegreeCourse(courseToAddName, degreeCourseNew);
            Course courseToRemove = courseRepository.findByNameAndDegreeCourse(courseToRemoveName, degreeCourseOld);

            // sanity checks
            if(courseToRemove == null)
                throw new ObjectNotFoundException(DomainType.COURSE);

            if(!courseToAdd.getCfu().equals(courseToRemove.getCfu()))
                throw new IllegalArgumentException("courses needs to have same cfu");

            // if the course to remove has examinations, throw exception
            if(examinationRepository
                .findExaminationsByStudent(register)
                .stream()
                .map(ExaminationMapper::mapToExaminationDto)
                .anyMatch(exam -> exam.getCourse().getCourseId().equals(courseToRemove.getCourseId())))
                throw new IllegalStateException("cannot remove course that has examinations");

            // if the course to remove has examinations appeals, delete them
            examinationAppealServiceImpl
                .getExaminationAppealsBooked(register)
                .stream()
                .filter(exam -> exam.getCourse().getCourseId().equals(courseToRemove.getCourseId()))
                .forEach(examAppeal -> examinationAppealServiceImpl
                    .deleteBookedExaminationAppeal(examAppeal.getId(), register));

            // add the new course
            student.getStudyPlan().addCourse(courseToAdd);
            // remove the old course
            student.getStudyPlan().removeCourse(courseToRemove);
            // save
            studyPlanRepository.saveAndFlush(student.getStudyPlan());
        } catch (DataAccessException e) {
            logger.error(DATA_ACCESS_ERROR, e);
        }
    }


}
