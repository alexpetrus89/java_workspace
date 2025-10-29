package com.alex.universitymanagementsystem.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.alex.universitymanagementsystem.component.validator.YearOfStudyValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = YearOfStudyValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidYearOfStudy {
    String message() default "Invalid year of study for course type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

