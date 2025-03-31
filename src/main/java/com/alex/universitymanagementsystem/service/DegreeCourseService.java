package com.alex.universitymanagementsystem.service;

import java.util.List;

import org.springframework.lang.NonNull;

import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.StudentDto;



public interface DegreeCourseService {

    /**
     * @return List<DegreeCourseDto>
     */
    List<DegreeCourseDto> getDegreeCourses();


    /**
     * Retrieves a degree course from the repository by its name and
     * maps it to a DTO.
     * @param name the name of the degree course
     * @return DegreeCourseDto object representing the degree course
     *         with the given name.
     * @throws NullPointerException if the name is null
     * @throws IllegalArgumentException if the name is empty.
     * @throws UnsupportedOperationException if the name is not unique
     */
    DegreeCourse getDegreeCourseByName(@NonNull String name)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;


    /**
     * Retrieves all courses of a given degree course from the repository and
     * maps them to DTOs.
     * @param name the name of the degree course
     * @return List<CourseDto> objects representing all courses of the given
     *         degree course.
     * @throws NullPointerException if the name is null.
     * @throws IllegalArgumentException if the name is empty.
     * @throws UnsupportedOperationException if the name is not unique
     */
    List<CourseDto> getCourses(@NonNull String name)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;


    /**
     * Retrieves all professors of a given degree course from the
     * repository and maps them to DTOs.
     * @param name the name of the degree course
     * @return List<ProfessorDto> representing all professors of the given
     *         degree course
     * @throws NullPointerException if the name is null.
     * @throws IllegalArgumentException if the name is blank.
     * @throws UnsupportedOperationException if the name is not unique.
     */
    List<ProfessorDto> getProfessors(@NonNull String name)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;


    /**
     * Retrieves all students of a given degree course from the
     * repository and maps them to DTOs.
     * @param name the name of the degree course.
     * @return List of StudentDto objects representing all students of the
     *         given degree course.
     * @throws NullPointerException if the name is null.
     * @throws IllegalArgumentException if the name is blank.
     * @throws UnsupportedOperationException if the name is not unique
     */
    List<StudentDto> getStudents(@NonNull String name)
        throws NullPointerException, IllegalArgumentException, UnsupportedOperationException;
}
