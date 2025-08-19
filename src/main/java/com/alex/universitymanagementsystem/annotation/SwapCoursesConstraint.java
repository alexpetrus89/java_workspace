package com.alex.universitymanagementsystem.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.alex.universitymanagementsystem.validator.SwapCoursesValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SwapCoursesValidator.class)
@Documented
public @interface SwapCoursesConstraint {
    String message() default "Invalid swap courses input";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

