package com.alex.universitymanagementsystem.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.alex.universitymanagementsystem.validator.BirthDateValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBirthDate {
    String message() default "La data di nascita non può essere odierna o futura.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
