package com.alex.universitymanagementsystem.component.validator;

import java.util.Locale;
import java.util.Set;

import com.alex.universitymanagementsystem.annotation.ValidCountry;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CountryValidator implements ConstraintValidator<ValidCountry, String> {

    private static final Set<String> ISO_COUNTRIES = Set.of(Locale.getISOCountries());

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank())
            return false;
        return ISO_COUNTRIES.contains(value);
    }
}

