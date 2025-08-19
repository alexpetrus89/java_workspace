package com.alex.universitymanagementsystem.validator;



import com.alex.universitymanagementsystem.annotation.ValidRegister;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegisterValidator implements ConstraintValidator<ValidRegister, String> {

    private static final String REGISTER_REGEX = "\\d{6}";

    @Override
    public boolean isValid(String register, ConstraintValidatorContext context) {
        if (register == null)
            return false;
        return register.matches(REGISTER_REGEX);
    }

}
