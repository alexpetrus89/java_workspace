package com.alex.studentmanagementsystem.service;

import java.util.List;

import com.alex.studentmanagementsystem.dto.CourseDto;
import com.alex.studentmanagementsystem.dto.DegreeCourseDto;
import com.alex.studentmanagementsystem.dto.ProfessorDto;
import com.alex.studentmanagementsystem.dto.StudentDto;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;

public interface DegreeCourseService {

    List<DegreeCourseDto> getDegreeCourses();

    DegreeCourseDto getDegreeCourseByName(String name)
        throws ObjectNotFoundException;

    List<CourseDto> getCoursesOfDegreeCourse(String name)
        throws ObjectNotFoundException;

    List<StudentDto> getStudentsOfDegreeCourse(String name)
        throws ObjectNotFoundException;

    List<ProfessorDto> getProfessorsOfDegreeCourse(String name)
        throws ObjectNotFoundException;

}
