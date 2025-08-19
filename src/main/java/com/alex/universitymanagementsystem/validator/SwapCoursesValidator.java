package com.alex.universitymanagementsystem.validator;

import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.annotation.SwapCoursesConstraint;
import com.alex.universitymanagementsystem.domain.Course;
import com.alex.universitymanagementsystem.domain.immutable.Register;
import com.alex.universitymanagementsystem.dto.SwapCoursesDto;
import com.alex.universitymanagementsystem.repository.CourseRepository;
import com.alex.universitymanagementsystem.repository.ExaminationRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


@Component
public class SwapCoursesValidator implements ConstraintValidator<SwapCoursesConstraint, SwapCoursesDto> {

    private static final String COURSE_TO_ADD_NAME = "courseToAddName";
    private static final String COURSE_TO_REMOVE_NAME = "courseToRemoveName";

    private final CourseRepository courseRepository;
    private final ExaminationRepository examinationRepository;

    public SwapCoursesValidator(
        CourseRepository courseRepository,
        ExaminationRepository examinationRepository
    ) {
        this.courseRepository = courseRepository;
        this.examinationRepository = examinationRepository;
    }

    @Override
    public boolean isValid(SwapCoursesDto dto, ConstraintValidatorContext context) {
        if (dto == null) return true;

        context.disableDefaultConstraintViolation();

        boolean valid = true;

        valid &= checkSameCourse(dto, context);
        valid &= checkCoursesExist(dto, context);

        return valid;
    }


    // private methods
    private boolean checkSameCourse(SwapCoursesDto dto, ConstraintValidatorContext context) {
        if (dto.getCourseToAddName() != null &&
            dto.getCourseToRemoveName() != null &&
            dto.getCourseToAddName().equalsIgnoreCase(dto.getCourseToRemoveName())) {

            context
                .buildConstraintViolationWithTemplate("You cannot replace a course with itself")
                .addPropertyNode(COURSE_TO_ADD_NAME)
                .addConstraintViolation();
            return false;
        }
        return true;
    }


    private boolean checkCoursesExist(SwapCoursesDto dto, ConstraintValidatorContext context) {
        if (dto.getCourseToAddName() == null || dto.getCourseToRemoveName() == null ||
            dto.getNewDegreeCourseName() == null || dto.getOldDegreeCourseName() == null)
            return true; // skip further checks if data is incomplete

        var courseToAddOpt = courseRepository.findByNameAndDegreeCourseName(dto.getCourseToAddName(), dto.getNewDegreeCourseName());
        var courseToRemoveOpt = courseRepository.findByNameAndDegreeCourseName(dto.getCourseToRemoveName(), dto.getOldDegreeCourseName());

        boolean valid = true;

        if (courseToAddOpt.isEmpty()) {
            context
                .buildConstraintViolationWithTemplate("New course not found")
                .addPropertyNode(COURSE_TO_ADD_NAME)
                .addConstraintViolation();
            valid = false;
        }

        if (courseToRemoveOpt.isEmpty()) {
            context
                .buildConstraintViolationWithTemplate("Old course not found")
                .addPropertyNode(COURSE_TO_REMOVE_NAME)
                .addConstraintViolation();
            valid = false;
        }

        if (courseToAddOpt.isPresent() && courseToRemoveOpt.isPresent()) {
            valid &= checkCfu(courseToAddOpt.get(), courseToRemoveOpt.get(), context);
            valid &= checkExams(dto.getRegister(), courseToRemoveOpt.get(), context);
        }

        return valid;
    }


    private boolean checkCfu(Course courseToAdd, Course courseToRemove, ConstraintValidatorContext context) {
        if (!courseToAdd.getCfu().equals(courseToRemove.getCfu())) {
            context
                .buildConstraintViolationWithTemplate("The new course must have the same number of CFU as the course to be replaced")
                .addPropertyNode(COURSE_TO_ADD_NAME)
                .addConstraintViolation();
            return false;
        }
        return true;
    }


    private boolean checkExams(Register register, Course courseToRemove, ConstraintValidatorContext context) {
        boolean hasExams = examinationRepository
            .findByStudent_Register(register)
            .stream()
            .anyMatch(exam -> exam.getCourse().getId().equals(courseToRemove.getId()));

        if (hasExams) {
            context
                .buildConstraintViolationWithTemplate("Cannot remove a course that already has examinations")
                .addPropertyNode(COURSE_TO_REMOVE_NAME)
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}




