package com.alex.universitymanagementsystem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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

    // constants
    private static final String ATTRIBUTE_STUDENT = "student";
    private static final String ATTRIBUTE_STUDENTS = "students";
    private static final String ERROR = "error/error";
    private static final String NOT_FOUND_PATH = "exception/object-not-found";
    private static final String ALREADY_EXISTS_PATH = "exception/object-already-exists";

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
     * @throws ObjectNotFoundException if the student is not found
     * @throws IllegalArgumentException if the register is null or is empty
     * @throws UnsupportedOperationException if the register is not unique
     */
    @GetMapping(path = "/read/register")
	public ModelAndView getStudentByRegister(@RequestParam String register) {

        try {
            Student student = StudentMapper.mapToStudent(studentServiceImpl.getStudentByRegister(new Register(register)));
            return new ModelAndView("student/read/read-result", ATTRIBUTE_STUDENT, student);

        } catch (ObjectNotFoundException e) {
            return new ModelAndView(ERROR, e.getMessage(),NOT_FOUND_PATH);
        }
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
        } catch (ObjectNotFoundException e) {
            return new ModelAndView(ERROR, e.getMessage(),NOT_FOUND_PATH);
        }
    }


    /**
     * Creates a new student
     * @return ModelAndView
     */
    @GetMapping(path = "/create")
    public ModelAndView createNewStudentAndReturnView() {
        return new ModelAndView("student/create/create", ATTRIBUTE_STUDENT, new Student());
    }


    /**
     * Updates a student
     * @return ModelAndView
     */
    @GetMapping(path = "/update")
    public ModelAndView updateStudentAndReturnView() {
        return new ModelAndView("student/update/update", ATTRIBUTE_STUDENT, new Student());
    }


    /**POST request*/
    /**
     * Creates a new student
     * @param studentDto the student data transfer object
     * @return ModelAndView
     * @throws ObjectAlreadyExistsException if the student already exists
     * @throws ObjectNotFoundException if the degree course does not exist.
     * @throws IllegalArgumentException if the register is null or empty
     */
    @PostMapping(path = "/create")
    public ModelAndView createNewStudent(@ModelAttribute StudentDto studentDto) {

        try{
            studentServiceImpl.addNewStudent(StudentMapper.mapToStudent(studentDto));
            Student student = StudentMapper.mapToStudent(studentDto);
            return new ModelAndView("student/create/create-result", ATTRIBUTE_STUDENT, student);
        } catch (ObjectAlreadyExistsException e) {
            return new ModelAndView(ERROR, e.getMessage(),ALREADY_EXISTS_PATH);
        }
    }


    /**PUT request*/

    /**
     * Updates a student
     * @param studentDto the student data transfer object
     * @return ModelAndView
     * @throws ObjectNotFoundException if the student does not exist
     * @throws IllegalArgumentException if the register is null or empty
     * @throws UnsupportedOperationException if the register is not unique
     */
    @PutMapping(path = "/update")
    public ModelAndView updateStudent(@ModelAttribute StudentDto studentDto) {

        try {
            studentServiceImpl.updateStudent(studentDto);
            Student student = StudentMapper.mapToStudent(studentDto);
            return new ModelAndView("student/update/update-result", ATTRIBUTE_STUDENT, student);
        } catch (RuntimeException e) {
            return new ModelAndView(ERROR, e.getMessage(),NOT_FOUND_PATH);
        }
    }


    /**DELETE request*/
    /**
     * Deletes a student
     * @param register the register of the student
     * @return ModelAndView
     * @throws ObjectNotFoundException if the student does not exist
     * @throws IllegalArgumentException if the register is null or empty
     */
    @DeleteMapping(path = "/delete/register")
    public ModelAndView deleteStudentByRegister(@RequestParam String register)
    {
        try {
            studentServiceImpl.deleteStudent(new Register(register));
            return new ModelAndView("student/delete/delete-result");
        } catch (RuntimeException e) {
            Map<String, Object> model = new HashMap<>();
            model.put("title", "Errore");
            model.put("errorMessage", e.getMessage());
            model.put("stackTrace", e.getStackTrace());
            return new ModelAndView(ERROR, model);
        }
    }

}
