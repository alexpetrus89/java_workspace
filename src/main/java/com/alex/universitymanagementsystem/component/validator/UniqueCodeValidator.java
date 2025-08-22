package com.alex.universitymanagementsystem.component.validator;

import com.alex.universitymanagementsystem.annotation.ValidUniqueCode;
import com.alex.universitymanagementsystem.domain.immutable.UniqueCode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueCodeValidator implements ConstraintValidator<ValidUniqueCode, UniqueCode> {
    @Override
    public boolean isValid(UniqueCode value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value.code().matches("^[a-zA-Z0-9]{8}$");
    }
}

