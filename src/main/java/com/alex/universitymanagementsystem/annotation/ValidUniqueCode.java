package com.alex.universitymanagementsystem.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.alex.universitymanagementsystem.component.validator.UniqueCodeValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = UniqueCodeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUniqueCode {
    String message() default "Unique code is invalid, Unique code must be exactly 8 characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

