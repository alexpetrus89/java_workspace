package com.alex.universitymanagementsystem.component.validator;

import com.alex.universitymanagementsystem.annotation.ValidYearOfStudy;
import com.alex.universitymanagementsystem.enum_type.DegreeType;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.utils.CourseDtoCarrier;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class YearOfStudyValidator implements ConstraintValidator<ValidYearOfStudy, CourseDtoCarrier> {

    private final DegreeCourseRepository degreeCourseRepository;

    public YearOfStudyValidator(DegreeCourseRepository degreeCourseRepository) {
        this.degreeCourseRepository = degreeCourseRepository;
    }

    @Override
    public boolean isValid(CourseDtoCarrier course, ConstraintValidatorContext context) {
        if (course == null || course.getYearOfStudy() == null || course.getDegreeCourseName().isBlank())
            return false;

        DegreeType degreeType = degreeCourseRepository
            .findByName(course.getDegreeCourseName())
            .orElseThrow()
            .getGraduationClass();
        Integer year = course.getYearOfStudy();

        if (degreeType == null) return false;

        return switch (degreeType) {
            case BACHELOR -> year >= 1 && year <= 3;
            case MASTER -> year >= 1 && year <= 2;
            default -> false;
        };
    }
}

