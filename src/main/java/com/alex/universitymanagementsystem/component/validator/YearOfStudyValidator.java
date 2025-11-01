package com.alex.universitymanagementsystem.component.validator;


import com.alex.universitymanagementsystem.annotation.ValidYearOfStudy;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;
import com.alex.universitymanagementsystem.utils.CourseValidatable;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class YearOfStudyValidator implements ConstraintValidator<ValidYearOfStudy, CourseValidatable> {

    private final DegreeCourseRepository degreeCourseRepository;

    public YearOfStudyValidator(DegreeCourseRepository degreeCourseRepository) {
        this.degreeCourseRepository = degreeCourseRepository;
    }

    @Override
    public boolean isValid(CourseValidatable course, ConstraintValidatorContext context) {
        if (course == null ||
            course.getYearOfStudy() == null ||
            course.getDegreeCourseName() == null ||
            course.getDegreeCourseName().isBlank()) {
            return false;
        }

        return degreeCourseRepository.findByName(course.getDegreeCourseName())
            .map(dc -> dc.getGraduationClass())
            .map(type -> switch (type) {
                case BACHELOR -> course.getYearOfStudy() >= 1 && course.getYearOfStudy() <= 3;
                case MASTER -> course.getYearOfStudy() >= 1 && course.getYearOfStudy() <= 2;
                default -> false;
            })
            .orElse(false);
    }
}


