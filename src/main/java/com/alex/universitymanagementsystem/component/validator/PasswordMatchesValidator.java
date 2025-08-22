package com.alex.universitymanagementsystem.component.validator;

import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.annotation.PasswordMatches;
import com.alex.universitymanagementsystem.utils.PasswordCarrier;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


@Component
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordCarrier> {

    @Override
    public boolean isValid(PasswordCarrier carrier, ConstraintValidatorContext context) {
        if (carrier == null) return false;
        String password = carrier.getPassword();
        String confirm  = carrier.getConfirm();
        return password != null && confirm != null && password.equals(confirm);
    }
}

