package com.alex.universitymanagementsystem.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.alex.universitymanagementsystem.domain.immutable.CourseId;
import com.alex.universitymanagementsystem.dto.CourseDto;
import com.alex.universitymanagementsystem.mapper.CourseMapper;
import com.alex.universitymanagementsystem.repository.CourseRepository;

@Component
public class StringToCourseDtoConverter implements Converter<String, CourseDto> {

    private final CourseRepository courseRepository;

    public StringToCourseDtoConverter(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Metodo che converte una stringa in un oggetto CourseDto.
     *
     * @param source la stringa da convertire
     * @return l'oggetto Course convertito
     */
    @Override
    public CourseDto convert(@NonNull String source) {
        try {
            // Recupera l'ID del corso dalla stringa
            String courseId = source.split("@")[1];
            // Recupera il corso dal database utilizzando l'ID
            CourseDto course = CourseMapper.mapToCourseDto(courseRepository.findById(new CourseId(courseId)).orElse(null));
            System.out.println("STAMPA STO CAZZO DI COURSE DTO: " + course.toString());
            System.out.println("STAMPA STO CAZZO DI COURSE DTO ID: " + course.getCourseId().toString());
            return course;
        } catch (DataAccessException e) {
            return null;
        }
    }

}
