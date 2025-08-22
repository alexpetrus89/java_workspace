package com.alex.universitymanagementsystem.component.validator;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.annotation.ValidBirthDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class BirthDateValidator implements ConstraintValidator<ValidBirthDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
        if (dob == null)
            return true; // La validazione @NotNull dovrebbe gestire i valori null
        return dob.isBefore(LocalDate.now());
    }
}
