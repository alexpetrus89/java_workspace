package com.alex.universitymanagementsystem.component.validator;

import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.annotation.PasswordMatches;
import com.alex.universitymanagementsystem.dto.RegistrationForm;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegistrationForm> {

    @Override
    public boolean isValid(RegistrationForm form, ConstraintValidatorContext context) {
        if (form.getPassword() == null || form.getConfirm() == null)
            return false;
        return form.getPassword().equals(form.getConfirm());
    }

}
