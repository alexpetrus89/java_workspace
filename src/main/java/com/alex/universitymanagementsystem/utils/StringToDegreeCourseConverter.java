package com.alex.universitymanagementsystem.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.alex.universitymanagementsystem.domain.DegreeCourse;
import com.alex.universitymanagementsystem.exception.ObjectNotFoundException;
import com.alex.universitymanagementsystem.repository.DegreeCourseRepository;



public class StringToDegreeCourseConverter
    implements Converter<String, DegreeCourse>
{

    // instance variables
    private final DegreeCourseRepository degreeCourseRepository;

    // constructor
    public StringToDegreeCourseConverter(
        DegreeCourseRepository degreeCourseRepository
    ) {
        this.degreeCourseRepository = degreeCourseRepository;
    }

    /**
     * Converts a string representation of a degree course name into a
     * DegreeCourse object by searching the repository.
     * Resolve problem of upper case
     * @param source the name of the degree course to convert
     * @return the DegreeCourse object if found, otherwise throws an
     *         ObjectNotFoundException
     * @throws ObjectNotFoundException if no degree course with the given name
     *                                 exists
     * @throws NullPointerException if the source is null
     */
    @Override
    @Nullable
    public DegreeCourse convert(@NonNull String source) {
        return degreeCourseRepository.findByName(source.trim().toUpperCase());
    }

}