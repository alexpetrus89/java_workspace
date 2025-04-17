package com.alex.universitymanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;
import com.alex.universitymanagementsystem.enum_type.DomainType;


@ResponseStatus(value = HttpStatus.CONFLICT)
public class ObjectAlreadyExistsException extends RuntimeException {

    private final String message;

    // Constructors
    public ObjectAlreadyExistsException(Object type) {
        super(formatMessageStatic(type));
        this.message = formatMessageStatic(type);
    }

    // Metodo di formattazione del messaggio
    private static String formatMessageStatic(Object type) {
        return switch (type) {
            case Register register -> String.format("Student with registration number %s already exists", register.toString());
            case UniqueCode uniqueCode -> String.format("Professor with unique code %s already exists", uniqueCode.toString());
            case DomainType domainType -> String.format("%s already exists", getIdentifierName(domainType));
            default -> "Unknown error";
        };
    }

    // Metodo di recupero del nome dell'identificatore
    private static String getIdentifierName(DomainType type) {
        return switch (type) {
            case DomainType.USER -> "User";
            case DomainType.STUDENT -> "Student";
            case DomainType.PROFESSOR -> "Professor";
            case DomainType.COURSE -> "Course";
            case DomainType.DEGREE_COURSE -> "Degree course";
            case DomainType.EXAMINATION -> "Examination";
            case DomainType.EXAMINATION_APPEAL -> "Examination appeal";
            default -> "Unknown";
        };
    }

    // getter
    public String getBaseMessage() {
        return message;
    }

}
