package com.alex.universitymanagementsystem.service;

import java.util.Set;

import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;



public interface StudyPlanService {

    /**
     * return study plan ordering
     * @param register the student register
     * @return String
     * @throws ObjectNotFoundException if the study plan does not exist
     */
    String getOrderingByRegister(@NonNull Register register) throws ObjectNotFoundException;

    /**
     * return list of courses
     * @param register the student register
     * @return Set<CourseDto>
     */
    Set<CourseDto> getCoursesByRegister(@NonNull Register register);


    /**
     * Change courses to the study plan
     * @param register student register
     * @param string name of degree course of new course
     * @param string name of degree course of old course
     * @param string name of the course to add
     * @param string name of the course to remove
     * @throws NullPointerException if any of the parameters are null
     * @throws ObjectAlreadyExistsException if a course with the same name already exists
     * @throws ObjectNotFoundException if a degree course with the given name does not exist
     * @throws IllegalArgumentException if the cfu of the new course is not equal to the cfu of the old course
     */
    void changeCourse(
        @NonNull Register register,
        @NonNull String degreeCourseOfNewCourse,
        @NonNull String degreeCourseOfOldCourse,
        @NonNull String courseToAddName,
        @NonNull String courseToRemoveName
    ) throws NullPointerException, ObjectAlreadyExistsException, ObjectNotFoundException, IllegalArgumentException;


}
