package com.alex.universitymanagementsystem.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.alex.universitymanagementsystem.component.validator.AdminCodeValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = AdminCodeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAdminCode {
    String message() default "Invalid Admin code, Admin code must be exactly 8 characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
