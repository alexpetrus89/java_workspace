package com.alex.universitymanagementsystem.component.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alex.universitymanagementsystem.annotation.UniqueUsername;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.service.impl.RegistrationServiceImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private static final Logger log = LoggerFactory.getLogger(UniqueUsernameValidator.class);

    private final RegistrationServiceImpl registrationService;

    public UniqueUsernameValidator(RegistrationServiceImpl registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(username))
            // Non gestiamo qui null/blank, ci pensano @NotBlank / @Size
            return false;

        try {
            if (registrationService.isUsernameAlreadyTaken(username)) {
                reject(context, "Username is already taken", "usernameAlreadyTaken");
                return false;
            }
            return true;
        } catch (DataAccessServiceException e) {
            log.error("Error checking username availability", e);
            return false;
        }
    }

    private void reject(ConstraintValidatorContext context, String message, String fieldName) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
            .addPropertyNode(fieldName)
            .addConstraintViolation();
    }
}

