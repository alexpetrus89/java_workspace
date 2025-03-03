package com.alex.universitymanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;


@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException {

    // constants
    private static final String OBJECT_NOT_FOUND = "object not found";
    // student
    private static final String STUDENT_WITH_REGISTER = "student with registration number ";
	private static final String STUDENT_WITH_NAME = "student with name ";
    // professor
    private static final String PROFESSOR_WITH_FISCAL_CODE = "professor with fiscal code ";
    private static final String PROFESSOR_WITH_UNIQUE_CODE = "professor with unique code ";
    private static final String PROFESSOR_WITH_NAME = "professor with name ";
    // course
    private static final String COURSE_WITH_NAME = "course with name ";
    // degree course
    private static final String DEGREE_COURSE_WITH_NAME = "course degree with name ";
    // examination
    private static final String EXAMINATION_OF_COURSE_NAMED = "examination of course named ";

	private static final String NOT_PRESENT = " not present";
    private static final String MIX = "%s%s%s";

    //instance variables
    private final String message;

    // constructors
    public ObjectNotFoundException() {
        super();
        this.message = "UNKNOWN ERROR";
    }

    // constructors
    public ObjectNotFoundException(Register register) {
        super(String.format(MIX, STUDENT_WITH_REGISTER, register, NOT_PRESENT));
        this.message = String.format(MIX, STUDENT_WITH_REGISTER, register, NOT_PRESENT);
    }

    public ObjectNotFoundException(UniqueCode uniqueCode) {
        super(String.format(MIX, PROFESSOR_WITH_UNIQUE_CODE, uniqueCode, NOT_PRESENT));
        this.message = String.format(MIX, PROFESSOR_WITH_UNIQUE_CODE, uniqueCode, NOT_PRESENT);
    }

    public ObjectNotFoundException(String message, String identifier) {
        super(String.format(OBJECT_NOT_FOUND));
        switch(identifier) {
            case "student" -> this.message = String.format(MIX, STUDENT_WITH_NAME, message, NOT_PRESENT);
            case "professor_name" -> this.message = String.format(MIX, PROFESSOR_WITH_NAME, message, NOT_PRESENT);
            case "professor_fiscal_code" -> this.message = String.format(MIX, PROFESSOR_WITH_FISCAL_CODE, message, NOT_PRESENT);
            case "course" -> this.message = String.format(MIX, COURSE_WITH_NAME, message, NOT_PRESENT);
            case "degree_course" -> this.message = String.format(MIX, DEGREE_COURSE_WITH_NAME, message, NOT_PRESENT);
            case "examination" -> this.message = String.format(MIX, EXAMINATION_OF_COURSE_NAMED, message, NOT_PRESENT);
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
