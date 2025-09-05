package com.alex.universitymanagementsystem.component.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alex.universitymanagementsystem.annotation.UniqueFiscalCode;
import com.alex.universitymanagementsystem.exception.DataAccessServiceException;
import com.alex.universitymanagementsystem.service.impl.RegistrationServiceImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueFiscalCodeValidator implements ConstraintValidator<UniqueFiscalCode, String> {

    private static final Logger logger = LoggerFactory.getLogger(UniqueFiscalCodeValidator.class);

    private final RegistrationServiceImpl registrationService;

    public UniqueFiscalCodeValidator(RegistrationServiceImpl registrationService) {
        this.registrationService = registrationService;
    }


    @Override
    public boolean isValid(String fiscalCode, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(fiscalCode))
            return false;
        try {
            if(registrationService.isFiscalCodeAlreadyTaken(fiscalCode)) {
                reject(context, "Error with fiscal code", "fiscalCodeAlreadyTaken");
                return false;
            }
            return true;
        } catch (DataAccessServiceException e) {
            logger.error("Error checking fiscal code availability", e);
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
