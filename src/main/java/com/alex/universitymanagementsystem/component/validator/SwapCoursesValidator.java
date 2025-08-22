package com.alex.universitymanagementsystem.component.validator;

import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.annotation.SwapCoursesConstraint;
import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.dto.SwapCoursesDto;
import com.alex.universitymanagementsystem.repository.CourseRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


@Component
public class SwapCoursesValidator implements ConstraintValidator<SwapCoursesConstraint, SwapCoursesDto> {

    private static final String COURSE_TO_ADD = "courseToAdd";
    private static final String COURSE_TO_REMOVE = "courseToRemove";

    private final CourseRepository courseRepository;

    public SwapCoursesValidator(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public boolean isValid(SwapCoursesDto dto, ConstraintValidatorContext context) {
        if (dto == null) return false;

        context.disableDefaultConstraintViolation();

        boolean valid = true;

        valid &= checkSameCourse(dto, context);
        valid &= checkCoursesExist(dto, context);

        return valid;
    }


    // private methods
    private boolean checkSameCourse(SwapCoursesDto dto, ConstraintValidatorContext context) {
        if (dto.getCourseToAdd() != null &&
            dto.getCourseToRemove() != null &&
            dto.getCourseToAdd().equalsIgnoreCase(dto.getCourseToRemove())) {

            context
                .buildConstraintViolationWithTemplate("You cannot replace a course with itself")
                .addPropertyNode(COURSE_TO_ADD)
                .addConstraintViolation();
            return false;
        }
        return true;
    }


    private boolean checkCoursesExist(SwapCoursesDto dto, ConstraintValidatorContext context) {
        if (dto.getCourseToAdd() == null || dto.getCourseToRemove() == null ||
            dto.getDegreeCourseOfNewCourse() == null || dto.getDegreeCourseOfOldCourse() == null)
            return false; // skip further checks if data is incomplete

        var courseToAddOpt = courseRepository.findByNameAndDegreeCourseName(dto.getCourseToAdd(), dto.getDegreeCourseOfNewCourse());
        var courseToRemoveOpt = courseRepository.findByNameAndDegreeCourseName(dto.getCourseToRemove(), dto.getDegreeCourseOfOldCourse());

        boolean valid = true;

        if (courseToAddOpt.isEmpty()) {
            context
                .buildConstraintViolationWithTemplate("New course not found")
                .addPropertyNode(COURSE_TO_ADD)
                .addConstraintViolation();
            valid = false;
        }

        if (courseToRemoveOpt.isEmpty()) {
            context
                .buildConstraintViolationWithTemplate("Old course not found")
                .addPropertyNode(COURSE_TO_REMOVE)
                .addConstraintViolation();
            valid = false;
        }

        if (courseToAddOpt.isPresent() && courseToRemoveOpt.isPresent()) {
            valid &= checkCfu(courseToAddOpt.get(), courseToRemoveOpt.get(), context);
        }

        return valid;
    }


    private boolean checkCfu(Course courseToAdd, Course courseToRemove, ConstraintValidatorContext context) {
        if (!courseToAdd.getCfu().equals(courseToRemove.getCfu())) {
            context
                .buildConstraintViolationWithTemplate("The new course must have the same number of CFU as the course to be replaced")
                .addPropertyNode(COURSE_TO_ADD)
                .addConstraintViolation();
            return false;
        }
        return true;
    }


}




