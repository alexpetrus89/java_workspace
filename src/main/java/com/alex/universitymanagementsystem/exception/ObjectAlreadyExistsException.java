package com.alex.universitymanagementsystem.exception;


import com.alex.universitymanagementsystem.entity.immutable.FiscalCode;
import com.alex.universitymanagementsystem.entity.immutable.Register;
import com.alex.universitymanagementsystem.entity.immutable.UniqueCode;
import com.alex.universitymanagementsystem.enum_type.DomainType;


public class ObjectAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // instance variables
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
            case FiscalCode fiscalCode -> String.format("Professor with fiscal code %s already exists", fiscalCode.toString());
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
            case DomainType.EXAMINATION_OUTCOME -> "Examination outcome";
            default -> "Unknown";
        };
    }

    // getter
    @Override
    public String getMessage() {
        return message;
    }

}
