package com.alex.universitymanagementsystem.exception;


public class DuplicateFiscalCodeException extends RuntimeException {

    public DuplicateFiscalCodeException(String fiscalCode) {
        super("Fiscal code '" + fiscalCode + "' is already taken by another user");
    }

}

