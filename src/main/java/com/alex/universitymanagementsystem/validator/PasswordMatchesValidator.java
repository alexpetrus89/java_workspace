package com.alex.universitymanagementsystem.validator;

import com.alex.universitymanagementsystem.annotation.PasswordMatches;
import com.alex.universitymanagementsystem.utils.RegistrationForm;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegistrationForm> {

    @Override
    public boolean isValid(RegistrationForm form, ConstraintValidatorContext context) {
        if (form.getPassword() == null || form.getConfirm() == null)
            return false;
        return form.getPassword().equals(form.getConfirm());
    }

}
