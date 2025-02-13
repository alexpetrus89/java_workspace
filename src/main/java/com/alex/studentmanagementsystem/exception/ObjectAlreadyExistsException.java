package com.alex.studentmanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ObjectAlreadyExistsException extends RuntimeException {

    // constants
    private static final String OBJECT_ALREADY_EXISTS = "object already exists";
    // student
    private static final String STUDENT_WITH_REGISTER = "student with registration number ";
	private static final String STUDENT_WITH_NAME = "student with name ";
    // professor
    private static final String PROFESSOR_WITH_FISCAL_CODE = "professor with fiscal code ";
    private static final String PROFESSOR_WITH_UNIQUE_CODE = "professor with unique code ";
    private static final String PROFESSOR_WITH_NAME = "professor with name ";
    // course
    private static final String COURSE_WITH_NAME = "course with name ";
    private static final String EXAMINATION_OF_COURSE_NAMED = "examination of course named ";

    private static final String ALREADY_EXISTS = " already exists";
    private static final String MIX = "%s%s%s";

    //instance variables
    private final String message;

    // constructors
    public ObjectAlreadyExistsException() {
        super();
        this.message = "UNKNOWN ERROR";
    }

    public ObjectAlreadyExistsException(Register register) {
        super(String.format(MIX,STUDENT_WITH_REGISTER, register,ALREADY_EXISTS));
        this.message = String.format(MIX,STUDENT_WITH_REGISTER, register,ALREADY_EXISTS);
    }

    public ObjectAlreadyExistsException(UniqueCode uniqueCode) {
        super(String.format(MIX, PROFESSOR_WITH_UNIQUE_CODE, uniqueCode, ALREADY_EXISTS));
        this.message = String.format(MIX, PROFESSOR_WITH_UNIQUE_CODE, uniqueCode, ALREADY_EXISTS);
    }

    public ObjectAlreadyExistsException(String message, String identifier) {
        super(String.format(OBJECT_ALREADY_EXISTS));
        switch(identifier) {
            case "student" -> this.message = String.format(MIX, STUDENT_WITH_NAME, message, ALREADY_EXISTS);
            case "professor" -> this.message = String.format(MIX, PROFESSOR_WITH_NAME, message, ALREADY_EXISTS);
            case "professor_fiscal_code" -> this.message = String.format(MIX, PROFESSOR_WITH_FISCAL_CODE, message, ALREADY_EXISTS);
            case "course" -> this.message = String.format(MIX, COURSE_WITH_NAME, message, ALREADY_EXISTS);
            case "examination" -> this.message = String.format(MIX, EXAMINATION_OF_COURSE_NAMED, message, ALREADY_EXISTS);
            case "user" -> this.message = String.format(message);
            default -> this.message = "UNKNOWN ERROR";
        }
    }

    // getters
    @Override
    public String getMessage() {
        return message;
    }

}
