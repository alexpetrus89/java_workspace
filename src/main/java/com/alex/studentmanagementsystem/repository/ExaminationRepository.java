package com.alex.studentmanagementsystem.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alex.studentmanagementsystem.domain.Examination;
import com.alex.studentmanagementsystem.domain.immutable.CourseId;
import com.alex.studentmanagementsystem.domain.immutable.ExaminationId;
import com.alex.studentmanagementsystem.domain.immutable.Register;

@Repository
public interface ExaminationRepository
    extends JpaRepository<Examination, ExaminationId>
{

    @Query("SELECT e FROM Examination e WHERE e.student.register = :studentRegister")
    List<Examination> findExaminationsByStudent(@Param("studentRegister") Register studentRegister);

    @Query("SELECT e FROM Examination e WHERE e.course.id = :courseId")
    List<Examination> findExaminationsByCourseId(@Param("courseId") CourseId courseId);

    @Query("SELECT e FROM Examination e WHERE e.course.name = :courseName")
    List<Examination> findExaminationsByCourseName(@Param("courseName") String courseName);

}
