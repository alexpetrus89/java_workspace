package com.alex.universitymanagementsystem.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.universitymanagementsystem.domain.Student;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.StudentDto;
import com.alex.universitymanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.mapper.StudentMapper;
import com.alex.universitymanagementsystem.service.impl.StudentServiceImpl;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    // logger
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    // constants
    private static final String ATTRIBUTE_STUDENT = "student";
    private static final String ATTRIBUTE_STUDENTS = "students";
    private static final String ERROR_URL = "/exception/error";
    private static final String NOT_FOUND_URL = "exception/object-not-found";

    // instance variable
    private final StudentServiceImpl studentServiceImpl;

    /** Autowired - dependency injection  - constructor */
    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }


    // model and view methods
    /** GET request */

    /**
     * Retrieves all students
     * @return ModelAndView
     */
    @GetMapping(path = "/view")
	public ModelAndView getStudentsAndReturnView() {
        List<StudentDto> students = studentServiceImpl.getStudents();
        return new ModelAndView("student/student-list", ATTRIBUTE_STUDENTS, students);
    }

    /**
     * Retrieves a student by register
     * @param register the register of the student
     * @return ModelAndView
     */
    @GetMapping(path = "/read/register")
	public ModelAndView getStudentByRegister(@RequestParam String register) {
        StudentDto student = studentServiceImpl.getStudentByRegister(new Register(register));
        return new ModelAndView("student/read/read-result", ATTRIBUTE_STUDENT, student);
    }

    /**
     * Retrieves a student by name
     * @param name the name of the student
     * @return ModelAndView
     * @throws ObjectNotFoundException if the student is not found
     * @throws IllegalArgumentException if the name is null or is empty
     * @throws UnsupportedOperationException if the name is not unique
     */
    @GetMapping(path = "/read/name")
	public ModelAndView getStudentsByName(@RequestParam String name) {

        try{
            List<Student> students = studentServiceImpl
                .getStudentsByFullname(name.toLowerCase())
                .stream()
                .map(StudentMapper::mapToStudent)
                .toList();
            return new ModelAndView("student/read/read-results", ATTRIBUTE_STUDENTS, students);
        } catch (ObjectNotFoundException | IllegalArgumentException | UnsupportedOperationException e) {
            logger.error(e.getMessage(), e);
            return new ModelAndView(ERROR_URL, e.getMessage(),NOT_FOUND_URL);
        }
    }


    /**
     * Updates a student
     * @return ModelAndView
     */
    @GetMapping(path = "/update")
    public ModelAndView updateStudentAndReturnView() {
        return new ModelAndView("student/update/update", ATTRIBUTE_STUDENT, new Student());
    }


    /**PUT request*/

    /**
     * Updates a student
     * @param studentDto the student data transfer object
     * @return ModelAndView
     */
    @PutMapping(path = "/update")
    public ModelAndView updateStudent(@ModelAttribute StudentDto studentDto) {

        try {
            studentServiceImpl.updateStudent(studentDto);
            Student student = StudentMapper.mapToStudent(studentDto);
            return new ModelAndView("student/update/update-result", ATTRIBUTE_STUDENT, student);
        } catch (ObjectAlreadyExistsException | ObjectNotFoundException | IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            return new ModelAndView(ERROR_URL, e.getMessage(), NOT_FOUND_URL);
        }
    }


}
