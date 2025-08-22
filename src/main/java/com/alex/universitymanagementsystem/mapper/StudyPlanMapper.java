package com.alex.universitymanagementsystem.mapper;

import java.util.stream.Collectors;

import com.alex.universitymanagementsystem.domain.StudyPlan;
import com.alex.universitymanagementsystem.dto.StudyPlanDto;

public class StudyPlanMapper {

    private StudyPlanMapper() {}

    public static StudyPlan toEntity(StudyPlanDto dto) {
        if(dto == null) return null;
        return new StudyPlan(
            dto.getOrdering(),
            dto.getCourses()
                .stream()
                .map(CourseMapper::toEntity)
                .collect(Collectors.toSet())
        );
    }

    public static StudyPlanDto toDto(StudyPlan studyPlan) {
        if(studyPlan == null) return null;
        return new StudyPlanDto(
            studyPlan.getOrdering(),
            studyPlan.getCourses()
                .stream()
                .map(CourseMapper::toDto)
                .collect(Collectors.toSet())
        );
    }

}
