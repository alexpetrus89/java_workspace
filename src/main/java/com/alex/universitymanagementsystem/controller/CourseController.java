package com.alex.universitymanagementsystem.controller;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.dto.CreateCourseDto;
import com.alex.universitymanagementsystem.dto.DegreeCourseDto;
import com.alex.universitymanagementsystem.dto.ProfessorDto;
import com.alex.universitymanagementsystem.dto.UpdateCourseDto;
import com.alex.universitymanagementsystem.entity.Professor;
import com.alex.universitymanagementsystem.mapper.ProfessorMapper;
import com.alex.universitymanagementsystem.service.CourseService;
import com.alex.universitymanagementsystem.service.DegreeCourseService;
import com.alex.universitymanagementsystem.service.ProfessorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/course")
public class CourseController {

    // constants
    private static final String COURSE = "course";

    // instance variables
    private final CourseService courseService;
    private final ProfessorService professorService;
    private final DegreeCourseService degreeCourseService;

    public CourseController(CourseService courseService, ProfessorService professorService, DegreeCourseService degreeCourseService) {
        this.courseService = courseService;
        this.professorService = professorService;
        this.degreeCourseService = degreeCourseService;
    }


    /**
     * retrieves all courses
     * @return ModelAndView
     */
    @GetMapping(path = "/read/courses")
    public ModelAndView getAllCourses() {
        Set<CourseDto> courses = courseService.getCourses();
        return new ModelAndView("course/course-list", "courses", courses);
    }


    /**
     * retrieves a course by its name and degree course name
     * @param courseName
     * @param degreeCourseName
     * @return ModelAndView
     */
    @GetMapping(path = "/read/course")
    public ModelAndView getCourse(@RequestParam String courseName, @RequestParam String degreeCourseName) {
        CourseDto course = courseService.getCourseByNameAndDegreeCourseName(courseName, degreeCourseName);
        return new ModelAndView("User_admin/course/read/read-result", COURSE, course);
    }


    /**
     * retrieves all courses by professor
     * @return ModelAndView
     */
    @GetMapping(path = "/read/professor")
    public ModelAndView getCoursesByProfessor(@AuthenticationPrincipal Professor professor) {
        ProfessorDto professorDto = ProfessorMapper.toDto(professor);
        List<CourseDto> courses = courseService.getCoursesByProfessor(professorDto);
        return new ModelAndView("user_professor/courses/courses", "courses", courses);
    }


    /**
     * creates a new course
     * @return ModelAndView
     */
    @GetMapping(path = "/create")
    public ModelAndView instantiateCourseForCreate() {
        return new ModelAndView("User_admin/course/create/create", COURSE, new CreateCourseDto());
    }


    /**
     * Update Course
     * @return ModelAndView
     */
    @GetMapping(path = "/update")
    public ModelAndView instantiateCourseForUpdate() {
        return new ModelAndView("user_admin/course/update/update", COURSE, new UpdateCourseDto());
    }


    /**
     * create a new course
     * @param course CourseDto object containing course details
     * @return ModelAndView
     */
    @PostMapping(path = "/create")
    public ModelAndView createNewCourse(@Valid @ModelAttribute("course") CreateCourseDto formDto) {

        ProfessorDto professor = professorService.getProfessorByUniqueCode(formDto.getUniqueCode());
        DegreeCourseDto degreeCourse = degreeCourseService.getDegreeCourseByName(formDto.getDegreeCourseName());

        CourseDto courseDto = new CourseDto(
            formDto.getName(),
            formDto.getType(),
            formDto.getCfu(),
            professor,
            degreeCourse
        );

        CourseDto saved = courseService.addNewCourse(courseDto);
        return new ModelAndView("user_admin/course/create/create-result", COURSE, saved);
    }


    /**
     * update a course
     * @param courseDto CourseDto object containing updated course details
     * @return ModelAndView
     */
    @PutMapping(path = "/update")
    public ModelAndView updateCourse(@Valid @ModelAttribute UpdateCourseDto dto) {
        CourseDto course = courseService.updateCourse(dto);
        return new ModelAndView("user_admin/course/update/update-result", COURSE, course);
    }


    /**
     * deletes a course by name
     * @param courseName name of the course
     * @param degreeCourseName degree course name
     * @return ModelAndView
     */
    @DeleteMapping("/delete/{degreeCourseName}/{courseName}")
    public ModelAndView deleteCourseByName(@PathVariable String courseName, @PathVariable String degreeCourseName) {
        CourseDto courseDto = courseService.getCourseByNameAndDegreeCourseName(courseName, degreeCourseName);
        courseService.deleteByNameAndDegreeCourse(courseName, degreeCourseName);
        return new ModelAndView("user_admin/course/delete/delete-result", COURSE, courseDto);
    }



}

