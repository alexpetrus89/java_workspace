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

    /**
     * add a new course to the study plan
     * @param register student register
     * @param string name of the course to add
     * @throws ObjectAlreadyExistsException
     */
    void addCourse(@NonNull Register register, @NonNull String name) throws ObjectAlreadyExistsException;

    /**
     * remove course from study plan
     * @param register student register
     * @param name name of the course to remove
     * @throws ObjectNotFoundException
     */
    void removeCourse(@NonNull Register register, @NonNull String name) throws ObjectNotFoundException;

}
