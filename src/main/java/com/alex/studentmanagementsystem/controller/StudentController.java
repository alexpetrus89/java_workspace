package com.alex.studentmanagementsystem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alex.studentmanagementsystem.domain.Student;
import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.dto.StudentDto;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.mapper.StudentMapper;
import com.alex.studentmanagementsystem.service.implementation.StudentServiceImplementation;
import com.alex.studentmanagementsystem.utility.CreateView;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    private static final String ERROR = "error";
    private static final String NOT_FOUND_PATH = "exception/object-not-found";
    private static final String ALREADY_EXISTS_PATH = "exception/object-already-exists";

    // instance variable
    private final StudentServiceImplementation studentServiceImplementation;

    /** Autowired - dependency injection */
    public StudentController(
        StudentServiceImplementation studentServiceImplementation
    ) {
        this.studentServiceImplementation = studentServiceImplementation;
    }



    /** GET request */
    @GetMapping(path = "/view/all")
	public ResponseEntity<List<StudentDto>> getStudents() {
        return new ResponseEntity<>(
            studentServiceImplementation.getStudents(),
            HttpStatus.OK
        );
    }

    @GetMapping(path = "/view/{studentRegister}")
    public ResponseEntity<Optional<StudentDto>> getStudentsByRegister(
        @PathVariable Register studentRegister
    ) {
        try {
            StudentDto studentDto =
                studentServiceImplementation.getStudentByRegister(studentRegister);
            return new ResponseEntity<>(Optional.of(studentDto), HttpStatus.OK);
        } catch (ObjectNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/view/{studentName}")
    public ResponseEntity<Optional<StudentDto>> getStudentsByName(
        @PathVariable String studentName
    ) {
        try {
            StudentDto studentDto =
                studentServiceImplementation.getStudentByName(studentName);
                return new ResponseEntity<>(Optional.of(studentDto), HttpStatus.OK);
        } catch (ObjectNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    /** PUT request */
    @PutMapping(path = "/update/{studentRegister}")
    @Transactional
    public void updateStudentOld(
        @PathVariable Register studentRegister,
        @RequestBody StudentDto studentDto
    ) {
        studentServiceImplementation.updateStudent(studentDto);
    }























    // model and view methods
    /** GET request */
    @GetMapping(path = "/view")
	public ModelAndView getStudentsAndReturnView() {
        List<StudentDto> students = studentServiceImplementation.getStudents();

        return new CreateView(
            "students",
            students,
            "student/student-list"
        ).getModelAndView();
    }

    /**GET request*/
    @GetMapping(path = "/read/register")
	public ModelAndView getStudentByRegisterAndReturnView(
        @RequestParam String studentRegister
    ) {
        try {
            StudentDto studentDto =
                studentServiceImplementation
                    .getStudentByRegister(new Register(studentRegister));

            return new CreateView(
                StudentMapper.mapToStudent(studentDto),
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

    @GetMapping(path = "/read/name")
	public ModelAndView getStudentByNameAndReturnView(
        @RequestParam String studentName
    ) {
        try{
            StudentDto studentDto =
                studentServiceImplementation.getStudentByName(studentName);

            return new CreateView(
                StudentMapper.mapToStudent(studentDto),
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


    @GetMapping("/create")
    public ModelAndView createNewStudentAndReturnView() {
        return new CreateView(
            new Student(),
            "student/create/create"
        ).getModelAndView();
    }


    @GetMapping("/update")
    public ModelAndView updateStudentAndReturnView() {
        return new CreateView(
            new Student(),
            "student/update/update"
        ).getModelAndView();
    }




    /**POST request*/
    @PostMapping("/create")
    @Transactional // con l'annotazione transactional effettua una gestione propria degli errori
    public ModelAndView createNewStudent(@ModelAttribute StudentDto studentDto){
        try{
            studentServiceImplementation.addNewStudent(studentDto);

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
    @PutMapping("/update")
    @Transactional
    public ModelAndView updateStudent(@ModelAttribute StudentDto studentDto) {

        try {
            studentServiceImplementation.updateStudent(studentDto);

            return new CreateView(
                StudentMapper.mapToStudent(studentDto),
                "student/update/update-result"
            ).getModelAndView();

        } catch (ObjectNotFoundException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }




    /**DELETE request*/
    @DeleteMapping(path = "/delete/register")
    @Transactional
    public ModelAndView deleteStudentByRegister(@RequestParam Register studentRegister)
    {
        try {
            studentServiceImplementation.deleteStudent(studentRegister);

            return new CreateView("student/delete/delete-result")
                .getModelAndView();

        } catch (ObjectNotFoundException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }

    @DeleteMapping(path = "/delete/name")
    @Transactional
    public ModelAndView deleteStudentByName(@RequestParam String studentName) {
        try{
            StudentDto studentDto =
                studentServiceImplementation.getStudentByName(studentName);
            studentServiceImplementation.deleteStudent(studentDto.getRegister());

            return new CreateView("student/delete/delete-result")
                .getModelAndView();

        } catch (ObjectNotFoundException e) {
            return new CreateView(
                ERROR,
                e.getMessage(),
                NOT_FOUND_PATH
            ).getModelAndView();
        }
    }

}
