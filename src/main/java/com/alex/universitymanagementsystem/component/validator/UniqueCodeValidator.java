package com.alex.universitymanagementsystem.component.validator;

import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.annotation.ValidUniqueCode;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueCodeValidator implements ConstraintValidator<ValidUniqueCode, String> {

    private static final String REGISTER_REGEX = "^[a-zA-Z0-9]{8}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value.matches(REGISTER_REGEX);
    }
}

