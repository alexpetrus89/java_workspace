package com.alex.studentmanagementsystem.utility;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.alex.studentmanagementsystem.domain.DegreeCourse;
import com.alex.studentmanagementsystem.exception.ObjectNotFoundException;
import com.alex.studentmanagementsystem.repository.DegreeCourseRepository;

public class StringToDegreeCourseConverter
    implements Converter<String, DegreeCourse>
{

    private final DegreeCourseRepository degreeCourseRepository;

    public StringToDegreeCourseConverter(
        DegreeCourseRepository degreeCourseRepository
    ) {
        this.degreeCourseRepository = degreeCourseRepository;
    }

    @Override
    @Nullable
    public DegreeCourse convert(@NonNull String source) {
        return degreeCourseRepository
            .findByName(source)
            .orElseThrow(() -> new ObjectNotFoundException(source, "degree_course"));
    }

}