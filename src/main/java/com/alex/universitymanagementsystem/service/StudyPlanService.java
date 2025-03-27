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
    String getOrderingByRegister(Register register) throws ObjectNotFoundException;

    /**
     * return list of courses
     * @param register the student register
     * @return Set<CourseDto>
     * @throws ObjectNotFoundException if the study plan does not exist
     */
    Set<CourseDto> getCoursesByRegister(@NonNull Register register) throws ObjectNotFoundException;


    void changeCourse(
        @NonNull Register register,
        @NonNull String degreeCourseOfNewCourse,
        @NonNull String degreeCourseOfOldCourse,
        @NonNull String courseToAddName,
        @NonNull String courseToRemoveName
    ) throws ObjectAlreadyExistsException, ObjectNotFoundException;


}
