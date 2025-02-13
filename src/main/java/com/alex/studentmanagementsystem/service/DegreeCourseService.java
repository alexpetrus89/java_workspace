package com.alex.studentmanagementsystem.service;

import java.util.List;

import com.alex.studentmanagementsystem.dto.DegreeCourseDto;
import com.alex.studentmanagementsystem.dto.StudentDto;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;

public interface DegreeCourseService {

    /**
     * @return List<DegreeCourseDto>
     */
    List<DegreeCourseDto> getDegreeCourses();

    /**
     * @param String name
     * @return DegreeCourseDto object
     * @throws ObjectNotFoundException if no degree course with the given name exists
     */
    DegreeCourseDto getDegreeCourseByName(String name)
        throws ObjectNotFoundException;

    /**
     * @param String name
     * @return List<StudentDto>
     * @throws ObjectNotFoundException if no degree course with the given name exists
     */
    List<StudentDto> getStudents(String name)
        throws ObjectNotFoundException;

}
