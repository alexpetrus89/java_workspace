package com.alex.universitymanagementsystem.service;

import java.util.List;

import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;



public interface DegreeCourseService {

    /**
     * @return List<DegreeCourseDto>
     */
    List<DegreeCourseDto> getDegreeCourses();


    /**
     * @param name the name of the degree course
     * @return DegreeCourseDto
     * @throws ObjectNotFoundException if no degree course with the given name exists
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is empty
     * @throws UnsupportedOperationException if the name is not unique
     */
    DegreeCourseDto getDegreeCourseByName(@NonNull String name)
        throws ObjectNotFoundException;


    /**
     * @param name the name of the degree course
     * @return List<StudentDto>
     * @throws ObjectNotFoundException if no degree course with the given name exists
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is empty
     * @throws UnsupportedOperationException if the name is not unique
     */
    List<StudentDto> getStudents(@NonNull String name)
        throws ObjectNotFoundException;


    /**
     * Retrieves all professors of a given degree course from the repository
     * and maps them to DTOs.
     * @param name the name of the degree course
     * @return List<ProfessorDto>
     * @throws ObjectNotFoundException if no degree course with the given name exists
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is empty
     * @throws UnsupportedOperationException if the name is not unique
     */
    public List<ProfessorDto> getProfessors(@NonNull String name)
        throws ObjectNotFoundException;


    /**
     * Retrieves all courses of a given degree course from the repository
     * and maps them to DTOs.
     * @param name the name of the degree course
     * @return List<CourseDto>
     * @throws ObjectNotFoundException if no degree course with the given name exists
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is empty
     * @throws UnsupportedOperationException if the name is not unique
     */
    public List<CourseDto> getCourses(@NonNull String name)
        throws ObjectNotFoundException;

}
