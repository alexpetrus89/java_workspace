package com.alex.universitymanagementsystem.component.validator;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.annotation.ValidPassword;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


@Component
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    // Configurazione delle regole
    private static final int MIN_LENGTH = 8;

    private static final Pattern UPPERCASE = Pattern.compile(".*[A-Z].*");
    private static final Pattern LOWERCASE = Pattern.compile(".*[a-z].*");
    private static final Pattern DIGIT = Pattern.compile(".*\\d.*");
    private static final Pattern SPECIAL = Pattern.compile(".*[!@#$%^&*()].*");

    private static final List<String> COMMON_PASSWORDS = List.of(
            "password", "12345678", "qwerty", "letmein", "admin"
    );

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isBlank()) {
            return setMessage(context, "Password is required");
        }

        if (password.length() < MIN_LENGTH)
            return setMessage(context, "Password must be at least " + MIN_LENGTH + " characters long");

        if (!UPPERCASE.matcher(password).matches())
            return setMessage(context, "Password must contain at least one uppercase letter");

        if (!LOWERCASE.matcher(password).matches())
            return setMessage(context, "Password must contain at least one lowercase letter");

        if (!DIGIT.matcher(password).matches())
            return setMessage(context, "Password must contain at least one number");

        if (!SPECIAL.matcher(password).matches())
            return setMessage(context, "Password must contain at least one special character (!@#$%^&*())");

        if (COMMON_PASSWORDS.contains(password.toLowerCase()))
            return setMessage(context, "Password is too common");

        return true;
    }

    private boolean setMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
            .addConstraintViolation();
        return false;
    }
}

