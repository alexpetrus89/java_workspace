package com.alex.universitymanagementsystem.component.validator;


import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.annotation.ValidFiscalCode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class FiscalCodeValidator implements ConstraintValidator<ValidFiscalCode, String> {

    private static final String FISCAL_CODE_REGEX = "^[a-zA-Z0-9]{1,16}$";

    @Override
    public boolean isValid(String fiscalCode, ConstraintValidatorContext context) {
        if (fiscalCode == null || fiscalCode.isEmpty())
            return false;
        return fiscalCode.matches(FISCAL_CODE_REGEX);
    }
}

