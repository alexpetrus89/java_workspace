package com.alex.universitymanagementsystem.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.alex.universitymanagementsystem.component.validator.RegisterValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;


@Documented
@Constraint(validatedBy = RegisterValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRegister {
    String message() default "Invalid register number, register number must be exactly 6 digits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
