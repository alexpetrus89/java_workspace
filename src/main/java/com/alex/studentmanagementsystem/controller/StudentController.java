package com.alex.studentmanagementsystem.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.studentmanagementsystem.domain.Student;
import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.dto.StudentDto;
import com.alex.studentmanagementsystem.exception.ObjectAlreadyExistsException;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.StudentMapper;
import com.alex.studentmanagementsystem.service.implementation.StudentServiceImpl;
import com.alex.studentmanagementsystem.utility.CreateView;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    // constants
    private static final String ERROR = "error";
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

        return new CreateView(
            "students",
            studentServiceImpl.getStudents(),
            "student/student-list"
        ).getModelAndView();
    }

    /**
     * Retrieves a student by register
     * @param studentRegister the registration number to search
     * @return ModelAndView
     * @throws ObjectNotFoundException if the student is not found
     * @throws NullPointerException if the register is null
     */
    @GetMapping(path = "/read/register")
	public ModelAndView getStudentByRegister(@RequestParam String register) {

        try {
            return new CreateView(
                StudentMapper.mapToStudent(
                    studentServiceImpl.getStudentByRegister(new Register(register))
                ),
                "student/read/read-result"
            ).getModelAndView();

        } catch (ObjectNotFoundException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }

    /**
     * Retrieves a student by name
     * @param String studentName
     * @return ModelAndView
     * @throws ObjectNotFoundException if the student is not found
     * @throws NullPointerException if the name is null
     */
    @GetMapping(path = "/read/name")
	public ModelAndView getStudentByName(@RequestParam String name) {

        try{
            return new CreateView(
                StudentMapper.mapToStudent(studentServiceImpl.getStudentByName(name)),
                "student/read/read-result"
            ).getModelAndView();

        } catch (ObjectNotFoundException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }

    /**
     * Creates a new student
     * @return ModelAndView
     */
    @GetMapping("/create")
    public ModelAndView createNewStudentAndReturnView() {
        return new CreateView(
            new Student(),
            "student/create/create"
        ).getModelAndView();
    }

    /**
     * Updates a student
     * @return ModelAndView
     */
    @GetMapping("/update")
    public ModelAndView updateStudentAndReturnView() {
        return new CreateView(
            new Student(),
            "student/update/update"
        ).getModelAndView();
    }


    /**POST request*/

    /**
     * Creates a new student
     * @param studentDto the student data transfer object
     * @return ModelAndView
     * @throws ObjectAlreadyExistsException if the student already exists
     * @throws ObjectNotFoundException if the degree course does not exist.
     * @throws NullPointerException if the student data transfer object is null
     * @throws IllegalArgumentException if the register is null or empty
     */
    @PostMapping("/create")
    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public ModelAndView createNewStudent(@ModelAttribute StudentDto studentDto) {

        try{
            studentServiceImpl.addNewStudent(studentDto);

            return new CreateView(
                StudentMapper.mapToStudent(studentDto),
                "student/create/create-result"
            ).getModelAndView();

        } catch (RuntimeException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                ALREADY_EXISTS_PATH
            ).getModelAndView();
        }
    }


    /**PUT request*/

    /**
     * Updates a student
     * @param studentDto the student data transfer object
     * @return ModelAndView
     * @throws ObjectNotFoundException if the student does not exist
     * @throws NullPointerException if the student data transfer object is null
     * @throws IllegalArgumentException if the register is null or empty
     */
    @PutMapping("/update")
    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public ModelAndView updateStudent(@ModelAttribute StudentDto studentDto) {

        try {
            studentServiceImpl.updateStudent(studentDto);

            return new CreateView(
                StudentMapper.mapToStudent(studentDto),
                "student/update/update-result"
            ).getModelAndView();

        } catch (RuntimeException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }


    /**DELETE request*/
    /**
     * Deletes a student
     * @param Register studentRegister
     * @return ModelAndView
     * @throws ObjectNotFoundException if the student does not exist
     */
    @DeleteMapping(path = "/delete/register")
    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public ModelAndView deleteStudentByRegister(@RequestParam Register register)
    {
        try {
            studentServiceImpl.deleteStudent(register);

            return new CreateView("student/delete/delete-result")
                .getModelAndView();

        } catch (RuntimeException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }

    /**
     * Deletes a student
     * @param String name
     * @return ModelAndView
     * @throws ObjectNotFoundException
     * @throws NullPointerException
     */
    @DeleteMapping(path = "/delete/name")
    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public ModelAndView deleteStudentByName(@RequestParam String name) {

        try{
            StudentDto studentDto = studentServiceImpl.getStudentByName(name);
            studentServiceImpl.deleteStudent(studentDto.getRegister());

            return new CreateView("student/delete/delete-result")
                .getModelAndView();

        } catch (RuntimeException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }

}
