package com.alex.studentmanagementsystem.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import com.alex.studentmanagementsystem.domain.immutable.Register;
import com.alex.studentmanagementsystem.domain.immutable.UniqueCode;

import org.springframework.http.HttpStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends IllegalStateException {

    private static final String OBJECT_NOT_FOUND = "object not found";

    private static final String STUDENT_WITH_REGISTER = "student with registration number ";
	private static final String STUDENT_WITH_NAME = "student with name ";

    private static final String PROFESSOR_WITH_FISCAL_CODE = "professor with fiscal code ";
    private static final String PROFESSOR_WITH_UNIQUE_CODE = "professor with unique code ";
    private static final String PROFESSOR_WITH_NAME = "professor with name ";
    private static final String COURSE_WITH_NAME = "course with name ";
    private static final String DEGREE_COURSE_WITH_NAME = "course degree with name ";

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
        if(identifier.equals("student"))
            this.message = String.format(MIX, STUDENT_WITH_NAME, message, NOT_PRESENT);
        else if(identifier.equals("professor_name"))
            this.message = String.format(MIX, PROFESSOR_WITH_NAME, message, NOT_PRESENT);
        else if(identifier.equals("professor_fiscal_code"))
            this.message = String.format(MIX, PROFESSOR_WITH_FISCAL_CODE, message, NOT_PRESENT);
        else if(identifier.equals("course"))
            this.message = String.format(MIX, COURSE_WITH_NAME, message, NOT_PRESENT);
        else if(identifier.equals("degree_course"))
            this.message = String.format(MIX, DEGREE_COURSE_WITH_NAME, message, NOT_PRESENT);
        else if(identifier.equals("examination"))
            this.message = String.format(MIX, EXAMINATION_OF_COURSE_NAMED, message, NOT_PRESENT);
        else if (identifier.equals("user"))
            this.message = String.format(message);
        else {
            this.message = "UNKNOWN ERROR";
        }
    }


    // getters
    @Override
    public String getMessage() {
        return message;
    }
}
